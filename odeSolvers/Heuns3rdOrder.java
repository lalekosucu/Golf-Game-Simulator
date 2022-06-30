/* This class uses Heun's 3rd order method to solve ordinary differential equations.
 */

package odeSolvers;

import terrain.Terrain;

public class Heuns3rdOrder implements ODESolver{
    
    private Terrain terrain;
    private Model model;
    
    public Heuns3rdOrder(Terrain terrain, Model model) {
       this.terrain = terrain;
       this.model = model;
    }
    
    public double[] getNextState(double[] stateVector) {

        //ki,1 = hi * f(ti,wi)

        double p1x = stateVector[0];
        double p1y = stateVector[1];
        double v1x = stateVector[2];
        double v1y = stateVector[3];

        double a1x = model.calculateAcceleration_x(stateVector); // the initial acceleration that velocityX has.
        double a1y = model.calculateAcceleration_y(stateVector); // the initial acceleration that velocityY has.

        double p2x =0, p2y = 0, v2x = 0, v2y = 0, a2x = 0, a2y = 0;

        // ki,2 = hi * f(ti + 1/3 * hi , wi + 1/3 * ki,1)

        p2x = p1x + 1/3.d * h * v1x;
        p2y = p1y + 1/3.d * h * v1y;
        v2x = v1x + 1/3.d * h * a1x;
        v2y = v1y + 1/3.d * h * a1y;

        double[] stateVector2 = new double[]{p2x , p2y , v2x , v2y};

        a2x = model.calculateAcceleration_x(stateVector2);
        a2y = model.calculateAcceleration_y(stateVector2);

        double p3x =0, p3y = 0, v3x = 0, v3y = 0, a3x = 0, a3y = 0;

        // ki,3 = hi * f(ti + 2/3 * hi , wi + 2/3 * ki,2)

        p3x = p1x + 2/3.d* h * v2x;
        p3y = p1y + 2/3.d* h * v2y;
        v3x = v1x + 2/3.d* h * a2x;
        v3y = v1y + 2/3.d* h * a2y;

        double[] stateVector3 = new double[]{p3x , p3y , v3x , v3y};

        a3x = model.calculateAcceleration_x(stateVector3);
        a3y = model.calculateAcceleration_y(stateVector3);

        //wi+1 = wi + 1/4 * (ki,1 + 3 * ki,3)

        double new_x  = p1x + h * 1/4.d * ( v1x + 3*v3x );
        double new_y  = p1y + h * 1/4.d * ( v1y + 3*v3y );
        double new_vx = v1x + h * 1/4.d * ( a1x + 3*a3x );
        double new_vy = v1y + h * 1/4.d * ( a1y + 3*a3y );

        return new double[]{new_x , new_y , new_vx , new_vy};
    }
    
}