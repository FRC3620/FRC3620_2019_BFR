package org.usfirst.frc3620.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc3620.misc.Hand;
import org.usfirst.frc3620.misc.DPad;
import org.usfirst.frc3620.misc.XBoxConstants;
import org.usfirst.frc3620.robot.commands.*;
import org.usfirst.frc3620.robot.subsystems.LiftSubsystem;
import org.usfirst.frc3620.robot.subsystems.PivotSubsystem;
import org.usfirst.frc3620.robot.subsystems.TrashSubsystem;
import org.usfirst.frc3620.robot.subsystems.VisionSubsystem;
import org.usfirst.frc3620.robot.subsystems.LiftSubsystem.LiftDecider;

import org.usfirst.frc3620.robot.paths.*;

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
    private Joystick magicBoardJoystick;

    @SuppressWarnings("resource")
	public OI() {
        //to interface with joysticks, no special initiallization nessessary
        driverJoystick = new Joystick(0);
        operatorJoystick = new Joystick(1);
        magicBoardJoystick = new Joystick(2);

        DPad operatorDPad = new DPad(operatorJoystick, 0);

        Robot.rumbleSubsystemDriver.setController(driverJoystick);
        Robot.rumbleSubsystemOperator.setController(operatorJoystick);
        // map buttons to Joystick buttons here
        
        //Declare buttons
            
            //driver controls
            Button reverseDrive = new JoystickButton(driverJoystick, XBoxConstants.BUTTON_RIGHT_BUMPER);
            Button conveyorL = new JoystickButton(driverJoystick, XBoxConstants.BUTTON_X);
            Button conveyorR = new JoystickButton(driverJoystick, XBoxConstants.BUTTON_B);
            Button inTakeIn = new JoystickButton(driverJoystick, XBoxConstants.BUTTON_LEFT_BUMPER);
            Button driveIn = new JoystickButton(driverJoystick, XBoxConstants.BUTTON_A);
            Button cargoShipLineUp = new JoystickButton(driverJoystick, XBoxConstants.BUTTON_Y);

            //operator controls 
            Button hatchExtend = new JoystickButton(operatorJoystick, XBoxConstants.BUTTON_X);
            Button hatchCollect = new JoystickButton(operatorJoystick, XBoxConstants.BUTTON_B);
            Button inTakeOut = new JoystickButton(operatorJoystick, XBoxConstants.BUTTON_START);
            Button trashIn = new JoystickButton(operatorJoystick, XBoxConstants.BUTTON_Y);
            Button habClimbButton = new JoystickButton(operatorJoystick, XBoxConstants.BUTTON_LEFT_BUMPER);
            Button middlePos = new JoystickButton(operatorJoystick, XBoxConstants.BUTTON_RIGHT_BUMPER);
            Button topPos = new JoystickButton(operatorJoystick, XBoxConstants.BUTTON_LEFT_BUMPER);
            Button liftHome = new JoystickButton(operatorJoystick, XBoxConstants.BUTTON_A);
            Button lockLiftPinsButton = new JoystickButton(operatorJoystick, XBoxConstants.BUTTON_BACK);

            operatorDPad.down().whenPressed(new SetPivotAngleCommand(PivotSubsystem.DesiredAngle.Bottom));
            operatorDPad.up().whenPressed(new SetPivotAngleCommand(PivotSubsystem.DesiredAngle.Top));
            operatorDPad.right().whenPressed(new SetPivotAngleCommand(PivotSubsystem.DesiredAngle.Middle));
            operatorDPad.left().whenPressed(new SetPivotAngleCommand(PivotSubsystem.DesiredAngle.Middle));

            //buttons run commands
            inTakeIn.toggleWhenPressed(new IntakeCommand());
            inTakeOut.toggleWhenPressed(new OutTakeCommand());
            trashIn.toggleWhenPressed(new TrashInCommand());
            conveyorL.whileHeld(new TrashLeftCommand());
            conveyorR.whileHeld(new TrashRightCommand());
            hatchExtend.toggleWhenPressed(new HatchExtendCommand());
            hatchCollect.toggleWhenPressed(new HatchCollectCommand());
            habClimbButton.whenPressed(new HabClimbCommand());
            liftHome.whenPressed(new SetLiftHeightCommand(LiftSubsystem.SETPOINT_CARGO_TRASHIN, true));
            middlePos.whenPressed(new SetLiftHeightCommand(LiftSubsystem.SETPOINT_CARGO_ROCKET_MIDDLE, true));
            topPos.whenPressed(new SetLiftHeightCommand(LiftSubsystem.SETPOINT_CARGO_ROCKET_TOP, true));
            reverseDrive.whenPressed(new ToggleReverseCommand());
            driveIn.whileHeld(new AutoMoveForwardCommand(10,.7));
            cargoShipLineUp.whileHeld(new AutoLineUpWithCargoshipCommand());
            lockLiftPinsButton.toggleWhenPressed(new LockLiftPinsCommand());

            SmartDashboard.putData(new HabInstrumentationCommand());

             //Magic Board Controls
             Button liftRocket1 = new JoystickButton(magicBoardJoystick,9);
             Button liftRocket2 = new JoystickButton(magicBoardJoystick,10);
             Button liftRocket3 = new JoystickButton(magicBoardJoystick,11);
             Button cargoShip1 = new JoystickButton(magicBoardJoystick, 7);
             Button pivotLevel1 = new JoystickButton(magicBoardJoystick, 6);
             Button pivotLevel2 = new JoystickButton(magicBoardJoystick, 5);
             Button pivotLevel3 = new JoystickButton(magicBoardJoystick, 4);
             Button trashLeft = new JoystickButton(magicBoardJoystick, 2);
             Button trashRight = new JoystickButton(magicBoardJoystick, 3);
             Button camSwitch = new JoystickButton(magicBoardJoystick, 8);


             liftRocket1.whenPressed(new LiftMagicCommand(LiftSubsystem.LiftHeight.ROCKET1));
             liftRocket2.whenPressed(new LiftMagicCommand(LiftSubsystem.LiftHeight.ROCKET2));
             liftRocket3.whenPressed(new LiftMagicCommand(LiftSubsystem.LiftHeight.ROCKET3));
             cargoShip1.whenPressed(new LiftMagicCommand(LiftSubsystem.LiftHeight.CARGOSHIP));
             pivotLevel1.whenPressed(new SetPivotAngleCommand(PivotSubsystem.DesiredAngle.Bottom));
             pivotLevel2.whenPressed(new SetPivotAngleCommand(PivotSubsystem.DesiredAngle.Middle));
             pivotLevel3.whenPressed(new SetPivotAngleCommand(PivotSubsystem.DesiredAngle.Top));
             trashRight.whileHeld(new TrashRightCommand());
             trashLeft.whileHeld(new TrashLeftCommand());
             camSwitch.whenPressed(new SwitchCameraCommand());            

            SmartDashboard.putData("Rumble both", new RumbleCommand(Robot.rumbleSubsystemDriver, Hand.BOTH, 0.2, 60.0));
            SmartDashboard.putData("Rumble left", new RumbleCommand(Robot.rumbleSubsystemDriver, Hand.LEFT, 0.2, 3.0));

            SmartDashboard.putData("AutonomousAlign from 45", new AutoAlignmentTemplate(Robot.visionSubsystem.getFrontTargetDistance(), Robot.visionSubsystem.getFrontTargetAngle()));
            SmartDashboard.putData("AlignToPointD", new AlignToPointD());
            SmartDashboard.putData("TrainingPath", new TrainingPath());
            SmartDashboard.putData("CenterOnTarget", new VisionAlignmentCommand());
            SmartDashboard.putData("TapTarget", new TravelAlignPushCommand());
            SmartDashboard.putData("DriveForward", new AutoMoveForwardCommand(15,.7));
            SmartDashboard.putData("Align to Hatch Target", new AutonomousAlignmentAndApproachCommand());
            SmartDashboard.putData("LineUpWithCargoship", new AutoLineUpWithCargoshipCommand());

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

    public LiftSubsystem.LiftDecider getLiftDecider(){
        if (magicBoardJoystick.getRawButton(12)){
            return LiftDecider.CARGO;
        }else{
            return LiftDecider.HATCH;
        }
    }
}