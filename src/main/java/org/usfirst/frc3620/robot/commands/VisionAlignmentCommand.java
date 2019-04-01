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

import edu.wpi.first.wpilibj.command.Command;

public class VisionAlignmentCommand extends Command {
  Logger logger = EventLogging.getLogger(getClass(), Level.INFO);

  public VisionAlignmentCommand() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  //  requires(Robot.visionSubsystem);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    EventLogging.commandMessage(logger);

    Robot.visionSubsystem.configurePID();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    Robot.visionSubsystem.runPID();
    double speed = Robot.visionSubsystem.getPIDOutput();
    Robot.driveSubsystem.autoDriveTank(-speed,speed);
    logger.info("PID Speed: {}", speed);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    /* double yaw = Robot.visionSubsystem.getTargetYaw();
    if (yaw > 3 || yaw < -3){
      return false;
    }*/
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    EventLogging.commandMessage(logger);

    Robot.visionSubsystem.disablePID();
    Robot.driveSubsystem.stopDrive();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    EventLogging.commandMessage(logger);

    Robot.visionSubsystem.disablePID();
    Robot.driveSubsystem.stopDrive();
  }
}
