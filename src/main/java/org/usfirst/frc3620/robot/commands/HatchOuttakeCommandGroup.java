package org.usfirst.frc3620.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class HatchOuttakeCommandGroup extends CommandGroup{
    public HatchOuttakeCommandGroup(){
        addSequential(new HatchPusherOutCommand());
        addSequential(new WaitJustALittle(0.2));
        addSequential(new HatchFingerDownCommand());
        addSequential(new WaitJustALittle(0.2));
        addSequential(new HatchPusherInCommand());
        addSequential(new WaitJustALittle(0.2));
       // addSequential(new HatchFingerUpCommand());
    }
}