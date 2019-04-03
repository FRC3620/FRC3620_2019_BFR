package org.usfirst.frc3620.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.slf4j.Logger;
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
public class HabInstrumentationCommand extends Command {
	Logger logger = EventLogging.getLogger(getClass(), Level.INFO);

    IFastDataLogger dataLogger;

    public HabInstrumentationCommand() {
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
    	EventLogging.commandMessage(logger);

        dataLogger = new FastDataLoggerCollections();
        dataLogger.setInterval(0.1);
        dataLogger.setFilename("hab");
        dataLogger.setFilenameTimestamp(new Date());

        if (Robot.liftSubsystem.checkForLiftEncoder()) {
            dataLogger.addDataProvider("liftHeight", () -> Robot.liftSubsystem.getLiftHeight());
        }
        if (RobotMap.liftSubsystemMax != null) {
            dataLogger.addDataProvider("liftMotorRequestedOutput", () -> RobotMap.liftSubsystemMax.get());
            dataLogger.addDataProvider("liftMotorAppliedOutput", () -> RobotMap.liftSubsystemMax.getAppliedOutput());
            dataLogger.addDataProvider("liftMotorCurrent", () -> RobotMap.liftSubsystemMax.getOutputCurrent());
        }
        if (Robot.pivotSubsystem.checkForPivotEncoder()) {
            dataLogger.addDataProvider("pivotAngle", () -> Robot.pivotSubsystem.getPivotAngle());
        }
        if (RobotMap.pivotSubsystemMax != null) {
            dataLogger.addDataProvider("pivotMotorAppliedOutput", () -> RobotMap.pivotSubsystemMax.getAppliedOutput());
            dataLogger.addDataProvider("pivotMotorCurrent", () -> RobotMap.pivotSubsystemMax.getOutputCurrent());
        }
        if (RobotMap.pivotSubsystemMax2 != null) {
            dataLogger.addDataProvider("pivotMotor2AppliedOutputPower", () -> RobotMap.pivotSubsystemMax2.getAppliedOutput());
            dataLogger.addDataProvider("pivotMotor2Current", () -> RobotMap.pivotSubsystemMax2.getOutputCurrent());
        }

        if(Robot.driveSubsystem.ahrs!=null) {
            dataLogger.addDataProvider("NAV pitch", () -> Robot.driveSubsystem.ahrs.getPitch());
            dataLogger.addDataProvider("NAV roll", () -> Robot.driveSubsystem.ahrs.getRoll());
            dataLogger.addDataProvider("NAV angle", () -> Robot.driveSubsystem.ahrs.getAngle());
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