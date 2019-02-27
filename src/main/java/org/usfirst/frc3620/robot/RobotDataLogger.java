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

	public RobotDataLogger (DataLogger robotDataLogger, CANDeviceFinder canDeviceFinder) {
		robotDataLogger.addDataProvider("robotMode", () -> Robot.currentRobotMode.toString());
		robotDataLogger.addDataProvider("robotModeInt", () -> Robot.currentRobotMode.ordinal());
		robotDataLogger.addDataProvider("batteryVoltage", () -> f2(RobotController.getBatteryVoltage()));

		// do not log extra stuff
		if (canDeviceFinder.isDevicePresent(CANDeviceId.CANDeviceType.PDP, 0)) {
			powerDistributionPanel = new PowerDistributionPanel();
		 	robotDataLogger.addDataProvider("pdp.totalCurrent", () -> f2(powerDistributionPanel.getTotalCurrent()));
			robotDataLogger.addDataProvider("pdp.totalPower", () -> f2(powerDistributionPanel.getTotalPower()));
			robotDataLogger.addDataProvider("pdp.totalEnergy", () -> f2(powerDistributionPanel.getTotalEnergy()));
			
			// this needs work!

	/*		robotDataLogger.addDataProvider("drive.l0.pdpcurrent", () -> f2(powerDistributionPanel.getCurrent(13)));
			robotDataLogger.addDataProvider("drive.l1.pdpcurrent", () -> f2(powerDistributionPanel.getCurrent(14)));
			robotDataLogger.addDataProvider("drive.l2.pdpcurrent", () -> f2(powerDistributionPanel.getCurrent(15)));
			robotDataLogger.addDataProvider("drive.r3.pdpcurrent", () -> f2(powerDistributionPanel.getCurrent(2)));
			robotDataLogger.addDataProvider("drive.r4.pdpcurrent", () -> f2(powerDistributionPanel.getCurrent(1)));
			robotDataLogger.addDataProvider("drive.r5.pdpcurrent", () -> f2(powerDistributionPanel.getCurrent(0))); */
		}

		if (RobotMap.driveSubsystemMaxLeftA != null) {
			robotDataLogger.addDataProvider("drive.l1.power", () -> f2(RobotMap.driveSubsystemMaxLeftA.get()));
			robotDataLogger.addDataProvider("drive.l1.current",
					() -> f2(RobotMap.driveSubsystemMaxLeftA.getOutputCurrent()));
		}
		if (RobotMap.driveSubsystemMaxLeftB != null) {
			robotDataLogger.addDataProvider("drive.l2.current",
					() -> f2(RobotMap.driveSubsystemMaxLeftB.getOutputCurrent()));
		}
		//
		if (RobotMap.driveSubsystemMaxRightA != null) {
			robotDataLogger.addDataProvider("drive.r1.power", () -> f2(RobotMap.driveSubsystemMaxRightA.get()));
			robotDataLogger.addDataProvider("drive.r1.current",
					() -> f2(RobotMap.driveSubsystemMaxRightA.getOutputCurrent()));
		}
		if (RobotMap.driveSubsystemMaxRightB != null) {
			robotDataLogger.addDataProvider("drive.r2.current",
					() -> f2(RobotMap.driveSubsystemMaxRightB.getOutputCurrent()));
		}
	}

	private DecimalFormat f2Formatter = new DecimalFormat("#.##");

	private String f2(double f) {
		String rv = f2Formatter.format(f);
		return rv;
	}
}