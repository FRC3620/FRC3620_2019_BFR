package org.usfirst.frc3620.robot.commands;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.Timer;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.robot.subsystems.RumbleSubsystem;
import org.usfirst.frc3620.misc.Hand;

/**
 * @author Nick Zimanski (SlippStream)
 * @version 1/26/19
 */
public class RumbleCommand extends Command {
    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
    public RumbleSubsystem rumbleSubsystem;
    public Hand rumbleHand;
    public Float rumbleIntensity; // 0f to 1f
    public Double rumbleDuration; // In seconds
    
    private Timer timer = new Timer();


	
    public RumbleCommand(RumbleSubsystem subsystem, Hand hand, Float intensity, Double duration) {
        requires(subsystem);
        if (hand == null) {hand = Hand.BOTH;}
        if (intensity == null) {intensity = 1f;}
        if (duration == null) {duration = 3.0;}

        rumbleSubsystem = subsystem;
        rumbleDuration = duration;
        rumbleHand = hand;
        rumbleIntensity = intensity;
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        EventLogging.commandMessage(logger);
        rumbleSubsystem.clearRumble(Hand.BOTH);
        rumbleSubsystem.setRumble(rumbleHand, rumbleIntensity);
        timer.reset();
        timer.start();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        if (timer.get() >= rumbleDuration) {rumbleSubsystem.clearRumble(rumbleHand);}
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