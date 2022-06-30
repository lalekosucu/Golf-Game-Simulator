/* This solver implements the Euler method.
 */

package odeSolvers;

import terrain.Terrain;

public class Euler implements ODESolver{
    
    private Terrain terrain;
    private Model model;
    
    public Euler(Terrain terrain, Model model) {
       this.terrain = terrain;
       this.model = model;
    }
    
    @Override
    public double[] getNextState(double[] stateVector) {
        double xPrimePrime = model.calculateAcceleration_x(stateVector);
        double yPrimePrime = model.calculateAcceleration_y(stateVector);
        
        // The next step in the Euler method.
        stateVector[0]=stateVector[0] + h*stateVector[2];
        stateVector[1]=stateVector[1] + h*stateVector[3];
        stateVector[2]=stateVector[2] + h*xPrimePrime;
        stateVector[3]=stateVector[3] + h*yPrimePrime;

        return stateVector; 
    }
}
