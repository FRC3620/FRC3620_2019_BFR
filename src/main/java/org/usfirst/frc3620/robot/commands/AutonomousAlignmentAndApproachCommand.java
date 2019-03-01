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

public class AutonomousAlignmentAndApproachCommand extends CommandGroup {
  double interceptAngle;
  double distance;
  public AutonomousAlignmentAndApproachCommand() {
    double leftDiff = 135 - Robot.driveSubsystem.getRealAngle();
    double rightDiff = 215 - Robot.driveSubsystem.getRealAngle();
    if(Math.abs(rightDiff) > Math.abs(leftDiff)){
      interceptAngle = leftDiff;
    } else if(Math.abs(leftDiff) > Math.abs(rightDiff)){
      interceptAngle = rightDiff;
    }

    interceptAngle = Robot.visionSubsystem.getFrontTargetAngle();
   
    if(Math.abs(interceptAngle) > 10){
      addSequential(new AlignToPointD());
    }    
    addSequential(new AutoMoveForwardCommand(10,.7));
    
  }
}
