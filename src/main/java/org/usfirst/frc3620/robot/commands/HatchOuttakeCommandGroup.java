package org.usfirst.frc3620.robot.commands;

import org.usfirst.frc3620.robot.Robot;
import org.usfirst.frc3620.robot.RobotMap;
import org.usfirst.frc3620.robot.subsystems.HatchSubsystem;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class HatchOuttakeCommandGroup extends CommandGroup{
    public HatchOuttakeCommandGroup(){
        addSequential(new HatchPusherOutCommand());
        addSequential(new WaitJustALittle(1));
        addSequential(new HatchFingerDownCommand());
        addSequential(new WaitJustALittle(1));
        addSequential(new HatchPusherInCommand());
        addSequential(new WaitJustALittle(1));
        addSequential(new HatchFingerUpCommand());
    }
}