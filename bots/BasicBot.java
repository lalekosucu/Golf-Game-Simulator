/* This class will decide at which velocity the ball should be hit,
 * given a terrain and starting coordinates.
 * Its mechanisms are very straightforward.
 */

package bots;

import java.util.*;
import java.util.Arrays;
import java.util.ArrayList;

import physicsEngine.PhysicsEngine;
import terrain.Terrain;

public class BasicBot implements Bot{

    // Coordinates for ball
    public double x;
    public double y;
    public double vx;
    public double vy;

    // updating vx vy 
    // The closer the constant is to 1 , the more precise it is , the lesser the more important the changes in vx vy.
    // Must be inferior to 1
    public static final double constant = 0.9; 

    // Terrain and physic Engine
    private Terrain terrain;
    private PhysicsEngine engine;

    public BasicBot(Terrain terrain) {
      this.terrain = terrain;
      engine = new PhysicsEngine(terrain);
    }
    
    // Algorithm that decides the velocity.
    public double[] getNextMove(double x, double y){
        vx = terrain.distanceXTarget(x);
        vy = terrain.distanceYTarget(y);
        
        // Updates the values for vx and vy until the total speed is less than 5.
        while(Math.sqrt((vx*vx) + (vy*vy)) > 5){
            vx = vx*constant;
            vy = vy*constant;
        }
        double[] correctVelocity = {vx,vy};
        return correctVelocity;
    }
}