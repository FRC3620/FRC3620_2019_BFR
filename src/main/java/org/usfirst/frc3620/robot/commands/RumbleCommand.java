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
    public RumbleSubsystem subsystem;
    public Hand hand;
    public Double intensity; // 0.1f to 1f
    public Double duration; // In seconds

    private Hand handDefault = Hand.BOTH;
    private Double intensityDefault = 1.0;
    private Double durationDefault = 3.0;
    private boolean continuous;

    private Timer timer = new Timer();

    public RumbleCommand() {}
	
    public void setRumble(RumbleSubsystem subsystem, Hand hand, Double intensity, Double duration) {
        requires(subsystem); //requires the subsystem provided by caller

        this.subsystem = subsystem;
        this.duration = duration;
        this.hand = hand;
        this.intensity = intensity;

        this.continuous = false;
    }

    public void setRumble(RumbleSubsystem subsystem, Double intensity, Double duration) {
        requires(subsystem); //requires the subsystem provided by caller

        this.subsystem = subsystem;
        this.duration = duration;
        this.hand = null;
        this.intensity = intensity;

        this.continuous = false;
    }

    public void setRumble(RumbleSubsystem subsystem) {
        requires(subsystem); //requires the provided subsystem by caller

        this.subsystem = subsystem;
        this.duration = null;
        this.hand = null;
        this.intensity = null;

        this.continuous = false;
    }

    public void setRumble(RumbleSubsystem subsystem, Hand hand, Double intensity) {
        requires(subsystem); //requires the subsystem provided by caller

        this.subsystem = subsystem;
        this.duration = 0.05;
        this.hand = hand;
        this.intensity = intensity;

        this.continuous = true;
    }


    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        EventLogging.commandMessage(logger);

        //sets the defaults
        if (duration == null) {duration = durationDefault;}
        if (hand == null) {hand = handDefault;}
        if (intensity == null) {intensity = intensityDefault;}

        //Clears the rumble
        subsystem.clearRumble();

        //Sets the rumble and starts the timer
        subsystem.setRumble(hand, intensity);
        if (!continuous) {
            timer.reset();
            timer.start();
        }
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        if (!continuous) {
            if (timer.get() >= rumbleDuration) {rumbleSubsystem.clearRumble();}
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        //Finishes when either the command is continuous, or the timer is up
        if (continuous) {return true;}
        else {
            if (timer.get() >= rumbleDuration) {return true;}
        }
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