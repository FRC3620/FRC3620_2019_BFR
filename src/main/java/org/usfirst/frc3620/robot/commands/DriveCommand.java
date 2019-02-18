package org.usfirst.frc3620.robot.commands;
import edu.wpi.first.wpilibj.command.Command;
import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.misc.CANDeviceFinder;
import org.usfirst.frc3620.robot.Robot;

/**
 *
 */
public class DriveCommand extends Command {
	Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
    public static CANDeviceFinder canDeviceFinder;
    public DriveCommand() {
        requires(Robot.driveSubsystem);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        EventLogging.commandMessage(logger);
        canDeviceFinder = new CANDeviceFinder();
        logger.info ("CANDEVICEfinder found {}", canDeviceFinder.getDeviceList());
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        //gets values from Y-axis of Right stick on gamepad, X-axis goes unused
        double vertical = Robot.oi.getLeftVerticalJoystickSquared();
        //gets values from X-axis of Left stick on gamepad, Y-axis goes unused
        double horizontal = Robot.oi.getRightHorizontalJoystickSquared();
        //displays current values on gamepad
            //Calls method to drive motors, declared in subsystem, sends real values to motors
            if(canDeviceFinder.isMAXPresent(1)) {
                Robot.driveSubsystem.arcadeDrive(horizontal, -vertical);
            }
            else{
                Robot.driveSubsystem.arcadeDrive(-vertical, horizontal);
            }
            
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        EventLogging.commandMessage(logger);
        //stops robot
        Robot.driveSubsystem.stopDrive();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run or when cancelled by whileHeld
    @Override
    protected void interrupted() {
        EventLogging.commandMessage(logger);
        //stops robot
        Robot.driveSubsystem.stopDrive();
    }
}