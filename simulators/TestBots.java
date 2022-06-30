/* This class test all bots on all terrains.
 */

package simulators;

import java.io.PrintWriter;

import bots.Bot;
import bots.BotFactory;
import terrain.Terrain;
import terrain.TerrainBuilder;
import physicsEngine.PhysicsEngine;

import physicsEngine.BallCoordinates;

public class TestBots {
   
   private static final int noTerrains = 6;
   private static final String[] botNames = {
      "basicbot",
      "hillclimbing"
   };
   
   // The PrintWriter object is used to write the results into a csv file.
   private PrintWriter writer;
   private Terrain terrain;
   private PhysicsEngine engine;
   private double[] input;
   
   // Constructor which immediately runs the test.
   public TestBots(PrintWriter writer) {
      this.writer = writer;
      
      // Writes the bot names in the file.
      for (int i = 0; i < botNames.length; i++) {
        writer.print("," + botNames[i]);
      }
      writer.println();
      
      run();
   }
   
   // Runs the test.
   private void run() {
      // Goes trhough all the instances.
      for (int instance = 1; instance <= noTerrains; instance++) {
         terrain = TerrainBuilder.build(instance);
         engine = new PhysicsEngine(terrain);
         input = TerrainBuilder.getStartingPosition(instance);
         
         writer.print(instance);
         // Goes through all the bots.
         for (int i = 0; i < botNames.length; i++) {
            // Gets the output of the bot.
            Bot bot = BotFactory.build(botNames[i], terrain);
            double[] move = bot.getNextMove(input[0], input[1]);
            // Tests the output of the bot and prints the results in the file.
            double[] stateVector = {input[0], input[1], move[0], move[1]};
            double[] result = engine.testMove(stateVector);
            writer.print("," + terrain.distanceToTarget(result[0], result[1]));
         }
         // The results of every instance is printed on a new line.
         writer.println();
      }
   }
}