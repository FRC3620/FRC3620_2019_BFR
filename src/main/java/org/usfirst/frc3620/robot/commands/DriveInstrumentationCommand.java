package org.usfirst.frc3620.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.slf4j.Logger;
import org.usfirst.frc3620.logger.DataLogger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.logger.FastDataLoggerCollections;
import org.usfirst.frc3620.logger.IFastDataLogger;
import org.usfirst.frc3620.robot.Robot;
import org.usfirst.frc3620.robot.RobotMap;

import java.util.Date;

/**
 *
 */
public class DriveInstrumentationCommand extends Command {
	Logger logger = EventLogging.getLogger(getClass(), Level.INFO);

    IFastDataLogger dataLogger;

    public DriveInstrumentationCommand() {
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
    	EventLogging.commandMessage(logger);

        dataLogger = new FastDataLoggerCollections();
        dataLogger.setInterval(0.1);
        dataLogger.setFilename("drive");
        dataLogger.setFilenameTimestamp(new Date());
        if(RobotMap.leftsideCANEncoder != null) {
            dataLogger.addDataProvider("Left Side Velocity", () -> RobotMap.leftsideCANEncoder.getVelocity());
            dataLogger.addDataProvider("Right Side Velocity", () -> RobotMap.rightsideCANEncoder.getVelocity());

            dataLogger.addDataProvider("Commanded Power Left", () -> RobotMap.driveSubsystemMaxLeftA.get());
            dataLogger.addDataProvider("Commanded Power Right", () -> RobotMap.driveSubsystemMaxRightA.get());

            dataLogger.addDataProvider("Actual Power Left", () -> RobotMap.driveSubsystemMaxLeftA.getAppliedOutput());
            dataLogger.addDataProvider("Actual Power Right", () -> RobotMap.driveSubsystemMaxRightA.getAppliedOutput());

            dataLogger.addDataProvider("Actual Current Left A", () -> RobotMap.driveSubsystemMaxLeftA.getOutputCurrent());
            dataLogger.addDataProvider("Actual Current Right A", () -> RobotMap.driveSubsystemMaxRightA.getOutputCurrent());

            dataLogger.addDataProvider("Actual Current Left B", () -> RobotMap.driveSubsystemMaxLeftB.getOutputCurrent());
            dataLogger.addDataProvider("Actual Current Right B", () -> RobotMap.driveSubsystemMaxRightB.getOutputCurrent());
        
        
        }

        dataLogger.start();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
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
    	dataLogger.done();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run or when cancelled by whileHeld
    @Override
    protected void interrupted() {
    	EventLogging.commandMessage(logger);
        dataLogger.done();
    }
}