package org.usfirst.frc3620.robot.paths;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.robot.Robot;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;


public class AlignToPointD extends AbstractPath{
    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);

    double x;
    double y;
    double r;
    private final double d = 3;
    double angle;

    public void calculateX(){
        x = r*(Math.cos(angle)) - d;
     //   logger.info("X: {}", x);
    }

    public void calculateY(){
        y = r*(Math.sin(angle));
     //   logger.info("Y: {}", y);
    }

    public void setDistanceAndIntercept(){
        r = Robot.visionSubsystem.getFrontTargetDistance();
        angle = Math.toRadians(Robot.visionSubsystem.getFrontTargetAngle());
    }

    @Override
    public double getPathfinderP() {
       return 0.008;
    }

    @Override
    public Waypoint[] getMyWaypoints(){
        setDistanceAndIntercept();
        calculateX();
        calculateY();
        System.out.println("X: " + x + " Y: " + y + " angle: " + angle + "R: " + r);
        return new Waypoint[] {
            new Waypoint(0, 0, Pathfinder.d2r(0)),
            new Waypoint(y, -x, angle),
        };


        
    }
    


}