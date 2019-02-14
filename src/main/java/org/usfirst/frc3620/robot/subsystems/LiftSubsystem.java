package org.usfirst.frc3620.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.misc.RobotMode;
import org.usfirst.frc3620.robot.Robot;
import org.usfirst.frc3620.robot.RobotMap;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class LiftSubsystem extends Subsystem {
    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);

    public static final double SETPOINT_BOTTOM = 3;
    public static final double SETPOINT_TOP = 9;

    private final CANSparkMax liftMax = RobotMap.liftSubsystemMax;
    private final DigitalInput topLimit = RobotMap.liftLimitSwitchTop;
    private final DigitalInput bottomLimit = RobotMap.liftLimitSwitchBottom;
    private final CANEncoder liftEncoder = RobotMap.liftEncoder;

    private boolean encoderisvalid = false;
    private double desiredHeight = 0;

    public LiftSubsystem(){
        // this code gets run when the LiftSubsystem is created 
        // (when the robot is rebooted.)
        resetEncoder();
    }


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
        
        if(checkForLiftEncoder()) {
            SmartDashboard.putNumber("LiftEncoderPosition", liftEncoder.getPosition());
        }
        SmartDashboard.putNumber("liftEncoderInInches", getLiftHeight());

        if(Robot.getCurrentRobotMode() == RobotMode.TELEOP || Robot.getCurrentRobotMode() == RobotMode.AUTONOMOUS){
            if(isBottomLimitDepressed()){
                resetEncoder();
                encoderisvalid = true;
            }

            if(encoderisvalid){
                double currentheight = getLiftHeight();
                double error = currentheight - desiredHeight;
                if(Math.abs(error) > 1){
                    turnBrakeOff();
                    if(error > 0){
                        liftMove(-0.2);
                    }

                    if(error < 0){
                        liftMove(0.2);
                    }
                }else{
                    liftStop();
                    turnBrakeOn();
                }

            }else{
                liftMove(-0.2);
                turnBrakeOff();
            }

        }

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

    double ticstoinches(double tics) { 
        // turning the encoder readings from tics to feet
        double inches = tics * 0.321;
        return inches;
    }

    public double getLiftHeight() {
        if(checkForLiftEncoder()) {
            double tics = liftEncoder.getPosition();
            double howfarwehavemoved = tics - liftEncoderZeroValue;
            double feet = ticstoinches(howfarwehavemoved);
            return feet;
        } else {
            return(0);
        }
    }

    private double liftEncoderZeroValue;
    
    public boolean checkForLiftEncoder() {
        return(!(liftEncoder == null));
    }

    public void resetEncoder(){
        if(checkForLiftEncoder()) {
            liftEncoderZeroValue = liftEncoder.getPosition();
        }
    }

    public void setDesiredHeight(double h) {
        desiredHeight = h;
    }

    private void turnBrakeOn(){
        
    }

    private void turnBrakeOff(){

    }
}