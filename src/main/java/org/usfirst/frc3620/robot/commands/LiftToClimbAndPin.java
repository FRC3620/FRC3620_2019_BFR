package org.usfirst.frc3620.robot.commands;

import org.usfirst.frc3620.robot.Robot;
import org.usfirst.frc3620.robot.RobotMap;
import org.usfirst.frc3620.robot.subsystems.PivotSubsystem;
import org.usfirst.frc3620.robot.subsystems.LiftSubsystem;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class LiftToClimbAndPin extends CommandGroup{
    public LiftToClimbAndPin(){
        addSequential(new SetLiftHeightCommand(LiftSubsystem.SETPOINT_CLIMB_PINENGAGE, true));
       // addSequential(new WaitJustALittle(Robot.liftSubsystem.checkForLiftEncoder(), LiftSubsystem.SETPOINT_CLIMB_PINENGAGE));
        addSequential(new LockLiftPinsCommand());
    }
}