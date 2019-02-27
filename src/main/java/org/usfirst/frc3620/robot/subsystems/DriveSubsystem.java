package org.usfirst.frc3620.robot.subsystems;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.robot.RobotMap;
import org.usfirst.frc3620.robot.commands.DriveCommand;

import edu.wpi.first.wpilibj.command.Subsystem;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.kauailabs.navx.frc.AHRS;

/**
 *
 */
public class DriveSubsystem extends Subsystem {
    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);    

    public AHRS ahrs = null;
    private boolean gotCompBot;
    private boolean reverseModeQuestion;

    private final DifferentialDrive differentialDrive = RobotMap.driveSubsystemDifferentialDrive;

    public DriveSubsystem(){
        super();               
		ahrs = new AHRS(edu.wpi.first.wpilibj.SPI.Port.kMXP);
		//ahrs.enableLogging(false);
		
        gotCompBot = RobotMap.amICompBot();

        resetCANDriveEncoders();
    }

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
        if(checkForCANDriveEncoders()) {
            SmartDashboard.putNumber("leftsideEncoder", RobotMap.leftsideCANEncoder.getPosition());
            SmartDashboard.putNumber("rightsideEncoder", RobotMap.rightsideCANEncoder.getPosition());
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

    public boolean areWeInReverseMode(){
        return reverseModeQuestion;
    }

    public void toggleReverseMode(){
       if (reverseModeQuestion == true){
           reverseModeQuestion = false;
       }
       else{
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
            double feet = ticstofeet(howfarwehavemoved);
            return feet;
        } else {
            return(0);
        }
    
    }
    public double getRightSideDistance() {
        if(checkForCANDriveEncoders()) {
            double tics = RobotMap.rightsideCANEncoder.getPosition();
            double howfarwehavemoved = tics - rightEncoderZeroValue;
            double feet = ticstofeet(-howfarwehavemoved);
            return feet;
        } else {
            return(0);
        }
    }

    double leftEncoderZeroValue, rightEncoderZeroValue;

    public boolean checkForCANDriveEncoders() {
        return(!(RobotMap.leftsideCANEncoder==null));
    }

    public void resetCANDriveEncoders(){
        if(checkForCANDriveEncoders()) {
            leftEncoderZeroValue = RobotMap.leftsideCANEncoder.getPosition();
            rightEncoderZeroValue = RobotMap.rightsideCANEncoder.getPosition();
        }
    }
}