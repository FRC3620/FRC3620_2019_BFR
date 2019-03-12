package org.usfirst.frc3620.robot.commands;
import edu.wpi.first.wpilibj.command.Command;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.robot.Robot;

/**
 *
 */
public class TrashRightCommand extends Command {
	Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
    
    private int leftOrRight;

    public TrashRightCommand() {
        // requires(Robot.laserCannonSubsystem);
        requires(Robot.liftSubsystem);
    }

    public int getLeftOrRight(){
        double currentNavXHeading = Robot.driveSubsystem.getRealAngle();
        if(currentNavXHeading < 120 || currentNavXHeading > 240){
            return 1;
        } else if(currentNavXHeading >= 120 && currentNavXHeading <= 240){
            return -1;
        } else{
            return 1;
        }
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        EventLogging.commandMessage(logger);
        leftOrRight = getLeftOrRight();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        Robot.trashSubsystem.conveyorBeltRight(-1*leftOrRight);
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
        Robot.trashSubsystem.conveyorBeltOff();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run or when cancelled by whileHeld
    @Override
    protected void interrupted() {
        Robot.trashSubsystem.conveyorBeltOff();
    	EventLogging.commandMessage(logger);
    }
}