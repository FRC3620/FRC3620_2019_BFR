package org.usfirst.frc3620.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

import org.usfirst.frc3620.misc.XBoxConstants;
import org.usfirst.frc3620.misc.Hand;
import org.usfirst.frc3620.robot.commands.*;

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
    private JoystickButton button_a;

    public OI() {
        //to interface with joysticks, no special initiallization nessessary
        driverJoystick = new Joystick(0);
        operatorJoystick = new Joystick(1);
        button_a = new JoystickButton(driverJoystick, XBoxConstants.BUTTON_A);

        Robot.rumbleSubsystemDriver.setController(driverJoystick);
        Robot.rumbleSubsystemOperator.setController(operatorJoystick);

        // map buttons to Joystick buttons here
    }

    public Joystick getDriverJoystick() {
        return driverJoystick;
    }
    public Joystick getOperatorJoystick() {
        return operatorJoystick;
    }
    
    static {
        SmartDashboard.putData("Rumble both", new RumbleCommand(Robot.rumbleSubsystemDriver, Hand.BOTH, 1f, 60f));
        SmartDashboard.putData("Rumble left", new RumbleCommand(Robot.rumbleSubsystemDriver, Hand.LEFT, 0.5f, 3.0f));
    }

    public double computeSquareWithDeadband (double position, double deadband) {
        if (Math.abs(position) < deadband) {
            return 0;
        }

    	double rv = position * position;
    	if(position < 0) {
    		rv = -rv;
    	}
    	return rv;
    }

    public double getLeftHorizontalJoystickSquared() {
        //gets value from x or y axis on joysticks on gamepad. In this istance, Left X
    	return computeSquareWithDeadband(driverJoystick.getRawAxis(XBoxConstants.AXIS_LEFT_X), 0.2);
    }

    public double getRightVerticalJoystickSquared() {
        //gets value from x or y axis on joysticks on gamepad. In this istance, Right Y
        return computeSquareWithDeadband(driverJoystick.getRawAxis(XBoxConstants.AXIS_RIGHT_Y), 0.2);
    }

}