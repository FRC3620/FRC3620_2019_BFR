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
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class PivotSubsystem extends Subsystem implements PIDSource, PIDOutput {
    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);

    public enum PivotMode {
        MANUAL, AUTOMAGIC, HAB
    }
    
    public enum DesiredAngle{Bottom, Middle, Top}

    public static final double SETANGLE_BOTTOM = 80;
    public static final double SETANGLE_MIDDLE = 75;
    public static final double SETANGLE_TOP = 5;

    private final CANSparkMax pivotMax = RobotMap.pivotSubsystemMax;
    private final CANEncoder pivotEncoder = RobotMap.pivotEncoder;
    private final DigitalInput topPivotLimit = RobotMap.pivotLimitSwitch;
    private final PIDController pivotPIDContoller;

    private boolean encoderisvalid = false;
    private double desiredAngle = SETANGLE_TOP;
    private double PIDpower = 0;
    private PivotMode currentPivotMode = PivotMode.AUTOMAGIC;

    public PivotSubsystem(){
        pivotPIDContoller = new PIDController(0, 0, 0, 0, this, this);
        setPIDSourceType(PIDSourceType.kDisplacement);
        pivotPIDContoller.setInputRange(0, 100);
        pivotPIDContoller.setOutputRange(-0.3, 0.2);
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(...);
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop

        SmartDashboard.putBoolean("pivotLimitSwitch", isTopPivotLimitDepressed());

        if(checkForPivotEncoder()) {
            SmartDashboard.putNumber("pivotAngleInTics", pivotEncoder.getPosition());
        }
        SmartDashboard.putBoolean("pivotEncoderIsValid", encoderisvalid);
        SmartDashboard.putNumber("pivotAngleInDegrees", getPivotAngle());
        SmartDashboard.putNumber("pivotDesiredAngle", desiredAngle);
        SmartDashboard.putBoolean("pivotEncoderIsValid", encoderisvalid);
        SmartDashboard.putNumber("Pitch", Robot.driveSubsystem.ahrs.getPitch());
        SmartDashboard.putNumber("Roll", Robot.driveSubsystem.ahrs.getRoll());
        SmartDashboard.putString("pivotMode", currentPivotMode.toString());

        if(Robot.getCurrentRobotMode() == RobotMode.TELEOP || Robot.getCurrentRobotMode() == RobotMode.AUTONOMOUS){
            if(isTopPivotLimitDepressed() && !encoderisvalid){
                resetEncoder();
                encoderisvalid = true;
            }

            //look at joystick position to get into manualmode.
            double xPos = Robot.oi.getClimberHorizontalJoystick();
            double yPos = Robot.oi.getClimberVerticalJoystick();
            if (Math.abs(xPos) > 0.2 || Math.abs(yPos) > 0.2){
                setCurrentPivotMode(PivotMode.MANUAL);
            }

            if(currentPivotMode == PivotMode.MANUAL) {
                periodicManualMode();
            } else if(currentPivotMode == PivotMode.AUTOMAGIC) {
                //automagic
                if(encoderisvalid){
                    periodicAutoMagicMode();
                }else{
                    // we want to be in, but we are not there yet
                    // we need to do some pivotMove with a negative
                    pivotMove(-0.1);
                }
            } else if(currentPivotMode == PivotMode.HAB) {
                periodicHAB();
            } else {
                logger.warn("Pivot Mode Not Normal!");
            }
        }
        SmartDashboard.putNumber("pivotMotorPower", pivotMax.getAppliedOutput());
    }

    private void periodicHAB() {
        //+lift power makes the lift go down
        //-lift power makes the lift go up 
        double liftMotorPower = Robot.liftSubsystem.getMaxPower();

        //+pivotMotorPower makes the intake push down
        //-pivotMotorPower makes the intake come up
        double pivotMotorPower = (liftMotorPower)*(1./2.);

        // - pitch = nose down.
        // + pitch = nose up
        double pitch = Robot.driveSubsystem.ahrs.getPitch();
        double adjustFactor = 1.0;
        if(Math.abs(pitch) > 3) {
            /*
            If and only if the |pitch| is greater than 5, the formula 
            below is meant to return 0.8 if the pitch is negative and 
            1.2 if the pitch is positive, correcting for any "wobbling"
            */
            adjustFactor = (1.0 + (pitch/Math.abs(pitch)*0.5));
        }
        SmartDashboard.putNumber("HAB lift motor power", liftMotorPower);
        SmartDashboard.putNumber("HAB pitch", pitch);
        SmartDashboard.putNumber("HAB pivot motor power (pre-adjust)", pivotMotorPower);
        SmartDashboard.putNumber("HAB adjust factor", adjustFactor);
        pivotMotorPower = pivotMotorPower * adjustFactor;
        SmartDashboard.putNumber("HAB pivot motor power (post-adjust)", pivotMotorPower);
        pivotMove(pivotMotorPower);
    }
    
    private void periodicAutoMagicMode(){
        double currentAngle = getPivotAngle();
        // positive error is we are out too far
        double error = currentAngle - desiredAngle;
        SmartDashboard.putNumber("pivotError", error);
        if (desiredAngle == SETANGLE_TOP) {
            if(Math.abs(error) > 0) {
                    if (error > 0) {
                    // we want to be in, but we are not there yet
                    // we need to do some pivotMove with a negative
                    //Power was halved for two neo pivot
                    pivotMove(-0.35);
                } else {
                    pivotStop();
                }
            } else{
                pivotStop();
            }
        } else if (desiredAngle == SETANGLE_BOTTOM) {
            if(Math.abs(error) > 10) {
                // we want to be out 
                if (error < 0) {
                    // we want to be out, but we are not there yet
                    // we need to do some pivotMove with a positive
                    //Power was halved for two neo pivot
                    pivotMove(0.3);
                } else {
                    pivotStop();
                }
            } else{
                pivotStop();
            }
        }else{
            //Power was halved for two neo pivot
            pivotMove(PIDpower/2); 
        }
    }
   
       
        


        public double calculatePivotAngle(DesiredAngle desiredAngle){
            switch (desiredAngle) {
                case Bottom:
                    return SETANGLE_BOTTOM;
                case Middle:
                    return SETANGLE_MIDDLE;
                case Top:
                    return SETANGLE_TOP;
                default:
                    return SETANGLE_BOTTOM;
               
            }
              
        }

        private void TellPivotAngleBottom(){
            double currentAngle = getPivotAngle();
            if (currentAngle == SETANGLE_BOTTOM){
                logger.info("BottomPivot Set");
            } else {
                logger.info("Not Bottom");
            }
        }

        private void TellPivotAngleMiddle(){
            double currentAngle = getPivotAngle();
            if (currentAngle == SETANGLE_MIDDLE){
                logger.info("MiddlePivot Set");
            } else {
                logger.info("Not Middle");
            }
        }

        
        private void TellPivotAngleTop(){
            double currentAngle = getPivotAngle();
            if (currentAngle == SETANGLE_TOP){
                logger.info("TopPivot");
            } else {
                logger.info("Not Top");
            }
        }



    private void periodicManualMode(){
        double yPos = Robot.oi.getClimberVerticalJoystick();
        // joystick position is negative if we push away from ourselves.
        // if we are pushing away from ourselves, that means we want to 
        // push the intake out to the front of the robot.
        // pivotMove needs a positive number to move out to the front of
        // the robot.
        // so we need to change the sign.
        double power = -yPos * 0.7;
        if(power < -0.2){
            power = -0.2;
        }
        pivotMove(power);
    }
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public boolean isTopPivotLimitDepressed(){
        if(topPivotLimit.get() == true){
            return false;
        }
        return true;
    } 

    /**
     * Move the pivot, disabling if we go past the limit switch
     * @param power amount of power to apply to motor. Positive 
     * moves the pivot out to the front of the robot.
     */

    public void pivotMove(double power) {
        if((isTopPivotLimitDepressed() == true) && (power < 0)){
            power = 0;
        }
        if(encoderisvalid && (getPivotAngle() < -5) && (power < 0 )){
            power = 0;
        }
        SmartDashboard.putNumber("pivot power", power);
        pivotMax.set(-power);
    }

    public void pivotStop() {
        SmartDashboard.putNumber("pivot power", 0);
        pivotMax.set(0);
    }

    double ticstodegrees(double tics) { 
        // turning the encoder readings from tics to feet
        double angle = tics * -(90/27.38075);
        return angle;
    }

    /**
     * 
     * @return
     */
    public double getPivotAngle() {
        if(checkForPivotEncoder()) {
            double tics = pivotEncoder.getPosition();
            double howfarwehavemoved = tics - pivotEncoderZeroValue;
            double degrees = ticstodegrees(howfarwehavemoved);
            return degrees;
        } else {
            return(0);
        }
    }

    public PivotMode getCurrentPivotMode() {
        return currentPivotMode;
    }

    public void setCurrentPivotMode(PivotMode newPivotMode) {
        if (currentPivotMode != newPivotMode) {
            logger.info ("Changing PivotMode from {} to {}", currentPivotMode, newPivotMode);
        }
        currentPivotMode = newPivotMode;

        if (currentPivotMode != PivotMode.AUTOMAGIC) {
            pivotPIDContoller.disable();          
        }
    }

    private double pivotEncoderZeroValue;

    public boolean checkForPivotEncoder() {
        return(!(pivotEncoder == null));
    }

    public void resetEncoder(){
        if(checkForPivotEncoder()) {
            pivotEncoderZeroValue = pivotEncoder.getPosition();
        }
    }

    public void setDesiredAngle(double v) {
        logger.info("setting desired angle {}", v);
        desiredAngle = v;
        if (currentPivotMode != PivotMode.AUTOMAGIC) {
            logger.info ("going to automagic mode");
        }
        currentPivotMode = PivotMode.AUTOMAGIC;

        if (desiredAngle == SETANGLE_BOTTOM || desiredAngle == SETANGLE_TOP) {
            pivotPIDContoller.disable();
        } else {
            pivotPIDContoller.setSetpoint(desiredAngle);
            if (!pivotPIDContoller.isEnabled()) {
                // set the P, I, D, FF
                double p = SmartDashboard.getNumber("pivotP", 0.01);
                double i = SmartDashboard.getNumber("pivotI", 0);
                double d = SmartDashboard.getNumber("pivotD", 0);
                double f = SmartDashboard.getNumber("pivotF", 0);

                logger.info("_pivotP={}", p);
                logger.info("_pivotI={}", i);
                logger.info("_pivotD={}", d);
                logger.info("_pivotF={}", f);

                pivotPIDContoller.setP(p);
                pivotPIDContoller.setI(i);
                pivotPIDContoller.setD(d);
                pivotPIDContoller.setF(f);
                pivotPIDContoller.reset();
                pivotPIDContoller.enable();
            }
        }
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
        double pos = getPivotAngle();
        return pos;
	}
}