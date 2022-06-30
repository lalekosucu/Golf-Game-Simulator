/* This class is used as a tool to calculate certain formula's 
 * involving Pythagoras' theorem.
 */

package terrain.tools;

public class Pythagoras {
   
   // Calculates the distance between two points.
   public static double distanceP2P(double x1, double y1, double x2, double y2) {
      // Distance in the x and y direction.
      double dx = x1 - x2;
      double dy = y1 - y2;
      // Total distance.
      return Math.sqrt(dx*dx + dy*dy);
   }
   
   // Uses Pythagotras' theorem to calculate the total speed given the x and y components.
   public static double totalSpeed(double vx, double vy) {
      return Math.sqrt(vx*vx + vy*vy);
   }
}