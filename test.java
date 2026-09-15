
import MachinePackage.*;

public class test {

	public static void main(String[] args) {

		MachineFactory t = new MachineFactory();
		t.buildFactoryFromFile("./machines.txt", "./plumbing.txt");

		System.out.println(t.toString());
				
	}
	
}
