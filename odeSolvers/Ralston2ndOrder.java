/* This class uses Ralstons 2nd order method to solve ordinary differential equations.
 */

package odeSolvers;

import terrain.Terrain;

public class Ralston2ndOrder implements ODESolver{

    private Terrain terrain;
    private Model model;
    
    public Ralston2ndOrder(Terrain terrain, Model model) {
       this.terrain = terrain;
       this.model = model;
    }

    public double[] getNextState(double[] stateVector) {
    
        double p1x = stateVector[0];
        double p1y = stateVector[1];
        double v1x = stateVector[2];
        double v1y = stateVector[3];

        //ki,1 = hi * f(ti,wi)

        double a1x = model.calculateAcceleration_x(stateVector); // the initial acceleration that velocityX has.
        double a1y = model.calculateAcceleration_y(stateVector); // the initial acceleration that velocityY has.

        double p2x =0, p2y = 0, v2x = 0, v2y = 0, a2x = 0, a2y = 0;
            
        // ki,2 = hi * f(ti + 2/3 * hi , wi + 2/3 * ki,1)

        p2x = p1x + 2/3.d * h * v1x;   
        p2y = p1y + 2/3.d * h * v1y;
        v2x = v1x + 2/3.d * h * a1x;
        v2y = v1y + 2/3.d * h * a1y;

        double[] stateVector2 = new double[]{p2x , p2y , v2x , v2y};

        a2x = model.calculateAcceleration_x(stateVector2);
        a2y = model.calculateAcceleration_y(stateVector2);

        //wi+1 = wi + 1/4 * (ki,1 + 3 * ki,2)

        double new_x  = p1x + h * 1/4.d * ( v1x + 3*v2x );
        double new_y  = p1y + h * 1/4.d * ( v1y + 3*v2y );
        double new_vx = v1x + h * 1/4.d * ( a1x + 3*a2x );
        double new_vy = v1y + h * 1/4.d * ( a1y + 3*a2y );

        return new double[]{new_x , new_y , new_vx , new_vy};
    }
}
