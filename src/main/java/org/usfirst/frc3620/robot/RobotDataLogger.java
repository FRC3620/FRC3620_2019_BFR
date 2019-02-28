package org.usfirst.frc3620.robot;

import java.text.DecimalFormat;

import org.usfirst.frc3620.logger.DataLogger;
import org.usfirst.frc3620.misc.CANDeviceFinder;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.RobotController;
import org.usfirst.frc3620.misc.CANDeviceId;

public class RobotDataLogger {
	PowerDistributionPanel powerDistributionPanel = null;
	DriverStation driverStation = DriverStation.getInstance();

	public RobotDataLogger (DataLogger dataLogger, CANDeviceFinder canDeviceFinder) {
		powerDistributionPanel = new PowerDistributionPanel();

		dataLogger.addDataProvider("robotMode", () -> Robot.currentRobotMode.toString());
		dataLogger.addDataProvider("robotModeInt", () -> Robot.currentRobotMode.ordinal());
		dataLogger.addDataProvider("batteryVoltage", () -> f2(RobotController.getBatteryVoltage()));

		// do not log extra stuff
		if (canDeviceFinder.isDevicePresent(CANDeviceId.CANDeviceType.PDP, 0)) {
		 	dataLogger.addDataProvider("pdp.totalCurrent", () -> f2(powerDistributionPanel.getTotalCurrent()));
			dataLogger.addDataProvider("pdp.totalPower", () -> f2(powerDistributionPanel.getTotalPower()));
			dataLogger.addDataProvider("pdp.totalEnergy", () -> f2(powerDistributionPanel.getTotalEnergy()));
			
			// this needs work!

	/*		robotDataLogger.addDataProvider("drive.l0.pdpcurrent", () -> f2(powerDistributionPanel.getCurrent(13)));
			robotDataLogger.addDataProvider("drive.l1.pdpcurrent", () -> f2(powerDistributionPanel.getCurrent(14)));
			robotDataLogger.addDataProvider("drive.l2.pdpcurrent", () -> f2(powerDistributionPanel.getCurrent(15)));
			robotDataLogger.addDataProvider("drive.r3.pdpcurrent", () -> f2(powerDistributionPanel.getCurrent(2)));
			robotDataLogger.addDataProvider("drive.r4.pdpcurrent", () -> f2(powerDistributionPanel.getCurrent(1)));
			robotDataLogger.addDataProvider("drive.r5.pdpcurrent", () -> f2(powerDistributionPanel.getCurrent(0))); */
		}

		if (RobotMap.driveSubsystemMaxLeftA != null) {
			dataLogger.addDataProvider("drive.l1.appliedOutput", () -> f2(RobotMap.driveSubsystemMaxLeftA.getAppliedOutput()));
			dataLogger.addDataProvider("drive.l1.current",
					() -> f2(RobotMap.driveSubsystemMaxLeftA.getOutputCurrent()));
		}
		if (RobotMap.driveSubsystemMaxLeftB != null) {
			dataLogger.addDataProvider("drive.l2.current",
					() -> f2(RobotMap.driveSubsystemMaxLeftB.getOutputCurrent()));
		}
		//
		if (RobotMap.driveSubsystemMaxRightA != null) {
			dataLogger.addDataProvider("drive.r1.appliedOutput", () -> f2(RobotMap.driveSubsystemMaxRightA.getAppliedOutput()));
			dataLogger.addDataProvider("drive.r1.current",
					() -> f2(RobotMap.driveSubsystemMaxRightA.getOutputCurrent()));
		}
		if (RobotMap.driveSubsystemMaxRightB != null) {
			dataLogger.addDataProvider("drive.r2.current",
					() -> f2(RobotMap.driveSubsystemMaxRightB.getOutputCurrent()));
		}
		if (Robot.liftSubsystem.checkForLiftEncoder()) {
			dataLogger.addDataProvider("liftHeight", () -> Robot.liftSubsystem.getLiftHeight());
		}
		if (RobotMap.liftSubsystemMax != null) {
			dataLogger.addDataProvider("liftMotorAppliedOutput", () -> RobotMap.liftSubsystemMax.getAppliedOutput());
			dataLogger.addDataProvider("liftMotorCurrent", () -> RobotMap.liftSubsystemMax.getOutputCurrent());
		}
		if (Robot.pivotSubsystem.checkForPivotEncoder()) {
			dataLogger.addDataProvider("pivotAngle", () -> Robot.pivotSubsystem.getPivotAngle());
		}
		if (RobotMap.pivotSubsystemMax != null) {
			dataLogger.addDataProvider("pivotMotorAppliedOutput", () -> RobotMap.pivotSubsystemMax.getAppliedOutput());
			dataLogger.addDataProvider("pivotMotorCurrent", () -> RobotMap.pivotSubsystemMax.getOutputCurrent());
		}
		if (RobotMap.pivotSubsystemMax2 != null) {
			dataLogger.addDataProvider("pivotMotor2AppliedOutputPower", () -> RobotMap.pivotSubsystemMax2.getAppliedOutput());
			dataLogger.addDataProvider("pivotMotor2Current", () -> RobotMap.pivotSubsystemMax2.getOutputCurrent());
		}

	}

	private DecimalFormat f2Formatter = new DecimalFormat("#.##");

	private String f2(double f) {
		String rv = f2Formatter.format(f);
		return rv;
	}
}