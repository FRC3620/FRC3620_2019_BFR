package org.usfirst.frc3620.robot.subsystems;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.robot.RobotMap;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class IntakeSubsystem extends Subsystem {

    private final Victor intakeRollerTop = RobotMap.intakeSubsystemUpperMotor;
    private final Victor intakeRollerBottom = RobotMap.intakeSubsystemLowerMotor;
    private final Victor intakeRollerMiddle = RobotMap.intakeSubsystemMiddleMotor;
    private final Ultrasonic ultraSonicZoom = new Ultrasonic(0,1);
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
    public void intakeIn(){
        intakeRollerTop.set(-1);
        intakeRollerBottom.set(1);
    }

    public void intakeOut(){
        intakeRollerTop.set(1);
        intakeRollerBottom.set(-1);
    } 

    public void intakeOff(){
        intakeRollerTop.set(0);
        intakeRollerBottom.set(0);
    }
        
    public double readEncoder(){
        ultraSonicZoom.getRangeInches();
    }
    
    public void TrashIn(){
        intakeRollerTop.set(-1);
        intakeRollerMiddle.set(1);
    }

    public void TrashOff(){
        intakeRollerTop.set(0);
        intakeRollerMiddle.set(0);
    }

}
