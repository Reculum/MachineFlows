# MachineFlows

**MachineFlows** is a Java package designed to determine whether a GregTech factory can be jumpstarted and to find a material-flow configuration that satisfies its production requirements[cite: 1].

Given a description of the machines in a factory and their connections, MachineFlows formulates a linear optimization problem to determine how much material must flow through each pipe and how much work each machine must perform to sustain the production circuit[cite: 1].

---

## Features

* **Factory Modeling:** Describe machines, their material inputs and outputs, and the pipes connecting them.
* **Jumpstart Feasibility:** Determine whether the factory's material-balance constraints admit a feasible solution[cite: 1].
* **Material-Flow Calculation:** Compute the amount of each material that must travel through each pipe.
* **Jumpstarting Requirements:** Calculate the machine-cycle multipliers associated with the resulting material allocation.
* **Flow Optimization:** Minimize the total flow across the factory's pipes to select a particular feasible configuration.
* **Discrete Scaling:** machine/cycle multipliers are converted from their floating numbers to fractions! Helping the user derive integer machine/cycle multipliers.

---

## How It Works

MachineFlows represents a factory as a network of machines connected by material-carrying pipes. 

* **Modeling:** Each machine is described by its material requirements and production capabilities, while pipes specify which material can be transferred between which machines.
* **Constraints:** The package constructs a system of linear constraints describing the factory's material requirements (input requirements must be satisfied, and machine outputs are bounded by maximum production amounts).
* **Optimization:** A linear programming solver searches for a feasible configuration that minimizes the sum of material flows through the pipes, outputting pipe quantities and machine/cycle multipliers.

### Output
For a feasible factory, MachineFlows reports:
* The material that must travel through each pipe.
* The quantity of material to be transferred through that pipe.
* The machine/cycle multipliers associated with the computed configuration.

---

## Mathematical Model

MachineFlows uses linear programming to solve a material-allocation problem utilizing:
* Linear constraints derived from machine input and output requirements.
* Nonnegative decision variables for pipe flows and machine-cycle multipliers[cite: 1].
* A factory-output condition for the whole circuit.
* An objective function minimizing total pipe flow.

> *Note:* The solver is implemented in Java using **Apache Commons Math**.

---

## Limitations

* **Model vs. Reality:** MachineFlows models material quantities and machine input/output relationships. It does not simulate the temporal execution of machines or model detailed mechanics of material transfers. 

---

## Usage

In the /examples folder the user can find four example factories and easily infer how to create their own. Also, the test.java file includes a template on how to compute the actual flows starting from some factory files.


```Java
import MachinePackage.*;
import MachineAlgos.*;

public class test {

	public static void main(String[] args) {

		//define the path to the folder containing the
		//factory files
		String path = "./examples/Factory-2/";


		//define a new MachineFacotry Object
		MachineFactory t = new MachineFactory();


		//Construct your Machine Factory using the blueprints in the
		//Factory folder
		try{
			t.buildFactoryFromFile(path + "machines.tsv", path + 
			"plumbing.tsv");
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}


		//print out the Factory structure
		//and show the flows needed from each machine to what ther machine
		System.out.println(t.toString());
		FactorySolverClass.ShowStableFactoryFlows(t);

				
	}
	
}
```
