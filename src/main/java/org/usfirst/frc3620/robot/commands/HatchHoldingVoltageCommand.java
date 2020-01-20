package org.usfirst.frc3620.robot.commands;
import edu.wpi.first.wpilibj.command.Command;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.robot.Robot;

/**
 *
 */
public class HatchHoldingVoltageCommand extends Command {
	Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
	
    public HatchHoldingVoltageCommand() {
        // requires(Robot.laserCannonSubsystem);
        requires(Robot.hatchSubsystem);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
    	EventLogging.commandMessage(logger);
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        if(Robot.hatchSubsystem.isHatchHoldLimitThere() == true){
            if(Robot.hatchSubsystem.isHatchHoldLimitDepressed()){
                Robot.hatchSubsystem.grab(0);
                return;
            }else{
                Robot.hatchSubsystem.grab(0.12);
                return;
            }
        }
        Robot.hatchSubsystem.grab(0.12);
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
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run or when cancelled by whileHeld
    @Override
    protected void interrupted() {
        EventLogging.commandMessage(logger);
    }
}