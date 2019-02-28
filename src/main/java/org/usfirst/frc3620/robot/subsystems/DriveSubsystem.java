package org.usfirst.frc3620.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.robot.RobotMap;
import org.usfirst.frc3620.robot.commands.DriveCommand;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveSubsystem extends Subsystem {
    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);    

    public AHRS ahrs = null /* will set in the constructor */;

    PIDSourceType pidSourceType = PIDSourceType.kDisplacement;
    boolean gotCompBot;

    private final DifferentialDrive differentialDrive = RobotMap.driveSubsystemDifferentialDrive;
    private Encoder driveEncoderLeft = RobotMap.leftSideEncoder;
    private Encoder driveEncoderRight = RobotMap.rightSideEncoder;
    private boolean reverseModeQuestion;
    
    public double automaticHeading;
    public boolean complainedAboutMissingAhrs;
    public double initialNavXReading = 0;

    public DriveSubsystem(){
        super();               
		ahrs = new AHRS(edu.wpi.first.wpilibj.SPI.Port.kMXP);
		// ahrs.enableLogging(false);
       
        gotCompBot = RobotMap.amICompBot();
        
        try{
            resetEncoders();
        } catch(NullPointerException nullPointer){
            resetCANDriveEncoders();
        }
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here. Drive command runs in background at all times
        setDefaultCommand(new DriveCommand());
    }

   
    @Override
    public void periodic() {
        // Put code here to be run every loop
        if(checkForCANDriveEncoders()) {
            SmartDashboard.putNumber("leftsideEncoder", RobotMap.leftsideCANEncoder.getPosition());
            SmartDashboard.putNumber("rightsideEncoder", RobotMap.rightsideCANEncoder.getPosition());
        }
        SmartDashboard.putNumber("rightsideEncoderInFeet", getRightSideDistance());
        SmartDashboard.putNumber("leftsideEncoderInFeet", getLeftSideDistance());
        SmartDashboard.putNumber("NavX Heading", getRealAngle());
       
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
        if (differentialDrive != null) {
            differentialDrive.arcadeDrive(y, x);
        }
        SmartDashboard.putNumber("Y diff. drive", y);
        SmartDashboard.putNumber("X diff. drive", x);
    }

    public int readLeftEncRaw(){
      return  driveEncoderLeft.getRaw();
    }
    
    public int readRightEncRaw(){
        return  driveEncoderRight.getRaw();
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

    public double getRealAngle(){
        double angle = getAngle();
        double realAngle = angle % 360;
        if (realAngle < 0){
            realAngle += 360;
        }
        return realAngle;
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

    public boolean areWeInReverseMode(){
        return reverseModeQuestion;
    }

    public void toggleReverseMode(){
       if (reverseModeQuestion == true){
           reverseModeQuestion = false;
       } else{
           reverseModeQuestion = true;
       }
       logger.info ("reverse mode toggled, now {}", reverseModeQuestion);
    }

    public void clearReverseMode(){
        reverseModeQuestion = false;
        logger.info ("reverse mode cleared");
    }

    /**
     * shut down the robot.
     */
    public void stopDrive() {
        //stops robot
        if (differentialDrive != null) {
            differentialDrive.stopMotor();
        }
    }

    public double getLeftSideDistance() {
        if(checkForCANDriveEncoders()) {
            double tics = RobotMap.leftsideCANEncoder.getPosition();
            double howfarwehavemoved = tics - leftEncoderZeroValue;
            double feet = ticsToFeet(howfarwehavemoved);
            return feet;
        } else {
            return(0);
        }
    
    }
    public double getRightSideDistance() {
        if(checkForCANDriveEncoders()) {
            double tics = RobotMap.rightsideCANEncoder.getPosition();
            double howfarwehavemoved = tics - rightEncoderZeroValue;
            double feet = ticsToFeet(-howfarwehavemoved);
            return feet;
        } else  {
            return(0);
        }
    }

    double leftEncoderZeroValue, rightEncoderZeroValue;

    public boolean checkForCANDriveEncoders() {
        return(!(RobotMap.leftsideCANEncoder==null));
    }
    
    public void resetEncoders(){
        driveEncoderLeft.reset();
        driveEncoderRight.reset();
    }

    public void resetCANDriveEncoders(){
        if(checkForCANDriveEncoders()) {
            leftEncoderZeroValue = RobotMap.leftsideCANEncoder.getPosition();
            rightEncoderZeroValue = RobotMap.rightsideCANEncoder.getPosition();
        }
    }

	public PIDSource getAhrsPidSource() {
		if (ahrsIsConnected()) {
			return ahrs;
		} else {
			return new PIDSource() {

				@Override
				public void setPIDSourceType(PIDSourceType pidSource) {
				}

				@Override
				public double pidGet() {
					return 0;
				}

                @Override
                public PIDSourceType getPIDSourceType() {
                    return null;
				}
			};
		}
	}
}