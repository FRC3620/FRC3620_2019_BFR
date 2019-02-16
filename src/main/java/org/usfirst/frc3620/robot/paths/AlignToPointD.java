package org.usfirst.frc3620.robot.paths;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class AlignToPointD extends AbstractPath{
    double x;
    double y;
    double r;
    private final double d = 2.5;
    double angle;

    public AlignToPointD(double distance, double theta){
        r = distance;
        angle = theta;
    }

    public void calculateX(){
        x = r*(Math.cos(angle)) - d;
    }

    public void calculateY(){
        y = r*(Math.sin(angle));
    }

    @Override
    public Waypoint[] getMyWaypoints(){
        calculateX();
        calculateY();
        System.out.println("X: " + x + " Y: " + y + " angle: " + angle);
        return new Waypoint[] {
            new Waypoint(0, 0, Pathfinder.d2r(0)),
            new Waypoint(x, -y, Pathfinder.d2r(angle)),
        };


        
    }
    


}