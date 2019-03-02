package org.usfirst.frc3620.robot.commands;
import edu.wpi.first.wpilibj.command.Command;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.misc.RobotMode;
import org.usfirst.frc3620.robot.Robot;
import org.usfirst.frc3620.robot.subsystems.DriveSubsystem;
import org.usfirst.frc3620.robot.subsystems.PivotSubsystem;
import org.usfirst.frc3620.robot.subsystems.PivotSubsystem.PivotMode;

/**
 *
 */
public class HabClimbCommand extends Command {
	Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
	
    public HabClimbCommand() {
        requires(Robot.driveSubsystem);
        // requires(Robot.laserCannonSubsystem);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        Robot.pivotSubsystem.setCurrentPivotMode(PivotSubsystem.PivotMode.HAB);
        Robot.liftSubsystem.setManualMode();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        double vertical = Robot.oi.getLeftVerticalJoystickSquared();

        Robot.driveSubsystem.arcadeDrive(-vertical, 0);
        Robot.driveSubsystem.habDrive(-vertical);
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
        Robot.pivotSubsystem.setCurrentPivotMode(PivotSubsystem.PivotMode.MANUAL);
        Robot.driveSubsystem.stopDrive();
        Robot.driveSubsystem.habDrive(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run or when cancelled by whileHeld
    @Override
    protected void interrupted() {
    	EventLogging.commandMessage(logger);
        Robot.pivotSubsystem.setCurrentPivotMode(PivotSubsystem.PivotMode.MANUAL);
        Robot.driveSubsystem.stopDrive();
        Robot.driveSubsystem.habDrive(0);
    }
}