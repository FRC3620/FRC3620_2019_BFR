package org.usfirst.frc3620.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.misc.CANDeviceFinder;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    public static DifferentialDrive driveSubsystemDifferentialDrive;

    static Logger logger = EventLogging.getLogger(RobotMap.class, Level.INFO);

    public static CANSparkMax driveSubsystemMaxLeftA;
    public static CANSparkMax driveSubsystemMaxLeftB;
    public static CANSparkMax driveSubsystemMaxRightA;
    public static CANSparkMax driveSubsystemMaxRightB;
    public static CANDeviceFinder CANFinder;
    @SuppressWarnings("deprecation")
	public static void init() {
        CANFinder = new CANDeviceFinder();
        logger.info("CAN Device Finder Instantitated: {}",CANFinder.getDeviceList());
        
        SpeedControllerGroup groupLeft
        SpeedControllerGroup groupRight
        if(CANFinder.isMAXPresent()) {
            CANSparkMax driveSubsystemMaxLeftA = new CANSparkMax(1, MotorType.kBrushless);
            driveSubsystemMaxLeftA.setInverted(false);

            CANSparkMax driveSubsystemMaxLeftB = new CANSparkMax(2, MotorType.kBrushless);
            driveSubsystemMaxLeftB.setInverted(false);

            CANSparkMax driveSubsystemMaxRightA = new CANSparkMax(3, MotorType.kBrushless);
            driveSubsystemMaxRightA.setInverted(false);

            CANSparkMax driveSubsystemMaxRightB = new CANSparkMax(4, MotorType.kBrushless);
            driveSubsystemMaxRightB.setInverted(false);

            groupLeft = new SpeedControllerGroup(driveSubsystemMaxLeftA, driveSubsystemMaxLeftB);
            groupRight = new SpeedControllerGroup(driveSubsystemMaxRightA, driveSubsystemMaxRightB);
        } else {
            Victor driveSubsystemLeftSpeedControllerA = new Victor(0);
            driveSubsystemLeftSpeedControllerA.setName("DriveSubsystem", "LeftA");
            driveSubsystemLeftSpeedControllerA.setInverted(false);

            Victor driveSubsystemLeftSpeedControllerB = new Victor(1);
            driveSubsystemLeftSpeedControllerB.setName("DriveSubsystem", "LeftB");
            driveSubsystemLeftSpeedControllerB.setInverted(false);

            Victor driveSubsystemRightSpeedControllerA = new Victor(2);
            driveSubsystemRightSpeedControllerA.setName("DriveSubsystem", "RightA");
            driveSubsystemRightSpeedControllerA.setInverted(false);

            Victor driveSubsystemRightSpeedControllerB = new Victor(3);
            driveSubsystemRightSpeedControllerB.setName("DriveSubsystem", "RightB");
            driveSubsystemRightSpeedControllerB.setInverted(false);

            groupLeft = new SpeedControllerGroup(driveSubsystemLeftSpeedControllerA, driveSubsystemLeftSpeedControllerB);
            groupRight = new SpeedControllerGroup(driveSubsystemRightSpeedControllerA, driveSubsystemRightSpeedControllerB);
        }
        driveSubsystemDifferentialDrive = new DifferentialDrive(groupLeft, groupRight);
        driveSubsystemDifferentialDrive.setName("DriveSubsystem", "Drive");
        driveSubsystemDifferentialDrive.setSafetyEnabled(true);
        driveSubsystemDifferentialDrive.setExpiration(0.1);
        driveSubsystemDifferentialDrive.setMaxOutput(1.0);

        //new code

        CANDeviceFinder canDeviceFinder = new CANDeviceFinder();
        logger.info ("CANDEVICEfinder found {}", canDeviceFinder.getDeviceList());


        if (canDeviceFinder.isPCMPresent(0)) {
            // instantiate Pneumatics here
        }

    }


}
