
import MachinePackage.*;
import MachineAlgos.*;

import java.util.Map;
import java.util.HashMap;

public class test {

	public static void main(String[] args) {

		MachineFactory t = new MachineFactory();
		t.buildFactoryFromFile("./machines.txt", "./plumbing.txt");

		System.out.println(t.toString());

		Map<Material, Float> ProductionAmounts = new HashMap<Material, Float>();
		Material deltaMaterial = new Material("delta");

		ProductionAmounts.put(deltaMaterial, (float) 1.0);

		int[] IntegerAmounts = FactorySolverClass.ComputeMinimalMachineConfig(t, ProductionAmounts);

		System.out.print(IntegerAmounts);
				
	}
	
}
