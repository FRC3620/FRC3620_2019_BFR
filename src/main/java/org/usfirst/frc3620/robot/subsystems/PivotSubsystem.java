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
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 *
 */
public class PivotSubsystem extends Subsystem {
    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
    
    public static final double SETANGLE_BOTTOM = 90;
    public static final double SETANGLE_TOP = 0;

    private final CANSparkMax pivotMax = RobotMap.pivotSubsystemMax;
    private final CANEncoder pivotEncoder = RobotMap.pivotEncoder;
    private final DigitalInput topPivotLimit = RobotMap.pivotLimitSwitch;
 
    private boolean encoderisvalid = false;
    private double desiredAngle = 0;

    public PivotSubsystem(){

    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(...);
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop
        if(checkForLiftEncoder()) {
            SmartDashboard.putNumber("pivotAngleInTics", pivotEncoder.getPosition());
        }
        SmartDashboard.putNumber("pivotAngleInDegrees", getPivotAngle());

        if(Robot.getCurrentRobotMode() == RobotMode.TELEOP || Robot.getCurrentRobotMode() == RobotMode.AUTONOMOUS){
            if(isTopLimitDepressed()){
                resetEncoder();
                encoderisvalid = true;
            }

            if(encoderisvalid){
                double currentAngle = getPivotAngle();
                // positive error is we are out too far
                double error = currentAngle - desiredAngle;
                if(Math.abs(error) > 10) {
                    if (desiredAngle > 0) {
                        // we want to be out 
                        if (error < 0) {
                            // we want to be out, but we are not there yet
                            // we need to do some pivotMove with a positive
                            pivotMove(0.2);
                        } else {
                            pivotStop();
                        }
                    } else {
                        // we want to be in
                        if (error > 0) {
                            // we want to be in, but we are not there yet
                            // we need to do some pivotMove with a negative
                            pivotMove(-0.2);
                        } else {
                            pivotStop();
                        }
                    }
                }else{
                    pivotStop();
                }
            }else{
                // encoder is not valid, let's start coming in
                pivotMove(-0.2);
            }
        }
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public boolean isTopLimitDepressed(){
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
        if(isTopLimitDepressed() == true && power < 0){
            power = 0;
        }
        pivotMax.set(power);
    }

    public void pivotStop() {
        pivotMax.set(0);
    }

    double ticstodegrees(double tics) { 
        // turning the encoder readings from tics to feet
        double angle = tics * -1;
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
    }
}