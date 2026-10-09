
import MachinePackage.*;
import MachineAlgos.*;
import org.apache.commons.math3.optim.PointValuePair;



public class test {

	public static void main(String[] args) {

		String path = "./examples/Factory-2/";

		MachineFactory t = new MachineFactory();
		t.buildFactoryFromFile(path + "machines.txt", path + "plumbing.txt");

		System.out.println(t.toString());

		PointValuePair sol = FactorySolverClass.ComputeSelfSustainingFlow(t);

		System.out.println(sol);

				
	}
	
}
