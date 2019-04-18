package org.usfirst.frc3620.robot.subsystems;

import com.revrobotics.CANSparkMax;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.robot.RobotMap;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class HatchSubsystem extends Subsystem {
    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
    
    private Solenoid finger = RobotMap.hatchSubsystemFinger;
    private Solenoid pusher = RobotMap.hatchSubsystemPusher;
    private CANSparkMax grabber = RobotMap.hatchSubsystemMax;
    private Servo pivot = RobotMap.hatchSubsystemServo;

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

    public void hatchOut() {
        if (pusher != null) {
            pusher.set(true);
        } else {
            logger.info ("Can't push hatch pusher out, it's not there!");
        }
    }

    public void hatchIn() {
        if (pusher != null) {
            pusher.set(false);
        } else {
            logger.info ("Can't pull hatch pusher in, it's not there!");
        }
    }

    public void fingerOut() {
        if (finger != null) {
            finger.set(true);
        } else {
            logger.info ("Can't push hatch pusher out, it's not there!");
        }
    }

    public void fingerIn() {
        if (finger != null) {
            finger.set(false);
        } else {
            logger.info ("Can't pull hatch pusher in, it's not there!");
        }
    }
    
    public void grab(double speed) {
        grabber.set(speed);
    }

    public void release(double speed){
        grabber.set(-speed);
    }
}