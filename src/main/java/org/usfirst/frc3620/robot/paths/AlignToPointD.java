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
    double setValue;
    double angularTolerance = 10;
    double setpoints[] = new double[]{
        30, 150, 180, 210, 330
      };


    public void calculateX(){
        x = r*(Math.cos(Math.abs(angle))) - d;
     //   logger.info("X: {}", x);
    }

    public void calculateY(){
        y = r*(Math.sin(Math.abs(angle)));
     //   logger.info("Y: {}", y);
    }

    public void setDistanceAndIntercept(){
        double currentHeading = Robot.driveSubsystem.getRealAngle();
        r = Robot.visionSubsystem.getFrontTargetDistance();
        angle = Robot.visionSubsystem.getFrontTargetAngle();
        logger.info("Current Heading = {}", currentHeading);
        logger.info("interceptAngle = {}", angle);

        for(double possibleSetpoint: setpoints){
            if((Math.abs(currentHeading + angle - possibleSetpoint) < angularTolerance) || (Math.abs(currentHeading - angle - possibleSetpoint) < angularTolerance)){
                setValue = possibleSetpoint;
                logger.info("Setpoint in degrees = {}", setValue);
             }
        }
    

            angle = currentHeading - setValue;
            logger.info("angle = {}", angle);
            angle = Math.toRadians(angle);
     //  angle = Math.toRadians(Robot.visionSubsystem.getFrontTargetAngle()); 
    }

    @Override
    public double getPathfinderP() {
       return 0.001;
    }
    //What works: 0.001 P, 0.01 D

    public double getPathfinderD(){
        return 0.000;
    }

    public int getLeftOrRightOfTarget(){
        //Based on current setup in final statement of getMyWaypoints(), right of target is 1, left of target is -1
        //Therefore, if the below is < 0, then you're to the left of your real target and vice versa
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
            new Waypoint(y, getLeftOrRightOfTarget()*x, angle*getLeftOrRightOfTarget()),
        };


        
    } 

   /* @Override
    public void execute(){
        double currentNavXHeading = Robot.driveSubsystem.getRealAngle();
        super.execute();
        if(Math.abs(currentNavXHeading - setValue) < 5 ){
            super.finishedFlag = true;
        }

    } */


}