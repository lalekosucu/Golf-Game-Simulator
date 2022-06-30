/* This class uses Runge-Kutta 3 method to solve ordinary differential equations.
 */

package odeSolvers;

import terrain.Terrain;

public class Kuttas3rdOrder implements ODESolver{
    
    private Terrain terrain;
    private Model model;
    
    public Kuttas3rdOrder(Terrain terrain, Model model) {
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

        // ki,2 = hi * f(ti + 1/2 * hi , wi + 1/2 * ki,1)

        p2x = p1x + 1/2.d * h * v1x;
        p2y = p1y + 1/2.d * h * v1y;
        v2x = v1x + 1/2.d * h * a1x;
        v2y = v1y + 1/2.d * h * a1y;

        double[] stateVector2 = new double[]{p2x , p2y , v2x , v2y};

        a2x = model.calculateAcceleration_x(stateVector2);
        a2y = model.calculateAcceleration_y(stateVector2);

        double p3x =0, p3y = 0, v3x = 0, v3y = 0, a3x = 0, a3y = 0;

        // ki,3 = hi * f(ti + hi , wi - ki,1 + 2 * ki,2)

        p3x = p1x + - h * v1x + 2 * h * v2x;
        p3y = p1y + - h * v1y + 2 * h * v2y;
        v3x = v1x + - h * a1x + 2 * h * a2x;
        v3y = v1y + - h * a1y + 2 * h * a2y;

        double[] stateVector3 = new double[]{p3x , p3y , v3x , v3y};

        a3x = model.calculateAcceleration_x(stateVector3);
        a3y = model.calculateAcceleration_y(stateVector3);

        //wi+1 = wi + 1/6 * (ki,1 + 4 * ki,2 + ki,3)

        double new_x  = p1x + h * 1/6.d * ( v1x + 4*v2x + v3x );
        double new_y  = p1y + h * 1/6.d * ( v1y + 4*v2y + v3y );
        double new_vx = v1x + h * 1/6.d * ( a1x + 4*a2x + a3x );
        double new_vy = v1y + h * 1/6.d * ( a1y + 4*a2y + a3y );

        return new double[]{new_x , new_y , new_vx , new_vy};
    }

}