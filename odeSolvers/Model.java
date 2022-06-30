/* This interface is used in objects that calculate the acceleration.
 */

package odeSolvers;

public interface Model {
   
   // Some constants that are used in the calculations.
   public static final double g = 9.81;
   public static final double alpha = ODESolver.h;
   
   public double calculateAcceleration_x(double[] stateVector);
   public double calculateAcceleration_y(double[] stateVector);
}