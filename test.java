
import MachinePackage.*;
import MachineAlgos.*;

public class test {

	public static void main(String[] args) {

		//define the path to the folder containing the
		//factory files
		String path = "./examples/Factory-2/";


		//define a new MachineFacotry Object
		MachineFactory t = new MachineFactory();


		//Construct your Machine Factory using the blueprints in the Factory folder
		try{t.buildFactoryFromFile(path + "machines.tsv", path + "plumbing.tsv");}
		catch(Exception e){System.out.println(e.getMessage());}


		//print out the Factory structure
		//and show the flows needed from each machine to what ther machine
		System.out.println(t.toString());
		FactorySolverClass.ShowStableFactoryFlows(t);

				
	}
	
}
