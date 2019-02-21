package org.usfirst.frc3620.robot.subsystems;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.robot.RobotMap;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc3620.misc.LineSensor;
/**
 *
 */
public class LineSubsystem extends Subsystem {

    private final DigitalInput lineSensorL = RobotMap.lineSensorL;
    private final DigitalInput lineSensorR = RobotMap.lineSensorR;

    private final Counter lineSensorCounterL = RobotMap.lineSensorCounterL;
    private final Counter lineSensorCounterR = RobotMap.lineSensorCounterR;

    boolean lineSensorLDetectionStatus;
    boolean lineSensorRDetectionStatus;

    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        lineSensorLDetectionStatus  = false; //not detected line;
        lineSensorRDetectionStatus  = false; //not detected line;
    }

    @Override
    public void periodic() {
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
    public void setLineDetectionStatus(LineSensor pos, boolean status)
    {
        if(pos == LineSensor.LEFT_SENSOR)
        {
            lineSensorLDetectionStatus = status;
        }
        else
        {
            lineSensorRDetectionStatus = status;
        }
    }
    public boolean getLineDetectionStatus(LineSensor pos)
    {
        if(pos == LineSensor.LEFT_SENSOR)
        {
            return lineSensorLDetectionStatus;
        }
        else
        {
            return lineSensorRDetectionStatus;
        }
    }
}
