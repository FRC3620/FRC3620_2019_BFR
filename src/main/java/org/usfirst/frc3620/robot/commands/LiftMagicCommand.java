package org.usfirst.frc3620.robot.commands;
import edu.wpi.first.wpilibj.command.Command;


import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.robot.subsystems.LiftSubsystem;
import org.usfirst.frc3620.robot.Robot;

/**
 *
 */
public class LiftMagicCommand extends Command {
    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
    LiftSubsystem.LiftHeight liftHeight;
    LiftSubsystem.LiftDecider liftDecider;
    
    public LiftMagicCommand(LiftSubsystem.LiftHeight _liftHeight, LiftSubsystem.LiftDecider ld) {
        liftHeight = _liftHeight;
        liftDecider = ld;
    }
    public LiftMagicCommand(LiftSubsystem.LiftHeight _liftHeight) {
        liftHeight = _liftHeight;
        liftDecider = null;
        // requires(Robot.laserCannonSubsystem);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        EventLogging.commandMessage(logger);
        LiftSubsystem.LiftDecider liftDeciderThisOne = liftDecider;
        if(liftDeciderThisOne == null) {
        liftDeciderThisOne = Robot.oi.getLiftDecider();
        }
        double desiredLiftHeight = Robot.liftSubsystem.calculateLiftHeight(liftHeight, liftDeciderThisOne);
        logger.info("setting left height for {} {} to {}", liftHeight, liftDeciderThisOne, desiredLiftHeight);
        Robot.liftSubsystem.setDoingPID(true);
        Robot.liftSubsystem.setDesiredHeight(desiredLiftHeight);
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