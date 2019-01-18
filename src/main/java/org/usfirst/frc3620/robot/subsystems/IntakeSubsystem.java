package org.usfirst.frc3620.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.robot.RobotMap;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.*;

/**
 *
 */
public class IntakeSubsystem extends Subsystem {

    private final WPI_TalonSRX intakeRollerTop = RobotMap.intakeSubsystemUpperIntakeMotor;
    private final WPI_TalonSRX intakeRollerBottom = RobotMap.intakeSubsystemLowerIntakeMotor;
    private final Ultrasonic ultraSonicZoom = new Ultrasonic(0,1);
    Preferences speedPreferences = Preferences.getInstance();
    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
    
    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(...);
        ultraSonicZoom.setAutomaticMode(true);
    }



    @Override
    public void periodic() {
        // Put code here to be run every loop
    }
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    public void intakeIn(double speed){
        intakeRollerTop.set(speed);
        intakeRollerBottom.set(speed);
    }

    public void intakeOut(double speed){
        intakeRollerTop.set(-speed);
        intakeRollerBottom.set(-speed);
    } 

    public void intakeOff(){
        intakeRollerTop.set(0);
        intakeRollerBottom.set(0);
    }
        
    public double readEncoder(){
       return ultraSonicZoom.getRangeInches();
    }
    
    public double getSpeedFromDashboard(){
        return speedPreferences.getDouble("IntakeSpeed", 0.5);
    }
    
}
