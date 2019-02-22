package org.usfirst.frc3620.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.logger.FastDataLoggerCollections;
import org.usfirst.frc3620.logger.IFastDataLogger;
import org.usfirst.frc3620.robot.Robot;
import org.usfirst.frc3620.robot.RobotMap;
import org.usfirst.frc3620.robot.subsystems.LiftSubsystem;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */
public class HabInstrumentationCommand extends Command {
	Logger logger = EventLogging.getLogger(getClass(), Level.INFO);

    IFastDataLogger iFastDataLogger;

    public HabInstrumentationCommand() {
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
    	EventLogging.commandMessage(logger);

        SimpleDateFormat formatName = new SimpleDateFormat(
                "yyyyMMdd-HHmmss");
        String _timestampString = formatName.format(new Date());
    	String name = "hab_" + _timestampString;

        iFastDataLogger = new FastDataLoggerCollections();
        iFastDataLogger.setInterval(0.1);
        iFastDataLogger.setFilename(name);
        iFastDataLogger.addMetadata("pi", Math.PI);
        iFastDataLogger.addMetadata("e", Math.E);

        if(Robot.liftSubsystem.checkForLiftEncoder()) {
            iFastDataLogger.addDataProvider("getLiftHeight()", () -> Robot.liftSubsystem.getLiftHeight());
            iFastDataLogger.addDataProvider("Lift Motor power", () -> Robot.liftSubsystem.getMaxPower());
            iFastDataLogger.addDataProvider("Lift Motor current", () -> Robot.liftSubsystem.getMaxCurrent());
            iFastDataLogger.addDataProvider("Lift Motor bus voltage", () -> Robot.liftSubsystem.getMaxVoltage());
        }
        if(Robot.pivotSubsystem.checkForLiftEncoder()) {
            iFastDataLogger.addDataProvider("getPivotAngle()", () -> Robot.pivotSubsystem.getPivotAngle());
            iFastDataLogger.addDataProvider("Pivot Motor power", () -> Robot.pivotSubsystem.getMaxPower());
            iFastDataLogger.addDataProvider("Pivot Motor current", () -> Robot.pivotSubsystem.getMaxCurrent());
            iFastDataLogger.addDataProvider("Pivot Motor bus voltage", () -> Robot.pivotSubsystem.getMaxVoltage());
        }

        iFastDataLogger.addDataProvider("RIO X", () -> RobotMap.accel.getX());
        iFastDataLogger.addDataProvider("RIO Y", () -> RobotMap.accel.getY());
        iFastDataLogger.addDataProvider("RIO Z", () -> RobotMap.accel.getZ());

        if(Robot.driveSubsystem.ahrs!=null) {
            iFastDataLogger.addDataProvider("NAV pitch", () -> Robot.driveSubsystem.ahrs.getPitch());
            iFastDataLogger.addDataProvider("NAV roll", () -> Robot.driveSubsystem.ahrs.getRoll());
            iFastDataLogger.addDataProvider("NAV angle", () -> Robot.driveSubsystem.ahrs.getAngle());

            iFastDataLogger.addDataProvider("NAV x acc", () -> Robot.driveSubsystem.ahrs.getRawAccelX());
            iFastDataLogger.addDataProvider("NAV y acc", () -> Robot.driveSubsystem.ahrs.getRawAccelY());
            iFastDataLogger.addDataProvider("NAV z acc", () -> Robot.driveSubsystem.ahrs.getRawAccelZ());
        }

        iFastDataLogger.start();
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
    	iFastDataLogger.done();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run or when cancelled by whileHeld
    @Override
    protected void interrupted() {
    	EventLogging.commandMessage(logger);
        iFastDataLogger.done();
    }
}