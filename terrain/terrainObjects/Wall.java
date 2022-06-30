/* This class contains all the data of a wall in the terrain.
 */

package terrain.terrainObjects;

public class Wall {
   // Coordinates of the upper left point of the wall.
   private double x;
   private double y;
   // Size of the wall.
   private double width;
   private double height;
   
   // Constructor
   public Wall(double x, double y, double width, double height) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
   }
   
   // Returns whether a point is within the boundary of the wall.
   // This is used by the gui for coloring the terrain.
   public boolean inWall(double x, double y) {
      if (x >= this.x && x <= this.x+width && y >= this.y && y <= this.y+height) {
         return true;
      }
      return false;
   }
   
   // Returns wheter the ball has hit the ball.
   public boolean hitsWall(double[] stateVector) {
      if (stateVector[0] >= x && stateVector[0] <= x+width && stateVector[1] >= y && stateVector[1] <= y+height) {
         return true;
      }
      return false;
   }
   
   // Changes the speed of the ball, when it hits the wall.
   // The speed changes in the same way as light when it is reflected of a mirror.
   public void bounceOfWall(double[] stateVector) {
      double dx = Math.min(Math.abs(stateVector[0] - x), Math.abs(stateVector[0] - (x+width)));
      double dy = Math.min(Math.abs(stateVector[1] - y), Math.abs(stateVector[1] - (y+height)));
      
      if (dx < dy) {
         stateVector[2] = -stateVector[2];
      }
      else if (dx == dy) {
         stateVector[2] = -stateVector[2];
         stateVector[3] = -stateVector[3];
      }
      else {
         stateVector[3] = -stateVector[3];
      }
   }
}