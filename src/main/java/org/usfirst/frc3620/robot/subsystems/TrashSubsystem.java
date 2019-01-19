package org.usfirst.frc3620.robot.subsystems;


import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import org.usfirst.frc3620.robot.RobotMap;
import org.usfirst.frc3620.robot.commands.DriveCommand;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class TrashSubsystem extends Subsystem {

    Preferences speedPreferences = Preferences.getInstance();
    private final WPI_TalonSRX conveyorMotor1 = RobotMap.conveyorBeltMotor1;
    private final WPI_TalonSRX conveyorMotor2 = RobotMap.conveyorBeltMotor2;
    private final WPI_VictorSPX conveyorFollower1 = RobotMap.conveyorBeltFollower1;
    private final WPI_VictorSPX conveyorFollower2 = RobotMap.conveyorBeltFollower2;

    public void initDefaultCommand() {
        // Set the default command for a subsystem here. Drive command runs in background at all times
        setDefaultCommand(new DriveCommand());
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop

    }

    public void conveyorBeltLeft(double speed){
        conveyorMotor1.set(speed);
        conveyorFollower1.set(speed);
    }

    public void conveyorBeltRight(double speed){
        conveyorMotor2.set(speed);
        conveyorFollower2.set(speed);
    }

    public void conveyorBeltOff(){
        conveyorMotor1.set(0);
        conveyorMotor2.set(0);
        conveyorFollower1.set(0);
        conveyorFollower2.set(0);
    }

    public double getSpeedFromDashboard(){
        return speedPreferences.getDouble("IntakeSpeed", 0.5);
    }
}