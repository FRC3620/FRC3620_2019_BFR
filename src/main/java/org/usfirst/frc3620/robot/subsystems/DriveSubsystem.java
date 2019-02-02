package org.usfirst.frc3620.robot.subsystems;

import org.usfirst.frc3620.robot.RobotMap;
import org.usfirst.frc3620.robot.commands.DriveCommand;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveSubsystem extends Subsystem {

    private final DifferentialDrive differentialDrive = RobotMap.driveSubsystemDifferentialDrive;
    private Encoder driveEncoderLeft;
    private Encoder driveEncoderRight;
    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here. Drive command runs in background at all times
        setDefaultCommand(new DriveCommand());
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop

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

    /**
     * shut down the robot.
     */
    public void stopDrive() {
        //stops robot
        differentialDrive.stopMotor();
    }

}