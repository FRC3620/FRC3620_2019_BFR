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
/**
 *
 */
public class LineDetectionCommand extends Command {
    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
    boolean isThereALineL;
   
    
    public LineDetectionCommand() {
        requires(Robot.intakeSubsystem);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        EventLogging.commandMessage(logger);
        isThereALineL = false;
        
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        
        if(Robot.intakeSubsystem.readLineSensorL()){
            if(isThereALineL == false)
            {
                //add command for rumble 
                RumbleCommand rumble = new RumbleCommand(Robot.rumbleSubsystemDriver,Hand.LEFT,1f);
                System.out.println("Detected Left");
            }
            isThereALineL = true;    
        }

        if(Robot.intakeSubsystem.readLineSensorLDirectly()){
            if(isThereALineL == true)
            {
                RumbleCommand rumble =  new RumbleCommand(Robot.rumbleSubsystemDriver,Hand.LEFT,0f);
                System.out.println("NOT Detected Left");
            }
            isThereALineL = false;
            Robot.intakeSubsystem.resetLineSensorL();
            
        }
        SmartDashboard.putBoolean("Line Left?", isThereALineL);
        
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
        //turn off Rumble
        Robot.intakeSubsystem.resetLineSensorL();
        
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run or when cancelled by whileHeld
    @Override
    protected void interrupted() {
        EventLogging.commandMessage(logger);
        //turn off Rumble
        Robot.intakeSubsystem.resetLineSensorL();
        
    }
}