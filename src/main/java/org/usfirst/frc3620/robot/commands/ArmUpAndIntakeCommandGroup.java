package org.usfirst.frc3620.robot.commands;

import org.usfirst.frc3620.robot.Robot;
import org.usfirst.frc3620.robot.RobotMap;
import org.usfirst.frc3620.robot.subsystems.PivotSubsystem;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ArmUpAndIntakeCommandGroup extends CommandGroup{
    public ArmUpAndIntakeCommandGroup(){
        addSequential(new SetPivotAngleCommand(PivotSubsystem.DesiredAngle.Top));
        addSequential(new WaitJustALittle(RobotMap.pivotLimitSwitch, true));
        addSequential(new TrashInCommand());
    }
}

