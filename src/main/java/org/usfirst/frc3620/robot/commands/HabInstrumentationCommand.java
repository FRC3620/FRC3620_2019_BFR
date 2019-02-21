package org.usfirst.frc3620.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.logger.FastDataLoggerCollections;
import org.usfirst.frc3620.logger.IFastDataLogger;

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

        iFastDataLogger.addDataProvider("r1", () -> Math.random());
        iFastDataLogger.addDataProvider("r2", () -> Math.random() * 2.0);
        iFastDataLogger.addDataProvider("r3", () -> Math.random() * 3.0);

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