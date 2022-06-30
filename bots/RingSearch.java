/* This class will decide at which velocity the ball should be hit,
 * given a terrain and starting coordinates.
 * The algorithm that is used is called Ring Search.
 */

package bots;

import physicsEngine.PhysicsEngine;
import terrain.Terrain;

public class RingSearch implements Bot{
   
   // Constants
   public static final double initialAccuracy = 1.0;
   public static final double noSteps = 4.0;
   public static final double maxSpeed = 5.0;
   public static final double noRings = maxSpeed/initialAccuracy;
   
   private Terrain terrain;
   private PhysicsEngine engine;
   
   // Constructor
   public RingSearch(Terrain terrain) {
      this.terrain = terrain;
      engine = new PhysicsEngine(terrain);
   }
   
   // Method that is called to get the next velocity at which the ball will be hit.
   public double[] getNextMove(double x, double y) {
      // Initialization of variables.
      double beginRing = 0.0;
      double endRing = 2.0*Math.PI;
      double r = maxSpeed/2.0;
      double accuracy = initialAccuracy;
      double[] coordinates = {x, y};
      
      Ring bestRing = null;
      for (int i = 0; i < noSteps; i++) {
         System.out.println("step" + i + " " + noRings);
         double bestDistance = Double.POSITIVE_INFINITY;
         // Goes through all the rings to find the best point.
         for (int j = 0; j < noRings; j++) {
            System.out.println("ring" + j);
            double nextRadius = r + (j - noRings/2.0)*accuracy;
            // Checks if a ring is feasible.
            if (nextRadius>=0 && nextRadius<=maxSpeed) {
               Ring ring = new Ring(beginRing, endRing, nextRadius, accuracy, coordinates);
               // Checks if a ring has the best value.
               if (bestRing == null || ring.getBestDistance() < bestRing.getBestDistance()) {
                  bestDistance = ring.getBestDistance();
                  bestRing = ring;
               }
            }
         }
         
         // The variables are updated.
         double ringLength = endRing-beginRing;
         double bestAngle = bestRing.getBestAngle();
         beginRing = bestAngle-ringLength/2.0;
         endRing = bestAngle+ringLength/2.0;
         r = bestRing.getRadius();
         accuracy /= 2.0;
      }
      return bestRing.getBestMove();
   }
   
   // This object is used to calculate the best point on a ring.
   private class Ring {
      private double r;
      private boolean isFullRing;
      private double[] vx;
      private double[] vy;
      private double[] distance;
      
      public Ring(double beginRing, double endRing, double r, double accuracy, double[] coordinates) {
         this.r = r;
         isFullRing = (endRing-beginRing == 2*Math.PI);
         setRing(beginRing, endRing, r, accuracy, coordinates);
      }
      
      // When the radius is to small, it will be concidered to be the point in the center of the domain.
      private void setPoint(double[] coordinates) {
         isFullRing = true;
         vx = new double[]{0.0};
         vy = new double[]{0.0};
         distance = new double[]{getDistance(vx[0], vy[0], coordinates)};
      }
      
      // Sets all the coordinates of all the point on the ring with their distance to the target.
      private void setRing(double beginRing, double endRing, double r, double accuracy, double[] coordinates) {
         int noPoints = (int)((endRing-beginRing)*r/accuracy);
         if (noPoints == 0) {
            setPoint(coordinates);
            return;
         }
         // The distance between point on the ring.
         double stepSize = (endRing-beginRing)/(double)noPoints;
         
         // When the ring is not full, there will be a point at the outer point.
         if (!isFullRing) {
            noPoints++;
         }
         
         vx = new double[noPoints];
         vy = new double[noPoints];
         distance = new double[noPoints];
         
         // Fills in the values for vx, vy and distance for all the points on the ring.
         for (int i = 0; i < noPoints; i++) {
            vx[i] = Math.cos(beginRing+i*stepSize)*r;
            vy[i] = Math.sin(beginRing+i*stepSize)*r;
            distance[i] = getDistance(vx[i], vy[i], coordinates);
         }
      }
      
      // Get the distance from the ball to the target after it has been hit.
      private double getDistance(double vx, double vy, double[] coordinates) {
         double[] newSpeed = {vx, vy};
         double[] newCoordinates = engine.testMove(coordinates, newSpeed);
         return terrain.distanceToTarget(newCoordinates[0], newCoordinates[1]);
      }
      
      // Return the lowerest distance of all the points.
      public double getBestDistance() {
         return distance[bestIndex()];
      }
      
      // Returns the vx and vy of the best move.
      public double[] getBestMove() {
         int bestIndex = bestIndex();
         return new double[]{vx[bestIndex], vy[bestIndex]};
      }
      
      // Returns the angle of the best point.
      public double getBestAngle() {
         int bestIndex = bestIndex();
         return Math.atan(vy[bestIndex]/vx[bestIndex]);
      }
      
      // Returns the radius of this ring.
      public double getRadius() {
         return r;
      }
      
      // Returns the index of the best point.
      private int bestIndex() {
         double bestDistance = Double.POSITIVE_INFINITY;
         int bestIndex = -1;
         for (int i = 0; i < distance.length; i++) {
            double newDistance = distance[i];
            if (newDistance < bestDistance) {
               bestDistance = newDistance;
               bestIndex = i;
            }
         }
         return bestIndex;
      }
   }
}