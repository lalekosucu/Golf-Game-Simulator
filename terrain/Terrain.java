/* The Terrain class is the the only class of the terrain package
 * that is used outside the package.
 * This class stores all relevant information of the golf course:
 * heightprofile, friction coefficients and the target.
 */

package terrain;

import java.util.*;
import javafx.scene.paint.Color;

import terrain.formula.Formula;
import terrain.terrainObjects.*;

public class Terrain {
   
   // The heightprofile.
   private Formula surfaceFormula;
   
   // The friction coefficients.
   private double frictionCoefficient;
   private double staticFrictionCoefficient;
   private double sandpitFrictionCoefficient;
   private double sandpitStaticFrictionCoefficient;
   
   // Terrain objects.
   private Target target;
   private ArrayList<Tree> trees;
   private ArrayList<Sandpit> sandpits;
   private ArrayList<Wall> walls;
   
   // When Terrain is constructed, only the heightprofile is added.
   public Terrain(String s) {
      surfaceFormula = new Formula(s);
      trees = new ArrayList<Tree>();
      sandpits = new ArrayList<Sandpit>();
      walls = new ArrayList<Wall>();
   }
   
   // Adds the friction coefficients.
   public void addFriction(double friction, double staticFriction) {
      frictionCoefficient = friction;
      staticFrictionCoefficient = staticFriction;
   }
   
   // Adds the friction coefficients for when the ball lies in a sandpit.
   public void addSandpitFriction(double friction, double staticFriction) {
      sandpitFrictionCoefficient = friction;
      sandpitStaticFrictionCoefficient = staticFriction;
   }
   
   // Adds the target and its radius.
   public void addTarget(double xt, double yt, double rt) {
      target = new Target(xt, yt, rt);
   }
   
   // Adds a tree object to the terrain.
   public void addTree(double xTree, double yTree, double r){
      trees.add(new Tree(xTree, yTree, r));
   }
   
   // Adds a wall object to the terrain.
   public void addWall(double x, double y, double width, double height){
      walls.add(new Wall(x, y, width, height));
   }
   
   // Ads a sandpit object to the terrain.
   public void addSandpit(double xMin, double yMin, double xMax, double yMax, double r){
      sandpits.add(new Sandpit(xMin, yMin, xMax, yMax, r));
   }
   
   // Returns the slope of x at (x, y).
   public double getSlopeX(double x, double y) {
      return surfaceFormula.slopeX(x, y);
   }
   
   // Returns the slope of y at (x, y).
   public double getSlopeY(double x, double y) {
      return surfaceFormula.slopeY(x, y);
   }
   
   // Returns the heigth at (x, y).
   public double getHeight(double x, double y) {
      return surfaceFormula.calc(x, y);
   }
   
   // Returns the kinetic friction.
   public double getFriction(double x, double y) {
      for (int i = 0; i < sandpits.size(); i++) {
         if (sandpits.get(i).inSandpit(x, y)) {
            return sandpitFrictionCoefficient;
         }
      }
      return frictionCoefficient;
   }
   
   // Returns the static friction.
   public double getStaticFriction(double x, double y) {
      if (inSandpit(x, y)) {
         return sandpitStaticFrictionCoefficient;
      }
      return staticFrictionCoefficient;
   }
   
   // Checks if the given coordinates lie in a sandpit.
   private boolean inSandpit(double x, double y) {
      // Goes through all the sandpits.
      for (int i = 0; i < sandpits.size(); i++) {
         // Checks if the point lies in the sandpit.
         if (sandpits.get(i).inSandpit(x, y)) {
            return true;
         }
      }
      return false;
   }
   
   // Checks if the given coordinates lie in a wall.
   private boolean inWall(double x, double y) {
      // Goes through all the walls.
      for (int i = 0; i < walls.size(); i++) {
         // Checks if the point lies in the sandpit.
         if (walls.get(i).inWall(x, y)) {
            return true;
         }
      }
      return false;
   }
   
