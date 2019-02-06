package org.usfirst.frc3620.robot;

import com.revrobotics.CANEncoder;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.InputMode;
import com.revrobotics.CANSparkMaxLowLevel.ConfigParameter;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.misc.CANDeviceFinder;
import com.kauailabs.navx.frc.AHRS;


import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Counter;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */

 /**
  * @author Nick Zimanski (SlippStream)
  * @version 1/25/19
  *
  * Added light subsystem functionality, initialized to PWM 9
  */
public class RobotMap {
    public static AHRS driveSubsystemAHRS;
    public static DifferentialDrive driveSubsystemDifferentialDrive;
    public static DifferentialDrive driveSubsystemCANDifferentialDrive;
    public static SpeedController lightSubsystemLightPWM;

    public static Victor intakeSubsystemUpperMotor;
    public static Victor intakeSubsystemLowerMotor;
    public static Victor intakeSubsystemMiddleMotor;

    public static WPI_TalonSRX conveyorBeltMotorL;
    public static WPI_TalonSRX conveyorBeltMotorR;
    public static WPI_TalonSRX conveyorBeltMotorC;

    public static DigitalInput lineSensor;
    public static DigitalInput liftLimitSwitchTop;
    public static DigitalInput liftLimitSwitchBottom;
    public static DigitalInput practiceBotJumper;   //Added from 2018 code
    public static DigitalInput exampleSubsystemDigitalInput0;
    public static Counter counter; 
    public static Solenoid liftSubsystemBrake;

    public static CANEncoder leftsideEncoder, rightsideEncoder, liftEncoder;
    public static CANSparkMax driveSubsystemMaxLeftA;
    public static CANSparkMax driveSubsystemMaxLeftB;
    public static CANSparkMax driveSubsystemMaxRightA;
    public static CANSparkMax driveSubsystemMaxRightB;
    public static CANSparkMax liftSubsystemMax;
    public static CANDeviceFinder canDeviceFinder;

    static Logger logger = EventLogging.getLogger(RobotMap.class, Level.INFO);
    
    @SuppressWarnings("deprecation")
	public static void init() {
        canDeviceFinder = new CANDeviceFinder();
        logger.info ("CANDEVICEfinder found {}", canDeviceFinder.getDeviceList());

        liftSubsystemMax = new CANSparkMax(5, MotorType.kBrushless);
        resetMaxToKnownState(liftSubsystemMax);
        liftEncoder = liftSubsystemMax.getEncoder();
        liftLimitSwitchTop = new DigitalInput(1);
        liftLimitSwitchBottom = new DigitalInput(2);

        SpeedControllerGroup groupLeft;
        SpeedControllerGroup groupRight;
        if(canDeviceFinder.isMAXPresent(1)) {
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

            groupLeft = new SpeedControllerGroup(driveSubsystemMaxLeftA, driveSubsystemMaxLeftB);
            groupRight = new SpeedControllerGroup(driveSubsystemMaxRightA, driveSubsystemMaxRightB);

        } else {

            WPI_TalonSRX driveSubsystemLeftSpeedControllerA = new WPI_TalonSRX(1);
            resetTalonToKnownState(driveSubsystemLeftSpeedControllerA);

            WPI_VictorSPX driveSubsystemLeftSpeedControllerB = new WPI_VictorSPX(2);
            resetTalonToKnownState(driveSubsystemLeftSpeedControllerB);

            WPI_VictorSPX  driveSubsystemLeftSpeedControllerC = new WPI_VictorSPX(3);
            resetTalonToKnownState(driveSubsystemLeftSpeedControllerC);

            WPI_TalonSRX  driveSubsystemRightSpeedControllerA = new WPI_TalonSRX(4);
            resetTalonToKnownState(driveSubsystemRightSpeedControllerA);
            driveSubsystemRightSpeedControllerA.setInverted(true);

            WPI_VictorSPX  driveSubsystemRightSpeedControllerB = new WPI_VictorSPX(5);
            resetTalonToKnownState(driveSubsystemRightSpeedControllerB);
            driveSubsystemRightSpeedControllerB.setInverted(true);

            WPI_VictorSPX  driveSubsystemRightSpeedControllerC = new WPI_VictorSPX(6);
            resetTalonToKnownState(driveSubsystemRightSpeedControllerC);
            driveSubsystemRightSpeedControllerC.setInverted(true);

            groupLeft = new SpeedControllerGroup(driveSubsystemLeftSpeedControllerA, driveSubsystemLeftSpeedControllerB, driveSubsystemLeftSpeedControllerC);
            groupRight = new SpeedControllerGroup(driveSubsystemRightSpeedControllerA, driveSubsystemRightSpeedControllerB, driveSubsystemRightSpeedControllerC);
        }
        
        driveSubsystemDifferentialDrive = new DifferentialDrive(groupLeft, groupRight);
        driveSubsystemDifferentialDrive.setName("DriveSubsystem", "Drive");
        driveSubsystemDifferentialDrive.setSafetyEnabled(true);
        driveSubsystemDifferentialDrive.setExpiration(0.1);
        driveSubsystemDifferentialDrive.setMaxOutput(1.0);

        LiveWindow.addActuator("DriveSubsystem", "CANDifferentialDrive", driveSubsystemCANDifferentialDrive);

        //new code
        conveyorBeltMotorL = new WPI_TalonSRX(7);
        conveyorBeltMotorR = new WPI_TalonSRX(8);
        conveyorBeltMotorC = new WPI_TalonSRX(9);

        
        intakeSubsystemUpperMotor = new Victor(4);
        intakeSubsystemLowerMotor = new Victor(5);
        intakeSubsystemMiddleMotor = new Victor(6);

        lightSubsystemLightPWM = new Spark(9);

		LiveWindow.addActuator("LightSubsystem", "LightPWM", (Spark) lightSubsystemLightPWM);
        lightSubsystemLightPWM.setInverted(false);
        
        //initiating line sensor
        lineSensor = new DigitalInput(0);
        counter = new Counter(lineSensor);
        counter.setUpSourceEdge(false, true);

        if (canDeviceFinder.isPCMPresent(0)) {
            // instantiate Pneumatics here
            liftSubsystemBrake = new Solenoid(1);
        }

        driveSubsystemAHRS = new AHRS(edu.wpi.first.wpilibj.SPI.Port.kMXP);
		LiveWindow.addSensor("Drivetrain", "AHRS", driveSubsystemAHRS);

        LiveWindow.addSensor("ExampleSubsystem", "Digital Input 0", exampleSubsystemDigitalInput0);

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