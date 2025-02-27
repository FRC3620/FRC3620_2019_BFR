package org.usfirst.frc3620.robot.subsystems;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.misc.Hand;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * @author Nick Zimanski (SlippStream)
 * @version 2/09/19
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
 * INTENSITY is a double between 0.1 and 1.0 (decides how hard to rumble the controller -- greater is harder (default is 1.0))
 * DURATION is a positive double in seconds (decides how long to rumble the controller (default is 1.0))
 * 
 * TO GET THE COMMAND TO RUN WHILE [CONDITION]:
 * 
 * use new RumbleCommand([SUBSYSTEM], [HAND], [INTENSITY])
 * to start the command. When your condition terminates,
 * instantiate the command again with 0.0 for the intesity
 * 
 * Note: This will get overwritten if another rumble starts on the same controller
 * 
 * P.S.: Hand.LEFT provides a choppy, visceral rumble where Hand.RIGHT provides a lighter, smooth rumble
 */
public class RumbleSubsystem extends Subsystem {
    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);

    private Joystick controller;
    private Boolean disabled = false;

    //sets the controller for this instance of the subsystem
    public void setController(Joystick controller) {this.controller = controller;} 

    public void setDisabled(Boolean disabled) {this.disabled = disabled;}

    //Called from RumbleSubsystem. Directly sets controller rumble
    public void setRumble (Hand hand, Double intensity) {
        if (!disabled) {
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
    }

    //Called from RumbleSubsystem. Clears the rumble of part or all of the controller.
    public void clearRumble () {
        if (!disabled) {
            //clears the rumble
            controller.setRumble(RumbleType.kRightRumble, 0);
            controller.setRumble(RumbleType.kLeftRumble, 0);
        }
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