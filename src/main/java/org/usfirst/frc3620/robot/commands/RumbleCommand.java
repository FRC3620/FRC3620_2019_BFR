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
 * @version 2/06/19
 */
public class RumbleCommand extends Command {
    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
    final RumbleSubsystem subsystem;
    public Hand hand;
    public Double intensity; // 0.1f to 1f
    public Double duration = 0.0; // In seconds

    private Hand handDefault = Hand.BOTH;
    private Double intensityDefault = 1.0;
    private Double durationDefault = 1.0;
    private boolean continuous = true;

    private Timer timer = new Timer();

    public RumbleCommand(RumbleSubsystem subsystem) {
        requires(subsystem); //requires the subsystem provided by caller
        this.subsystem = subsystem;
    }

    /**
     * @param hand The side(s) of the controller to rumble
     * @param intensity A double between 0.1 and 1 definining the intensity (1 is highest)
     * @param duration A double in seconds of how long you want the rumble to last
     */
    public void setRumble(Hand hand, Double intensity, Double duration) {
        this.duration = duration;
        this.hand = hand;
        this.intensity = intensity;
        this.continuous = false;
       
        this.startRumble();
    }

    /**
     * @param intensity A double between 0.1 and 1.0 definining the intensity (1.0 is highest)
     * @param duration A double in seconds of how long you want the rumble to last
     * @see hand is defaulted to Hand.BOTH
     */
    public void setRumble(Double intensity, Double duration) {
        this.duration = duration;
        this.hand = Hand.BOTH;
        this.intensity = intensity;
        this.continuous = false;

        this.startRumble();
    }

    /**

     * @see hand is defaulted to Hand.BOTH
     * @see intensity is defaulted to 1.0
     * @see duration is defaulted to 1.0
     */
    public void setRumble() {
        this.duration = 1.0;
        this.hand = Hand.BOTH;
        this.intensity = 1.0;
        this.continuous = false;

        this.startRumble();
    }

    /**
     * @param hand The side(s) of the controller to rumble
     * @param intensity A double between 0.1 and 1 definining the intensity (1 is highest)
     * @see THIS METHOD SHOULD ONLY BE USED IF YOU INTED TO CLEAR IT
     */
    public void setRumble(Hand hand, Double intensity) {
        this.duration = null;
        this.hand = hand;
        this.intensity = intensity;
        this.continuous = true;

        this.startRumble();
    }

    /**
     * @see This will clear all rumbles on the controller no matter what started it
     */
    public void clearRumble() {
        subsystem.clearRumble();
    }

    /**
     * Only called within this class to initiate the rumble and start the timer
     */
    private void startRumble() {
        subsystem.clearRumble();
        timer.stop();
        timer.reset();
        timer.start();
        subsystem.setRumble(hand, intensity);
    }
    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        EventLogging.commandMessage(logger);
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        if (!continuous) {
            if (timer.get() >= duration) {subsystem.clearRumble();}
        }
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
        subsystem.clearRumble();
        EventLogging.commandMessage(logger);
    }
}