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
    private final DigitalInput lineSensorL = RobotMap.lineSensorL;
    private final DigitalInput lineSensorR = RobotMap.lineSensorR;
    private final Counter lineSensorCounterL = RobotMap.lineSensorCounterL;
    private final Counter lineSensorCounterR = RobotMap.lineSensorCounterR;
    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
    
    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new LineDetectionCommand());
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop
        SmartDashboard.putNumber("Counter Left Sensor", lineSensorCounterL.get());
        SmartDashboard.putNumber("Counter Right Sensor", lineSensorCounterR.get());
        SmartDashboard.putBoolean("Did we see a line for Left sensor?", readLineSensorL());
        SmartDashboard.putBoolean("Did we see a line for Right sensor?", readLineSensorR());
        //SmartDashboard.putBoolean("Direct Line Sensor Left input", readLineSensorLDirectly());
        //SmartDashboard.putBoolean("Direct Line Sensor Right input", readLineSensorRDirectly());
        if(readLineSensorLDirectly()){
            resetLineSensorL();
        }
        /*if(readLineSensorRDirectly()){
            resetLineSensorR();
        }*/
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

    public boolean readLineSensorL(){
        if(lineSensorCounterL.get() != 0){
            return true;
        }
        return false;
    }
    public boolean readLineSensorR(){
        if(lineSensorCounterR.get() != 0){
            return true;
        }
        return false;
    }
    public void resetLineSensorL(){
        lineSensorCounterL.reset();
    }
    public void resetLineSensorR(){
        lineSensorCounterR.reset();
    }

    public boolean readLineSensorLDirectly(){ //if we see a line, this outputs FALSE
        return lineSensorL.get();
    }
    public boolean readLineSensorRDirectly(){ //if we see a line, this outputs FALSE
        return lineSensorR.get();
    }
}
