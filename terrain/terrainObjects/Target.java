/* This class contains all the date of the target,
 * that needs to be hit by the ball.
 */

package terrain.terrainObjects;

import terrain.tools.Pythagoras;

public class Target {
    
    // Coordinates and radius of the target.
    public final double x;
    public final double y;
    public final double r;
    
    // Constructor.
    public Target(double x, double y, double r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }
    
    // Checks given the coordinates of the ball if the ball has hit the target.
    public boolean goal(double x, double y) {
        return Pythagoras.distanceP2P(x, y, this.x, this.y) <= r;
    }
    
    // Returns the distance between a point and the middle of the target.
    public double getDistance(double x, double y) {
       return Pythagoras.distanceP2P(x, y, this.x, this.y);
    }
    
    // Returns the distance in the X-direction between a point and the middel of the target.
    public double distanceXTarget(double x) {
       return this.x - x;
    }
   
    // Returns the distance in the Y-direction between a point and the middel of the target.
    public double distanceYTarget(double y) {
       return this.y - y;
    }
}
