package org.usfirst.frc3620.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc3620.misc.Hand;
import org.usfirst.frc3620.misc.TriggerButton;
import org.usfirst.frc3620.misc.DPad;
import org.usfirst.frc3620.misc.XBoxConstants;
import org.usfirst.frc3620.robot.commands.*;
import org.usfirst.frc3620.robot.paths.AlignToPointD;
import org.usfirst.frc3620.robot.paths.AutoAlignmentTemplate;
import org.usfirst.frc3620.robot.paths.TrainingPath;
import org.usfirst.frc3620.robot.subsystems.LiftSubsystem;
import org.usfirst.frc3620.robot.subsystems.PivotSubsystem;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    //// CREATING BUTTONS
    // One type of button is a joystick button which is any button on a joystick.
    // You create one by telling it which joystick it's on and which button
    // number it is.
    // Joystick stick = new Joystick(port);
    // Button button = new JoystickButton(stick, buttonNumber);

    // There are a few additional built in buttons you can use. Additionally,
    // by subclassing Button you can create custom triggers and bind those to
    // commands the same as any other Button.

    //// TRIGGERING COMMANDS WITH BUTTONS
    // Once you have a button, it's trivial to bind it to a button in one of
    // three ways:

    // Start the command when the button is pressed and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenPressed(new ExampleCommand());

    // Run the command while the button is being held down and interrupt it once
    // the button is released.
    // button.whileHeld(new ExampleCommand());

    // Start the command when the button is released  and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ExampleCommand());

    private Joystick driverJoystick;
    private Joystick operatorJoystick;

    @SuppressWarnings("resource")
	public OI() {
        //to interface with joysticks, no special initiallization nessessary
        driverJoystick = new Joystick(0);
        operatorJoystick = new Joystick(1);

        DPad driverDpad = new DPad(driverJoystick, 0);
        DPad operatorDPad = new DPad(operatorJoystick, 0);

        Robot.rumbleSubsystemDriver.setController(driverJoystick);
        Robot.rumbleSubsystemOperator.setController(operatorJoystick);
        // map buttons to Joystick buttons here
        
        //Declare buttons
            
            //driver controls
            Button reverseDrive = new JoystickButton(driverJoystick, XBoxConstants.BUTTON_RIGHT_BUMPER);
            Button inTakeIn = new JoystickButton(driverJoystick, XBoxConstants.BUTTON_LEFT_BUMPER);
            Button driveIn = new JoystickButton(driverJoystick, XBoxConstants.BUTTON_A);
            Button switchCamera = new JoystickButton(driverJoystick, XBoxConstants.BUTTON_Y);
            Button inTakeOut = new JoystickButton(driverJoystick, XBoxConstants.BUTTON_START);
            TriggerButton getRumbleLeft = new TriggerButton(driverJoystick, true, 0.4);
            TriggerButton getRumbleRight = new TriggerButton(driverJoystick, false, 0.4);

            //operator controls 
            Button hatchExtend = new JoystickButton(operatorJoystick, XBoxConstants.BUTTON_X);
            Button hatchCollect = new JoystickButton(operatorJoystick, XBoxConstants.BUTTON_B);
            Button trashIn = new JoystickButton(operatorJoystick, XBoxConstants.BUTTON_Y);
            Button habClimbButton = new JoystickButton(operatorJoystick, XBoxConstants.BUTTON_LEFT_BUMPER);
            Button middlePos = new JoystickButton(operatorJoystick, XBoxConstants.BUTTON_RIGHT_BUMPER);
            Button liftHome = new JoystickButton(operatorJoystick, XBoxConstants.BUTTON_A);
            Button lockLiftPinsButton = new JoystickButton(operatorJoystick, XBoxConstants.BUTTON_BACK);
            Button cargoHeight = new JoystickButton(operatorJoystick, XBoxConstants.BUTTON_START);

            driverDpad.up().whenPressed(new ChangeSlotSelectionLevelCommand(true));
            driverDpad.right().toggleWhenPressed(new ChangeSlotSelectionSideCommand(true));
            driverDpad.left().toggleWhenPressed(new ChangeSlotSelectionSideCommand(false));
            driverDpad.down().whenPressed(new ChangeSlotSelectionLevelCommand(false));

            Button trashLeftButton = new TriggerButton(operatorJoystick, true, 0.6);
            trashLeftButton.whileHeld(new TrashLeftCommand());

            Button trashRightButton = new TriggerButton(operatorJoystick, false, 0.6);
            trashRightButton.whileHeld(new TrashRightCommand());
            
            operatorDPad.down().whenPressed(new SetPivotAngleCommand(PivotSubsystem.DesiredAngle.Bottom));
            operatorDPad.up().whenPressed(new SetPivotAngleCommand(PivotSubsystem.DesiredAngle.Top));
            operatorDPad.right().whenPressed(new SetPivotAngleCommand(PivotSubsystem.DesiredAngle.Middle));
            operatorDPad.left().whenPressed(new SetPivotAngleCommand(PivotSubsystem.DesiredAngle.Middle));

            //buttons run commands
            inTakeIn.toggleWhenPressed(new IntakeCommand());
            inTakeOut.toggleWhenPressed(new OutTakeCommand());
            trashIn.toggleWhenPressed(new TrashInCommand());
            hatchExtend.toggleWhenPressed(new HatchOuttakeCommandGroup());
            hatchCollect.toggleWhenPressed(new HatchCollectCommand());
            habClimbButton.whenPressed(new HabClimbCommand());
            liftHome.whenPressed(new SetLiftHeightCommand(LiftSubsystem.SETPOINT_CARGO_TRASHIN, true));
            middlePos.whenPressed(new SetLiftHeightCommand(LiftSubsystem.SETPOINT_CARGO_ROCKET_MIDDLE, true));
            reverseDrive.whenPressed(new ToggleReverseCommand());
            driveIn.whileHeld(new AutoMoveForwardCommand(10,.7));
            lockLiftPinsButton.toggleWhenPressed(new LockLiftPinsCommand());
            cargoHeight.whenPressed(new SetLiftHeightCommand(LiftSubsystem.SETPOINT_CARGO_CARGOSHIP, true));
            switchCamera.whenPressed(new SwitchCameraCommand());
            //SmartDashboard.putData(new HabInstrumentationCommand());
            getRumbleLeft.toggleWhenPressed(new AutoCargoAlignRumbleLeft());
            getRumbleRight.toggleWhenPressed(new AutoCargoAlignRumbleRight());



            SmartDashboard.putData(new HabInstrumentationCommand());

           

            SmartDashboard.putData("Rumble both", new RumbleCommand(Robot.rumbleSubsystemDriver, Hand.BOTH, 0.2, 60.0));
            SmartDashboard.putData("Rumble left", new RumbleCommand(Robot.rumbleSubsystemDriver, Hand.LEFT, 0.2, 3.0));

            SmartDashboard.putData("AutonomousAlign from 45", new AutoAlignmentTemplate(Robot.visionSubsystem.getFrontTargetDistance(), Robot.visionSubsystem.getFrontTargetAngle()));
            SmartDashboard.putData("TrainingPath", new TrainingPath());
            SmartDashboard.putData("CenterOnTarget", new VisionAlignmentCommand());
            SmartDashboard.putData("TapTarget", new TravelAlignPushCommand());
            SmartDashboard.putData("DriveForward", new AutoMoveForwardCommand(15,.7));
            SmartDashboard.putData("Align to Hatch Target", new AutonomousAlignmentAndApproachCommand());
            SmartDashboard.putData("LineUpWithCargoship", new AutoLineUpWithCargoshipRightCommand());

        }

    public Joystick getDriverJoystick() {
        return driverJoystick;
    }
    public Joystick getOperatorJoystick() {
        return operatorJoystick;
    }
    
    public double computeDeadband (double position, double deadband) {
        if (Math.abs(position) < deadband) {
            return 0;
        }

        //values eventually passed to arcadeDrive, which squares the values itself
    	return position;
    }

    public double getLeftVerticalJoystickSquared() {
        //gets value from x or y axis on joysticks on gamepad. In this istance, Left X
    	return computeDeadband(driverJoystick.getRawAxis(XBoxConstants.AXIS_LEFT_Y), 0);
    }

    public double getRightHorizontalJoystickSquared() {
        //gets value from x or y axis on joysticks on gamepad. In this istance, Right Y
        return computeDeadband(driverJoystick.getRawAxis(XBoxConstants.AXIS_RIGHT_X), 0);
    }

    public double getRightVerticalJoystick() {
        //gets value from x or y axis on joysticks on gamepad. In this istance, Right
        return computeDeadband(driverJoystick.getRawAxis(XBoxConstants.AXIS_RIGHT_Y), 0.2);
       }

    public double getClimberVerticalJoystick() {
        //gets value from y axis on (left)Climberjoystick on operatorJoystick. 
    	return computeDeadband(operatorJoystick.getRawAxis(XBoxConstants.AXIS_LEFT_Y), 0);
    }

    public double getClimberHorizontalJoystick() {
        //gets value from x axis on (left)Climberjoystick on operatorJoystick.
    	return computeDeadband(operatorJoystick.getRawAxis(XBoxConstants.AXIS_LEFT_X), 0);
    }

    public double getLiftManualVerticalJoystick(){
        //gets value from y axis on (right)LiftJoystick on operatorJoystick.
        return computeDeadband(operatorJoystick.getRawAxis(XBoxConstants.AXIS_RIGHT_Y), 0.2);
    }

    public double getLiftManualHorizontalJoystick(){
        //gets value from  axis on (right)LiftJoystick on operatorJoystick.
        return computeDeadband(operatorJoystick.getRawAxis(XBoxConstants.AXIS_RIGHT_X), 0.2);
    }

}