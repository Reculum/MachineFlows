MachineFlows

MachineFlows is a Java package for determining whether a GregTech factory can be jumpstarted and finding a material-flow configuration that satisfies its production requirements.

Given a description of the machines in a factory and their connections, MachineFlows formulates a linear optimization problem to determine how much material must flow through each pipe and how much work each machine must perform to sustain the production circuit.

Features
Factory modeling: Describe machines, their material inputs and outputs, and the pipes connecting them.
Jumpstart feasibility: Determine whether the factory's material-balance constraints admit a feasible solution.
Material-flow calculation: Compute the amount of each material that must travel through each pipe.
Jumpstarting requirements: Calculate the machine-cycle multipliers associated with the resulting material allocation.
Flow optimization: Minimize the total flow across the factory's pipes to select a particular feasible configuration.
How It Works

MachineFlows represents a factory as a network of machines connected by material-carrying pipes.

Each machine is described by its material requirements and production capabilities. Pipes specify which material can be transferred between which machines.

The package then constructs a system of linear constraints describing the material requirements of the factory. Input requirements must be satisfied, while machine outputs are bounded by their maximum production amounts.

A linear programming solver searches for a feasible configuration that minimizes the sum of the material flows through the pipes. The resulting solution specifies the quantities associated with each pipe and the machine-cycle multipliers needed to satisfy the modeled production constraints.

Output

For a feasible factory, MachineFlows reports:

The material that must travel through each pipe.
The quantity of material to be transferred through that pipe.
The machine-cycle multipliers associated with the computed configuration.

These quantities provide a candidate jumpstarting configuration for the factory.

Mathematical Model

MachineFlows uses linear programming to solve a material-allocation problem.

The current formulation uses:

Linear constraints derived from machine input and output requirements.
Nonnegative decision variables for pipe flows and machine-cycle multipliers.
A normalization condition for factory-output machines.
An objective function minimizing total pipe flow.

The solver is implemented in Java using Apache Commons Math.

Scope and Limitations

MachineFlows models material quantities and machine input/output relationships. It does not simulate the temporal execution of machines or model the detailed mechanics of material transfers.

Its results therefore concern feasibility within the specified mathematical model. Their applicability to a physical factory depends on the accuracy of the machine descriptions and the correspondence between the modeled constraints and the game's actual mechanics.

Integer scaling of machine-cycle multipliers is a separate consideration when a factory requires discrete machine counts or indivisible material quantities.

Project Status

MachineFlows is an ongoing project. The current implementation focuses on linear programming for factory material allocation, with further work planned on validation and integer-compatible configurations.

Dependencies
Java
Apache Commons Math
License

See the repository's license file for the applicable terms.
