
package MachineAlgos;
import MachinePackage.*;
import java.util.LinkedList;


public class FactoryMatrixClass{

	public FactoryMatrixClass(){}

	private Machine[] LocateFactoryOutputs(MachineFactory Factory){

		Integer nMachines = Factory.getNumMachines();
		Integer nMaterials = Factory.getNumMaterials();

		Machine[] Machines = Factory.getMachineList();
		Material[] Materials = Factory.getMaterialList();

		Machine[] FactoryOutputs = new Machine[nMachines];

		for (int k = 0; k < nMachines; k++){

			Boolean outputtingToNoMachine = true;
			Machine MachineK = Machines[k];

			for (int j = 0; j < nMaterials; j++){

				Material MaterialJ = Materials[j];				

				LinkedList<Machine> outMachinesMaterialJ = (MachineK.getOutMachinesByMaterial()).get(MaterialJ);

				outputtingToNoMachine = outputtingToNoMachine && (outMachinesMaterialJ.size() == 0);


			}

			if (outputtingToNoMachine){FactoryOutputs[k] = MachineK;}

		}

		return FactoryOutputs;

	}

	//public static Float[][] ComputeFactoryMatrix(MachineFactory Factory){}
	
}
