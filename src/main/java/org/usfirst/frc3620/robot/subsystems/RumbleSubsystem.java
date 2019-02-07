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
 * @version 2/06/19
 * 
 * 
 */
public class RumbleSubsystem extends Subsystem {
    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
    Timer timeOutController = new Timer(); 

    private Joystick controller;

    //sets the controller for this instance of the subsystem
    public void setController(Joystick newController) {controller = newController;} 

    //Called from RumbleSubsystem. Directly sets controller rumble
    public void setRumble (Hand hand, Double intensity) {
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

    //Called from RumbleCommand. Clears the rumble of part or all of the controller.
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