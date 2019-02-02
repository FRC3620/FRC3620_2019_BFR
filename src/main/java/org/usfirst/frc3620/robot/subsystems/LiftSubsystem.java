package org.usfirst.frc3620.robot.subsystems;



import com.revrobotics.CANSparkMax;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.robot.Robot;
import org.usfirst.frc3620.robot.RobotMap;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;


/**
 *
 */
public class LiftSubsystem extends Subsystem {
    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);

    private final CANSparkMax liftMax = RobotMap.liftSubsystemMax;
    private final Solenoid liftBrake = RobotMap.liftSubsystemBrake;
    private final DigitalInput topLimit = RobotMap.liftLimitSwitchTop;
    private final DigitalInput bottomLimit = RobotMap.liftLimitSwitchBottom;

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(...);
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop
    }

    public boolean isBottomLimitDepressed(){
        if(bottomLimit != null){
            liftMax.set(0);    
        }
        return false;
    }
    
    public boolean isTopLimitDepressed(){
        if(topLimit != null){
            liftMax.set(0);    
        }
        return false;
    }

    public void liftUp(double speed){
        liftMax.set(speed);
   }

    public void liftDown(double speed){
        liftMax.set(-speed);
   }

    public void liftStop(){
        liftBrake.set(true);
        liftMax.set(0);
   }

}