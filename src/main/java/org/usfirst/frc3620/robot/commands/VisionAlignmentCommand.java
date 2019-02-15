/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc3620.robot.commands;

import org.slf4j.Logger;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.robot.Robot;
import org.usfirst.frc3620.robot.subsystems.VisionSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class VisionAlignmentCommand extends Command {
  Logger logger = EventLogging.getLogger(getClass(), Level.INFO);

  private double distance;
  private double angle;
  private double yaw;
  
  
  public VisionAlignmentCommand() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(Robot.visionSubsystem);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    angle = Robot.visionSubsystem.getTargetAngle();
    logger.info("Target angle: ", angle);

    distance = Robot.visionSubsystem.getTargetDistance();
    logger.info("Target distance: ", distance);

    yaw = Robot.visionSubsystem.getTargetYaw();
    logger.info("Target yaw: ", yaw);
    
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
