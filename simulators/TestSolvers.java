/* This class test all FirstOrderSolver.
 */

package simulators;

import java.io.PrintWriter;

import odeSolvers.ODESolver;
import odeSolvers.SolverFactory;
import terrain.Terrain;
import terrain.TerrainBuilder;
import physicsEngine.PhysicsEngine;

public class TestSolvers {
   
   private static String defaultModel = "SimpleModel";
   private static String[] solverNames = {
      "euler",
      "midpoint",
      "ralston",
      "heun",
      "rungekutta4"
   };
   
   // The PrintWriter object is used to write the results into a csv file.
   private PrintWriter writer;
   private Terrain terrain;
   private double[] input;
   
   // Constructor which immediately runs the test.
   public TestSolvers(PrintWriter writer, int instance) {
      this.writer = writer;
      writer.println(",x coordinate,y coordinate,run time");
      terrain = TerrainBuilder.build(instance);
      input = TerrainBuilder.readInput(instance);
      
      run();
   }
   
   // Runs the test.
   private void run() {
      // Goes through all the solvers.
      for (int i = 0; i < solverNames.length; i++) {
         // Creates a PhysicsEngine with the given solver type.
         ODESolver solver = SolverFactory.build(solverNames[i], defaultModel, terrain);
         PhysicsEngine engine = new PhysicsEngine(terrain, solver);
         
         // Tests the solver and keeps track of its running time.
         long startTime = System.nanoTime();
         double[] result = engine.testMove(input);
         long endTime = System.nanoTime();
         
         // Prints the results on the csv file.
         writer.println(solverNames[i] + "," + result[0] + "," + result[1] + "," + (endTime-startTime));
      }
   }
}