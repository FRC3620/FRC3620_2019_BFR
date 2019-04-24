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

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.misc.CANDeviceFinder;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import org.usfirst.frc3620.misc.CANDeviceId;
import org.usfirst.frc3620.misc.CANDeviceId.CANDeviceType;

import java.util.*;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */

 public class RobotMap {
    private static NetworkTableInstance inst = NetworkTableInstance.getDefault();
    private static NetworkTable networkTable = inst.getTable("ChickenVision");
    private static NetworkTableEntry missingHardwareTableEntry = networkTable.getEntry("Missing hardware");

    public static Encoder leftSideEncoder, rightSideEncoder;
    public static CANEncoder leftsideCANEncoder, rightsideCANEncoder;
    public static CANSparkMax driveSubsystemMaxLeftA;
    public static CANSparkMax driveSubsystemMaxLeftB;
    public static CANSparkMax driveSubsystemMaxRightA;
    public static CANSparkMax driveSubsystemMaxRightB;
    public static CANDeviceFinder canDeviceFinder;

    public static DifferentialDrive driveSubsystemDifferentialDrive;

    public static Accelerometer accel;

    public static Relay visionSubsystemNightLight;

    public static WPI_TalonSRX intakeSubsystemUpperMotor;
    public static WPI_TalonSRX intakeSubsystemLowerMotor;
    public static WPI_TalonSRX intakeSubsystemMiddleMotor;

    public static WPI_VictorSPX conveyorBeltMotorTop;
    public static WPI_VictorSPX conveyorBeltMotorBottom;
    public static WPI_VictorSPX habDriveMotor;

    public static Counter lineSensorCounterL; 
    public static Counter lineSensorCounterR;
    public static DigitalInput lineSensorL;
    public static DigitalInput lineSensorR;

    public static CANSparkMax pivotSubsystemMax;
    public static CANSparkMax pivotSubsystemMax2;
    public static CANEncoder pivotEncoder;
    public static DigitalInput pivotLimitSwitch;

    public static CANSparkMax liftSubsystemMax;
    public static CANEncoder liftEncoder;
    public static DigitalInput liftLimitSwitchTop;
    public static DigitalInput liftLimitSwitchBottom;
    public static Solenoid liftLockPinSolenoid;

    public static CANSparkMax hatchSubsystemMax;
    public static Solenoid hatchSubsystemFlipper;
    public static Compressor c;
    public static DigitalInput hatchLimitSwitch;

    public static Spark lightSubsystemLightPWM;

    // no touchee!
    private static DigitalInput practiceBotJumper;

    static Logger logger = EventLogging.getLogger(RobotMap.class, Level.INFO);

    private static Map<CANDeviceId, String> requiredDevices = new TreeMap<>();
    
    public static void init() {
        canDeviceFinder = new CANDeviceFinder();
        logger.info ("CANDEVICEfinder found {}", canDeviceFinder.getDeviceSet());

        practiceBotJumper = new DigitalInput(0);

        accel = new BuiltInAccelerometer();

        SpeedControllerGroup groupLeft = null;
        SpeedControllerGroup groupRight = null;

        requiredDevices.put(new CANDeviceId(CANDeviceType.MAX, 1), "DriveLeftSideA");
        requiredDevices.put(new CANDeviceId(CANDeviceType.MAX, 2), "DriveLeftSideB");
        requiredDevices.put(new CANDeviceId(CANDeviceType.MAX, 3), "DriveRightSideA");
        requiredDevices.put(new CANDeviceId(CANDeviceType.MAX, 4), "DriveRightSideB");
        if (amICompBot() || canDeviceFinder.isDevicePresent(CANDeviceType.MAX, 1)) {
            driveSubsystemMaxLeftA = new CANSparkMax(1, MotorType.kBrushless);
            resetMaxToKnownState(driveSubsystemMaxLeftA);
            fixupDriveMax(driveSubsystemMaxLeftA);
            leftsideCANEncoder = driveSubsystemMaxLeftA.getEncoder();

            driveSubsystemMaxLeftB = new CANSparkMax(2, MotorType.kBrushless);
            resetMaxToKnownState(driveSubsystemMaxLeftB);
            fixupDriveMax(driveSubsystemMaxLeftB);

            driveSubsystemMaxRightA = new CANSparkMax(3, MotorType.kBrushless);
            resetMaxToKnownState(driveSubsystemMaxRightA);
            fixupDriveMax(driveSubsystemMaxRightA);
            rightsideCANEncoder = driveSubsystemMaxRightA.getEncoder();

            driveSubsystemMaxRightB = new CANSparkMax(4, MotorType.kBrushless);
            resetMaxToKnownState(driveSubsystemMaxRightB);
            fixupDriveMax(driveSubsystemMaxRightB);

            groupLeft = new SpeedControllerGroup(driveSubsystemMaxLeftA, driveSubsystemMaxLeftB);
            groupRight = new SpeedControllerGroup(driveSubsystemMaxRightA, driveSubsystemMaxRightB);

        } else if (canDeviceFinder.isDevicePresent(CANDeviceType.SRX, 1)) {

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

            leftSideEncoder = new Encoder(6,7, true, EncodingType.k4X);
            rightSideEncoder = new Encoder(8,9, true, EncodingType.k4X);

            groupLeft = new SpeedControllerGroup(driveSubsystemLeftSpeedControllerA, driveSubsystemLeftSpeedControllerB, driveSubsystemLeftSpeedControllerC);
            groupRight = new SpeedControllerGroup(driveSubsystemRightSpeedControllerA, driveSubsystemRightSpeedControllerB, driveSubsystemRightSpeedControllerC);
        } else {
            logger.warn ("paraplegic robot");
        }

        if (groupLeft != null) {
            driveSubsystemDifferentialDrive = new DifferentialDrive(groupLeft, groupRight);
            driveSubsystemDifferentialDrive.setName("DriveSubsystem", "Drive");
            driveSubsystemDifferentialDrive.setSafetyEnabled(true);
            driveSubsystemDifferentialDrive.setExpiration(0.1);
            driveSubsystemDifferentialDrive.setMaxOutput(1.0);
        }
        
        requiredDevices.put(new CANDeviceId(CANDeviceType.SPX, 7), "ConveyorBeltTop");
        conveyorBeltMotorTop = new WPI_VictorSPX(7);
        resetTalonToKnownState(conveyorBeltMotorTop);

        requiredDevices.put(new CANDeviceId(CANDeviceType.SPX, 8), "ConveyorBeltBottom");
        conveyorBeltMotorBottom = new WPI_VictorSPX(8);
        resetTalonToKnownState(conveyorBeltMotorBottom);

        requiredDevices.put(new CANDeviceId(CANDeviceType.SRX, 9), "IntakeUpper");
        intakeSubsystemUpperMotor = new WPI_TalonSRX(9);
        resetTalonToKnownState(intakeSubsystemUpperMotor);

        requiredDevices.put(new CANDeviceId(CANDeviceType.SRX, 10), "IntakeMiddle");
        intakeSubsystemMiddleMotor = new WPI_TalonSRX(10);
        resetTalonToKnownState(intakeSubsystemMiddleMotor);

        requiredDevices.put(new CANDeviceId(CANDeviceType.SRX, 11), "IntakeLower");
        intakeSubsystemLowerMotor = new WPI_TalonSRX(11);
        resetTalonToKnownState(intakeSubsystemLowerMotor);

        requiredDevices.put(new CANDeviceId(CANDeviceType.SPX, 13), "HabDrive");
        habDriveMotor = new WPI_VictorSPX(13);
        resetTalonToKnownState(habDriveMotor);
        
        visionSubsystemNightLight = new Relay(0);

        // lift motor power is positive going up
        requiredDevices.put(new CANDeviceId(CANDeviceType.MAX, 6), "LiftMax");
        liftSubsystemMax = new CANSparkMax(6, MotorType.kBrushless);
        resetMaxToKnownState(liftSubsystemMax);
        liftSubsystemMax.setOpenLoopRampRate(0.1);
        liftSubsystemMax.setSmartCurrentLimit(80);

        liftEncoder = liftSubsystemMax.getEncoder();
        liftLimitSwitchTop = new DigitalInput(1);
        liftLimitSwitchBottom = new DigitalInput(2);

        // pivot motor power is negitive when coming down
        requiredDevices.put(new CANDeviceId(CANDeviceType.MAX, 5), "PivotMAX");
        pivotSubsystemMax = new CANSparkMax(5, MotorType.kBrushless);
        resetMaxToKnownState(pivotSubsystemMax);
        pivotSubsystemMax.setOpenLoopRampRate(0.25);
        pivotSubsystemMax.setClosedLoopRampRate(0.25);

        pivotEncoder = pivotSubsystemMax.getEncoder();

        requiredDevices.put(new CANDeviceId(CANDeviceType.MAX, 12), "PivotMAX2");
        pivotSubsystemMax2 = new CANSparkMax(12, MotorType.kBrushless);
        resetMaxToKnownState(pivotSubsystemMax2);
        //second pivot motor is following the first 
        // but the inverse is set to true because of how they are mounted
        pivotSubsystemMax2.follow(pivotSubsystemMax, true);
        pivotSubsystemMax2.setOpenLoopRampRate(0.25);
        pivotSubsystemMax2.setClosedLoopRampRate(0.25);

        pivotLimitSwitch = new DigitalInput(5);

        requiredDevices.put(new CANDeviceId(CANDeviceType.MAX, 14), "HatchGrabberMAX");
        hatchSubsystemMax = new CANSparkMax(14, MotorType.kBrushless);
        resetMaxToKnownState(hatchSubsystemMax);

        hatchLimitSwitch = new DigitalInput(6);

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

        requiredDevices.put(new CANDeviceId(CANDeviceType.PCM, 0), "BottomPCM");
        if (amICompBot() || canDeviceFinder.isDevicePresent(CANDeviceType.PCM, 0)) {
            //instantiate Pneumatics here
            //doublesolenoids requires a PCM number first
            c = new Compressor(0);
            liftLockPinSolenoid = new Solenoid(0);
            hatchSubsystemFlipper = new Solenoid(1);
        }
    }

    public static boolean amICompBot(){
        if(practiceBotJumper.get() == true){
            return true;
        }
        return false;
    }

    public static void reportMissingDevices() {
        Set<CANDeviceId> requiredDeviceIds = requiredDevices.keySet();
        Set<CANDeviceId> missingDeviceIds = new TreeSet<>(requiredDeviceIds);
        missingDeviceIds.removeAll(canDeviceFinder.getDeviceSet());
        if (missingDeviceIds.size() > 0) {
            logger.error ("missing CAN bus devices: {}", missingDeviceIds);
            StringBuilder networkOutput = new StringBuilder();
            for (CANDeviceId canDeviceId : missingDeviceIds) {
                logger.info ("{} is {}", canDeviceId, requiredDevices.get(canDeviceId));
                networkOutput.append (canDeviceId.toString());
                networkOutput.append (" is ");
                networkOutput.append (requiredDevices.get(canDeviceId));
                networkOutput.append (", \n");
            }
            missingHardwareTableEntry.setString(networkOutput.toString());
        } else {
            missingHardwareTableEntry.setString("No missing hardware");
        }


    }

    static void resetMaxToKnownState (CANSparkMax x) {
		x.setInverted(false);
        x.setIdleMode(IdleMode.kBrake);
        x.setOpenLoopRampRate(1);
        x.setClosedLoopRampRate(1);
        x.setSmartCurrentLimit(50);
        //x.setSecondaryCurrentLimit(100, 0);
    }

    static void fixupDriveMax (CANSparkMax x) {
        x.setOpenLoopRampRate(0.6);
        x.setIdleMode(IdleMode.kCoast);
        x.setSmartCurrentLimit(65);
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