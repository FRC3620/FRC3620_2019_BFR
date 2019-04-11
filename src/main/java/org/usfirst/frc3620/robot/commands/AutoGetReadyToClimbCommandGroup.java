package org.usfirst.frc3620.robot.commands;


import org.usfirst.frc3620.robot.subsystems.PivotSubsystem;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoGetReadyToClimbCommandGroup extends CommandGroup{
    public AutoGetReadyToClimbCommandGroup(){
        addSequential(new SetPivotAngleCommand(PivotSubsystem.DesiredAngle.Climb));
        addSequential(new SetLiftHeightCommand(35, true));
        addSequential(new LockLiftPinsCommand());
    }
}