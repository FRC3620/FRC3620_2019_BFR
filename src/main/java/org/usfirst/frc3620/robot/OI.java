package org.usfirst.frc3620.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.usfirst.frc3620.robot.paths.TrainingPath;
import org.usfirst.frc3620.misc.XBoxConstants;
import org.usfirst.frc3620.misc.Hand;
import org.usfirst.frc3620.robot.commands.*;
import org.usfirst.frc3620.robot.subsystems.LiftSubsystem;

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

    public OI() {
        //to interface with joysticks, no special initiallization nessessary
        driverJoystick = new Joystick(0);
        operatorJoystick = new Joystick(1);

        Robot.rumbleSubsystemDriver.setController(driverJoystick);
        Robot.rumbleSubsystemOperator.setController(operatorJoystick);

        // map buttons to Joystick buttons here

            //Declare buttons
            Button inTakeIn = new JoystickButton(driverJoystick, XBoxConstants.BUTTON_RIGHT_BUMPER);
            Button inTakeOut = new JoystickButton(driverJoystick, XBoxConstants.BUTTON_LEFT_BUMPER);
            Button trashIn = new JoystickButton(driverJoystick, XBoxConstants.BUTTON_LEFT_STICK);
            Button conveyorL = new JoystickButton(driverJoystick, XBoxConstants.BUTTON_X);
            Button conveyorR = new JoystickButton(driverJoystick, XBoxConstants.BUTTON_Y);
            Button positionOne = new JoystickButton(driverJoystick, XBoxConstants.BUTTON_A);
            Button positionTwo = new JoystickButton(driverJoystick, XBoxConstants.BUTTON_B);

            //buttons run commands
            inTakeIn.toggleWhenPressed(new IntakeCommand());
            inTakeOut.toggleWhenPressed(new OutTakeCommand());
            trashIn.toggleWhenPressed(new TrashInCommand());
            conveyorL.whileHeld(new TrashLeftCommand());
            conveyorR.whileHeld(new TrashRightCommand());
<<<<<<< HEAD
            positionOne.whenPressed(new SetLiftHeightCommand(LiftSubsystem.SETPOINT_BOTTOM));
            positionTwo.whenPressed(new SetLiftHeightCommand(LiftSubsystem.SETPOINT_TOP));
=======

            SmartDashboard.putData("TrainingPath", new TrainingPath());
>>>>>>> 32b017556b4929dde7aaf26d09ee0c30468c9aab
        }

    public Joystick getDriverJoystick() {
        return driverJoystick;
    }
    public Joystick getOperatorJoystick() {
        return operatorJoystick;
    }
    
    static {
        SmartDashboard.putData("Rumble both", new RumbleCommand(Robot.rumbleSubsystemDriver, Hand.BOTH, 0.2f, 60f));
        SmartDashboard.putData("Rumble left", new RumbleCommand(Robot.rumbleSubsystemDriver, Hand.LEFT, 0.2f, 3.0f));
    }

    public double computeDeadband (double position, double deadband) {
        if (Math.abs(position) < deadband) {
            return 0;
        }

        //values eventually passed to arcadeDrive, which squares the values itself
    	return position;
    }

    public double getLeftHorizontalJoystickSquared() {
        //gets value from x or y axis on joysticks on gamepad. In this istance, Left X
    	return computeDeadband(driverJoystick.getRawAxis(XBoxConstants.AXIS_LEFT_X), 0.2);
    }

    public double getRightVerticalJoystickSquared() {
        //gets value from x or y axis on joysticks on gamepad. In this istance, Right Y
        return computeDeadband(driverJoystick.getRawAxis(XBoxConstants.AXIS_RIGHT_Y), 0.2);
    }
    
    
}