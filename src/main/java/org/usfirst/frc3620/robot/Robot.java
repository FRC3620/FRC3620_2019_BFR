package org.usfirst.frc3620.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.misc.RobotMode;
import org.usfirst.frc3620.robot.commands.*;
import org.usfirst.frc3620.robot.subsystems.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in 
 * the project.
 */
public class Robot extends TimedRobot {

    Command autonomousCommand;
    SendableChooser<Command> chooser = new SendableChooser<>();

    public static OI oi;
    
	// Team 3620 custom stuff
	static Logger logger;
	static RobotMode currentRobotMode = RobotMode.INIT, previousRobotMode;

    // declare Subsystems here
    public static DriveSubsystem driveSubsystem;
<<<<<<< HEAD
    public static LightSubsystem lightSubsystem;
    public static LiftSubsystem liftSubsystem;
=======

    public static RumbleSubsystem rumbleSubsystemDriver;
    public static RumbleSubsystem rumbleSubsystemOperator;

>>>>>>> 32b017556b4929dde7aaf26d09ee0c30468c9aab
    public static IntakeSubsystem intakeSubsystem;
    public static TrashSubsystem trashSubsystem;
    public static RumbleSubsystem rumbleSubsystemDriver;
    public static RumbleSubsystem rumbleSubsystemOperator;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {
		// set up logging
		logger = EventLogging.getLogger(Robot.class, Level.INFO);

        // set up hardware
        RobotMap.init();

        // set up subsystems
        // initalized drive subsystem, which control motors to move robot
        driveSubsystem = new DriveSubsystem();
      
        intakeSubsystem = new IntakeSubsystem();
        trashSubsystem = new TrashSubsystem();
        liftSubsystem = new LiftSubsystem();
        rumbleSubsystemDriver = new RumbleSubsystem();
        rumbleSubsystemOperator = new RumbleSubsystem();
        
        // OI must be constructed after subsystems. If the OI creates Commands
        //(which it very likely will), subsystems are not guaranteed to be
        // constructed yet. Thus, their requires() statements may grab null
        // pointers. Bad news. Don't move it.
        oi = new OI();

        // Add commands to Autonomous Sendable Chooser
        chooser.addDefault("Autonomous Command", new AutonomousCommand());
        SmartDashboard.putData("Auto mode", chooser);
    }

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    @Override
    public void disabledInit() {
		processRobotModeChange(RobotMode.DISABLED);
    }

    @Override
    public void disabledPeriodic() {
    	beginPeriodic();
    	Scheduler.getInstance().run();
    	endPeriodic();
    }

    @Override
    public void autonomousInit() {
    	processRobotModeChange(RobotMode.AUTONOMOUS);
		
        autonomousCommand = chooser.getSelected();
        // schedule the autonomous command (example)
        if (autonomousCommand != null) autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    @Override
    public void autonomousPeriodic() {
    	beginPeriodic();
        Scheduler.getInstance().run();
        endPeriodic();
    }

    @Override
    public void teleopInit() {
        RobotMap.canDeviceFinder.find();
        logger.info ("CAN bus = {}", RobotMap.canDeviceFinder.getDeviceList());
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();
        
		processRobotModeChange(RobotMode.TELEOP);
    }

    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopPeriodic() {
    	beginPeriodic();
        Scheduler.getInstance().run();
        endPeriodic();
    }
    
    @Override
	public void testInit() {
		// This makes sure that the autonomous stops running when
		// test starts running.
		if (autonomousCommand != null)
            ((Command) autonomousCommand).cancel();
            
		processRobotModeChange(RobotMode.TEST);
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
		beginPeriodic();
		//LiveWindow.run();
		endPeriodic();
	}
    
	/*
	 * this routine gets called whenever we change modes
	 */
	void processRobotModeChange(RobotMode newMode) {
		logger.info("Switching from {} to {}", currentRobotMode, newMode);
		
		if (currentRobotMode == RobotMode.INIT) {
			// RobotMap.checkTheCANBus();
		}
        
     
        
		previousRobotMode = currentRobotMode;
		currentRobotMode = newMode;

		// if any subsystems need to know about mode changes, let
		// them know here.
		// exampleSubsystem.processRobotModeChange(newMode);
		
	}

	/*
	 * these routines get called at the beginning and end of all periodics.
	 */
	void beginPeriodic() {
		// if some subsystems need to get called in all modes at the beginning
		// of periodic, do it here

		// don't need to do anything
	}

	void endPeriodic() {
		// if some subsystems need to get called in all modes at the end
		// of periodic, do it here
		//gearSubsystem.updateDashboard();

		// and log data!
		updateDashboard();
	}
	
	void updateDashboard() {
		//SmartDashboard.putNumber("driver y joystick", -Robot.m_oi.driveJoystick.getRawAxis(1));
		//SmartDashboard.putNumber("driver x joystick", Robot.m_oi.driveJoystick.getRawAxis(4));
    }
    
    public static RobotMode getCurrentRobotMode(){
        return currentRobotMode;
    }
    
}