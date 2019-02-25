package org.usfirst.frc3620.robot.commands;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.robot.Robot;

public class HatchIntakeCommand extends CommandGroup{
	Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
	
    public HatchIntakeCommand() {
        // requires(Robot.laserCannonSubsystem);
        addSequential(new HatchCollectCommand(false));
        addSequential(new HatchExtendCommand(true));
        addSequential(new WaitJustALittle(0.3));
        addSequential(new HatchCollectCommand(true));
        addSequential(new WaitJustALittle(0.3));
        addSequential(new HatchExtendCommand(false));
    }
}
