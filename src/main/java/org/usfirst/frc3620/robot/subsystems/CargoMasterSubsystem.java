package org.usfirst.frc3620.robot.subsystems;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.robot.commands.SlotSelectorLeftCommand;
import org.usfirst.frc3620.robot.commands.SlotSelectorRightCommand;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class CargoMasterSubsystem extends Subsystem {
    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
    
    public int slotSelectSlot;

    public boolean runSlotSelectionLeft = false;
    public boolean runSlotSelectionRight = false;

    SlotSelectorLeftCommand slotSelectorLeft = new SlotSelectorLeftCommand(1);
    SlotSelectorRightCommand slotSelectorRight = new SlotSelectorRightCommand(1);
    
    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(...);
    }

    @Override
    public void periodic() {
        if(runSlotSelectionLeft == true){
            if(slotSelectorLeft.isRunning()){
                if(slotSelectSlot == slotSelectorLeft.setSlot){
                    slotSelectorLeft.close();
                    slotSelectorLeft = new SlotSelectorLeftCommand(slotSelectSlot);
                    slotSelectorLeft.start();
                }
            } else {
                slotSelectorLeft.start();
            }
        } else if(runSlotSelectionRight == true){
            if(slotSelectorRight.isRunning()){
                if(slotSelectSlot == slotSelectorRight.setSlot){
                    slotSelectorRight.close();
                    slotSelectorRight = new SlotSelectorRightCommand(slotSelectSlot);
                    slotSelectorRight.start();
                }
            } else {
                slotSelectorRight.start();
            }
        }
         if(runSlotSelectionLeft == false){
            if(slotSelectorLeft.isRunning()){
                slotSelectorLeft.close();
            } 
        } 
         if(runSlotSelectionRight == false){
            if(slotSelectorRight.isRunning()){
                slotSelectorRight.close();
            }
        }
    }
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

}