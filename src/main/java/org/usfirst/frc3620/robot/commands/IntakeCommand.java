package org.usfirst.frc3620.robot.commands;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc3620.robot.Robot;
import org.usfirst.frc3620.robot.RobotMap;
import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;

/**
 *
 */
public class IntakeCommand extends Command {
	Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
	
    public IntakeCommand() {
        // requires(Robot.laserCannonSubsystem);
        requires(Robot.intakeSubsystem);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        EventLogging.commandMessage(logger);
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        RobotMap.intakeSubsystemMiddleMotor.set(0.5);
        RobotMap.intakeSubsystemLowerMotor.set(0.5);
        RobotMap.intakeSubsystemUpperMotor.set(-0.7);
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
        Robot.intakeSubsystem.intakeOff();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run or when cancelled by whileHeld
    @Override
    protected void interrupted() {
        EventLogging.commandMessage(logger);
        Robot.intakeSubsystem.intakeOff();
    }
}