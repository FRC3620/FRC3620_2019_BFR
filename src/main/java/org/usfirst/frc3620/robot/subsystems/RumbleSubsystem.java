package org.usfirst.frc3620.robot.subsystems;



import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.robot.Robot;
import org.usfirst.frc3620.misc.Hand;
import org.usfirst.frc3620.robot.commands.RumbleCommand;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;


/**
 * @author Nick Zimanski (SlippStream)
 * @version 2/02/19
 * 
 * READ ME:
 * 
 * To make the controller run from another file,
 * invoke it by instantiating one of the following:
 * 
 * new RumbleCommand([SUBSYSTEM], [HAND], [INTENSITY], [DURATION]) (preferred)
 * new RumbleCommand([SUBSYSTEM], [INTENSITY], [DURATION]) (preferred)
 * new RumbleCommand([SUBSYSTEM])
 * new RumbleCommand([SUBSYSTEM], [HAND], [INTENSITY]) (implies continuous)
 * 
 * where
 * SUBSYSTEM is Robot.rumbleSubsystemDriver OR Robot.rumbleSubsystemOperator (decides which controller to rumble)
 * HAND is Hand.RIGHT OR Hand.LEFT OR Hand.BOTH (decides which side of the controller to rumble (default is BOTH))
 * INTENSITY is a float between 0.1f and 1f (decides how hard to rumble the controller -- greater is harder (default is 1f))
 * DURATION is a positive float in seconds (decides how long to rumble the controller (default is 3f))
 * 
 * TO GET THE COMMAND TO RUN WHILE [CONDITION]:
 * 
 * use new RumbleCommand([SUBSYSTEM], [HAND], [INTENSITY])
 * to start the command. When your condition terminates,
 * instantiate the command again with 0f for the intesity
 * 
 * Note: This will get overwritten if another rumble starts on the same controller
 */
public class RumbleSubsystem extends Subsystem {
    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
    Timer timeOutDriverController = new Timer(); 
    Timer timeOutOperatorController = new Timer(); 

    private Joystick controller;

    //sets the controller for this instance of the subsystem
    public void setController(Joystick newController) {controller = newController;} 

    //Called from RumbleSubsystem. Directly sets controller rumble
    public void setRumble (Hand hand, Float intensity) {
        //Switch/case for rumbling different sides of the controller
        switch (hand) {
            case LEFT:
                controller.setRumble(RumbleType.kLeftRumble, intensity);
                break;
            case RIGHT:
                controller.setRumble(RumbleType.kRightRumble, intensity);
                break;
            default:
                controller.setRumble(RumbleType.kRightRumble, intensity);
                controller.setRumble(RumbleType.kLeftRumble, intensity);
        }
    }

    //Called from RumbleSubsystem. Clears the rumble of part or all of the controller.
    public void clearRumble () {
        //clears the rumble
            controller.setRumble(RumbleType.kRightRumble, 0);
            controller.setRumble(RumbleType.kLeftRumble, 0);
    }



    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(...);
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

}