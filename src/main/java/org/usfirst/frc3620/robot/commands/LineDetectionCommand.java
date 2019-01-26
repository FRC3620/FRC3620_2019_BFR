package org.usfirst.frc3620.robot.commands;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc3620.robot.Robot;
import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;

/**
 *
 */
public class LineDetectionCommand extends Command {
	Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
	
    public LineDetectionCommand() {
        // requires(Robot.laserCannonSubsystem);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
    	EventLogging.commandMessage(logger);
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        if(Robot.intakeSubsystem.readLineSensor()){
            //add command for rumble 
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        if(Robot.intakeSubsystem.readLineSensorDirectly()){
            return true;
        }
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        EventLogging.commandMessage(logger);
        //turn off Rumble
        Robot.intakeSubsystem.resetLineSensor();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run or when cancelled by whileHeld
    @Override
    protected void interrupted() {
        EventLogging.commandMessage(logger);
        //turn off Rumble
        Robot.intakeSubsystem.resetLineSensor();
    }
}