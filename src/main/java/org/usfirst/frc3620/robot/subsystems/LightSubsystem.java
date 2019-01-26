package org.usfirst.frc3620.robot.subsystems;



import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.robot.RobotMap;
import org.usfirst.frc3620.misc.BlinkinDict;
import org.usfirst.frc3620.misc.BlinkinDict.Color;
import org.usfirst.frc3620.misc.RobotMode;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SpeedController;


/**
 *
 */
public class LightSubsystem extends Subsystem {
    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);

    //Checks
    Timer initTime = new Timer();
    DriverStation.Alliance teamColor = DriverStation.getInstance().getAlliance();

    /**
     * @author Nick Zimanski (SlippStream)
     * @version 1/26/19
     * 
     * Added autonomous and teleop functions
     */
    
    HashMap<Integer, Double> lightsPriority = new HashMap<Integer, Double>();
    /**
     * Hashmap stores the priority of lighting effects as an integer tied to the PWM power as a double
     * to add a light effect, use lightsPriority.put([PRIORITY -- 0 IS HIGHEST], Color.[COLOR].value)
     * to remove a light effect, use lightsPriority.remove([PRIORITY], Color.[COLOR].value)
     * 
     * Check BlinkinDict.java for more info on color names
     */ 
    
    private final SpeedController lightPWM = RobotMap.lightSubsystemLightPWM;

    public void modeChange (RobotMode newMode, RobotMode previousMode) {
        //sets the lights to a green by defalt when in anything other than disabled
        if (newMode != RobotMode.DISABLED) {lightsPriority.put(3, Color.DARK_GREEN.value);}

        if ((newMode == RobotMode.TELEOP || newMode == RobotMode.AUTONOMOUS) && previousMode == RobotMode.DISABLED) {
            //fires when robot is put initialized from diasabled
            initTime.reset();
            initTime.start();
            //checks alliance color and strobes it
            if (teamColor == Alliance.Red) {lightsPriority.put(0, Color.STROBE_TEAMCOLOR2.value);}
            else if (teamColor == Alliance.Blue) {lightsPriority.put(0, Color.STROBE_TEAMCOLOR1.value);}

            if (newMode == RobotMode.AUTONOMOUS) {
                //fires if the robot initialized into auto from disabled
                lightsPriority.put(1, Color.LARSONSCANNER_GRAY.value);
            }
            else if (newMode == RobotMode.TELEOP) {
                //fires if the robot initialized into teleop from disabled
                lightsPriority.remove(1, Color.LARSONSCANNER_GRAY.value);
            }
        }
    }

    @Override
    public void initDefaultCommand() {
        //Unused
    }

    @Override
    public void periodic() {
        
        //activates 1.5 seconds after initialization
        if (initTime.get() >= 1.5) {
    		lightsPriority.remove(0);
    		initTime.stop();
        }
        
        //constantly updates team color
        teamColor = DriverStation.getInstance().getAlliance();
    	
        //Checks priority ladder
    	if (lightsPriority.get(0) != null) {lightPWM.set(lightsPriority.get(0));}
    	else if (lightsPriority.get(1) != null) {lightPWM.set(lightsPriority.get(1));}
    	else if (lightsPriority.get(2) != null) {lightPWM.set(lightsPriority.get(2));}
    	else if (lightsPriority.get(3) != null) {lightPWM.set(lightsPriority.get(3));}
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

}
