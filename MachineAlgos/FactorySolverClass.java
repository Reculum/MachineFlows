
package MachineAlgos;
import MachinePackage.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.lang.Math;


public class FactorySolverClass{

	public FactorySolverClass(){}

	public static float[][] ComputeConstraintsMatrix(MachineFactory f){

		int nMachines = f.getNumMachines();
		int nMaterials = f.getNumMaterials();
		int nPipes = f.getNumPipes();

		Machine[] MachineList = f.getMachineList();
		Material[] MaterialList = f.getMaterialList();
		Pipe[] PipeList = f.getPipeList();

		int nInputConsistency = 0;
		int nOutputConsistency = 0;
		boolean[][][] IsItZeroConstraint = new boolean[nMachines][nMaterials][2];

		for (int j = 0; j < nMachines; j++){

			Machine tempMachine = MachineList[j];
			Map<Material, Float> tempInputAmounts = tempMachine.getInputAmounts();
			Map<Material, Float> tempOutputAmounts = tempMachine.getOutputAmounts();

			for (int k = 0; k < nMachines; k++){

				Material tempMaterial = MaterialList[k];

				if (tempInputAmounts.get(tempMaterial) > 0){
					nInputConsistency += 1;
					IsItZeroConstraint[j][k][1] = false;
				}

				if (tempOutputAmounts.get(tempMaterial) > 0){
					nOutputConsistency += 1;
					IsItZeroConstraint[j][k][0] = false;
				}

			}
		}

		//by default, it gets filled with zeros by java
		float[][][][] ConstraintsTensor = new float[nMachines][nMaterials][2][nPipes + nMachines];

		for (int p = 0; p < nPipes; p++){

			Pipe tempPipe = PipeList[p];

			Machine machFrom = tempPipe.getMachineFrom();
			Machine machTo = tempPipe.getMachineTo();
			Material matAcross = tempPipe.getMaterialAcross();

			int machFromId = machFrom.getId();
			int machToId = machTo.getId();
			int matId = f.GetMaterialId(matAcross);

			//input consistency condition, the machine receving the material is getting it in INPUT ==> 1 on the third field

			if (!IsItZeroConstraint[machToId][matId][1]){

				float inputCoeff = machTo.getInputAmounts().get(matAcross);

				ConstraintsTensor[machToId][matId][1][p] = 1;
				ConstraintsTensor[machToId][matId][1][nPipes + machToId] = -inputCoeff;
			}

			//output consistency condition, the machine producing the material is OUTPUTTING it ==> 0 on the third field

			if (!IsItZeroConstraint[machFromId][matId][0]){

				float outputCoeff = machFrom.getOutputAmounts().get(matAcross);

				ConstraintsTensor[machFromId][matId][0][p] = 1;
				ConstraintsTensor[machFromId][matId][0][nPipes + machFromId] = -outputCoeff;
			}
		}

		int rowId = 0;
		int slackVariableId = 0;

		float[][] ConstraintsMatrix = new float[nInputConsistency + nOutputConsistency][nPipes + nMachines + nOutputConsistency];

		for (int i = 0; i < nMachines; i++){
			for (int j = 0; j < nMaterials; j++){
				for (int k = 0; k < 2; k++){

					//if the constraint is null, then there's none to be copied

					if (!IsItZeroConstraint[i][j][k]){

						for (int z = 0; z < nPipes + nMachines; z++){
							ConstraintsMatrix[rowId][z] = ConstraintsTensor[i][j][k][z];
						}

						if (k == 0){
							ConstraintsMatrix[rowId][nPipes + nMachines + slackVariableId] = 1;
						}

						rowId += 1;
						slackVariableId += 1;
					}
				}
			}
		}

		return ConstraintsMatrix;
	}


}
