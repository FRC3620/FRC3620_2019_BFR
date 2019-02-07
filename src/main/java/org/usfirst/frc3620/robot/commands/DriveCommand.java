package org.usfirst.frc3620.robot.commands;
import edu.wpi.first.wpilibj.command.Command;
import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.robot.Robot;

/**
 *
 */
public class DriveCommand extends Command {
    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
    double xInFeet;
    double yInFeet;
    
    public DriveCommand() {
        requires(Robot.driveSubsystem);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {

    	EventLogging.commandMessage(logger);
    }

    protected void calculateX(double leftSideEncoderLast, double leftSideEncoderCurrent, double rightSideEncoderLast, double rightSideEncoderCurrent){
       double leftSideDx = (leftSideEncoderCurrent - leftSideEncoderLast) * Math.cos(Robot.driveSubsystem.getRealAngle());
       double rightSideDx = (rightSideEncoderCurrent - rightSideEncoderLast) * Math.cos(Robot.driveSubsystem.getRealAngle());
       double leftSideDy = (leftSideEncoderCurrent - leftSideEncoderLast) * Math.sin(Robot.driveSubsystem.getRealAngle());
       double rightSideDy = (rightSideEncoderCurrent - rightSideEncoderLast) * Math.sin(Robot.driveSubsystem.getRealAngle());

       double realDx = (leftSideDx + rightSideDx)/2;
       double realDy = (leftSideDy + rightSideDy)/2;

       xInFeet = xInFeet + realDx;
       yInFeet = yInFeet + realDy;
    }

    public double getX(){
        return xInFeet;
    }

    public double getY(){
        return yInFeet;
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        leftSideEncoderCurrent = Robot.driveSubsystem.ticstofeet(Robot.driveSubsystem.)
        //gets values from Y-axis of Right stick on gamepad, X-axis goes unused
        double vertical = Robot.oi.getRightVerticalJoystickSquared();
        //gets values from X-axis of Left stick on gamepad, Y-axis goes unused
        double horizontal = Robot.oi.getLeftHorizontalJoystickSquared();
        //displays current values on gamepad
            //Calls method to drive motors, declared in subsystem, sends real values to motors
            Robot.driveSubsystem.arcadeDrive(-vertical, horizontal);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        EventLogging.commandMessage(logger);
        //stops robot
        Robot.driveSubsystem.stopDrive();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run or when cancelled by whileHeld
    @Override
    protected void interrupted() {
        EventLogging.commandMessage(logger);
        //stops robot
        Robot.driveSubsystem.stopDrive();
    }
}