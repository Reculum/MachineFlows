
package MachineAlgos;
import MachinePackage.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.lang.Math;


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

	public static int[] ComputeMinimalMachineConfig(MachineFactory Factory, Map<Material, Float> ProductionAmounts){


		//total number of machines
		int nMachines = Factory.getNumMachines();
		int nMaterials = Factory.getNumMaterials();
		Material[] MaterialList = Factory.getMaterialList();

		//two arrays for keeping track of the amounts needed and the machines already processed
		int[][] IntegerMachineAmountsByMaterial = new int[nMaterials][nMachines];
		int[] ReturnMachineAmounts = new int[nMachines];

		boolean[] MachineAlreadyVisited = new boolean[nMachines];
		LinkedList<Machine> ExplorationQ = new LinkedList<Machine>();

		for (int j = 0; j < nMaterials; j++){
			for (int k = 0; k < nMachines; k++){
				IntegerMachineAmountsByMaterial[j][k] = 0;
			}
		}

		for (int k = 0; k < nMachines; k++){
			MachineAlreadyVisited[k] = false;
			ReturnMachineAmounts[k] = 0;
		}

		//we need to start from the output machines, since their amounts are quite easy
		LinkedList<Machine> OutputMachines = LocateFactoryOutputs(Factory);
		Iterator<Machine> OutputMachinesIterator = OutputMachines.iterator();

		while (OutputMachinesIterator.hasNext()){

			int MachineAmount = 0;

			Machine OutMachine = OutputMachinesIterator.next();
			int OutMachineId = OutMachine.getId();

			Map<Material, Float> OutputAmounts = OutMachine.getOutputAmounts();			

			for (int j = 0; j < nMaterials; j++){

				Material ProdMaterial = MaterialList[j];
				
				//iterate over the materials that need to be produced
				//and check if they are, in fact, made by the current output machine
				//we are assuming that an output machine outputs ONLY ONE kind of material

				if (OutputAmounts.get(ProdMaterial) == 0){MachineAmount = 0;}
				else{
					int ProdRatio = (int) Math.ceil(ProductionAmounts.get(ProdMaterial) / OutputAmounts.get(ProdMaterial));
					MachineAmount = Math.max(MachineAmount, ProdRatio);
				}			

				IntegerMachineAmountsByMaterial[j][OutMachineId] = MachineAmount;

			}

			//append to the list
			
			ExplorationQ.addLast(OutMachine);

		}	
		

		while (!ExplorationQ.isEmpty()){

			//get the head of the list, removing it

			Machine CurrentMachine = ExplorationQ.removeFirst();

			if (MachineAlreadyVisited[CurrentMachine.getId()] == false){

				//make sure no machine gets processed twice
				MachineAlreadyVisited[CurrentMachine.getId()] = true;

				//now, we need the input amounts and what machines will input those amounts
				Map<Material, Float> inputAmountsByMaterial = CurrentMachine.getInputAmounts();
				Map<Material, LinkedList<Machine>> inputMachinesByMaterial = CurrentMachine.getInMachinesByMaterial();

				int CurrentMachineAmountNeeded = 0;
				for (int k = 0; k < nMaterials; k++){
					if (IntegerMachineAmountsByMaterial[k][CurrentMachine.getId()] > CurrentMachineAmountNeeded){
						CurrentMachineAmountNeeded = IntegerMachineAmountsByMaterial[k][CurrentMachine.getId()];
					}
				}

				for (int k = 0; k < nMaterials; k++){

					Material inMaterial = MaterialList[k];

					//we get how much of that material is standardly needed
					float MaterialInputAmount = inputAmountsByMaterial.get(inMaterial);

					//we also should know, by now, an underestimate of the total input flow for this specific material
					float TotInMaterialFlow = CurrentMachineAmountNeeded * MaterialInputAmount;

					//now, given a material, we aim to find the optimal machine amongst the ones outputting it
					int minimalProdRatio = Integer.MAX_VALUE;
					int minimalId = 0;
					Machine minimalMachine = null;
					
					//get a list of the appropriate machines
					LinkedList<Machine> inputMachinesForInMaterial = inputMachinesByMaterial.get(inMaterial);
					Iterator<Machine> inputMachinesIterator = inputMachinesForInMaterial.iterator();			

					while (inputMachinesIterator.hasNext()){

						Machine inputMachine = inputMachinesIterator.next();						
						float MaterialOutputAmount = inputMachine.getOutputAmounts().get(inMaterial);

						int IntegerRatio = (int) Math.ceil(TotInMaterialFlow / MaterialOutputAmount);

						//this boolean will represent whether or not we can actually ask for
						//more of this machine
						boolean IsMachineCompatible = true;
						
						if (MachineAlreadyVisited[inputMachine.getId()] == true){

							int inputMachineTotalAmount = 0;
							int inputMachineMaterialAmount = 0;

							//we get how many machines are known to be needed for this material
							inputMachineMaterialAmount = IntegerMachineAmountsByMaterial[k][inputMachine.getId()];
							//and we also compute how many are known to be needed in general

							for (int i = 0; i < nMaterials; i++){
								if (IntegerMachineAmountsByMaterial[i][inputMachine.getId()] > inputMachineTotalAmount){
									inputMachineTotalAmount = IntegerMachineAmountsByMaterial[i][inputMachine.getId()];
								}
							}
							
							//if the amount we would be asking for is bounded by the amount that was already computed
							//then, no inconsistencies will arise
							if (!(inputMachineTotalAmount >= inputMachineMaterialAmount + IntegerRatio)){
								IsMachineCompatible = false;
							}
						}

						if ( (IntegerRatio < minimalProdRatio) && (IsMachineCompatible)){

							minimalProdRatio = IntegerRatio;
							minimalId = inputMachine.getId();
							minimalMachine = inputMachine;
						}

					}

					//if at least on machine outputted this material, but none was compatible, then the circuit cannot
					//be satisfied

					if ( (minimalProdRatio == Integer.MAX_VALUE) && (inputMachinesForInMaterial.size() != 0)){
						System.out.println("It is IMPOSSIBLE to satisfy this circuit!!!");
						int[] ret = {0};
						return ret;
					}

					//if the minimal productio ratio is not the highest possible integer, then
					//we have found at least one compatible machine

					if (minimalProdRatio != Integer.MAX_VALUE){

						//this is crucial: we need to add the minimalProdRatio, because we don't know
						//how many machines the minimal one is piping this material into
						IntegerMachineAmountsByMaterial[k][minimalId] += minimalProdRatio;
						
						//this is also crucial: we have to absolutely avoid appending machines to the list
						//before having determined that we need at least one of them. Basically, we are considering
						//those that we need to satisfy the circuit in reverse-order from the Factory Output
						ExplorationQ.addLast(minimalMachine); 
					}

					

				}
			
			}

		}

		for (int k = 0; k < nMachines; k++){
			for (int j = 0; j < nMaterials; j++){

				if (IntegerMachineAmountsByMaterial[j][k] > ReturnMachineAmounts[k]){
					ReturnMachineAmounts[k] = IntegerMachineAmountsByMaterial[j][k];
				}

			}
		}

		return ReturnMachineAmounts;
	}
	
}
