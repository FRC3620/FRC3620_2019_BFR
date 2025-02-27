package org.usfirst.frc3620.robot.subsystems;

import com.revrobotics.CANSparkMax;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.robot.RobotMap;
import org.usfirst.frc3620.robot.commands.HatchHoldingVoltageCommand;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class HatchSubsystem extends Subsystem {
    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
    
    private final DigitalInput hatchLimit = RobotMap.hatchLimitSwitch;
    private final DigitalInput hatchHoldLimit = RobotMap.hatchHoldLimitSwitch;
    private Solenoid hatchSolenoid = RobotMap.hatchSubsystemFlipper;
    private CANSparkMax grabber = RobotMap.hatchSubsystemMax;

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(...);
        setDefaultCommand(new HatchHoldingVoltageCommand());
    }

    public boolean isHatchHoldLimitThere(){
        if(hatchHoldLimit == null){
            return false;
        }
        return true;
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop
        SmartDashboard.putBoolean("HatchSolenoidState", getHatchSolenoidState());
        SmartDashboard.putBoolean("HatchThereLimit", isHatchLimitDepressed());
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public boolean isHatchLimitDepressed(){
        if(hatchLimit.get() == true){
            return false; 
        }
        return true;
    }

    public boolean isHatchHoldLimitDepressed(){
        if(hatchHoldLimit.get() == true){
            return false; 
        }
        return true;
    }

    public void hatchOut() { //moves hatch mech up
        if (hatchSolenoid != null) {
            hatchSolenoid.set(false);
        } else {
            logger.info ("Can't push hatch mech out, it's not there!");
        }
    }

    public void hatchIn() { //moves hatch mech down
        if (hatchSolenoid != null) {
            hatchSolenoid.set(true);
        } else {
            logger.info ("Can't pull hatch mech in, it's not there!");
        }
    }
    
    public boolean getHatchSolenoidState(){
        return hatchSolenoid.get();
    }

    public void grab(double speed) {
        grabber.set(speed);
    }

    public void release(double speed){
        grabber.set(-speed);
    }
}