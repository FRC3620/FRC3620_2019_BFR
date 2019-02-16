package org.usfirst.frc3620.robot.commands;
import edu.wpi.first.wpilibj.command.Command;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.robot.Robot;

/**
 *
 */
public class TravelAlignPushCommand extends Command {
	Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
    private double distanceInitial;
    private double k;
    
    public TravelAlignPushCommand() {
        // requires(Robot.laserCannonSubsystem);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        EventLogging.commandMessage(logger);
        distanceInitial = Robot.visionSubsystem.getTargetDistance();
    }

    public void calculateK(){
        k = (0.8-0.3)/(distanceInitial - 2);
    }

    public double getLeftPower(double distance, double yaw){
        double power = (k*distance)-(2*k) + 0.3;
      /*  if(yaw > 0){
            power = power + 0.004*(yaw*yaw);
        } */
        return power;
    }

    public double getRightPower(double distance, double yaw){
        double power = (k*distance)-(2*k) + 0.3;
     /*   if(yaw < 0){
            power = power + 0.004*(yaw*yaw);
        } */
        return power;
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        double distance = Robot.visionSubsystem.getTargetDistance();
        double yaw = Robot.visionSubsystem.getTargetAngle();
        Robot.driveSubsystem.autoDriveTank(getLeftPower(distance, yaw), getRightPower(distance, yaw));
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        double distance = Robot.visionSubsystem.getTargetDistance();
        if(distance > 2){
            return false;
        }
        return true;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
    	EventLogging.commandMessage(logger);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run or when cancelled by whileHeld
    @Override
    protected void interrupted() {
    	EventLogging.commandMessage(logger);
    }
}