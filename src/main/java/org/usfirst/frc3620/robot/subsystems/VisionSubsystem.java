/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc3620.robot.subsystems;

import org.usfirst.frc3620.robot.commands.VisionAlignmentCommand;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Add your docs here.
 */
public class VisionSubsystem extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  private NetworkTableInstance inst = NetworkTableInstance.getDefault();
  private NetworkTable networkTable = inst.getTable("ChickenVision");

  private NetworkTableEntry targetAngle = networkTable.getEntry("angle frontCamera");
  private NetworkTableEntry targetDistance = networkTable.getEntry("realDistance frontCamera");
  private NetworkTableEntry targetYaw = networkTable.getEntry("tapeYaw frontCamera");

  

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand())
    setDefaultCommand(new VisionAlignmentCommand());
  }

  public double getTargetAngle(){
    double angle = targetAngle.getDouble(0);
    return angle;
  }

  public double getTargetDistance(){
    double distance = targetDistance.getDouble(0);
    return distance;
  }

  public double getTargetYaw(){
    double yaw = targetYaw.getDouble(0);
    return yaw;
  }
}
