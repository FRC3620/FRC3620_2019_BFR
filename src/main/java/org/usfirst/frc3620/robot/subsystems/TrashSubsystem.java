package org.usfirst.frc3620.robot.subsystems;


import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import org.usfirst.frc3620.robot.RobotMap;
import org.usfirst.frc3620.robot.commands.DriveCommand;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class TrashSubsystem extends Subsystem {

    Preferences speedPreferences = Preferences.getInstance();
    private final WPI_TalonSRX conveyorL = RobotMap.conveyorBeltMotorL;
    private final WPI_TalonSRX conveyorR = RobotMap.conveyorBeltMotorR;
    private final WPI_TalonSRX conveyorC = RobotMap.conveyorBeltMotorC;

    public void initDefaultCommand() {
        // Set the default command for a subsystem here. Drive command runs in background at all times
      
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop

    }

    public void conveyorBeltLeft(double speed){
        conveyorL.set(speed);
        conveyorC.set(speed);
    }

    public void conveyorBeltRight(double speed){
        conveyorR.set(speed);
        conveyorC.set(-speed);
    }

    public void conveyorBeltOff(){
        conveyorL.set(0);
        conveyorR.set(0);
        conveyorC.set(0);
    }

    public double getSpeedFromDashboard(){
        return speedPreferences.getDouble("IntakeSpeed", 0.5);
    }
    
}