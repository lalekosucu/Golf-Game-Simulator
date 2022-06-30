package simulators;

import java.io.PrintWriter;

import terrain.Terrain;
import odeSolvers.ODESolver;
import odeSolvers.SolverFactory;
import physicsEngine.PhysicsEngine;

public class TestModels {
   private static final double kineticFriction = 0.05;
   private static final double staticFriction = 0.2;
   private static final double[] input = {0.0, 0.0, 1.0, 0.0};
   
   private static final double maxSlope = 2.0 + Math.pow(10, -6);
   private static final double stepSize = 0.1;
   private static final double maxT = 0.25;
   
   private static final String defaultSolver = "RungeKutta4";
   private static final String[] modelNames = {
      "SimpleModel",
      "CompleteModel",
   };
   
   // The PrintWriter object is used to write the results into a csv file.
   private PrintWriter writer;
   
   // Constructor which immediately runs the test.
   public TestModels(PrintWriter writer) {
      this.writer = writer;
      writer.print("slope");
      for (String name: modelNames) {
         writer.print(","+name);
      }
      writer.println();
      
      run();
   }
   
   private void run() {
      for (double slope = 0; slope <= maxSlope; slope += stepSize) {
         System.out.println(slope);
         writer.print(slope);
         Terrain terrain = new Terrain(slope+"*x+1");
         terrain.addFriction(kineticFriction, staticFriction);
         for (String modelName: modelNames) {
            System.out.println(modelName);
            ODESolver solver = SolverFactory.build(defaultSolver, modelName, terrain);
            PhysicsEngine engine = new PhysicsEngine(terrain, solver);
            
            double[] result = engine.testMove(input, maxT);
            
            // Prints the results on the csv file.
            writer.print("," + result[0]);
         }
         writer.println();
      }
   }
}