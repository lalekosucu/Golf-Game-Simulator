/* This class uses Runge-Kutta 4 methods to solve ordinary differential equations.
 */

package odeSolvers;

import terrain.Terrain;

public class RungeKutta4 implements ODESolver{
    
    private Terrain terrain;
    private Model model;
    
    public RungeKutta4(Terrain terrain, Model model) {
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

        p2x = p1x + 0.5 * h * v1x;
        p2y = p1y + 0.5 * h * v1y;
        v2x = v1x + 0.5 * h * a1x;
        v2y = v1y + 0.5 * h * a1y;

        double[] stateVector2 = new double[]{p2x , p2y , v2x , v2y};

        a2x = model.calculateAcceleration_x(stateVector2);
        a2y = model.calculateAcceleration_y(stateVector2);

        double p3x =0, p3y = 0, v3x = 0, v3y = 0, a3x = 0, a3y = 0;

        // ki,3 = hi * f(ti + 1/2 * hi , wi + 1/2 * ki,2)

        p3x = p1x + 0.5 * h * v2x;
        p3y = p1y + 0.5 * h * v2y;
        v3x = v1x + 0.5 * h * a2x;
        v3y = v1y + 0.5 * h * a2y;

        double[] stateVector3 = new double[]{p3x , p3y , v3x , v3y};

        a3x = model.calculateAcceleration_x(stateVector3);
        a3y = model.calculateAcceleration_y(stateVector3);

        double p4x =0, p4y = 0, v4x = 0, v4y = 0, a4x = 0, a4y = 0;

        // ki,4 = hi * f(ti + hi , wi + ki,3)

        p4x = p1x + 1 * h * v3x;
        p4y = p1y + 1 * h * v3y;
        v4x = v1x + 1 * h * a3x;
        v4y = v1y + 1 * h * a3y;

        double[] stateVector4 = new double[]{p4x , p4y , v4x , v4y};

        a4x = model.calculateAcceleration_x(stateVector4);
        a4y = model.calculateAcceleration_y(stateVector4);

        //wi+1 = wi + 1/6 * (ki,1 + 2 * ki,2 + 2 * ki,3 + ki,4)

        double new_x  = p1x + h * 1/6.d * ( v1x + 2*v2x + 2*v3x + v4x);
        double new_y  = p1y + h * 1/6.d * ( v1y + 2*v2y + 2*v3y + v4y);
        double new_vx = v1x + h * 1/6.d * ( a1x + 2*a2x + 2*a3x + a4x);
        double new_vy = v1y + h * 1/6.d * ( a1y + 2*a2y + 2*a3y + a4y);

        return new double[]{new_x , new_y , new_vx , new_vy};
    }
}