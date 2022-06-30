/* This simulator can be used to play one move with a given bot.
 */

package simulators;

import java.util.Random;

import terrain.Terrain;
import bots.Bot;
import bots.BotFactory;
import physicsEngine.PhysicsEngine;
import physicsEngine.BallCoordinates;

public class PlayBot {
   private static final double errorBound = 0.1;
   
   private Bot bot;
   private Terrain terrain;
   private PhysicsEngine pe;
   
   // Constructor.
   public PlayBot(String botType, Terrain terrain) {
      this.bot = BotFactory.build(botType, terrain);
      this.terrain = terrain;
      this.pe = new PhysicsEngine(terrain);
   }
   
   // Plays one move of the bot.
   public void play(double[] startCoordinates) {
      play(startCoordinates[0], startCoordinates[1]);
   }
   
   // Plays one move of the bot.
   public void play(double x, double y) {
      double[] stateVector = {x, y, 0, 0};
      
      // Gets the next move of the bot and plays it using a PhysicsEngine.
      double[] move = bot.getNextMove(stateVector[0], stateVector[1]);
      stateVector[2] = move[0];
      stateVector[3] = move[1];
      pe.getNextCoordinates(stateVector);
      
      // Gets the next coordinate and prints some data about the move.
      double[] newCoordinates = BallCoordinates.getCoordinates();
      System.out.println(move[0] + " " + move[1]);
      System.out.println(newCoordinates[0] + " " + newCoordinates[1]);
      System.out.println(terrain.distanceToTarget(newCoordinates[0], newCoordinates[1]));
      
   }
   
   // Plays one move of the bot with noise.
   public void playWithNoise(double[] startCoordinates) {
      playWithNoise(startCoordinates[0], startCoordinates[1]);
   }
   
   // Plays one move of the bot with noise.
   public void playWithNoise(double x, double y) {
      Random error = new Random();
      x += error.nextDouble()*2*errorBound - errorBound;
      y += error.nextDouble()*2*errorBound - errorBound;
      
      double[] stateVector = {x, y, 0, 0};
      
      // Gets the next move of the bot and plays it using a PhysicsEngine.
      double[] move = bot.getNextMove(stateVector[0], stateVector[1]);
      stateVector[2] = move[0] + (error.nextDouble()*2*errorBound - errorBound);
      stateVector[3] = move[1] + (error.nextDouble()*2*errorBound - errorBound);
      pe.getNextCoordinates(stateVector);
      
      // Gets the next coordinate and prints some data about the move.
      double[] newCoordinates = BallCoordinates.getCoordinates();
      System.out.println(move[0] + " " + move[1]);
      System.out.println(newCoordinates[0] + " " + newCoordinates[1]);
      System.out.println(terrain.distanceToTarget(newCoordinates[0], newCoordinates[1]));
      
   }
}