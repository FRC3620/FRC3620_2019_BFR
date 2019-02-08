package org.usfirst.frc3620.robot.paths;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class TrainingPath extends AbstractPath{

    @Override
    public Waypoint[] getMyWaypoints(){
        return new Waypoint[] {
            new Waypoint(0, 0, Pathfinder.d2r(0)),
            new Waypoint(20, 20, Pathfinder.d2r(90)),
        };   
    }
    
    @Override
    public double getPathfinderP(){
        return 0.0025;
    }



}