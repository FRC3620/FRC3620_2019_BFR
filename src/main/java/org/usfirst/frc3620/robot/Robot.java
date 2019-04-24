package org.usfirst.frc3620.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.slf4j.Logger;
import org.usfirst.frc3620.logger.DataLogger;
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

    Command setPivotAngle;
    Command autonomousCommand;
    SendableChooser<Command> chooser = new SendableChooser<>();

    public static OI oi;
    
	// Team 3620 custom stuff
	static Logger logger;
	static RobotMode currentRobotMode = RobotMode.INIT, previousRobotMode;

    // declare Subsystems here
    public static DriveSubsystem driveSubsystem;
    public static LightSubsystem lightSubsystem;
    public static LiftSubsystem liftSubsystem;
    public static IntakeSubsystem intakeSubsystem;
    public static TrashSubsystem trashSubsystem;
    public static RumbleSubsystem rumbleSubsystemDriver;
    public static RumbleSubsystem rumbleSubsystemOperator;
    public static HatchSubsystem hatchSubsystem;
    public static PivotSubsystem pivotSubsystem;
    public static VisionSubsystem visionSubsystem;

    // data logging
    public static DataLogger robotDataLogger;
    public static DriverStation driverStation;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {
		// set up logging
        logger = EventLogging.getLogger(Robot.class, Level.INFO);
        driverStation = driverStation.getInstance();
        
        // set up hardware
        RobotMap.init();
        RobotMap.reportMissingDevices();

        // set up subsystems
        // initalized drive subsystem, which control motors to move robot
        driveSubsystem = new DriveSubsystem();
        intakeSubsystem = new IntakeSubsystem();
        trashSubsystem = new TrashSubsystem();
        liftSubsystem = new LiftSubsystem();
        rumbleSubsystemDriver = new RumbleSubsystem();
        rumbleSubsystemOperator = new RumbleSubsystem();
        hatchSubsystem = new HatchSubsystem();
        pivotSubsystem = new PivotSubsystem();
        visionSubsystem = new VisionSubsystem();

        setPivotAngle = new SetPivotAngleCommand(PivotSubsystem.DesiredAngle.Top);
        
        // OI must be constructed after subsystems. If the OI creates Commands
        //(which it very likely will), subsystems are not guaranteed to be
        // constructed yet. Thus, their requires() statements may grab null
        // pointers. Bad news. Don't move it.
        oi = new OI();

          // Add commands to Autonomous Sendable Chooser
        chooser.addDefault("Autonomous Command", new AutonomousCommand());
        //SmartDashboard.putData("Auto mode", chooser);

        // get data logging going
        robotDataLogger = new DataLogger();
        new RobotDataLogger(robotDataLogger, RobotMap.canDeviceFinder);
        robotDataLogger.setInterval(1.000);
        robotDataLogger.start();
        //OperatorView operatorView = new OperatorView();
       // operatorView.operatorViewInit(RobotMap.amICompBot());
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
        logMatchInfo();

        autonomousCommand = chooser.getSelected();
        // schedule the autonomous command (example)
        if (autonomousCommand != null) autonomousCommand.start();
        visionSubsystem.turnLightSwitchOn();
    }

    /**
     * 
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
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();

        processRobotModeChange(RobotMode.TELEOP);
        logMatchInfo();

        driveSubsystem.clearReverseMode();
        visionSubsystem.turnLightSwitchOff();
        hatchSubsystem.hatchOut();
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

		RobotMap.canDeviceFinder.find();
		RobotMap.reportMissingDevices();
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
        //SmartDashboard.putData("Turn off Operator Rumble", new RumbleCommand(rumbleSubsystemOperator, true));
        //SmartDashboard.putData("Turn on Operator Rumble", new RumbleCommand(rumbleSubsystemOperator, false));
        //SmartDashboard.putData("Turn off Driver Rumble", new RumbleCommand(rumbleSubsystemDriver, true));
        //SmartDashboard.putData("Turn on Driver Rumble", new RumbleCommand(rumbleSubsystemDriver, false));
		//SmartDashboard.putNumber("driver y joystick", -Robot.m_oi.driveJoystick.getRawAxis(1));
        //SmartDashboard.putNumber("driver x joystick", Robot.m_oi.driveJoystick.getRawAxis(4));
        SmartDashboard.putNumber("Match time", (int) driverStation.getMatchTime());
    }
    
    public static RobotMode getCurrentRobotMode(){
        return currentRobotMode;
    }

    void logMatchInfo() {
	    DriverStation ds = DriverStation.getInstance();
	    if (ds.isFMSAttached()) {
	        logger.info ("FMS attached. Event name {}, match type {}, match number {}, replay number {}",
                    ds.getEventName(), ds.getMatchType(), ds.getMatchNumber(), ds.getReplayNumber());
        }
	    logger.info ("Alliance {}, position {}", ds.getAlliance(), ds.getLocation());
    }
}