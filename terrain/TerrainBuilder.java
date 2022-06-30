/* This class read the data from the input files.
 * There are two types of files: terrain and moves.
 * Terrain files contain all the information about the terrain and its objects.
 * Moves files contain the starting position of the ball and possibly speed.
 */

package terrain;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class TerrainBuilder {
   
   // Creates an object of type Terrain by reading the terrain file.
   public static Terrain build(int instance) {
      try {
         // Used to read the file.
         File file = new File("terrain" + instance + ".txt");
         Scanner scan = new Scanner(file);
         
         // Sets the height profile and friction coeficients of the terrain.
         Terrain ter = new Terrain(scan.nextLine());
         String str = scan.nextLine();
         String[] s = str.split("\\s");
         ter.addFriction(Double.parseDouble(s[0]), Double.parseDouble(s[1]));
         
         // Adds all the object to the terrain.
         while (scan.hasNextLine()) {
            addObject(ter, scan.nextLine());
         }
         
         scan.close();
         return ter;
      } 
      catch (FileNotFoundException e) {
         e.printStackTrace();
      }
      return null;
   }
   
   // Adds an object to a terrain object.
   private static void addObject(Terrain ter, String object) {
      String[] s = object.split("\\s");
      
      // Checks which type of object is contained in the string.
      switch (s[0]) {
         // Adds the target.
         case "target":
            double xTarget = Double.parseDouble(s[1]);
            double yTarget = Double.parseDouble(s[2]);
            double rTarget = Double.parseDouble(s[3]);
            ter.addTarget(xTarget, yTarget, rTarget);
            break;
         // Adds a tree to the Terrain.
         case "tree":
            double xTree = Double.parseDouble(s[1]);
            double yTree = Double.parseDouble(s[2]);
            double rTree = Double.parseDouble(s[3]);
            ter.addTree(xTree, yTree, rTree);
            break;
         // Adds a sandpit to the Terrain.
         case "sandpit":
            double xMin = Double.parseDouble(s[1]);
            double yMin = Double.parseDouble(s[2]);
            double xMax = Double.parseDouble(s[3]);
            double yMax = Double.parseDouble(s[4]);
            double rSandpit = Double.parseDouble(s[5]);
            ter.addSandpit(xMin, yMin, xMax, yMax, rSandpit);
            break;
         // Adds a wall to the Terrain.
         case "wall":
            double x = Double.parseDouble(s[1]);
            double y = Double.parseDouble(s[2]);
            double width = Double.parseDouble(s[3]);
            double height = Double.parseDouble(s[4]);
            ter.addWall(x, y, width, height);
            break;
         // Some unrecognizable string is in the file.
         default:
            System.out.println("Wrong input TerrainBuilder.addObject(Terrain, String)");
      }
   }
   
   // Reads the coordinates and motion of the ball.
   public static double[] readInput(int n) {
      try {
         // Used to read the file.
         File file = new File("moves" + n + ".txt");
         Scanner scan = new Scanner(file);
         
         // Creates an input array to store the coordinates and motion of the ball.
         String str = scan.nextLine();
         String[] s1 = str.split("\\s");
         str = scan.nextLine();
         String[] s2 = str.split("\\s");
         double[] input = {Double.parseDouble(s1[0]), Double.parseDouble(s1[1]), Double.parseDouble(s2[0]), Double.parseDouble(s2[1])};
         scan.close();
         return input;
      } 
      catch (FileNotFoundException e) {
         e.printStackTrace();
      }
      return null;
   }
   
   // Reads only the coordinates of the ball.
   public static double[] getStartingPosition(int n) {
      try {
         // Used to read the file.
         File file = new File("moves" + n + ".txt");
         Scanner scan = new Scanner(file);
         
         // Creates an input array to store the coordinates of the ball.
         String str = scan.nextLine();
         String[] s1 = str.split("\\s");
         double[] input = {Double.parseDouble(s1[0]), Double.parseDouble(s1[1])};
         scan.close();
         return input;
      } 
      catch (FileNotFoundException e) {
         e.printStackTrace();
      }
      return null;
   }
}