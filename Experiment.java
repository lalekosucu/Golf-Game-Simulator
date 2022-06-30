/* This class is used to run either the TestBot or TestSolver class.
 */

import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;

import simulators.TestSolvers;
import simulators.TestBots;
import simulators.TestModels;

public class Experiment {
   // The instance is only used for TestSolver.
   private static final int instance = 1;
   
   // Name of the file in which the results will be printed.
   private static final String fileName = "results";
   private static final String testType = "TestModel";
   
   public static void main(String[] args) {
      try {
         // Creates a PrintWriter with a new csv file for the results.
         PrintWriter writer = new PrintWriter(new File(fileName + ".csv"));
         
         // Runs the chosen test.
         switch (testType) {
         case "TestSolvers":
            new TestSolvers(writer, instance);
            break;
         case "TestBots":
            new TestBots(writer);
            break;
         case "TestModel":
            new TestModels(writer);
            break;
         default:
            System.out.println("Invalid test type");
            break;
         }
         
         writer.close();
      } 
      catch (FileNotFoundException exc) {
         exc.printStackTrace();
      }
   }
}