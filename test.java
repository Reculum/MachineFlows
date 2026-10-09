
import MachinePackage.*;
import MachineAlgos.*;

import java.util.Map;
import java.util.HashMap;

public class test {

	public static void main(String[] args) {

		String path = "./examples/Factory-2/";

		MachineFactory t = new MachineFactory();
		t.buildFactoryFromFile(path + "machines.txt", path + "plumbing.txt");

		System.out.println(t.toString());

		float[][] matr = FactorySolverClass.ComputeConstraintsMatrix(t);
				
	}
	
}