   // Returns the coordinates at the center of the target.
   public double[] getTargetCoordinates() {
      double[] coordinates = {target.x, target.y};
      return coordinates;
   }
   
   // Returns whether or not (x, y) is close enough to the target.
   public boolean goal(double x, double y) {
      return target.goal(x, y);
   }
   
   // Returns the distance to the center of the target.
   public double distanceToTarget(double x, double y) {
      return target.getDistance(x, y);
   }
   
   // Returns the distance to the center of the target in the X-direction.
   public double distanceXTarget(double x) {
      return target.distanceXTarget(x);
   }
   
   // Returns the distance to the center of the target in the Y-direction.
   public double distanceYTarget(double y) {
      return target.distanceYTarget(y);
   }
   
   // Checks if a point lies witian a tree.
   public boolean hitsTree(double x, double y) {
      // Goes through all the trees.
      for (Tree tree: trees) {
         // Checks if the point lies within the tree.
         if (tree.isInRadius(x, y)) {
            return true;
         }
      }
      return false;
   }
   
   // Returns the coordinates of the center of all the trees.
   public double[][] getTreeCoordinates() {
      double[][] coordinates = new double[trees.size()][2];
      for (int i = 0; i < trees.size(); i++) {
         coordinates[i] = trees.get(i).getCoordinates();
      }
      return coordinates;
   }
   
   // Changes the speed of the stateVector if the ball bounces of a tree.
   public void checkAndBounceOfTree(double[] stateVector) {
      // Goes through all the trees.
      for (Tree tree: trees) {
         // Checks if the ball has hit the tree.
         if (tree.isInRadius(stateVector[0], stateVector[1])) {
            // Changes the speed accordingly.
            tree.bounceOfTree(stateVector);
         }
      }
   }
   
   // Changes the speed of the stateVector if the ball bounces of a wall.
   public boolean checkAndBounceOfWall(double[] stateVector) {
      boolean hasHitWall = false;
      
      // Goes through all the walls.
      for (Wall wall: walls) {
         // Checks if the ball has hit the wall.
         if (wall.hitsWall(stateVector)) {
            // Changes the speed accordingly.
            wall.bounceOfWall(stateVector);
            hasHitWall = true;
         }
      }
      return hasHitWall;
   }
   
   // Return the highest point of a set of point given by the paramteres.
   // Is used by the gui for determining the color of the grass at each height.
   public double getHeighestPoint(double x0, double y0, double rectangleX, double rectangleY, double noRectangles) {
      double heighestPoint = 0;
      // Goes trhough all the retangles.
      for( int i=0; i < noRectangles; i++) {    
         for( int j=0; j < noRectangles; j++) {
            // Calculates the height of the center of each rectangle.
            double centerXCoordinate = i*rectangleX + rectangleX/2 - x0;
            double centerYCoordinate = i*rectangleY + rectangleY/2 - y0;
            double height = getHeight(centerXCoordinate, centerYCoordinate);
            // Checks if the current point is the heighest.
            if (height > heighestPoint) {
               heighestPoint = height;
            }
         }
      }
      return heighestPoint;
   }
   
   // Returns the color of a point in the terrain.
   public Color getColor(double x, double y, double heighestPoint) {
      // A sandpit is yellow/orange.
      if (inSandpit(x, y)) {
         return  Color.rgb(255,255,0);
      }
      // A wall is brown.
      if (inWall(x, y)) {
         return Color.rgb(255,153,51);
      }
      // The area around the target is black.
      if (goal(x, y)) {
         return  Color.rgb(0,0,0);
      }
      // A lake is blue.
      double height = getHeight(x, y);
      if (height < 0) {
         return Color.rgb(0,0,150);
      }
      // The grass varies between light and dark green according to the height.
      int green = 255 - (int)(height / heighestPoint * 150);
      return Color.rgb(0,green,0);
   }
}