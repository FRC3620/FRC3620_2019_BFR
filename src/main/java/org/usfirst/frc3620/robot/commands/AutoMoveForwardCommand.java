/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc3620.robot.commands;

import org.usfirst.frc3620.robot.Robot;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class AutoMoveForwardCommand extends Command implements PIDOutput, PIDSource {

  Logger logger = EventLogging.getLogger(getClass(), Level.INFO);

  static final double kP = 0.01; //2019: 0.01    0.03  WORKS WELL: 0.01
	
	static final double kI = 0.000003;	//2019: 0.0000030   0.0000020
	
  static final double kD = 0.0075; //2019: 0.0075    0.3         0.03   0.0045
	
  static final double kF = 0; //2019: 0
	double sideStick;
	
  double howLongWeWantToMove = 0;
	double howFastToMove = 0;
	
	Timer timer = new Timer();
	
  PIDController pidDriveStraight = new PIDController(kP, kI, kD, kF, this, this);
  
  PIDSourceType pidSourceType = PIDSourceType.kDisplacement;

  public AutoMoveForwardCommand(double howLongInSeconds, double howFast) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(Robot.driveSubsystem);
    pidDriveStraight.setOutputRange(-.5, .5);
    pidDriveStraight.setInputRange(-150, 150);
    pidDriveStraight.setContinuous(false);
    pidDriveStraight.setAbsoluteTolerance(0);
    howFastToMove = howFast;
    howLongWeWantToMove = howLongInSeconds;

    
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    logger.info("AutomatedMoveForward start");

    Robot.visionSubsystem.turnLightSwitchOn();
  
    pidDriveStraight.setSetpoint(0);
    pidDriveStraight.reset();
    pidDriveStraight.enable();
  
    timer.reset();
    timer.start();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {

    double vertical = Robot.oi.getLeftVerticalJoystickSquared();
    logger.info("turnValue: {}", sideStick);
    Robot.driveSubsystem.arcadeDrive(-vertical, -sideStick);
    
    
  }
  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return timer.get() > howLongWeWantToMove;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.visionSubsystem.turnLightSwitchOff();
    pidDriveStraight.disable();
    Robot.driveSubsystem.stopDrive();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }

  @Override
  public void setPIDSourceType(PIDSourceType pidSource) {

  }

  @Override
  public PIDSourceType getPIDSourceType() {
    return pidSourceType;
  }

  @Override
  public double pidGet() {
    return Robot.visionSubsystem.getFrontTargetYaw();
  }

  @Override
  public void pidWrite(double output) {
    sideStick =  output;
  }
}
