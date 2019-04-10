package org.usfirst.frc3620.robot.commands;
import edu.wpi.first.wpilibj.command.Command;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.robot.Robot;

/**
 *
 */
public class TrashLeftCommand extends Command {
	Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
    
    private int leftOrRight;

    public TrashLeftCommand() {
        // requires(Robot.laserCannonSubsystem);
        requires(Robot.liftSubsystem);
    }

    
    public int getLeftOrRight(){
        double currentNavXHeading = Robot.driveSubsystem.getRealAngle();

        /* The purpose of the following if-else chain is to determine if you're pointing the same
        way that you started or pointing the opposite direction. 
        
        If you're pointing the same direction (i.e pointed in the arc from -120 (i.e 240 with absolute) to 120) 
        that you started in, left and right are still left and right, and thus, 
        you should apply the same power in the same direction that you would normally.
        
        If you are pointing the other direction (i.e facing backwards on the arc from 120 to 240),
        left is now right and right is now left compared to the direction you started in.
        Therefore, make the belts go the other direction by multiplying their power by -1.
        
        The purpose of making the arc from -120 to 120 was that, if we are lining up to do a shot at the front of
        the cargo ship, we will either be pointed at ~90 or ~270, so we should ensure that the directions of the shot
        don't unduely and surprisingly change. */

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
        double power = Robot.oi.getLeftOperatorTrigger();
        Robot.trashSubsystem.conveyorBeltLeft(power*leftOrRight);
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
        EventLogging.commandMessage(logger);
        Robot.trashSubsystem.conveyorBeltOff();
    }
}