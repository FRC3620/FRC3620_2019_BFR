package org.usfirst.frc3620.robot.subsystems;

import org.usfirst.frc3620.robot.RobotMap;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class TrashSubsystem extends Subsystem {

    Preferences speedPreferences = Preferences.getInstance();
    private final SpeedController conveyorT = RobotMap.conveyorBeltMotorTop;
    private final SpeedController conveyorB = RobotMap.conveyorBeltMotorBottom;

    public void initDefaultCommand() {
        // Set the default command for a subsystem here. Drive command runs in background at all times
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop
    }

    public void conveyorBeltLeft(double speed){
        conveyorT.set(-speed);
        conveyorB.set(-speed);
    }

    public void conveyorBeltRight(double speed){
        conveyorT.set(speed);
        conveyorB.set(speed);
    }

    public void conveyorBeltOff(){
        conveyorT.set(0);
        conveyorB.set(0);
    }
}