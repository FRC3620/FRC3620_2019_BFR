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
    private final double stoppingDistance = 3;
    private final double startingPower = 0.4;
    
    public TravelAlignPushCommand() {
        // requires(Robot.laserCannonSubsystem);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        EventLogging.commandMessage(logger);
        calculateK();
        distanceInitial = Robot.visionSubsystem.getTargetDistance();
    }
    public double sech(double x){
        return 1/Math.cosh(x);
    }

    public void calculateK(){
        k = (startingPower)/(-sech(distanceInitial-stoppingDistance) + 1.14);
    
    }

    public double getLeftPower(double distance, double yaw){
        double power = k*(-sech(distance - stoppingDistance) + 1.14);
        if(yaw > 31){
            power = power + 0.2*(0.006451*yaw);
        } else if(yaw > 0){
            power = power + 0.2*(-(0.06/31)*yaw + 0.14);
        }
        return power;
    }

    public double getRightPower(double distance, double yaw){
        double power = k*(-sech(distance - stoppingDistance) + 1.14);
        
        if(yaw < -31){
            power = power - 0.2*(0.006451*yaw);
        } else if(yaw < 0){
            power = power + 0.2*(-(0.06/31)*yaw + 0.14);
        }
        return power;
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        double distance = Robot.visionSubsystem.getTargetDistance();
        double yaw = Robot.visionSubsystem.getTargetYaw();
        Robot.driveSubsystem.autoDriveTank(getLeftPower(distance, yaw), getRightPower(distance, yaw));
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        double distance = Robot.visionSubsystem.getTargetDistance();
        if(distance > stoppingDistance){
            return false;
        }
        return true;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        EventLogging.commandMessage(logger);
        Robot.driveSubsystem.autoDriveTank(0,0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run or when cancelled by whileHeld
    @Override
    protected void interrupted() {
        Robot.driveSubsystem.autoDriveTank(0,0);
    	EventLogging.commandMessage(logger);
    }
}