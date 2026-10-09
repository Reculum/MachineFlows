
import MachinePackage.*;
import MachineAlgos.*;

public class test {

	public static void main(String[] args) {

		String path = "./examples/Factory-2/";

		MachineFactory t = new MachineFactory();
		t.buildFactoryFromFile(path + "machines.txt", path + "plumbing.txt");

		System.out.println(t.toString());

		FactorySolverClass.ShowStableFactoryFlows(t);

				
	}
	
}
