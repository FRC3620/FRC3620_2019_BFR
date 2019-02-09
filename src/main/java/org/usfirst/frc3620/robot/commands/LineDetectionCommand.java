package org.usfirst.frc3620.robot.commands;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc3620.robot.Robot;
import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.misc.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc3620.robot.commands.RumbleCommand;
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
        System.out.println("In LineDetectCommand Consructor ");
        //requires(Robot.lineSubsystem);

        lineSensor = pos;
        if(lineSensor == LineSensor.LEFT_SENSOR)
            System.out.println("In LineDetectCommand Consruction for Left Sensor");
        else
            System.out.println("In LineDetectCommand Consruction for Right Sensor");

        
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        System.out.println("In LineDetectCommand initialize " + this + " " + lineSensor);
        EventLogging.commandMessage(logger);
        isThereALine = false;
       // rumbleCommand = new RumbleCommand(Robot.rumbleSubsystemDriver,Hand.LEFT,1f);        
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        /*
        if(lineSensor == LineSensor.LEFT_SENSOR)
            System.out.println("LineDetectCommand execute for left");
        else
            System.out.println("LineDetectCommand execute for right");
            */
        //reading the debounce counter (the counter will increment if line is detected.)
        if(Robot.lineSubsystem.readLineDetectionCounter(lineSensor)){
            // Line sensor detected now perform action
            if(isThereALine == false)
            {
               // rumbleCommand.start();
              // rumbleCommand = new RumbleCommand(Robot.rumbleSubsystemDriver,Hand.LEFT,1f);
               
               if(lineSensor == LineSensor.LEFT_SENSOR)
                    System.out.println("Detected Left");
                else
                    System.out.println("Detected Right");
                    
            }
            isThereALine = true;    
        }
        //reading the line sensor (digital input directly) to check 
        if(Robot.lineSubsystem.readliveSensorInput(lineSensor)){
            // Line sensor NOT detected, so do action
            if(isThereALine == true)
            {                
               // rumbleCommand.cancel();
              // rumbleCommand = new RumbleCommand(Robot.rumbleSubsystemDriver,Hand.LEFT,0f); 
              
               if(lineSensor == LineSensor.LEFT_SENSOR)
                    System.out.println("NO Line Detected Left");
                else
                    System.out.println("NO Line Detected Right");
                    
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