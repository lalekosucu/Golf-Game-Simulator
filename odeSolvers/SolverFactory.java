/* This class serves as a factory for building FirstOrderSolvers.
 * Given a solverType String the class will return the apropriate FirstOrderSolver.
 */

package odeSolvers;

import terrain.Terrain;

public class SolverFactory {
   public static ODESolver build(String solverType, String modelType, Terrain terrain) {
      // The Strings are set to lower case to make the factory case insensitive.
      solverType = solverType.toLowerCase();
      modelType = modelType.toLowerCase();
      
      Model model = null;
      switch (modelType) {
         case "simplemodel":
            model = new SimpleModel(terrain);
            break;
         case "completemodel":
            model = new CompleteModel(terrain);
            break;
         default:
            System.out.println("Invalid model type");
            break;
      }
      
      switch (solverType) {
         case "euler":
            return new Euler(terrain, model);
         case "ralston":
            return new Ralston2ndOrder(terrain, model);
         case "heuns3rd":
            return new Heuns3rdOrder(terrain, model);
         case "kuttas3rd":
            return new Kuttas3rdOrder(terrain, model);
         case "rungekutta4":
            return new RungeKutta4(terrain, model);
      }
      // When the solverType is not recognized an erro message will be printed.
      System.out.println("Invalid odeSolver type");
      return null;
   }
}