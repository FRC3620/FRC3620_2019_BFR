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
    
    public static final double SETANGLE_BOTTOM = 75;
    public static final double SETANGLE_MIDDLE = 55;
    public static final double SETANGLE_TOP = 0;

    private final CANSparkMax pivotMax = RobotMap.pivotSubsystemMax;
    private final CANEncoder pivotEncoder = RobotMap.pivotEncoder;
    private final DigitalInput topPivotLimit = RobotMap.pivotLimitSwitch;
    private final PIDController pivotPIDContoller;

    private boolean encoderisvalid = false;
    private double desiredAngle = SETANGLE_TOP;
    private double PIDpower = 0;

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

        SmartDashboard.putBoolean("Pivot limit switch", isTopPivotLimitDepressed());

        if(checkForLiftEncoder()) {
            SmartDashboard.putNumber("pivotAngleInTics", pivotEncoder.getPosition());
        }
        SmartDashboard.putNumber("pivotAngleInDegrees", getPivotAngle());
        SmartDashboard.putNumber("desiredAngle", desiredAngle);

        if(Robot.getCurrentRobotMode() == RobotMode.TELEOP || Robot.getCurrentRobotMode() == RobotMode.AUTONOMOUS){
            if(isTopPivotLimitDepressed() && !encoderisvalid){
                resetEncoder();
                encoderisvalid = true;
            }

            if(encoderisvalid){
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
                            pivotMove(-0.1);
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
                            pivotMove(1);
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
            }else{
                // encoder is not valid, let's start coming in
                //Power was halved for two neo pivot
                pivotMove(-0.1);
            }
        }
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
     * Move the lift, disabling if we go past the limit switch
     * @param power amount of power to apply to motor. Positive 
     * moves the pivot out to the front of the robot.
     */

    public void pivotMove(double power) {
        if((isTopPivotLimitDepressed() == true) && (power < 0)){
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
        if(checkForLiftEncoder()) {
            double tics = pivotEncoder.getPosition();
            double howfarwehavemoved = tics - pivotEncoderZeroValue;
            double degrees = ticstodegrees(howfarwehavemoved);
            return degrees;
        } else {
            return(0);
        }
    }

    private double pivotEncoderZeroValue;

    public boolean checkForLiftEncoder() {
        return(!(pivotEncoder == null));
    }

    public void resetEncoder(){
        if(checkForLiftEncoder()) {
            pivotEncoderZeroValue = pivotEncoder.getPosition();
        }
    }

    public void setDesiredAngle(double v) {
        desiredAngle = v;

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