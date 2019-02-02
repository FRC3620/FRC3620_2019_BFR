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
 * @version 2/02/19
 */
public class RumbleCommand extends Command {
    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
    public RumbleSubsystem rumbleSubsystem;
    public Hand rumbleHand;
    public Float rumbleIntensity; // 0.1f to 1f
    public Float rumbleDuration; // In seconds

    private Timer timer = new Timer();
	
    public RumbleCommand(RumbleSubsystem subsystem, Hand hand, Float intensity, Float duration) {
        requires(subsystem); //requires the subsystem provided by caller
        if (hand == null) {hand = Hand.BOTH;} //defaults to both hands
        if (intensity == null) {intensity = 1f;} //defaults to full intensity
        if (duration == null) {duration = 3.0f;} // defaults to 3 seconds of rumble

        rumbleSubsystem = subsystem;
        rumbleDuration = duration;
        rumbleHand = hand;
        rumbleIntensity = intensity;
    }

    public RumbleCommand(RumbleSubsystem subsystem, Float intensity, Float duration) {
        requires(subsystem); //requires the subsystem provided by caller
        if (intensity == null) {intensity = 1f;} //defaults to full intensity
        if (duration == null) {duration = 3.0f;} // defaults to 3 seconds of rumble

        rumbleSubsystem = subsystem;
        rumbleDuration = duration;
        rumbleHand = null;
        rumbleIntensity = intensity;
    }

    public RumbleCommand(RumbleSubsystem subsystem) {
        requires(subsystem); //requires the provided subsystem by caller

        rumbleSubsystem = subsystem;
        rumbleDuration = null;
        rumbleHand = null;
        rumbleIntensity = null;
    }


    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        EventLogging.commandMessage(logger);

        //Clears the rumble
        rumbleSubsystem.clearRumble();
        rumbleSubsystem.setRumble(rumbleHand, rumbleIntensity);
        timer.reset();
        timer.start();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        if (timer.get() >= rumbleDuration) {rumbleSubsystem.clearRumble();}
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
        rumbleSubsystem.clearRumble();
    }
}