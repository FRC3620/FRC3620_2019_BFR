/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc3620.robot.commands;

import org.usfirst.frc3620.robot.Robot;
import org.usfirst.frc3620.robot.paths.AlignToPointD;

import edu.wpi.first.wpilibj.command.CommandGroup;
//WIP -- Don't use yet!
public class AutonomousAlignmentAndApproachCommand extends CommandGroup {
  double interceptAngle = 0;
  double distance;
  
  public AutonomousAlignmentAndApproachCommand() {
    double leftDiff;
    double rightDiff;
    boolean left = false;
    addSequential(new VisionAlignmentCommand());
    //Premonition: It's going to grab the original heading, not the one after the centering.
    double currentHeading = Robot.driveSubsystem.getRealAngle();
    if(currentHeading > 90 && currentHeading < 270){
      leftDiff = 150 - currentHeading;
      rightDiff = 240 - currentHeading;
    } else if(currentHeading < 90 || currentHeading > 270){
      leftDiff = 330 - currentHeading;
      rightDiff = 30 - currentHeading;
    } else {
      leftDiff = rightDiff = 0;
    }

    if(Math.abs(rightDiff) > Math.abs(leftDiff)){
      interceptAngle = leftDiff;
      left = true;
    } else if(Math.abs(leftDiff) > Math.abs(rightDiff)){
      interceptAngle = rightDiff;
      left = false;
    } else {
      interceptAngle = 0;
    }
                                        
    interceptAngle = (interceptAngle + Robot.visionSubsystem.getFrontTargetAngle())/2;

    
   
    if(Math.abs(interceptAngle) > 10){
      addSequential(new AlignToPointD(interceptAngle, left));
    }    
    addSequential(new AutoMoveForwardCommand(10,.7));
    
  }
}
