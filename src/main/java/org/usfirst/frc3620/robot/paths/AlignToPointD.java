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
       return 0.010;
    }
    //What works: 0.001 P, 0.01 D

    public double getPathfinderD(){
        return 0.005;
    }

    public int getLeftOrRightOfTarget(){
        //Based on current setup in final statement of getMyWaypoints(), right of target is 1, left of target is -1
        //Therefore, if the below is < 0, then you're to the left of your real target and vice versa
        if(Robot.visionSubsystem.getFrontSecondClosestYaw() > 0){
            return 1;
        } else if(Robot.visionSubsystem.getFrontSecondClosestYaw() < 0){
            return -1;   
        } else if(Robot.visionSubsystem.getFrontSecondClosestYaw() == 0){
            if(Math.abs(Robot.driveSubsystem.getRealAngle()) > 90){
                return -1;
            } else{
                return 1;
            }
        }
    }

    @Override
    public Waypoint[] getMyWaypoints(){
        setDistanceAndIntercept();
        calculateX();
        calculateY();
        System.out.println("X: " + x + " Y: " + y + " NavXangle: " + Robot.driveSubsystem.getAngle() + "R: " + r + " L or R: " + getLeftOrRightOfTarget());
        return new Waypoint[] {
            new Waypoint(0, 0, Pathfinder.d2r(0)),
            new Waypoint(y, getLeftOrRightOfTarget()*x, -angle*getLeftOrRightOfTarget()),
        };


        
    }
    


}