
package MachineAlgos;
import MachinePackage.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/////////////////////////////////////////

import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.NonNegativeConstraint;
import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.PointValuePair;


public class FactorySolverClass{

	public FactorySolverClass(){}

	private static LinkedList<Machine> LocateFactoryOutputs(MachineFactory Factory){

		int nMachines = Factory.getNumMachines();
		int nMaterials = Factory.getNumMaterials();

		Machine[] Machines = Factory.getMachineList();
		Material[] Materials = Factory.getMaterialList();

		LinkedList<Machine> FactoryOutputs = new LinkedList<Machine>();

		for (int k = 0; k < nMachines; k++){

			boolean outputtingToNoMachine = true;
			Machine MachineK = Machines[k];

			for (int j = 0; j < nMaterials; j++){

				Material MaterialJ = Materials[j];				

				LinkedList<Machine> outMachinesMaterialJ = (MachineK.getOutMachinesByMaterial()).get(MaterialJ);

				outputtingToNoMachine = outputtingToNoMachine && (outMachinesMaterialJ.size() == 0);

			}

			if (outputtingToNoMachine){FactoryOutputs.addLast(MachineK);}

		}

		return FactoryOutputs;

	}

	public static double[][] ComputeConstraintsMatrix(MachineFactory f){

		int nMachines = f.getNumMachines();
		int nMaterials = f.getNumMaterials();
		int nPipes = f.getNumPipes();

		Machine[] MachineList = f.getMachineList();
		Material[] MaterialList = f.getMaterialList();
		Pipe[] PipeList = f.getPipeList();

		int nInputConsistency = 0;
		int nOutputConsistency = 0;
		boolean[][][] IsNonZeroConstraint = new boolean[nMachines][nMaterials][2];

		for (int j = 0; j < nMachines; j++){

			Machine tempMachine = MachineList[j];
			Map<Material, Double> tempInputAmounts = tempMachine.getInputAmounts();
			Map<Material, Double> tempOutputAmounts = tempMachine.getOutputAmounts();

			for (int k = 0; k < nMaterials; k++){

				Material tempMaterial = MaterialList[k];

				if (tempInputAmounts.get(tempMaterial) > 0){
					nInputConsistency += 1;
					IsNonZeroConstraint[j][k][1] = true;
				}

				if (tempOutputAmounts.get(tempMaterial) > 0){
					nOutputConsistency += 1;
					IsNonZeroConstraint[j][k][0] = true;
				}

			}
		}

		//by default, it gets filled with zeros by java
		double[][][][] ConstraintsTensor = new double[nMachines][nMaterials][2][nPipes + nMachines];

		for (int p = 0; p < nPipes; p++){

			Pipe tempPipe = PipeList[p];

			Machine machFrom = tempPipe.getMachineFrom();
			Machine machTo = tempPipe.getMachineTo();
			Material matAcross = tempPipe.getMaterialAcross();

			int machFromId = machFrom.getId();
			int machToId = machTo.getId();
			int matId = f.GetMaterialId(matAcross);

			//input consistency condition, the machine receving the material is getting it in INPUT ==> 1 on the third field

			if (IsNonZeroConstraint[machToId][matId][1]){

				double inputCoeff = machTo.getInputAmounts().get(matAcross);

				ConstraintsTensor[machToId][matId][1][p] = 1;
				ConstraintsTensor[machToId][matId][1][nPipes + machToId] = -inputCoeff;
			}

			//output consistency condition, the machine producing the material is OUTPUTTING it ==> 0 on the third field

			if (IsNonZeroConstraint[machFromId][matId][0]){

				double outputCoeff = machFrom.getOutputAmounts().get(matAcross);

				ConstraintsTensor[machFromId][matId][0][p] = 1;
				ConstraintsTensor[machFromId][matId][0][nPipes + machFromId] = -outputCoeff;
			}
		}

		int rowId = 0;
		int slackVariableId = 0;

		double[][] ConstraintsMatrix = new double[nInputConsistency + nOutputConsistency][nPipes + nMachines + nOutputConsistency];

		for (int i = 0; i < nMachines; i++){
			for (int j = 0; j < nMaterials; j++){
				for (int k = 0; k < 2; k++){

					//if the constraint is null, then there's none to be copied

					if (IsNonZeroConstraint[i][j][k]){

						for (int z = 0; z < nPipes + nMachines; z++){
							ConstraintsMatrix[rowId][z] = ConstraintsTensor[i][j][k][z];
						}

						if (k == 0){
							ConstraintsMatrix[rowId][nPipes + nMachines + slackVariableId] = 1;
							slackVariableId += 1;
						}

						rowId += 1;						
					}
				}
			}
		}

		
		return ConstraintsMatrix;
	}

