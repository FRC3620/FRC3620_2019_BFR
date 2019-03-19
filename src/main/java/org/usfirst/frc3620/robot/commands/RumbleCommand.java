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

    private boolean disabled, continuous, disableChange = false;

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
        this(subsystem, null, intensity, duration);
    }

    /**
     * @param subsystem Which controller to rumble. Either Robot.rumbleSubsystemDriver or Robot.rumbleSubsystemOperator
     * 
     * @see Hand defaults to Hand.BOTH
     * @see Intensity defaults to 1.0
     * @see Duration defaults to 1.0
     */
    public RumbleCommand(RumbleSubsystem subsystem) {
        this(subsystem, null, null, null);
    }

    /**
     * @param subsystem Which controller to rumble. Either Robot.rumbleSubsystemDriver or Robot.rumbleSubsystemOperator
     * @param hand Which side of the controller to rumble. Either Hand.LEFT, Hand.RIGHT, or Hand.BOTH
     * @param intensity How hard to rumble the controller. Between 0.1 and 1.0
     * 
     * @see This command should ONLY be used if you plan on interrupting it with .cancel()
     */
    public RumbleCommand(RumbleSubsystem subsystem, Hand hand, Double intensity) {
        this(subsystem, hand, intensity, null);

        continuous = true;
    }

    /**
     * @param subsystem Which controller to disable/enable rumble for
     * @param disabled Which state to set the controller to
     * 
     * @see This command will stop any and all rumble from happening on the controller until it's enabled
     */
    public RumbleCommand(RumbleSubsystem subsystem, Boolean disabled) {
        this.subsystem = subsystem;
        this.disabled = disabled;
        this.disableChange = true;
    }


    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        EventLogging.commandMessage(logger);
        if (!disableChange) {
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
        }
        else {
            subsystem.setDisabled(this.disabled);
        }
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        //Finishes the command when the timer is up
        if (disableChange) {
            return true;
        }
        if (!continuous) {
            if (timer.get() >= duration) {return true;}
        }
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        EventLogging.commandMessage(logger);
        if (!disabled) {
            if (subsystem == Robot.rumbleSubsystemDriver) {
                logger.info("Driver rumble finished");
            }else{
                logger.info("Operator rumble finished");
            }

            subsystem.clearRumble();
        }
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run or when cancelled by whileHeld
    @Override
    protected void interrupted() {
        EventLogging.commandMessage(logger);
        if (subsystem == Robot.rumbleSubsystemDriver) {
            logger.info("Driver rumble interrupted");
        }
        else {
            logger.info("Operator rumble interrupted");
        }
        subsystem.clearRumble();
    } 
}