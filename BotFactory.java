/* This class serves as a factory for building bots that can play the game.
 * Given a botType String the class will return the apropriate FirstOrderSolver.
 */

package bots;

import terrain.Terrain;

public class BotFactory {
   public static Bot build(String botType, Terrain terrain) {
      // The String is set to lower case to make the factory case insensitive.
      botType = botType.toLowerCase();
      switch (botType) {
         case "basicbot":
            return new BasicBot(terrain);
         case "hillclimbing":
            return new HillClimbing(terrain);
         case "ringsearch":
            return new RingSearch(terrain);
      }
      // When the solverType is not recognized an erro message will be printed.
      System.out.println("Invalid Bot type");
      return null;
   }
}