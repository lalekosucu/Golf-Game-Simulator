/* This interface is used to solve ordinary differential equations.
 */

package odeSolvers;
import terrain.*;

public interface ODESolver {
    public static final double h = 0.01;
    
    public double[] getNextState(double[] stateVector);
}
