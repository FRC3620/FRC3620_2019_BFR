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

  private NetworkTableEntry targetAngle = networkTable.getEntry("angle frontCameratape");
  private NetworkTableEntry targetDistance = networkTable.getEntry("RealDistance frontCameratape");
  private NetworkTableEntry targetYaw = networkTable.getEntry("tapeYaw frontCameratape");

  private final double DESIRED_YAW = 0;
  private double error;

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
      error = getTargetYaw();
      //logger.info("Yaw: {}", error);
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
        double pos = getTargetYaw();
        return pos;
	}
}