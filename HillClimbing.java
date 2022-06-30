/* This class will decide at which velocity the ball should be hit,
 * given a terrain and starting coordinates.
 * It uses a HillClimbing aproach.
 */

package bots;

import physicsEngine.PhysicsEngine;
import terrain.Terrain;

public class HillClimbing implements Bot{
   
   // The boundary of the allowed domain of vx and vy.
   private static final double maxDistance = 5.0;
   // Constant used to determine the smallest step size.
   private static final double error = 0.1;
   // Constants used for determining the accuracy of the angle.
   private static final int noIterations = 4;
   private static final int noPoints = 8;
   
   private Terrain terrain;
   private PhysicsEngine engine;
   private double stepSize = maxDistance;
   
   // Constructor.
   public HillClimbing(Terrain terrain) {
      this.terrain = terrain;
      engine = new PhysicsEngine(terrain);
   }
   
   // Method that is called to get the next velocity at which the ball will be hit.
   public double[] getNextMove(double x, double y) {
      double[] coordinates = {x, y};
      double[] speed = {0.0, 0.0};
      
      // The step size starts at maxDistance/2 and will be divided by 2 at every step,
      // until the step size is lower than the given error constant.
      while (stepSize > error) {
         stepSize /= 2;
         
         // Gets the best angle at which the next step can be taken and takes that step.
         Angle bestAngle = getBestAngle(coordinates, speed);
         speed[0] += stepSize * Math.sin(bestAngle.angle);
         speed[1] += stepSize * Math.cos(bestAngle.angle);
      }
      
      return speed;
   }
   
   // Returns the best angle at which the current vx and vy can be aproved.
   private Angle getBestAngle(double[] coordinates, double[] speed) {
      Angle[] startingValues = new Angle[noPoints];
      int indexBestAngle = -1;
      double shortestDistance = Double.POSITIVE_INFINITY;
      
      // Tests noPoints angles that are equaly spread out in a circle.
      for (int i = 0; i < noPoints; i++) {
         // Generates a new Angle.
         double angle = i * Math.PI * 2/noPoints;
         startingValues[i] = new Angle(angle, coordinates, speed);
         
         // Tests if the new angle is better than the previous best.
         // If the new angle is better the best angle will be updated.
         if (startingValues[i].distance < shortestDistance) {
            indexBestAngle = i;
            shortestDistance = startingValues[i].distance;
         }
      }
      
      // A new array of size noPoints+1 is created.
      // In the midle of the array the best angle is stores.
      // Its neighboring angles are stored a the outer points of the array.
      Angle[] nextIteration = new Angle[noPoints+1];
      int midpoint = noPoints/2;
      nextIteration[0] = startingValues[(indexBestAngle-1+noPoints)%noPoints];
      nextIteration[midpoint] = startingValues[indexBestAngle];
      nextIteration[noPoints] = startingValues[(indexBestAngle+1)%noPoints];
      indexBestAngle = midpoint;
      
      // The accuracy of the angle is improved noIteration times.
      for (int i = 0; i < noIterations; i++) {
         // The first half of the array is filled with new angles that lie between the best angle and its neighbor stored at position 0.
         // There is equal space between every neighboring pair of angles.
         for (int j = 1; j < midpoint; j++) {
            // Generates a new angle.
            double angle = ((midpoint-j) * nextIteration[0].angle + j * nextIteration[midpoint].angle) / midpoint;
            nextIteration[j] = new Angle(angle, coordinates, speed);
            // Tests if the new angle is better than the previous best.
            // If the new angle is better the best angle will be updated.
            if (nextIteration[j].distance < shortestDistance) {
               indexBestAngle = j;
               shortestDistance = nextIteration[j].distance;
            }
         }
         
         // The second half of the array is filled with new angles that lie between the best angle and its neighbor stored at position noPoints.
         // There is equal space between every neighboring pair of angles.
         for (int j = midpoint+1; j < noPoints; j++) {
            // Generates a new angle.
            double angle = ((noPoints-j) * nextIteration[midpoint].angle + (j-midpoint) * nextIteration[noPoints].angle) / (noPoints - midpoint);
            nextIteration[j] = new Angle(angle, coordinates, speed);
            // Tests if the new angle is better than the previous best.
            // If the new angle is better the best angle will be updated.
            if (nextIteration[j].distance < shortestDistance) {
               indexBestAngle = j;
               shortestDistance = nextIteration[j].distance;
            }
         }
         
         // In the midle of the array the best angle is stores.
         // Its neighboring angles are stored a the outer points of the array.
         nextIteration[0] = nextIteration[indexBestAngle-1];
         nextIteration[noPoints] = nextIteration[indexBestAngle+1];
         nextIteration[midpoint] = nextIteration[indexBestAngle];
         indexBestAngle = midpoint;
      }
      return nextIteration[midpoint];
   }
   
   // An inner class used to store information about each angle.
   // It is also used to calculate the accuracy of each angle 
   // and the corresponding values of vx and vy of each angle.
   private class Angle {
      public final double angle;
      public final double distance;
      public Angle(double angle, double[] coordinates, double[] speed) {
         this.angle = angle;
         this.distance = getDistance(coordinates, speed);
      }
      
      // Generates the distance to the taget.
      private double getDistance(double[] coordinates, double[] speed) {
         double vx = speed[0] + stepSize * Math.sin(angle);
         double vy = speed[1] + stepSize * Math.cos(angle);
         double[] newSpeed = {vx, vy};
         double[] newCoordinates = engine.testMove(coordinates, newSpeed);
         return terrain.distanceToTarget(newCoordinates[0], newCoordinates[1]);
      }
      
      // Generates the corresponding vx and vy.
      public double[] getVxVy(double vx, double vy) {
         double[] vXvY = new double[2];
         vXvY[0] = Math.sin(angle);
         vXvY[1] = Math.cos(angle);
         return vXvY;
      }
   }
}