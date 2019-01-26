package org.usfirst.frc3620.robot;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.misc.CANDeviceFinder;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Counter;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    public static DifferentialDrive driveSubsystemDifferentialDrive;
    public static Victor intakeSubsystemUpperMotor;
    public static Victor intakeSubsystemLowerMotor;
    public static Victor intakeSubsystemMiddleMotor;

    public static WPI_TalonSRX conveyorBeltMotorL;
    public static WPI_TalonSRX conveyorBeltMotorR;
    public static WPI_TalonSRX conveyorBeltMotorC;
    
    public static DigitalInput lineSensor;
    public static Counter counter; 
    

    static Logger logger = EventLogging.getLogger(RobotMap.class, Level.INFO);


    @SuppressWarnings("deprecation")
	public static void init() {
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

        SpeedControllerGroup groupLeft = new SpeedControllerGroup(driveSubsystemLeftSpeedControllerA, driveSubsystemLeftSpeedControllerB);
        SpeedControllerGroup groupRight = new SpeedControllerGroup(driveSubsystemRightSpeedControllerA, driveSubsystemRightSpeedControllerB);

        driveSubsystemDifferentialDrive = new DifferentialDrive(groupLeft, groupRight);
        driveSubsystemDifferentialDrive.setName("DriveSubsystem", "Drive");
        driveSubsystemDifferentialDrive.setSafetyEnabled(true);
        driveSubsystemDifferentialDrive.setExpiration(0.1);
        driveSubsystemDifferentialDrive.setMaxOutput(1.0);
        
        conveyorBeltMotorL = new WPI_TalonSRX(1);
        conveyorBeltMotorR = new WPI_TalonSRX(2);
        conveyorBeltMotorC = new WPI_TalonSRX(3);

        intakeSubsystemUpperMotor = new Victor(4);
        intakeSubsystemLowerMotor = new Victor(5);
        intakeSubsystemMiddleMotor = new Victor(6);

        //initiating line sensor
        lineSensor = new DigitalInput(0);
        counter = new Counter(lineSensor);
        counter.setUpSourceEdge(false, true);

        CANDeviceFinder canDeviceFinder = new CANDeviceFinder();
        logger.info ("CANDEVICEfinder found {}", canDeviceFinder.getDeviceList());


        if (canDeviceFinder.isPCMPresent(0)) {
            // instantiate Pneumatics here
        }

    }


}
