package org.usfirst.frc3620.robot.commands;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc3620.robot.Robot;
import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.robot.subsystems.LineSubsystem;
import org.usfirst.frc3620.misc.LineSensor;

/**
 *
 */
public class LineDetectionCommand extends Command {
    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
    boolean isThereALine;
    RumbleCommand rumbleCommand;
    LineSensor lineSensor=LineSensor.LEFT_SENSOR; //default init
    
    
    public LineDetectionCommand(LineSensor pos) {
        lineSensor = pos;   
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        EventLogging.commandMessage(logger);
        isThereALine = false;
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        //reading the debounce counter (the counter will increment if line is detected.)
        if(Robot.lineSubsystem.readLineDetectionCounter(lineSensor)){
            // Line sensor detected now perform action
            if(isThereALine == false)
            {
                Robot.lineSubsystem.setLineDetectionStatus(lineSensor,true);
            }
            isThereALine = true;    
        }
        //reading the line sensor (digital input directly) to check 
        if(Robot.lineSubsystem.readliveSensorInput(lineSensor)){
            // Line sensor NOT detected, so do action
            if(isThereALine == true)
            {                
                Robot.lineSubsystem.setLineDetectionStatus(lineSensor,false);              
            }
            isThereALine = false;
            //reset the counter if not detected
            Robot.lineSubsystem.resetLineDetectionCounter(lineSensor);            
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
        //reset the counter 
        Robot.lineSubsystem.resetLineDetectionCounter(lineSensor);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run or when cancelled by whileHeld
    @Override
    protected void interrupted() {
        EventLogging.commandMessage(logger);
        //reset the counter 
        Robot.lineSubsystem.resetLineDetectionCounter(lineSensor);
    }
}