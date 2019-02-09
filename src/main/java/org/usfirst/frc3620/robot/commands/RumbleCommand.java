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

    private Hand rumbleHandDefault = Hand.BOTH;
    private Float rumbleIntensityDefault = 1f;
    private Float rumbleDurationDefault = 3f;
    private boolean continuous;

    private Timer timer = new Timer();
	
    public RumbleCommand(RumbleSubsystem subsystem, Hand hand, Float intensity, Float duration) {
        requires(subsystem); //requires the subsystem provided by caller

        rumbleSubsystem = subsystem;
        rumbleDuration = duration;
        rumbleHand = hand;
        rumbleIntensity = intensity;

        continuous = false;
    }

    public RumbleCommand(RumbleSubsystem subsystem, Float intensity, Float duration) {
        requires(subsystem); //requires the subsystem provided by caller

        rumbleSubsystem = subsystem;
        rumbleDuration = duration;
        rumbleHand = null;
        rumbleIntensity = intensity;

        continuous = false;
    }

    public RumbleCommand(RumbleSubsystem subsystem) {
        requires(subsystem); //requires the provided subsystem by caller

        rumbleSubsystem = subsystem;
        rumbleDuration = null;
        rumbleHand = null;
        rumbleIntensity = null;

        continuous = false;
    }

    public RumbleCommand(RumbleSubsystem subsystem, Hand hand, Float intensity) {
        requires(subsystem); //requires the subsystem provided by caller
        System.out.println("RumbleCommand called");
        rumbleSubsystem = subsystem;
        rumbleDuration = 0.05f;
        rumbleHand = hand;
        rumbleIntensity = intensity;

        continuous = true;
    }


    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        EventLogging.commandMessage(logger);
        System.out.println("Rumble Init");
        //sets the defaults
        if (rumbleDuration == null) {rumbleDuration = rumbleDurationDefault;}
        if (rumbleHand == null) {rumbleHand = rumbleHandDefault;}
        if (rumbleIntensity == null) {rumbleIntensity = rumbleIntensityDefault;}

        //Clears the rumble
        rumbleSubsystem.clearRumble();

        //Sets the rumble and starts the timer
        rumbleSubsystem.setRumble(rumbleHand, rumbleIntensity);
        if (!continuous) {
            timer.reset();
            timer.start();
        }
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        if (!continuous) {
            if (timer.get() >= rumbleDuration) 
            {
                rumbleSubsystem.clearRumble();
                System.out.println("clearRumble");
            }
        }
        System.out.println("Rumble execute");
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