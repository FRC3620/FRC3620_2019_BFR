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
    double leftEncoderCurrent;
    double leftEncoderLast;
    double rightEncoderCurrent;
    double rightEncoderLast;
    
    public DriveCommand() {
        requires(Robot.driveSubsystem);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        leftEncoderLast = Robot.driveSubsystem.ticsToFeet(Robot.driveSubsystem.readLeftEncRaw());
        rightEncoderLast = Robot.driveSubsystem.ticsToFeet(Robot.driveSubsystem.readRightEncRaw());
    	EventLogging.commandMessage(logger);
    }

    protected void calculateXAndY(double leftSideEncoderLast, double leftSideEncoderCurrent, double rightSideEncoderLast, double rightSideEncoderCurrent){
       double leftSideDx = (leftSideEncoderCurrent - leftSideEncoderLast) * Math.sin(Robot.driveSubsystem.getRealAngle());
       double rightSideDx = (rightSideEncoderCurrent - rightSideEncoderLast) * Math.sin(Robot.driveSubsystem.getRealAngle());
       double leftSideDy = (leftSideEncoderCurrent - leftSideEncoderLast) * Math.cos(Robot.driveSubsystem.getRealAngle());
       double rightSideDy = (rightSideEncoderCurrent - rightSideEncoderLast) * Math.cos(Robot.driveSubsystem.getRealAngle());

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
   /*     leftEncoderCurrent = Robot.driveSubsystem.ticsToFeet(Robot.driveSubsystem.readLeftEncRaw());
        rightEncoderCurrent = Robot.driveSubsystem.ticsToFeet(Robot.driveSubsystem.readRightEncRaw());
        calculateXAndY(leftEncoderLast, leftEncoderCurrent, rightEncoderLast, rightEncoderCurrent);
        leftEncoderLast = leftEncoderCurrent;
        rightEncoderLast = rightEncoderCurrent; 
        System.out.println("x: " + getX());
        System.out.println("y: " + getY()); */
 //       System.out.println("x: " + Robot.driveSubsystem.getX());
   //     System.out.println("y: " + Robot.driveSubsystem.getY());
        //gets values from Y-axis of Right stick on gamepad, X-axis goes unused
        double vertical = Robot.oi.getRightVerticalJoystickSquared();
        //gets values from X-axis of Left stick on gamepad, Y-axis goes unused
        double horizontal = Robot.oi.getLeftHorizontalJoystickSquared();
        //displays current values on gamepad
            //Calls method to drive motors, declared in subsystem, sends real values to motors
            Robot.driveSubsystem.arcadeDrive(-horizontal, vertical);
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