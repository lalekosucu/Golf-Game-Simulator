/* This object is used to calculate the acceleration using a simpler physics description.
 */

package odeSolvers;

import terrain.Terrain;

public class SimpleModel implements Model{

   protected Terrain terrain;
   
   public SimpleModel(Terrain terrain) {
      this.terrain = terrain;
   }
   
   public double calculateAcceleration_x(double[] stateVector){
         //Variable declaration
         double xPrimePrime;
         double slopeX = terrain.getSlopeX(stateVector[0], stateVector[1]);
         double slopeY = terrain.getSlopeY(stateVector[0], stateVector[1]);
         double kineticFriction = terrain.getFriction(stateVector[0], stateVector[1]);
          
         // Calculation of acceleration if the ball is moving.
         if (Math.sqrt(stateVector[2]*stateVector[2] + stateVector[3]*stateVector[3]) > alpha){
             xPrimePrime = -g*slopeX-(kineticFriction*g)*(stateVector[2]/Math.sqrt((stateVector[2]*stateVector[2])+(stateVector[3]*stateVector[3])));
             
         // Calculation of acceleration if the ball does not stay in rest.
         }else{
             xPrimePrime = -g*kineticFriction*(slopeX/(Math.sqrt((slopeX)*(slopeX) + (slopeY)*(slopeY))));
         }
         return xPrimePrime;
    }
    public double calculateAcceleration_y(double[] stateVector){
         //Variable declaration
         double yPrimePrime;
         double slopeX = terrain.getSlopeX(stateVector[0], stateVector[1]);
         double slopeY = terrain.getSlopeY(stateVector[0], stateVector[1]);
         double kineticFriction = terrain.getFriction(stateVector[0], stateVector[1]);
         //double staticFriction = terrain.getStaticFriction(stateVector[0], stateVector[1]);
 
         // Calculation of acceleration if the ball is moving.
         if (Math.sqrt(stateVector[2]*stateVector[2] + stateVector[3]*stateVector[3]) > alpha){
             yPrimePrime = -g*slopeY-(kineticFriction*g)*(stateVector[3]/Math.sqrt((stateVector[2]*stateVector[2])+(stateVector[3]*stateVector[3])));
 
         // Calculation of acceleration if the ball does not stay in rest.
         }else{ 
             yPrimePrime = -g*kineticFriction*(slopeY/(Math.sqrt((slopeX)*(slopeX) + (slopeY)*(slopeY))));
         }
         return yPrimePrime;
    }
}