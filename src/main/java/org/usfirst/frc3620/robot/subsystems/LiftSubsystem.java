package org.usfirst.frc3620.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.misc.RobotMode;
import org.usfirst.frc3620.robot.Robot;
import org.usfirst.frc3620.robot.RobotMap;
import org.usfirst.frc3620.robot.subsystems.PivotSubsystem.PivotMode;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class LiftSubsystem extends Subsystem implements PIDSource, PIDOutput {
    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);

    public enum LiftHeight{CARGOSHIP, ROCKET1, ROCKET2, ROCKET3}
    public enum LiftDecider{CARGO, HATCH}

    public static final double SETPOINT_BOTTOM = 0;
    public static final double SETPOINT_CARGO_TRASHIN = 3.25;
    public static final double SETPOINT_CARGO_CARGOSHIP = 16;
    public static final double SETPOINT_CARGO_ROCKET_MIDDLE = 27.5;
    public static final double SETPOINT_CARGO_ROCKET_TOP = 48;

    public static final double SETPOINT_HATCH_BOTTOM = 0;
    public static final double SETPOINT_HATCH_CARGOSHIP = 0;
    public static final double SETPOINT_HATCH_MIDDLE = 0;
    public static final double SETPOINT_HATCH_TOP = 0;

    private final CANSparkMax liftMax = RobotMap.liftSubsystemMax;
    private final DigitalInput topLimit = RobotMap.liftLimitSwitchTop;
    private final DigitalInput bottomLimit = RobotMap.liftLimitSwitchBottom;
    private final CANEncoder liftEncoder = RobotMap.liftEncoder;
    private final PIDController liftPIDContoller;

    private boolean encoderisvalid = false;
    private double desiredHeight = 0;
    private boolean autoMagicMode = true;
    private double PIDpower = 0;

    public boolean doingPID;

    public LiftSubsystem(){
        // this code gets run when the LiftSubsystem is created 
        // (when the robot is rebooted.)
        resetEncoder();
        liftPIDContoller = new PIDController(0, 0, 0, 0, this, this);
        setPIDSourceType(PIDSourceType.kDisplacement);
        liftPIDContoller.setInputRange(0, 55);
        liftPIDContoller.setOutputRange(-0.5, 1.0);
    }


    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(...);
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop
        SmartDashboard.putBoolean("liftTopLimitSwitch", isTopLimitDepressed());
        SmartDashboard.putBoolean("liftBottomLimitSwitch", isBottomLimitDepressed());
        
        if(checkForLiftEncoder()) {
            //SmartDashboard.putNumber("LiftEncoderPosition", liftEncoder.getPosition());
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
                setManualMode();
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
        SmartDashboard.putNumber("liftMotorPower", liftMax.getAppliedOutput());
        SmartDashboard.putString("liftMode", autoMagicMode ? "AUTOMAGIC" : "MANUAL");
        SmartDashboard.putNumber("liftSetpoint", desiredHeight);
        SmartDashboard.putBoolean("liftEncoderValid", encoderisvalid);
    }

    public void setManualMode() {
        if (autoMagicMode){
            logger.info("Switching to Manual Mode");
        }
        autoMagicMode = false;
        doingPID = false;
        liftPIDContoller.disable();
    }
    
    private void periodicAutoMagicMode(){
        double currentheight = getLiftHeight();
        double error = currentheight - desiredHeight;
        
        
        if(doingPID){
         
         liftMove(PIDpower);
          
        } else if(Math.abs(error) > 1){
            if(error > 0){
                liftMove(-0.3);
            }

            if(error < 0){
                liftMove(+0.3);
                
            }
        } 
        else {
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
        double speed = -yPos * 0.9;
        if (Robot.pivotSubsystem.getCurrentPivotMode() != PivotMode.HAB) {
            if(speed < -0.4){
                speed = -0.4;
            }
        }
        else{
            if(speed < -0.8){
                speed = -0.8;
            } 
        }

        if(encoderisvalid){
            double currentHeight = getLiftHeight();
            // we don't want the lift to blow past the 
            // limitswitch/hard stop and want power to be
            // low enough we don't go past it. 
            if(currentHeight > 35 && speed > 0.2) {
                speed = 0.2;
            } 
            
            if (Robot.pivotSubsystem.getCurrentPivotMode() != PivotMode.HAB) {

                if(currentHeight < 6 && speed < -0.1) {
                    speed = -0.1;
                }
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

    public double calculateLiftHeight(LiftHeight liftHeight, LiftDecider liftDecider){
        if (liftDecider == LiftDecider.CARGO){
            switch (liftHeight) {
                case CARGOSHIP:
                    return SETPOINT_CARGO_CARGOSHIP;
                case ROCKET1:
                    // TODO is this correct?
                    return SETPOINT_CARGO_TRASHIN;
                case ROCKET2:
                    return SETPOINT_CARGO_ROCKET_MIDDLE;
                case ROCKET3:
                    return SETPOINT_CARGO_ROCKET_TOP;
            }
        } else {
            switch (liftHeight) {
                case CARGOSHIP:
                    return SETPOINT_HATCH_CARGOSHIP;
                case ROCKET1:
                    // TODO is this correct?
                    return SETPOINT_HATCH_BOTTOM;
                case ROCKET2:
                    return SETPOINT_HATCH_MIDDLE;
                case ROCKET3:
                    return SETPOINT_HATCH_TOP;
            }
        }
        logger.warn ("we got hit with a combination of {} and {} that we can't handle", liftHeight, liftDecider);
        return SETPOINT_BOTTOM;
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
        logger.info("setting desired hieght to {}", h);
        desiredHeight = h;
        if (!autoMagicMode){
            logger.info("going to Automagic mode");
        }
        autoMagicMode = true;
        if(doingPID){
            liftPIDContoller.setSetpoint(desiredHeight);
            if (!liftPIDContoller.isEnabled()) {
                 // set the P, I, D, FF
                 //Base P: 0.04
                double p = SmartDashboard.getNumber("pivotP", 0.055);
                double i = SmartDashboard.getNumber("pivotI", 0.0001);
                double d = SmartDashboard.getNumber("pivotD", 0.12);
                double f = SmartDashboard.getNumber("pivotF", 0);
    
                logger.info("_pivotP={}", p);
                logger.info("_pivotI={}", i);
                logger.info("_pivotD={}", d);
                logger.info("_pivotF={}", f);
    
                liftPIDContoller.setP(p);
                liftPIDContoller.setI(i);
                liftPIDContoller.setD(d);
                liftPIDContoller.setF(f);

                liftPIDContoller.reset();
                liftPIDContoller.enable();
            }
        }
        
    }

    public void setDoingPID(boolean toPIDOrNotToPID){
        doingPID = toPIDOrNotToPID;
    }

    public double getMaxPower() {
        if (liftMax != null) {
            return (liftMax.get());
        } else {
            return Double.NaN;
        }
    }

    public double getMaxCurrent() {
        if (liftMax != null) {
            return(liftMax.getOutputCurrent());
        } else {
            return Double.NaN;
        }
    }

    public double getMaxBusVoltage() {
        if (liftMax != null) {
            return(liftMax.getBusVoltage());
        } else {
            return Double.NaN;
        }
    }

    public void lockLiftPins() {
        RobotMap.liftLockPinSolenoid.set(true);
    }

    public void unlockLiftPins() {
        RobotMap.liftLockPinSolenoid.set(false);
    }

    @Override
    public void pidWrite(double output) {
        PIDpower = output;
    }

    PIDSourceType pidSourceType;

    @Override
    public void setPIDSourceType(PIDSourceType pidSource) {
        pidSourceType = pidSource;
    }

    @Override
    public PIDSourceType getPIDSourceType() {
        return pidSourceType;
    }

    @Override
    public double pidGet() {
        double pos = getLiftHeight();
        return pos;
	}
}