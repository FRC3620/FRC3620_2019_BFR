package org.usfirst.frc3620.robot.commands;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class WaitJustALittle extends Command {
	double delay;
	Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
    Timer timer = new Timer();
    DigitalInput input;
    boolean desiredInputState;
    boolean doingDelay = false;

    public WaitJustALittle(double delaySeconds) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        this.delay = delaySeconds;
        doingDelay = true;
    }

    public WaitJustALittle(DigitalInput inputDevice, boolean desiredState) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        input = inputDevice;
        desiredInputState = desiredState;
        doingDelay = false;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	EventLogging.commandMessage(logger);
    	timer.reset();
        timer.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        boolean inputState = input.get();
        if(doingDelay == true){
            if(timer.get() > delay) {
                return true;
            }
        } else if(doingDelay == false){
            if(inputState == desiredInputState){
                return true;
            }
        } 
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	EventLogging.commandMessage(logger);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	EventLogging.commandMessage(logger);
    }
}