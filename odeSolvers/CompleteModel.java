/* This object is used to calculate the acceleration using the complete physics description.
 */

package odeSolvers;

import terrain.Terrain;

public class CompleteModel implements Model{

   protected Terrain terrain;
   
   public CompleteModel(Terrain terrain) {
      this.terrain = terrain;
   }
   
   public double calculateAcceleration_x(double[] stateVector){
         //Variable declaration
         double slopeX = terrain.getSlopeX(stateVector[0], stateVector[1]);
         double slopeY = terrain.getSlopeY(stateVector[0], stateVector[1]);
         double kineticFriction = terrain.getFriction(stateVector[0], stateVector[1]);
         
         double component1 = g*slopeX/(1 + slopeX*slopeX + slopeY*slopeY);
         double component2 = kineticFriction*g/Math.sqrt(1 + slopeX*slopeX + slopeY*slopeY);
         double component3;
         
         // Calculation of acceleration if the ball is moving.
         if (Math.sqrt(stateVector[2]*stateVector[2] + stateVector[3]*stateVector[3]) > alpha){
             component3 = stateVector[2]/Math.sqrt(stateVector[2]*stateVector[2] + stateVector[3]*stateVector[3] 
                                                   + Math.pow((slopeX*stateVector[2] + slopeY*stateVector[3]) , 2));
         // Calculation of acceleration if the ball does not stay in rest.
         }else{
             component3 = slopeX/Math.sqrt(slopeX*slopeX + slopeY*slopeY + Math.pow((slopeX*slopeX + slopeY*slopeY) , 2));
         }
         return -component1 - (component2*component3);
    }
    public double calculateAcceleration_y(double[] stateVector){
         //Variable declaration
         double slopeX = terrain.getSlopeX(stateVector[0], stateVector[1]);
         double slopeY = terrain.getSlopeY(stateVector[0], stateVector[1]);
         double kineticFriction = terrain.getFriction(stateVector[0], stateVector[1]);
         
         double component1 = g*slopeY/(1 + slopeX*slopeX + slopeY*slopeY);
         double component2 = kineticFriction*g/Math.sqrt(1 + slopeX*slopeX + slopeY*slopeY);
         double component3;
         
         // Calculation of acceleration if the ball is moving.
         if (Math.sqrt(stateVector[2]*stateVector[2] + stateVector[3]*stateVector[3]) > alpha){
             component3 = stateVector[3]/Math.sqrt(stateVector[2]*stateVector[2] + stateVector[3]*stateVector[3] 
                                                   + Math.pow((slopeX*stateVector[2] + slopeY*stateVector[3]) , 2));
         // Calculation of acceleration if the ball does not stay in rest.
         }else{
             component3 = slopeY/Math.sqrt(slopeX*slopeX + slopeY*slopeY + Math.pow((slopeX*slopeX + slopeY*slopeY) , 2));
         }
         return -component1 - (component2*component3);
    }
}