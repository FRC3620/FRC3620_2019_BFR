package org.usfirst.frc3620.robot.subsystems;



import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.robot.RobotMap;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;


/**
 *
 */
public class HatchSubsystem extends Subsystem {
    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
    
    private DoubleSolenoid finger = RobotMap.hatchSubsystemFinger;
    private DoubleSolenoid pusher = RobotMap.hatchSubsystemPusher;

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
        pusher.set(Value.kReverse);
    }

    public void hatchIn() {
        pusher.set(Value.kForward);
    }

    public void fingerOut() {
        finger.set(Value.kForward);
    }

    public void fingerIn() {
        finger.set(Value.kReverse);
    }

}