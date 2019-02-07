package org.usfirst.frc3620.robot.subsystems;

import java.awt.Robot;

import org.usfirst.frc3620.robot.RobotMap;
import org.usfirst.frc3620.robot.commands.DriveCommand;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.kauailabs.navx.frc.AHRS;

/**
 *
 */
public class DriveSubsystem extends Subsystem {

    public AHRS ahrs = null;
    private boolean gotCompBot;
    

    private final DifferentialDrive differentialDrive = RobotMap.driveSubsystemDifferentialDrive;

<<<<<<< HEAD
    public DriveSubsystem(){
        super();               
		ahrs = new AHRS(edu.wpi.first.wpilibj.SPI.Port.kMXP);
		ahrs.enableLogging(false);
		
        gotCompBot = RobotMap.practiceBotJumper.get();

        resetEncoder();
    }

=======
    public DriveSubsystem() {
        // this code gets run when the DriveSubsystem is created 
        // (when the robot is rebooted.)
        resetencoder();
    }
    
>>>>>>> 880993be790e3e712e8c1fae1f54f76bf991081b
    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here. Drive command runs in background at all times
        setDefaultCommand(new DriveCommand());
    }

    public boolean gotCompBot() {
		return gotCompBot;
	}

    @Override
    public void periodic() {
        // Put code here to be run every loop
        if(checkForDriveEncoders()) {
            SmartDashboard.putNumber("leftsideEncoder", RobotMap.leftsideEncoder.getPosition());
            SmartDashboard.putNumber("rightsideEncoder", RobotMap.rightsideEncoder.getPosition());
        }
        SmartDashboard.putNumber("rightsideEncoderInFeet", getRightSideDistance());
        SmartDashboard.putNumber("leftsideEncoderInFeet", getLeftSideDistance());

    }

    double ticstofeet(double tics) { 
            // turning the encoder readings from tics to feet
            double inches = tics / 0.583;
            double feet = inches / 12;
            return feet;
    }

    double ticstofeet(double tics) { 
            // turning the encoder readings from tics to feet
            double inches = tics / 0.583;
            double feet = inches / 12;
            return feet;
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    /**
     * drive the robot. positive Y is forward, positive X is to the right
     */

    public void arcadeDrive (double y, double x) {
        //sends values to motor
        //!!! Make sure robot is in open area, drive carefully
        differentialDrive.arcadeDrive(y, x);
        SmartDashboard.putNumber("Y diff. drive", y);
        SmartDashboard.putNumber("X diff. drive", x);
    }
    


    /**
     * shut down the robot.
     */
    public void stopDrive() {
        //stops robot
        differentialDrive.stopMotor();
    }

    public double getLeftSideDistance() {
        if(checkForDriveEncoders()) {
            double tics = RobotMap.leftsideEncoder.getPosition();
            double howfarwehavemoved = tics - leftEncoderZeroValue;
            double feet = ticstofeet(howfarwehavemoved);
            return feet;
        } else {
            return(0);
        }
    
    }
    public double getRightSideDistance() {
        if(checkForDriveEncoders()) {
            double tics = RobotMap.rightsideEncoder.getPosition();
            double howfarwehavemoved = tics - rightEncoderZeroValue;
            double feet = ticstofeet(-howfarwehavemoved);
            return feet;
        } else {
            return(0);
        }
    }

    double leftEncoderZeroValue, rightEncoderZeroValue;

    public boolean checkForDriveEncoders() {
        return(!(RobotMap.leftsideEncoder==null));
    }

    public void resetEncoder(){
        if(checkForDriveEncoders()) {
            leftEncoderZeroValue = RobotMap.leftsideEncoder.getPosition();
            rightEncoderZeroValue = RobotMap.rightsideEncoder.getPosition();
        }
    }
}