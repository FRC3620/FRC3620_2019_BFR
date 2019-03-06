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
    private final double d = 1;
    double angle;
    

    public AlignToPointD(double interceptAngle){
        angle = Math.toRadians(interceptAngle);
    }

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
     //  angle = Math.toRadians(Robot.visionSubsystem.getFrontTargetAngle()); 
    }

    @Override
    public double getPathfinderP() {
       return 0.00;
    }
    //What works: 0.001 P, 0.01 D

    public double getPathfinderD(){
        return 0.000;
    }

    public int getLeftOrRightOfTarget(){
        //Based on current setup in final statement of getMyWaypoints(), right of target is 1, left of target is -1
        //Therefore, if the below is < 0, then you're to the left of your real target and vice versa
        double currentHeading = Robot.driveSubsystem.getRealAngle();
        if(Robot.visionSubsystem.getFrontSecondClosestYaw() > 0){
            return 1;
        } else if(Robot.visionSubsystem.getFrontSecondClosestYaw() < 0){
            return -1;   
        } else if(Robot.visionSubsystem.getFrontSecondClosestYaw() == 0){ 
            if(angle < 0){
                return 1;
            } else if(angle  > 0){
                return -1;
            }
             else{
                return 0;
            }
        } else {
            return 0;
      }
    }

    @Override
    public Waypoint[] getMyWaypoints(){
        setDistanceAndIntercept();
        calculateX();
        calculateY();
        logger.info("LeftOrRight = {}", getLeftOrRightOfTarget());
        System.out.println("X: " + x + " Y: " + y + " NavXangle: " + Robot.driveSubsystem.getAngle() + "R: " + r + " L or R: " + getLeftOrRightOfTarget());
        return new Waypoint[] {
            new Waypoint(0, 0, Pathfinder.d2r(0)),
            new Waypoint(y, getLeftOrRightOfTarget()*x, -angle*getLeftOrRightOfTarget()),
        };


        
    } 

    @Override
    public void execute(){
        double currentNavXHeading = Robot.driveSubsystem.getRealAngle();
        super.execute();
        if(Math.abs(currentNavXHeading - angle) < 5 ){
            super.finishedFlag = true;
        }

    }


}