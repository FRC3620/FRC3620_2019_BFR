package org.usfirst.frc3620.robot;

import com.revrobotics.CANEncoder;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.misc.CANDeviceFinder;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Counter;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */

 public class RobotMap {
    public static Encoder leftsideEncoder, rightsideEncoder;
    public static CANEncoder leftsideCANEncoder, rightsideCANEncoder;
    public static CANSparkMax driveSubsystemMaxLeftA;
    public static CANSparkMax driveSubsystemMaxLeftB;
    public static CANSparkMax driveSubsystemMaxRightA;
    public static CANSparkMax driveSubsystemMaxRightB;
    public static CANDeviceFinder canDeviceFinder;

    public static DifferentialDrive driveSubsystemDifferentialDrive;
 
    public static WPI_TalonSRX intakeSubsystemUpperMotor;
    public static WPI_TalonSRX intakeSubsystemLowerMotor;
    public static WPI_TalonSRX intakeSubsystemMiddleMotor;

    public static WPI_TalonSRX conveyorBeltMotorTop;
    public static WPI_TalonSRX conveyorBeltMotorBottom;

    public static Counter lineSensorCounterL; 
    public static Counter lineSensorCounterR;
    public static DigitalInput lineSensorL;
    public static DigitalInput lineSensorR;

    public static CANSparkMax pivotSubsystemMax;
    public static CANEncoder pivotEncoder;
    public static DigitalInput pivotLimitSwitch;

    public static CANSparkMax liftSubsystemMax;
    public static CANEncoder liftEncoder;
    public static Solenoid liftSubsystemBrake;
    public static DigitalInput liftLimitSwitchTop;
    public static DigitalInput liftLimitSwitchBottom;

    public static Solenoid hatchSubsystemFinger;
    public static Solenoid hatchSubsystemPusher1;
    public static Solenoid hatchSubsystemPusher2;

    public static Spark lightSubsystemLightPWM;

    // no touchee!
    private static DigitalInput practiceBotJumper;

    static Logger logger = EventLogging.getLogger(RobotMap.class, Level.INFO);
    
    public static void init() {
        canDeviceFinder = new CANDeviceFinder();
        logger.info ("CANDEVICEfinder found {}", canDeviceFinder.getDeviceList());

        practiceBotJumper = new DigitalInput(0);

        SpeedControllerGroup groupLeft;
        SpeedControllerGroup groupRight;
        if(canDeviceFinder.isMAXPresent(1)) {
            CANSparkMax driveSubsystemMaxLeftA = new CANSparkMax(1, MotorType.kBrushless);
            resetMaxToKnownState(driveSubsystemMaxLeftA);
            leftsideCANEncoder = driveSubsystemMaxLeftA.getEncoder();

            CANSparkMax driveSubsystemMaxLeftB = new CANSparkMax(2, MotorType.kBrushless);
            resetMaxToKnownState(driveSubsystemMaxLeftB);

            CANSparkMax driveSubsystemMaxRightA = new CANSparkMax(3, MotorType.kBrushless);
            resetMaxToKnownState(driveSubsystemMaxRightA);
            rightsideCANEncoder = driveSubsystemMaxRightA.getEncoder();

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

        //new code
        conveyorBeltMotorTop = new WPI_TalonSRX(7);
        resetTalonToKnownState(conveyorBeltMotorTop);
        conveyorBeltMotorBottom = new WPI_TalonSRX(8);
        resetTalonToKnownState(conveyorBeltMotorBottom);

        intakeSubsystemUpperMotor = new WPI_TalonSRX(9);
        resetTalonToKnownState(intakeSubsystemUpperMotor);
        intakeSubsystemMiddleMotor = new WPI_TalonSRX(10);
        resetTalonToKnownState(intakeSubsystemMiddleMotor);
        intakeSubsystemLowerMotor = new WPI_TalonSRX(11);
        resetTalonToKnownState(intakeSubsystemLowerMotor);

        liftSubsystemMax = new CANSparkMax(6, MotorType.kBrushless);
        resetMaxToKnownState(liftSubsystemMax);
        liftSubsystemMax.setIdleMode(IdleMode.kBrake);
        liftEncoder = liftSubsystemMax.getEncoder();
        liftLimitSwitchTop = new DigitalInput(1);
        liftLimitSwitchBottom = new DigitalInput(2);

        pivotSubsystemMax = new CANSparkMax(5, MotorType.kBrushless);
        resetMaxToKnownState(pivotSubsystemMax);
        pivotSubsystemMax.setIdleMode(IdleMode.kBrake);
        pivotSubsystemMax.setRampRate(0.25);
        pivotEncoder = pivotSubsystemMax.getEncoder();
        pivotLimitSwitch = new DigitalInput(5);
        
        lightSubsystemLightPWM = new Spark(9);
        lightSubsystemLightPWM.setName("LightSubsystem", "LightPWM");
        lightSubsystemLightPWM.setInverted(false);
        
        //initiating line left sensor 
        lineSensorL = new DigitalInput(3);
        lineSensorCounterL = new Counter(lineSensorL);
        lineSensorCounterL.setUpSourceEdge(false, true);

        //initiating line right sensor 
        lineSensorR = new DigitalInput(4);
        lineSensorCounterR = new Counter(lineSensorR);
        lineSensorCounterR.setUpSourceEdge(false, true);

        if (canDeviceFinder.isPCMPresent(0)) {
            // instantiate Pneumatics here
            liftSubsystemBrake = new Solenoid(1);
            hatchSubsystemFinger = new Solenoid(2);
            hatchSubsystemPusher1 = new Solenoid(3);
            hatchSubsystemPusher2 = new Solenoid(4);
        }

    }

    public static boolean amICompBot(){
        if(practiceBotJumper.get() == true){
            return true;
        }
        return false;
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