
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.NonNegativeConstraint;
import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.PointValuePair;

import java.util.ArrayList;
import java.util.List;

public class TestLP {
    public static void main(String[] args) {
        // Objective: Maximize Z = 3x + 2y
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[] { 3, 2 }, 0);

        // Constraints:
        // 1) 2x + y <= 100
        // 2) x + y <= 80
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new double[] { 2, 1 }, Relationship.LEQ, 100));
        constraints.add(new LinearConstraint(new double[] { 1, 1 }, Relationship.LEQ, 80));

        try {
            // Instantiate and run the Simplex Solver
            SimplexSolver solver = new SimplexSolver();
            PointValuePair solution = solver.optimize(
                objective,
                new LinearConstraintSet(constraints),
                GoalType.MAXIMIZE,
                new NonNegativeConstraint(true)
            );

            System.out.println("Optimization Successful!");
            System.out.println("Optimal x: " + solution.getPoint()[0]);
            System.out.println("Optimal y: " + solution.getPoint()[1]);
            System.out.println("Max Value (Z): " + solution.getValue());

        } catch (Exception e) {
            System.err.println("Optimization failed: " + e.getMessage());
        }
    }
}
