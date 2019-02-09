package org.usfirst.frc3620.robot.commands;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.Timer;

import org.slf4j.Logger;
import org.usfirst.frc3620.robot.Robot;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.robot.subsystems.RumbleSubsystem;
import org.usfirst.frc3620.misc.Hand;

/**
 * @author Nick Zimanski (SlippStream)
 * @version 2/09/19
 * 
 * Finalised command -- rules in subsystem
 */
public class RumbleCommand extends Command {
    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
    public RumbleSubsystem subsystem;
    public Hand hand;
    public Double intensity; // 0.1 to 1.0
    public Double duration; // In seconds

    private Hand handDefault = Hand.BOTH;
    private Double intensityDefault = 1.0;
    private Double durationDefault = 1.0;

    private boolean continuous;

    private Timer timer = new Timer();
    
    /**
     * @param subsystem Which controller to rumble. Either Robot.rumbleSubsystemDriver or Robot.rumbleSubsystemOperator
     * @param hand Which side of the controller to rumble. Either Hand.LEFT, Hand.RIGHT, or Hand.BOTH
     * @param intensity How hard to rumble the controller. Between 0.1 and 1.0
     * @param duration How long the controller will rumble in seconds
     */
    public RumbleCommand(RumbleSubsystem subsystem, Hand hand, Double intensity, Double duration) {
        requires(subsystem); //requires the subsystem provided by caller

        this.subsystem = subsystem;
        this.duration = duration;
        this.hand = hand;
        this.intensity = intensity;

        continuous = false;
    }

    /**
     * @param subsystem Which controller to rumble. Either Robot.rumbleSubsystemDriver or Robot.rumbleSubsystemOperator
     * @param intensity How hard to rumble the controller. Between 0.1 and 1.0
     * @param duration How long the controller will rumble in seconds
     * 
     * @see Hand defaults to Hand.BOTH
     */
    public RumbleCommand(RumbleSubsystem subsystem, Double intensity, Double duration) {
        requires(subsystem); //requires the subsystem provided by caller

        this.subsystem = subsystem;
        this.duration = duration;
        this.hand = null;
        this.intensity = intensity;

        continuous = false;
    }

    /**
     * @param subsystem Which controller to rumble. Either Robot.rumbleSubsystemDriver or Robot.rumbleSubsystemOperator
     * 
     * @see Hand defaults to Hand.BOTH
     * @see Intensity defaults to 1.0
     * @see Duration defaults to 1.0
     */
    public RumbleCommand(RumbleSubsystem subsystem) {
        requires(subsystem); //requires the provided subsystem by caller

        this.subsystem = subsystem;
        this.duration = null;
        this.hand = null;
        this.intensity = null;

        continuous = false;
    }

    /**
     * @param subsystem Which controller to rumble. Either Robot.rumbleSubsystemDriver or Robot.rumbleSubsystemOperator
     * @param hand Which side of the controller to rumble. Either Hand.LEFT, Hand.RIGHT, or Hand.BOTH
     * @param intensity How hard to rumble the controller. Between 0.1 and 1.0
     * 
     * @see This command should ONLY be used if you plan on interrupting it with this command again with the intensity at 0.0
     */
    public RumbleCommand(RumbleSubsystem subsystem, Hand hand, Double intensity) {
        requires(subsystem); //requires the subsystem provided by caller

        this.subsystem = subsystem;
        this.duration = null;
        this.hand = hand;
        this.intensity = intensity;

        continuous = true;
    }


    // Called just before this Command runs the first time
    @Override
    protected void initialize() {

        //sets the defaults
        if (duration == null) {duration = durationDefault;}
        if (hand == null) {hand = handDefault;}
        if (intensity == null) {intensity = intensityDefault;}

        //logs info
        if (subsystem == Robot.rumbleSubsystemDriver) {
            logger.info("Rumbling driver controller");
        }
        else {
            logger.info("Rumbling operator controller");
        }

        //Clears the rumble
        subsystem.clearRumble();

        //Sets the rumble and starts the timer
        subsystem.setRumble(hand, intensity);
        if (!continuous) {
            timer.start();
        }

        EventLogging.commandMessage(logger);
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        //Finishes when either the command is continuous, or the timer is up
        if (continuous) {return true;}
        else {
            if (timer.get() >= duration) {return true;}
        }
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        if (subsystem == Robot.rumbleSubsystemDriver) {
            logger.info("Driver rumble finished");
        }
        else {
            logger.info("Operator rumble finished");
        }
        subsystem.clearRumble();

        EventLogging.commandMessage(logger);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run or when cancelled by whileHeld
    @Override
    protected void interrupted() {
        if (subsystem == Robot.rumbleSubsystemDriver) {
            logger.info("Driver rumble interrupted");
        }
        else {
            logger.info("Operator rumble interrupted");
        }
        subsystem.clearRumble();

        EventLogging.commandMessage(logger);
    }
}