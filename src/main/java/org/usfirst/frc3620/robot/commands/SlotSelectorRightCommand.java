package org.usfirst.frc3620.robot.commands;

import static org.junit.Assert.assertArrayEquals;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.misc.AverageJoePIDOutput;
import org.usfirst.frc3620.misc.AverageJoePIDSource;
import org.usfirst.frc3620.robot.Robot;
import org.usfirst.frc3620.robot.RobotMap;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
/**
 *
 */

 
public class SlotSelectorRightCommand extends Command {
    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);

    static final double kPDriveStraight = 0.0;
   
    static final double kIDriveStraight = 0;	
    
    static final double kDDriveStraight = 0;
    
    static final double kFDriveStraight = 0;

    static final double kPLineUp = .006;
   
    static final double kILineUp = 0;	
    
    static final double kDLineUp = 0.01;
    
    static final double kFLineUp = 0;

    static final double selectionSpeed = 0.4;

    static final double tolerance = 4;
    

    
    double sideStick;

    int setSlot;
    int a = 0;

    boolean weAreDone = false;
    
    
    PIDController pidDriveStraight = new PIDController(kPDriveStraight, kIDriveStraight, kDDriveStraight, kFDriveStraight, Robot.driveSubsystem.getAhrsPidSource(), new DriveStraightOutput());

    Command lineUpCommand = new AutoLineUpWithCargoshipRightCommand();
    Command rumbleCommand = new RumbleCommand(Robot.rumbleSubsystemDriver);
    
    /* Modify to get the POV pushed and use that for lefts and rights. */

    public SlotSelectorRightCommand(int slot) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
      requires(Robot.driveSubsystem);
      setSlot = slot;
      pidDriveStraight.setOutputRange(-.5, .5);
      pidDriveStraight.setInputRange(0.0f, 360.0f);
      pidDriveStraight.setContinuous(true);
     
    
    }
    
    

    // Called just before this Command runs the first time
    protected void initialize() {
      logger.info("AutoLineUpWithCargoshipCommand start");
      Robot.visionSubsystem.turnLightSwitchOn();
      if(Robot.visionSubsystem.getRightTargetPresent()){
        a = 1;
      }
      pidDriveStraight.setSetpoint(Robot.driveSubsystem.getRealAngle());
      pidDriveStraight.reset();
      pidDriveStraight.enable(); 
      if(setSlot == a){
        lineUpCommand.start();
      }
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
      double leftJoy = Robot.oi.getLeftVerticalJoystickSquared();
      double rightJoy = Robot.oi.getRightHorizontalJoystickSquared(); 
      if(a == 0){
        if(Robot.visionSubsystem.getRightTargetPresent()){
            a++;
        } else{
            Robot.driveSubsystem.arcadeDrive(leftJoy, rightJoy);
        }
      }
      if(a >= 1){
        if(!(setSlot == a)){
          if(Robot.visionSubsystem.getRightTargetPresent() == true){
              Robot.driveSubsystem.arcadeDrive(selectionSpeed, sideStick);
          } else if(Robot.visionSubsystem.getRightTargetPresent() == false){
              a++;
          }
            
          } else if(setSlot == a){
              if(Math.abs(Robot.visionSubsystem.getRightTargetYaw()) < tolerance){
                  weAreDone = true;
              } else{
                  if(lineUpCommand.isRunning() == true){
                    //Then we're all good...
                  }
                  else{
                    lineUpCommand.start();
                  }
              }
              
          }
      }
      
      
      
      logger.info("sideStick: {}", sideStick);
      logger.info("NavX heading {}", Robot.driveSubsystem.getAngle());
      logger.info("Corrected angle {}:", Robot.driveSubsystem.getRealAngle());
      
    
     
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
      // check to see if execute() thought we should be done
      if(weAreDone) {
        return true;
      }
      if (Robot.visionSubsystem.getRightTargetYaw() != 0){

        rumbleCommand.start();
        return false;
      } else {
        return true;
      }
    }

    // Called once after isFinished returns true
    protected void end() {
      logger.info("AutoLineUpWithCargoshipCommand end");
      
      pidDriveStraight.disable();
     
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
        return 0;
      }
  
    } 

   
    public class DriveStraightOutput extends AverageJoePIDOutput{

      @Override
      public void pidWrite(double output) {
        sideStick = output;
      }

    } 
     
    

    }
