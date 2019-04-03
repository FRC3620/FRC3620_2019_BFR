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
import org.usfirst.frc3620.misc.AverageJoePIDOutput;
import org.usfirst.frc3620.misc.AverageJoePIDSource;
import org.usfirst.frc3620.robot.Robot;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Command;

public class AutoLineUpWithCargoshipLeftCommand extends Command {
  
	
    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);

    static final double kPDriveStraight = 0.02;
   
    static final double kIDriveStraight = 0;	
    
    static final double kDDriveStraight = 0.02;
    
    static final double kFDriveStraight = 0.0;

    static final double kPLineUp = .004;
   
    static final double kILineUp = 0.0004;	
    
    static final double kDLineUp = 0.015;
    
    static final double kFLineUp = 0;

    double fwdStick;
    double sideStick;

    boolean weAreDone = false;

    
    
    PIDController pidDriveStraight = new PIDController(kPDriveStraight, kIDriveStraight, kDDriveStraight, kFDriveStraight, new DriveStraightSource(), new DriveStraightOutput());
    PIDController pidLineUp = new PIDController(kPLineUp, kILineUp, kDLineUp, kFLineUp, new LineUpSource(), new LineUpOutput());

    Command driverRumbleCommand = new RumbleCommand(Robot.rumbleSubsystemDriver);
    Command operatorRumbleCommand = new RumbleCommand(Robot.rumbleSubsystemOperator);
    
  
    public AutoLineUpWithCargoshipLeftCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
      requires(Robot.driveSubsystem);
      pidDriveStraight.setOutputRange(-.7, .7);
      pidDriveStraight.setInputRange(0.0f, 360.0f);
      pidDriveStraight.setContinuous(true); 

      pidLineUp.setOutputRange(-.5, .5);
      pidLineUp.setInputRange(-90,90);
      pidLineUp.setContinuous(false); 
    }
    
    

    // Called just before this Command runs the first time
    protected void initialize() {
      logger.info("AutoLineUpWithCargoshipCommand start");

      Robot.visionSubsystem.turnLightSwitchOn();
      double currentNavXHeading = Robot.driveSubsystem.getRealAngle();
      if(currentNavXHeading > 270 || currentNavXHeading < 90){
        pidDriveStraight.setSetpoint(0);
      } /* else if(currentNavXHeading < 90){
        pidDriveStraight.setSetpoint(1);
      } */ else if(currentNavXHeading > 90 && currentNavXHeading < 270){
        pidDriveStraight.setSetpoint(180);
      }
        
        pidDriveStraight.reset();
        pidDriveStraight.enable(); 

        pidLineUp.setSetpoint(0);
        pidLineUp.reset();
        pidLineUp.enable();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
      weAreDone = false;
      logger.info("fwdStick: {}", fwdStick);
     /* if(Robot.visionSubsystem.getLeftTargetPresent() == false){
        weAreDone = true;
        
        return;
      } */
      double horizontal = Robot.oi.getRightHorizontalJoystickSquared();
      Robot.driveSubsystem.arcadeDrive(fwdStick, sideStick);
      logger.info("sideStick: {}", sideStick);
      //logger.info("NavX heading {}", Robot.driveSubsystem.getAngle());
      //logger.info("Corrected angle {}:", Robot.driveSubsystem.getRealAngle());
      
    
      //SmartDashboard.putNumber("Fwd stick", fwdStick);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
      // check to see if execute() thought we should be done
      if(weAreDone) {
        return true;
      }
      //VVV Change at some point.
      if(Robot.visionSubsystem.getLeftTargetYaw() == 0){
        return false;
      }
      else if(Math.abs(Robot.visionSubsystem.getLeftTargetYaw()) < 20){
        driverRumbleCommand.start();
        operatorRumbleCommand.start();
        return false;
      } else if(Math.abs(Robot.visionSubsystem.getLeftTargetYaw()) >= 20){
        driverRumbleCommand.start();
        return false;
      }
        else {
        return true;
      } 
    }
    

    // Called once after isFinished returns true
    protected void end() {
      logger.info("AutoLineUpWithCargoshipCommand end");
      Robot.visionSubsystem.turnLightSwitchOff();
      
    //  pidDriveStraight.disable();
      pidLineUp.disable();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
      logger.info("AutoLineUpWithCargoshipCommand interrupted");
      
        end();
    }
    
    public class DriveStraightSource extends AverageJoePIDSource{

      @Override
      public double pidGet() {
        return Robot.driveSubsystem.getRealAngle();
      }
  
    } 

    public class LineUpSource extends AverageJoePIDSource{

      @Override
      public double pidGet() {
        if (Robot.visionSubsystem.getLeftTargetPresent()){
          return Robot.visionSubsystem.getLeftTargetYaw();
        }
        return 0;
      }
  
    }

    public class DriveStraightOutput extends AverageJoePIDOutput{

      @Override
      public void pidWrite(double output) {
        sideStick = output;
      }

    } 
     
    public class LineUpOutput extends AverageJoePIDOutput{

      @Override
      public void pidWrite(double output) {
        fwdStick = output;  
      }

    }
  }

  