package org.usfirst.frc3620.robot.paths;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class AutoAlignmentTemplate extends AbstractPath{
    double x;
    double y;
    double r;
    double angle;

    public AutoAlignmentTemplate(double distance, double theta){
        r = distance;
        angle = theta;
    }

    public void calculateX(){

    }

    public void calculateY(){

    }

    @Override
    public Waypoint[] getMyWaypoints(){
        return new Waypoint[] {
            new Waypoint(0, 0, Pathfinder.d2r(0)),
            new Waypoint(x, y, Pathfinder.d2r(angle)),
        };


        
    }
    


}