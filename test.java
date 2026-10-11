
import MachinePackage.*;
import MachineAlgos.*;

public class test {

	public static void main(String[] args) {

		String path = "./examples/Sola-1/";

		MachineFactory t = new MachineFactory();

		try{t.buildFactoryFromFile(path + "machines.tsv", path + "plumbing.tsv");}
		catch(Exception e){System.out.println(e.getMessage());}

		System.out.println(t.toString());

		FactorySolverClass.ShowStableFactoryFlows(t);

				
	}
	
}
