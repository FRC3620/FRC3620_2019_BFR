/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc3620.robot.commands;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.robot.Robot;
import org.usfirst.frc3620.robot.paths.AlignToPointD;

import edu.wpi.first.wpilibj.command.CommandGroup;
//WIP -- Don't use yet!
public class AutonomousAlignmentAndApproachCommand extends CommandGroup {
  double interceptAngle = 0;
  double distance;
  double angularTolerance = 10;
  double setpoints[] = new double[]{
    30, 150, 180, 210, 330
  };

  Logger logger = EventLogging.getLogger(getClass(), Level.INFO);

  double desiredSetPoint = 0;
 //PUT ON BUTTON!!!!!!!!
  public AutonomousAlignmentAndApproachCommand() {
    if(Math.abs(Robot.visionSubsystem.getFrontTargetYaw()) > 7){
    //  addSequential(new VisionAlignmentCommand());
    }
    
    //Premonition: It's going to grab the original heading, not the one after the centering.
    double currentHeading = Robot.driveSubsystem.getRealAngle();
    
    interceptAngle = Robot.visionSubsystem.getFrontTargetAngle();
    logger.info("Current Heading = {}", currentHeading);
    logger.info("interceptAngle = {}", interceptAngle);

    for(double possibleSetpoint: setpoints){
      if((Math.abs(currentHeading + interceptAngle - possibleSetpoint) < angularTolerance) || (Math.abs(currentHeading - interceptAngle - possibleSetpoint) < angularTolerance)){
        desiredSetPoint = possibleSetpoint;
        logger.info("Setpoint in degrees = {}", desiredSetPoint);
      }
    }
    

    interceptAngle = currentHeading - desiredSetPoint;
    logger.info("InterceptAngle = {}", interceptAngle);
    
   
    if(Math.abs(interceptAngle) > 20){
      addSequential(new AlignToPointD(interceptAngle));
    }    
    //addSequential(new AutoMoveForwardCommand(10,.7));
    
  }
}
