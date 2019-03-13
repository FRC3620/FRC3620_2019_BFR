package org.usfirst.frc3620.robot.commands;
import edu.wpi.first.wpilibj.command.Command;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.robot.subsystems.VisionSubsystem;
import org.usfirst.frc3620.robot.Robot;
/**
 *
 */
public class SwitchCameraCommand extends Command {
	Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
	
    public SwitchCameraCommand() {
        // requires(Robot.laserCannonSubsystem);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        EventLogging.commandMessage(logger);
        String[] camNames = Robot.visionSubsystem.getAllCameraNames();
        String currentCameraName = Robot.visionSubsystem.getCurrentCameraView();
        int resultAddress = -1;
        for (int i = 0; i < camNames.length; i ++ ){
            if(currentCameraName.equals(camNames[i])) {
                resultAddress = i;
                
            }
        }
        int nextCamera = resultAddress + 1;
        if (nextCamera >= camNames.length){
            nextCamera = 0;
        }
        logger.info("next camera:"+camNames[nextCamera]);
        Robot.visionSubsystem.setCurrentCameraView(camNames[nextCamera]);


    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return true;
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
    }
}