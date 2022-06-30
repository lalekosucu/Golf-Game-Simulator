/* Interface that is used for bots that can play the crazy putting game.
 */

package bots;

public interface Bot {
   // Every bot should have a method to generate the next velocity.
   public double[] getNextMove(double x, double y);
}