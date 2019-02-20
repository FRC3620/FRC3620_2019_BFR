package org.usfirst.frc3620.robot.subsystems;



import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.robot.RobotMap;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;


/**
 *
 */
public class HatchSubsystem extends Subsystem {
    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
    
    private Solenoid finger = RobotMap.hatchSubsystemFinger;
    private Solenoid pusher = RobotMap.hatchSubsystemPusher;

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
        pusher.set(true);
    }

    public void hatchIn() {
        pusher.set(false);
    }

    public void fingerOut() {
        finger.set(true);
    }

    public void fingerIn() {
        finger.set(false);
    }

}