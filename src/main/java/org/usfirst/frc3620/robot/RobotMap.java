package org.usfirst.frc3620.robot;

import com.revrobotics.CANEncoder;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.InputMode;
import com.revrobotics.CANSparkMaxLowLevel.ConfigParameter;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

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
    public static CANEncoder leftsideEncoder, rightsideEncoder;

    @SuppressWarnings("deprecation")
	public static void init() {
        CANSparkMax driveSubsystemMaxLeftA = new CANSparkMax(1, MotorType.kBrushless);
        resetMaxToKnownState(driveSubsystemMaxLeftA);

        leftsideEncoder = driveSubsystemMaxLeftA.getEncoder();

        CANSparkMax driveSubsystemMaxLeftB = new CANSparkMax(2, MotorType.kBrushless);
        resetMaxToKnownState(driveSubsystemMaxLeftB);

        CANSparkMax driveSubsystemMaxRightA = new CANSparkMax(3, MotorType.kBrushless);
        resetMaxToKnownState(driveSubsystemMaxRightA);

        rightsideEncoder = driveSubsystemMaxRightA.getEncoder();

        CANSparkMax driveSubsystemMaxRightB = new CANSparkMax(4, MotorType.kBrushless);
        resetMaxToKnownState(driveSubsystemMaxRightB);

        SpeedControllerGroup groupLeft = new SpeedControllerGroup(driveSubsystemMaxLeftA, driveSubsystemMaxLeftB);
        SpeedControllerGroup groupRight = new SpeedControllerGroup(driveSubsystemMaxRightA, driveSubsystemMaxRightB);
       
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

    static void resetMaxToKnownState (CANSparkMax x) {
		x.setInverted(false);
        x.setIdleMode(IdleMode.kCoast);
		x.setRampRate(1);
        x.setSmartCurrentLimit(50);
    }

    static void resetTalonToKnownState (BaseMotorController x) {
		x.setInverted(false);
		x.setNeutralMode(NeutralMode.Coast);
		x.set(ControlMode.PercentOutput, 0);
		x.configNominalOutputForward(0, 0);
		x.configNominalOutputReverse(0, 0);
		x.configPeakOutputForward(1, 0);
		x.configPeakOutputReverse(-1, 0);
		x.configNeutralDeadband(0.04, 0);
	}
}
