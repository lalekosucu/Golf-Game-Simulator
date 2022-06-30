/* This class is used to store all the data of a sandpit.
 * A sandpit belongs to a terrain and has the form of an ellipse.
 * In a sandpit the friction is higher than on the gras.
 */

package terrain.terrainObjects;

import terrain.tools.Pythagoras;

public class Sandpit {
    // Determines the size of the ellipse.
    // Is defined as the sum of the lengths between the focalpoint 
    // and a point on the line of the ellipse.
    private double r;
    
    // The focal point of the ellipse.
    private double xMin;
    private double xMax;
    private double yMin;
    private double yMax;

    // Constructor.
    public Sandpit(double xMin, double yMin, double xMax, double yMax, double r){
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.r = r;
    }
    
    // Returns if the given coordinates lay in the ellipse.
    public boolean inSandpit(double x, double y){
        double distanceLeftVector = Pythagoras.distanceP2P(x, y, xMin, yMin);
        double distanceRightVector = Pythagoras.distanceP2P(x, y, xMax, yMax);

        if(distanceLeftVector + distanceRightVector <= r){
            return true;
        }
        return false;
    }
}
