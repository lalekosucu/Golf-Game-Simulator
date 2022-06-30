/* This class contains all the data of a tree in the terrain.
 */

package terrain.terrainObjects;

import terrain.tools.Pythagoras;

public class Tree {
    
    // Coordinates and radius of the Tree.
    public double xTree;
    public double yTree;
    public double r;
    
    // Constructor.
    public Tree(double xTree, double yTree, double r){
        this.xTree = xTree;
        this.yTree = yTree;
        this.r = r;
    }
    
    // Returns the coordinates of the center of the tree. 
    public double[] getCoordinates() {
       return new double[]{xTree, yTree};
    }
    
    // Returns if a given point lies within the tree.
    public boolean isInRadius(double x, double y){
        return Pythagoras.distanceP2P(x, y, xTree, yTree) <= r;
    }
    
    // When the ball hits the tree it bounces of.
    // This method changes the speed of the stateVector according to the bouncing of.
    public void bounceOfTree(double[] stateVector) {
       double dx = stateVector[0]-xTree;
       double dy = stateVector[1]-yTree;
       double r = Pythagoras.distanceP2P(stateVector[0], xTree, stateVector[1], yTree);
       double v = Pythagoras.totalSpeed(stateVector[2], stateVector[3]);
       
       double theta1 = Math.acos(stateVector[2]/v);
       if (stateVector[3] < 0) {
          theta1 = -theta1;
       }
       double theta2 = Math.acos(dy/r);
       if (dx < 0) {
          theta2 = -theta2;
       }
       double theta = theta1 - theta2;
       double rotateAngle = Math.PI + 2*theta;
       stateVector[2] = stateVector[2]*Math.cos(rotateAngle) - stateVector[3]*Math.sin(rotateAngle);
       stateVector[3] = stateVector[2]*Math.sin(rotateAngle) + stateVector[3]*Math.cos(rotateAngle);
    }
}