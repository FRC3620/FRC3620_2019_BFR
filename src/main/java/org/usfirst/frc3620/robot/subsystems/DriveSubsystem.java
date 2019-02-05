package org.usfirst.frc3620.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.robot.RobotMap;
import org.usfirst.frc3620.robot.commands.DriveCommand;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.kauailabs.navx.frc.AHRS;

/**
 *
 */
public class DriveSubsystem extends Subsystem {

    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
    public AHRS ahrs = null /* will set in the constructor */;

    private final DifferentialDrive differentialDrive = RobotMap.driveSubsystemDifferentialDrive;
    private Encoder driveEncoderLeft;
    private Encoder driveEncoderRight;

    public double automaticHeading;
    public boolean complainedAboutMissingAhrs;

    public DriveSubsystem() {
        // this code gets run when the DriveSubsystem is created 
        // (when the robot is rebooted.)
        ahrs = new AHRS(edu.wpi.first.wpilibj.SPI.Port.kMXP);
		ahrs.enableLogging(false);
        resetencoder();
    }
    
    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here. Drive command runs in background at all times
        setDefaultCommand(new DriveCommand());
    }

   
    @Override
    public void periodic() {
        // Put code here to be run every loop
        SmartDashboard.putNumber("leftsideEncoder", RobotMap.leftsideEncoder.getPosition());
        SmartDashboard.putNumber("rightsideEncoder", RobotMap.rightsideEncoder.getPosition());
        SmartDashboard.putNumber("rightsideEncoderInFeet", getRightSideDistance());
        SmartDashboard.putNumber("leftsideEncoderInFeet", getLeftSideDistance());
      

    }

    double ticsToFeet(double tics) { 
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

    public int readLeftEncRaw(){
      return  driveEncoderLeft.getRaw();
    }
    
    public int readRightEncRaw(){
        return  driveEncoderRight.getRaw();
      }
    
    public void resetEncoders(){
        driveEncoderLeft.reset();
        driveEncoderRight.reset();
    }

    private double getSpeedModifier() {	// TODO Tune me!!
		return 1.0;
		
	}

    public void autoDriveTank(double left, double right) {
		if (differentialDrive != null) {
			left *= getSpeedModifier();
			right *= getSpeedModifier();
			differentialDrive.tankDrive(left, right, false);
		}
	}

    public boolean ahrsIsConnected() {
		return ahrs != null && ahrs.isConnected();
	}

	void complainAboutMissingAhrs() {
		if (!complainedAboutMissingAhrs) {
			logger.warn("Cannot read NavX, not connected");
		}
		complainedAboutMissingAhrs = true;
	}

	public double getPitch() {
		if (ahrsIsConnected()) {
			return ahrs.getPitch();
		} else {
			complainAboutMissingAhrs();
			return 0;
		}
	}

	public double getRoll() {
		if (ahrsIsConnected()) {
			return ahrs.getRoll();
		} else {
			complainAboutMissingAhrs();
			return 0;
		}
	}

	public double getAngle() {
		if (ahrsIsConnected()) {
			return ahrs.getAngle(); //Angle direction was opposite of 2017 testing (raw is clockwise-positive, corrected is clockwise-negative)
		} else {
			complainAboutMissingAhrs();
			return 0;
		}
    }

    public double getAutomaticHeading() {
		return automaticHeading;
	}
	
	public double changeAutomaticHeading(double changeAngle) {
		automaticHeading = automaticHeading + changeAngle;
		automaticHeading = normalizeAngle(automaticHeading);
		logger.info("Changing auto heading to" +  automaticHeading);
		return automaticHeading;
	}
	
	static public double normalizeAngle(double angle) {
		// bring into range of -360..360
		double newAngle = angle % 360;

		// if it's between -360..0, put it between 0..360
		if (newAngle < 0)
			newAngle += 360;

		return newAngle;
	}
	
	public double angleDifference(double angle1, double angle2) {
		double diff = Math.abs(angle1 - angle2);
		if (diff > 180) {
			diff = 360 - diff;
		}
		return diff;
	}

    /**
     * shut down the robot.
     */
    public void stopDrive() {
        //stops robot
        differentialDrive.stopMotor();
    }

    public double getLeftSideDistance() {
        double tics = RobotMap.leftsideEncoder.getPosition();
        double howfarwehavemoved = tics - leftEncoderZeroValue;
        double feet = ticsToFeet(howfarwehavemoved);
        return feet;
    
    }
    public double getRightSideDistance() {
        double tics = RobotMap.rightsideEncoder.getPosition();
        double howfarwehavemoved = tics - rightEncoderZeroValue;
        double feet = ticsToFeet(-howfarwehavemoved);
        return feet;
    }

    double leftEncoderZeroValue, rightEncoderZeroValue;

    public void resetencoder(){
        leftEncoderZeroValue = RobotMap.leftsideEncoder.getPosition();
        rightEncoderZeroValue = RobotMap.rightsideEncoder.getPosition();
    }
}