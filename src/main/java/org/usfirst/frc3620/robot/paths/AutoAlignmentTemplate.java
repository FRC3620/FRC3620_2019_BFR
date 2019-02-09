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
        if(angle != 0){
            x = r*(Math.sin(angle))*(Math.sin(1.5*angle))/
            (Math.sin(180-1.5*angle));
        } else if(angle > 60) {
            angle = 60;
            x = r*(Math.sin(angle))*(Math.sin(1.5*angle))/
            (Math.sin(180-1.5*angle));
        } else{
            x = 0;
        }
    }

    public void calculateY(){
        if(angle != 0){
            y = r*(Math.sin(angle))*(Math.cos(1.5*angle))/
            (Math.sin(180-1.5*angle));
        } else if(angle > 60) {
            angle = 60;
            y = r*(Math.sin(angle))*(Math.cos(1.5*angle))/
            (Math.sin(180-1.5*angle));
        } else{
            y = (2*r)/3;
        }
    }

    @Override
    public Waypoint[] getMyWaypoints(){
        calculateX();
        calculateY();
        return new Waypoint[] {
            new Waypoint(0, 0, Pathfinder.d2r(0)),
            new Waypoint(x, y, Pathfinder.d2r(angle)),
        };


        
    }
    


}