/* This class generates the motion of the ball when is moves with some speed over a terrain.
 * The motion of the ball is calculated using Eulers methos.
 */

package physicsEngine;

import java.util.Arrays;
import java.util.ArrayList;

import terrain.Terrain;
import odeSolvers.ODESolver;
import odeSolvers.SolverFactory;
import odeSolvers.Model;

public class PhysicsEngine {
   // The solver that is used by default.
   private static final String defaultSolver = "Euler";
   private static final String defaultModel = "SimpleModel";
   
   // Contains all information needed about the golf course.
   private Terrain ter;
   private ODESolver solver;
   
   // Constructs a PhysicsEngine object.
   public PhysicsEngine(Terrain ter) {
      this.ter = ter;
      solver = SolverFactory.build(defaultSolver, defaultModel, ter);
   }
   
   // Construct a PhysicsEngine object with a given solver.
   public PhysicsEngine(Terrain ter, ODESolver solver) {
      this.ter = ter;
      this.solver = solver;
   }
   
   // Generates the motion of the ball.
   // Returns a list of all the coordinates the ball has visited.
   public void getNextCoordinates(double[] stateVector) {
      double[] startPosition = {stateVector[0], stateVector[1]};
      
      // Gets the next step as long as the ball is moving.
      double totalSpeed = Math.sqrt(stateVector[2]*stateVector[2] + stateVector[3]*stateVector[3]);
      
      while (totalSpeed > Model.alpha || rollsDown(stateVector)){
         stateVector = solver.getNextState(stateVector);
         
         // The ball has hit the water.
         if (ter.getHeight(stateVector[0], stateVector[1]) < 0) {
            BallCoordinates.setNewCoordinates(startPosition[0], startPosition[1]);
            break;
         }
         
         // Checks if the ball has hit a tree or a wall.
         // When the ball hits a tree or a wall, stateVector will be updated.
         // Because the ball bounces of the tree or wall.
         ter.checkAndBounceOfTree(stateVector);
         if (ter.checkAndBounceOfWall(stateVector)) {
            stateVector[0] = BallCoordinates.getX();
            stateVector[1] = BallCoordinates.getY();
         }
         
         // Updates the coordinates of the ball.
         BallCoordinates.setNewCoordinates(stateVector[0], stateVector[1]); 
         totalSpeed = Math.sqrt(stateVector[2]*stateVector[2] + stateVector[3]*stateVector[3]);
      }
   }
   
   // Returns the point were the ball stops after it has been hit.
   public double[] testMove(double[] coordinates, double[] speed) {
      double[] stateVector = {coordinates[0], coordinates[1], speed[0], speed[1]};
      return testMove(stateVector);
   }
   
   // Returns the point were the ball stops after it has been hit.
   public double[] testMove(double[] stateVector) {
      double[] startPosition = {stateVector[0], stateVector[1]};
      
      // Gets the next step as long as the ball is moving.
      double totalSpeed = Math.sqrt(stateVector[2]*stateVector[2] + stateVector[3]*stateVector[3]);
      
      while (totalSpeed > Model.alpha || rollsDown(stateVector)){
         double previousX = stateVector[0];
         double previousY = stateVector[1];
         
         stateVector = solver.getNextState(stateVector);
         
         // The ball has hit the water.
         if (ter.getHeight(stateVector[0], stateVector[1]) < 0) {
            return startPosition;
         }
         
         // Checks if the ball has hit a tree or a wall.
         // When the ball hits a tree or a wall, stateVector will be updated.
         // Because the ball bounces of the tree or wall.
         ter.checkAndBounceOfTree(stateVector);
         if (ter.checkAndBounceOfWall(stateVector)) {
            stateVector[0] = previousX;
            stateVector[1] = previousY;
         }
          
         totalSpeed = Math.sqrt(stateVector[2]*stateVector[2] + stateVector[3]*stateVector[3]);
      }
      
      // Gets the last coordinates.
      double[] newCoordinates = {stateVector[0], stateVector[1]};
      return newCoordinates;
   }
   
   // Returns coordinates of the ball after maxT seconds.
   public double[] testMove(double[] startPosition, double maxT) {
      double[] stateVector = {startPosition[0], startPosition[1], startPosition[2], startPosition[3]};
      
      for (double t = 0; t < maxT; t+=(ODESolver.h)){
         stateVector = solver.getNextState(stateVector);
      }
      
      // Gets the last coordinates.
      double[] newCoordinates = {stateVector[0], stateVector[1]};
      
      return newCoordinates;
   }
   
   //Returns the absolute error of the coordinates of ball when t=1. 
   //This method is used for the experiments which are obtained using terrain 7. 
   public double[] testMoveTerrain7(double[] stateVector, double maxT) {
      final double exactValue = 1.2369796550818442265678443550921925915764280185483755151821327926;
      double[] xCoordinate_AbsError = new double[3];
      double absoluteError;
      double step=0;

      for (double t = 0; t < maxT; t+=(ODESolver.h)){ 

         stateVector = solver.getNextState(stateVector);
         System.out.println(t + " ");
         step++;
         
         if(step == 10 || step == 100 || step == 1000 || step == 10000){
            
            absoluteError = absoluteError(exactValue, stateVector[0]);

            xCoordinate_AbsError[0]=stateVector[0]; // the x coordinate that solvers found.
            xCoordinate_AbsError[1]=absoluteError;  // the absolute error of the approximation. 
            xCoordinate_AbsError[2]=exactValue;

            System.out.println("result "+ xCoordinate_AbsError[0] + " " + xCoordinate_AbsError[1] + " " + xCoordinate_AbsError[2]+ " ");
         }   
      }
      return xCoordinate_AbsError;
   }
   
   // This method calculates and then returns absolute error.
   public double absoluteError(double exactValue, double approximation){
      double absoluteError = Math.abs(exactValue-approximation); 
      return absoluteError;
   }
   
   // Checks if the gravitational force is bigger than static friction.
   public boolean rollsDown(double[] stateVector) {
      double x = stateVector[0];
      double y = stateVector[1];
      double vx = stateVector[2];
      double vy = stateVector[3];
      
      double slopeX = ter.getSlopeX(x, y);
      double slopeY = ter.getSlopeY(x, y);
      
      boolean check = ter.getStaticFriction(x, y) < (Math.sqrt((slopeX)*(slopeX) + (slopeY)*(slopeY)));
      return check;  
   }
}