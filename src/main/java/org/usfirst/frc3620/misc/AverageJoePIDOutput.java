/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc3620.misc;

import edu.wpi.first.wpilibj.PIDOutput;

/**
 * Add your docs here.
 */
public abstract class AverageJoePIDOutput implements PIDOutput {

    public abstract void pidWrite(double output);

}
