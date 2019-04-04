package org.usfirst.frc3620.robot.commands;
import edu.wpi.first.wpilibj.command.Command;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.robot.Robot;

/**
 *
 */
public class ChangeSlotSelectionSideCommand extends Command {
	Logger logger = EventLogging.getLogger(getClass(), Level.INFO);

    boolean left;

    public ChangeSlotSelectionSideCommand(boolean Left) {
        // requires(Robot.laserCannonSubsystem);
        left = Left;
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        EventLogging.commandMessage(logger);
        if(left == true){
            Robot.cargoMaster.runSlotSelectionLeft = true;
            Robot.cargoMaster.runSlotSelectionRight = false;
        } else if(left == false){
            Robot.cargoMaster.runSlotSelectionLeft = false;
            Robot.cargoMaster.runSlotSelectionRight = true;
        } else{
            Robot.cargoMaster.runSlotSelectionLeft = false;
            Robot.cargoMaster.runSlotSelectionRight = false;
        }
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
       
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        EventLogging.commandMessage(logger);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run or when cancelled by whileHeld
    @Override
    protected void interrupted() {
        EventLogging.commandMessage(logger);
    }
}