package org.usfirst.frc3620.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.robot.RobotMap;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc3620.misc.LineSensor;
import org.usfirst.frc3620.robot.commands.LineDetectionCommand;
/**
 *
 */
public class LineSubsystem extends Subsystem {

    private final DigitalInput lineSensorL = RobotMap.lineSensorL;
    private final DigitalInput lineSensorR = RobotMap.lineSensorR;

    private final Counter lineSensorCounterL = RobotMap.lineSensorCounterL;
    private final Counter lineSensorCounterR = RobotMap.lineSensorCounterR;

    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop
       
        //SmartDashboard.putBoolean("Direct Line Sensor Left input", readLineSensorLDirectly());
        //SmartDashboard.putBoolean("Direct Line Sensor Right input", readLineSensorRDirectly());
       // if(readLineSensorLDirectly()){
      //      resetLineSensorL();
      //  }
        /*if(readLineSensorRDirectly()){
            resetLineSensorR();
        }*/
    }
    
    //Line sensor code here

    public boolean readLineDetectionCounter(LineSensor pos){
        if(pos == LineSensor.LEFT_SENSOR)
        { 
            if(lineSensorCounterL.get() != 0){
                return true;
            }
            return false;
        }
        else
        { 
            if(lineSensorCounterR.get() != 0){
                return true;
            }
            return false;
        }
    }

    public boolean readliveSensorInput(LineSensor pos){
        if(pos == LineSensor.LEFT_SENSOR)
        {
            return lineSensorL.get();
        }
        else
        {
            return lineSensorR.get();
        }
       
    }

    public void resetLineDetectionCounter(LineSensor pos){
        
        if(pos == LineSensor.LEFT_SENSOR)
        {
            lineSensorCounterL.reset();
        }
        else
        {
            lineSensorCounterR.reset();
        }
    }
}
