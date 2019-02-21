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

    public static final double SETPOINT_BOTTOM = 0;
    public static final double SETPOINT_TRASHIN = 3.25;
    public static final double SETPOINT_CARGOSHIP = 16;
    public static final double SETPOINT_ROCKET_MIDDLE = 30.75;
    public static final double SETPOINT_ROCKET_TOP = 59;

    public static final double SETPOINT_HATCH_BOTTOM = 0;
    public static final double SETPOINT_HATCH_MIDDLE = 0;
    public static final double SETPOINT_HATCH_TOP = 0;

    private final CANSparkMax liftMax = RobotMap.liftSubsystemMax;
    private final DigitalInput topLimit = RobotMap.liftLimitSwitchTop;
    private final DigitalInput bottomLimit = RobotMap.liftLimitSwitchBottom;
    private final CANEncoder liftEncoder = RobotMap.liftEncoder;

    private boolean encoderisvalid = false;
    private double desiredHeight = 0;
    private boolean autoMagicMode = true;

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
            if(isBottomLimitDepressed() && !encoderisvalid){
                resetEncoder();
                encoderisvalid = true;
            }
           
            double xPos = Robot.oi.getLiftManualHorizontalJoystick();
            double yPos = Robot.oi.getLiftManualVerticalJoystick();
            if (Math.abs(xPos) > 0.2 || Math.abs(yPos) > 0.2){
                if (autoMagicMode){
                    logger.info("Switching to Manual Mode");
                }
                autoMagicMode = false;
            }

            if(!autoMagicMode){
                periodicManualMode();
            }else{
                //Automagic
                if (encoderisvalid){
                    periodicAutoMagicMode();
                }else{
                    //we want to be down, but we're not there yet
                    //we need to do some LiftMove with a negative
                    liftMove(-0.2);
                }
            }
        }
    }
    
    private void periodicAutoMagicMode(){
        double currentheight = getLiftHeight();
        double error = currentheight - desiredHeight;
        if(Math.abs(error) > 1){
            if(error > 0){
                liftMove(-0.3);
            }

            if(error < 0){
                liftMove(+0.3);
            }
        }else{
            liftStop();
        }
    }

    private void periodicManualMode() {
        double yPos = Robot.oi.getLiftManualVerticalJoystick();
        // joystick position is negative if we move up.
        // if we are moving up, that means we want to 
        // Move lift upward.
        // liftMove needs a positive number to move up.
        // so we need to change the sign. 
        double speed = -yPos * 0.5;
        if(speed < -0.3){
            speed = -0.3;
        }

        if(encoderisvalid){
            double currentHeight = getLiftHeight();
            // we don't want the lift to blow past the 
            // limitswitch/hard stop and want power to be
            // low enough we don't go past it. 
            if(currentHeight > 50 && speed > 0.2) {
                speed = 0.2;
            } 

            if(currentHeight < 6 && speed < -0.2) {
                speed = -0.2;
            }
        }

        liftMove(speed);
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

        liftMax.set(-speed);
    }

    public void liftStop(){
        //liftBrake.set(true);
        liftMax.set(0);
    }

    double ticstoinches(double tics) { 
        // turning the encoder readings from tics to inches
        double inches = tics * 0.508696934; //(9.75inches/19.16661837167tics)
        return inches;
    }

    public double getLiftHeight() {
        if(checkForLiftEncoder()) {
            double tics = liftEncoder.getPosition();
            double howfarwehavemoved = tics - liftEncoderZeroValue;
            double inches = ticstoinches(howfarwehavemoved);
            return -inches;
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
        logger.info("setting desired hieght");
        desiredHeight = h;
        if (!autoMagicMode){
            logger.info("going to Automagic mode");
        }
        autoMagicMode = true;
    }
}