	private static PointValuePair ComputeSelfSustainingFlow(MachineFactory f){

		//compute flow-consistency constraint matrix
		double[][] flowConsistencyMatrix = ComputeConstraintsMatrix(f);
		//compute factory output machines
		LinkedList<Machine> factoryOutputs = LocateFactoryOutputs(f);

		//useful constants
		int nPipes = f.getNumPipes();
		int nFactoryOutputs = factoryOutputs.size();
		int nFlowConstraints = flowConsistencyMatrix.length;
		
		//number of variables
		int nVariables = flowConsistencyMatrix[0].length;

	
		//compute size of the factory outputs and the relative constraint matrix		
		int rowId = 0;
		double[][] minimalOutputConstraints = new double[nFactoryOutputs][nVariables];
		Iterator<Machine> factoryOutputIter = factoryOutputs.iterator();

		while (factoryOutputIter.hasNext()){
			Machine factoryOutputMachine = factoryOutputIter.next();
			int FactoryOutputId = factoryOutputMachine.getId();
			minimalOutputConstraints[rowId][nPipes + FactoryOutputId] = 1;
		}

		//now we need to prepare the constraints
		List<LinearConstraint> Constraints = new ArrayList<>();


		//these are the flow consistency constraints
		for (int k = 0; k < nFlowConstraints; k++){
			Constraints.add(new LinearConstraint(flowConsistencyMatrix[k], Relationship.EQ, 0));
		}

		for (int k = 0; k < nFactoryOutputs; k++){
			Constraints.add(new LinearConstraint(minimalOutputConstraints[k], Relationship.EQ, 1));
		}

		//now, we prepare the linear objective function
		double[] linObjFunc = new double[nVariables];

		for (int k = 0; k < nPipes; k++){
			linObjFunc[k] = 1;
		}

		LinearObjectiveFunction objective = new LinearObjectiveFunction(linObjFunc, 0);

		
		// Instantiate and run the Simplex Solver
		SimplexSolver solver = new SimplexSolver();
		PointValuePair solution = solver.optimize(
			objective,
			new LinearConstraintSet(Constraints),
			GoalType.MINIMIZE,
			new NonNegativeConstraint(true)
		);

		return solution;


	}

	public static void ShowStableFactoryFlows(MachineFactory f){

		int nPipes = f.getNumPipes();
		int nMachines = f.getNumMachines();


		Pipe[] PipeList = f.getPipeList();
		Machine[] MachineList = f.getMachineList();
		String[] MachineNames = f.getMachineNames();

		PointValuePair sol = ComputeSelfSustainingFlow(f);


		String tempPrint = "List of Computed Minimal Flows" + "\n";

		tempPrint += "|" + "\n";
        tempPrint += "|" + "\n";

		for (int k = 0; k < nPipes; k++){

			Pipe tmpPipe = PipeList[k];

			String machFromName = MachineNames[tmpPipe.getMachineFrom().getId()];
			String machToName = MachineNames[tmpPipe.getMachineTo().getId()];
			String matAcrossName = tmpPipe.getMaterialAcross().getName();

			tempPrint += "|____" + "\t" + Double.toString(sol.getPoint()[k]) + " units of Material: " + matAcrossName + ", need to flow from: " 
					  + machFromName + ", to: " + machToName + "\n";

        	tempPrint += "|" + "\n";
		}

		tempPrint += "|__________________________________"+ "\n"+ "\n" + "\n"+ "\n";

		System.out.print(tempPrint);

		tempPrint = "List of Computed Machine Jumpstarting Amounts" + "\n";

		tempPrint += "|" + "\n";
        tempPrint += "|" + "\n";

		for (int k = nPipes; k < nPipes + nMachines; k++){

			String tmpMachName = MachineNames[MachineList[k - nPipes].getId()];

			tempPrint += "|____" + "\t" + Double.toString(sol.getPoint()[k]) + " amounts of Machine: " + tmpMachName + "\n";

        	tempPrint += "|" + "\n";

		}

		tempPrint += "|__________________________________" + "\n"+ "\n" + "\n"+ "\n";

		System.out.print(tempPrint);

	}
}
