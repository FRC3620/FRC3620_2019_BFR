package org.usfirst.frc3620.robot.subsystems;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class CargoMasterSubsystem extends Subsystem {
    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
    
    public int slotSelectSlot;

    boolean runSlotSelectionLeft = false;
    boolean runSlotSelectionRight = false;

    SlotSelectorLeftCommand slotSelectorLeft;
    
    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(...);
    }

    @Override
    public void periodic() {
        if()
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

}