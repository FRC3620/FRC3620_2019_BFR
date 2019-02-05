package org.usfirst.frc3620.robot.subsystems;



import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.robot.Robot;
import org.usfirst.frc3620.robot.RobotMap;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 *
 */
public class LiftSubsystem extends Subsystem {
    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);

    private final CANSparkMax liftMax = RobotMap.liftSubsystemMax;
    private final Solenoid liftBrake = RobotMap.liftSubsystemBrake;
    private final DigitalInput topLimit = RobotMap.liftLimitSwitchTop;
    private final DigitalInput bottomLimit = RobotMap.liftLimitSwitchBottom;
    private final CANEncoder liftEncoder = RobotMap.liftEncoder;

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(...);
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop
        SmartDashboard.putBoolean("Top limit switch", isTopLimitDepressed());
        SmartDashboard.putBoolean("Bottom limit switch", isBottomLimitDepressed());
        SmartDashboard.putNumber("Lift encoder position", liftEncoder.getPosition());
    }

    public boolean isBottomLimitDepressed(){
       if(bottomLimit.get() == true){
           return false; 
       }
       return true;
    }
    
    public boolean isTopLimitDepressed(){
        if(topLimit.get() == true){
            return false; 
        }
        return true;
    }

    /**
     * Move the lift up
     * @param speed positive is up
     */
    public void liftMove(double speed){
        if(isBottomLimitDepressed() == true && speed < 0){
            speed = 0;
        }

        if(isTopLimitDepressed() == true && speed > 0){
            speed = 0;
        }

        liftMax.set(speed);
    }

    public void liftStop(){
        //liftBrake.set(true);
        liftMax.set(0);
    }

}