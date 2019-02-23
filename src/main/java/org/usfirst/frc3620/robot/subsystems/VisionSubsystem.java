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

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * Add your docs here.
 */
public class VisionSubsystem extends Subsystem implements PIDSource, PIDOutput {
  Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  private NetworkTableInstance inst = NetworkTableInstance.getDefault();
  private NetworkTable networkTable = inst.getTable("ChickenVision");

  private NetworkTableEntry frontTargetAngle = networkTable.getEntry("angle frontCameratape");
  private NetworkTableEntry frontTargetDistance = networkTable.getEntry("RealDistance frontCameratape");
  private NetworkTableEntry frontTargetYaw = networkTable.getEntry("tapeYaw frontCameratape");
  private NetworkTableEntry frontIsThereTarget = networkTable.getEntry("tapeDetected frontCameratape");

  private NetworkTableEntry rightTargetAngle = networkTable.getEntry("angle rightCameratape");
  private NetworkTableEntry rightTargetDistance = networkTable.getEntry("RealDistance rightCameratape");
  private NetworkTableEntry rightTargetYaw = networkTable.getEntry("tapeYaw rightCameratape");
  private NetworkTableEntry rightIsThereTarget = networkTable.getEntry("tapeDetected rightCameratape");

  private NetworkTableEntry leftTargetAngle = networkTable.getEntry("angle leftCameratape");
  private NetworkTableEntry leftTargetDistance = networkTable.getEntry("RealDistance leftCameratape");
  private NetworkTableEntry leftTargetYaw = networkTable.getEntry("tapeYaw leftCameratape");
  private NetworkTableEntry leftIsThereTarget = networkTable.getEntry("tapeDetected leftCameratape");

  private final double DESIRED_YAW = 0;

  private final PIDController visionPIDController;
  
  private double PIDpower = 0;

  private final double kP = 0.0025;
  private final double kI = 0.00005;
  private final double kD = 0.000;
  private final double kF = .2;

  public VisionSubsystem(){
    visionPIDController = new PIDController(kP, kI, kD, kF, this, this);
    setPIDSourceType(PIDSourceType.kDisplacement);
    visionPIDController.setInputRange(-150, 150);
    visionPIDController.setOutputRange(-0.5, 0.5);
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand())
   // setDefaultCommand(new VisionAlignmentCommand());
  }

  @Override
    public void periodic() {
    }

  public double getFrontTargetAngle(){
    if (frontIsThereTarget.getBoolean(false)){
      double angle = frontTargetAngle.getDouble(0);
      return angle;
    }
    logger.info("No front target detected, returning angle = 0");
    return 0;
  }

  public double getFrontTargetDistance(){
    if (frontIsThereTarget.getBoolean(false)){
      double distance = frontTargetDistance.getDouble(0);
      return distance;
    }
    logger.info("No front target detected, returning distance = 0");
    return 0;
  }

  public double getFrontTargetYaw(){
    if (frontIsThereTarget.getBoolean(false)){
      double yaw = frontTargetYaw.getDouble(0);
      return yaw;
    }
    logger.info("No front target detected, returning yaw = 0");
    return 0;
  }

  public double getRightTargetYaw(){
    if (rightIsThereTarget.getBoolean(false)){
      double yaw = rightTargetYaw.getDouble(0);
      return yaw;
    }
    logger.info("No right target detected, returning yaw = 0");
    return 0;
  }

  public double getRightTargetAngle(){
    if (rightIsThereTarget.getBoolean(false)){
      double angle = rightTargetAngle.getDouble(0);
      return angle;
    }
    logger.info("No right target detected, returning angle = 0");
    return 0;
  }

  public double getRightTargetDistance(){
    if (rightIsThereTarget.getBoolean(false)){
      double distance = rightTargetDistance.getDouble(0);
      return distance;
    }
    logger.info("No front target detected, returning distance = 0");
    return 0;
  }

  public boolean getRightTargetPresent(){
      return rightIsThereTarget.getBoolean(false);
  }

  public double getLeftTargetYaw(){
    if (leftIsThereTarget.getBoolean(false)){
      double yaw = -leftTargetYaw.getDouble(0);
      return yaw;
    }
    logger.info("No left target detected, returning yaw = 0");
    return 0;
  }

  public double getLeftTargetAngle(){
    if (leftIsThereTarget.getBoolean(false)){
      double angle = leftTargetAngle.getDouble(0);
      return angle;
    }
    logger.info("No left target detected, returning angle = 0");
    return 0;
  }

  public double getLeftTargetDistance(){
    if (leftIsThereTarget.getBoolean(false)){
      double distance = leftTargetDistance.getDouble(0);
      return distance;
    }
    logger.info("No left target detected, returning distance = 0");
    return 0;
  }

  public double getPIDOutput(){
    return PIDpower;
  }

  public void runPID(){
    visionPIDController.setSetpoint(DESIRED_YAW);
  }

  public void disablePID(){
    visionPIDController.disable();
  }

  public void enablePID(){
    visionPIDController.enable();
  }

  public void configurePID(){

    double p = kP;
    double i = kI;
    double d = kD;
    double f = kF;

    logger.info("_visionP={}", p);
    logger.info("_visionI={}", i);
    logger.info("_visionD={}", d);
    logger.info("_visionF={}", f);

    visionPIDController.setP(p);
    visionPIDController.setI(i);
    visionPIDController.setD(d);
    visionPIDController.setF(f);
    visionPIDController.reset();
    visionPIDController.enable();
  }

  @Override
    public void pidWrite(double output) {
        PIDpower = output;
    }

    PIDSourceType pidSourceType;

    @Override
    public void setPIDSourceType(PIDSourceType pidSource) {
        pidSourceType = pidSource;
    }

    @Override
    public PIDSourceType getPIDSourceType() {
        return pidSourceType;
    }

    @Override
    public double pidGet() {
        double pos = getFrontTargetYaw();
        return pos;
	}
}