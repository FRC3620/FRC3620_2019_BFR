package org.usfirst.frc3620.robot.subsystems;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.robot.RobotMap;
import org.usfirst.frc3620.robot.commands.LineDetectionCommand;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.command.Subsystem;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
/**
 *
 */
public class IntakeSubsystem extends Subsystem {

    private final Victor intakeRollerTop = RobotMap.intakeSubsystemUpperMotor;
    private final Victor intakeRollerBottom = RobotMap.intakeSubsystemLowerMotor;
    private final Victor intakeRollerMiddle = RobotMap.intakeSubsystemMiddleMotor;
    private final DigitalInput lineSensor = RobotMap.lineSensor;
    private final Counter counter = RobotMap.counter;
    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
    
    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(...);
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop
        SmartDashboard.putNumber("Counter", counter.get());
        SmartDashboard.putBoolean("Did we see a line?", readLineSensor());
        SmartDashboard.putBoolean("Direct Line Sensor input", readLineSensorDirectly());
        if(readLineSensorDirectly()){
            resetLineSensor();
        }
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
    
    
    public void TrashIn(){
        intakeRollerTop.set(-1);
        intakeRollerMiddle.set(1);
    }

    public void TrashOff(){
        intakeRollerTop.set(0);
        intakeRollerMiddle.set(0);
    }
    //Line sensor code here

    public boolean readLineSensor(){
        if(counter.get() != 0){
            return true;
        }
        return false;
    }

    public void resetLineSensor(){
        counter.reset();
    }

    public boolean readLineSensorDirectly(){ //if we see a line, this outputs FALSE
        return lineSensor.get();
    }
}
