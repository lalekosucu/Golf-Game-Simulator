/* This class is used by the gui to get notified, 
 * when the coordinates of the ball change.
 * Since there is only one ball in the game, 
 * every method in this class is static.
 */

package physicsEngine;

import java.util.*;

import gui.Observer;

public class BallCoordinates {
   // Default coordinates.
   private static double x = 0.0;
   private static double y = 0.0;
   
   // List of observers.
   private static ArrayList<Observer> observers = new ArrayList<Observer>();
   
   // Changes the state of the ball.
   // Can only be done by a PhysicsEngine.
   protected static void setNewCoordinates(double newX, double newY) {
      x = newX;
      y = newY;
      // Updates all the observers.
      for (int i = 0; i < observers.size(); i++) {
         observers.get(i).update();
      }
   }
   
   // Returns the current coordinates of the ball.
   public static double[] getCoordinates() {
      double[] coordinates = {x, y};
      return coordinates;
   }
   
   // Returns the current X-coordinate of the ball.
   public static double getX() {
      return x;
   }
   
   // Returns the current X-coordinate of the ball.
   public static double getY() {
      return y;
   }
   
   // Adds an observer to the list of observers.
   public static void addObserver(Observer observer) {
      observers.add(observer);
   }
   
}