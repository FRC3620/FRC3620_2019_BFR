package org.usfirst.frc3620.robot.subsystems;



import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.robot.RobotMap;
import org.usfirst.frc3620.misc.BlinkinDict.Color;
import org.usfirst.frc3620.misc.RobotMode;
import org.usfirst.frc3620.misc.ColorTimers;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.io.Console;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SpeedController;


/**
 * @author Nick Zimanski (SlippStream)
 * @version 2/02/19
 * 
 * Added autonomous and teleop functions
 */
public class LightSubsystem extends Subsystem {

    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
    Timer initTime = new Timer();
    Boolean afterInitialized = false;
    ColorTimers colorToRemove = null;

    ArrayList<ColorTimers> colorTimers = new ArrayList<ColorTimers>();
    DriverStation.Alliance teamColor = DriverStation.getInstance().getAlliance();
    /**
     * @author Nick Zimanski (SlippStream)
     * @version 2/01/19
     * 
     * Added autonomous and teleop functions
     */
    
    /**
     * @see Hashmap stores the priority of lighting effects as an integer tied to the PWM power as a double
     * to add a light effect, use lightsPriority.put([PRIORITY -- 0 IS HIGHEST], Color.[COLOR].value)
     * to remove a light effect, use lightsPriority.remove([PRIORITY], Color.[COLOR].value)
     * @see Check BlinkinDict.java for more info on color names
     */
    HashMap<Integer, Double> lightsPriority = new HashMap<Integer, Double>();
    
    private final SpeedController lightPWM = RobotMap.lightSubsystemLightPWM;

    public void modeChange (RobotMode newMode, RobotMode previousMode) {
        //sets the lights to a green by defalt when in anything other than disabled
        if (newMode != RobotMode.DISABLED) {lightsPriority.put(4, Color.DARK_GREEN.value);}

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

        //fires when robot gets disabled
        if (newMode == RobotMode.DISABLED) {
            finished();
        }
    }

    /**
     * @param priority An integer between 0 and 5 that indicates the effect's priority (0 is highest)
     * @param color A blinkinDict color invoked with Color.[COLOR]
     * @param duration A double in seconds for how long you want the color to be displayed
     * @purpose sets an effect to be removed after a certain amount of time
     * @see this method is standalone
     * @see import org.usfirst.frc3620.misc.BlinkinDict.Color;
     */
    public void setEffect(Integer priority, Color color, Double duration) {
        colorTimers.add(new ColorTimers(priority, color, duration, initTime.get()));
        lightsPriority.putIfAbsent(priority, color.value);
        logger.info("Effect Set!");
    }

    /**
     * @param priority An integer between 0 and 5 that indicates the effect's priority (0 is highest)
     * @param color A blinkinDict color invoked with Color.[COLOR]
     * @purpose sets an effect with the assumption that it will be cleared
     * @see for use ONLY with: clearEffect(Integer priority, Color color)
     * @see import org.usfirst.frc3620.misc.BlinkinDict.Color
     */
    public void setEffect(Integer priority, Color color) {
        lightsPriority.putIfAbsent(priority, color.value);
    }

    /**
     * @param priority The priority passed to setEffect
     * @param color The blinkinDict color invoked for setEffect
     * @purpose clears an effect set with setEffect
     * @see for use ONLY with: setEffect(Integer priority, Color color)
     * @see import org.usfirst.frc3620.misc.BlinkinDict.Color
     */
    public void clearEffect(Integer priority, Color color) {
        if (lightsPriority.containsValue(color.value)) {
        lightsPriority.remove(priority, color.value);
        logger.info("Effect Removed!");
        }
    }

    @Override
    public void initDefaultCommand() {
        //Unused
    }

    @Override
    public void periodic() {
        
        //activates 1.5 seconds after initialization
        if (initTime.get() >= 1.5 && !afterInitialized) {
            lightsPriority.remove(0);
            afterInitialized = true;
        }
        
        //constantly updates team color
        teamColor = DriverStation.getInstance().getAlliance();

        //Checks set effects and removes finished effects
        for (ColorTimers effect : colorTimers) {
            final Integer priority = effect.getPriority();
            final Color color = effect.getColor();
            final Double duration = effect.getDuration();
            final Double startTime = effect.getStartTime();

            if (initTime.get() >= startTime + duration) {
                lightsPriority.remove(priority, color.value);
                colorToRemove = effect;
            }
        }
        if (colorToRemove != null) {
            colorTimers.remove(colorToRemove);
            colorToRemove = null;
        }


    	
        /**
         * Checks priority ladder.
         * Goes through all current light effects, displaying the one 
         * with the highest priority (0 is highest)
         */ 
    	if (lightsPriority.get(0) != null) {lightPWM.set(lightsPriority.get(0));}
    	else if (lightsPriority.get(1) != null) {lightPWM.set(lightsPriority.get(1));}
    	else if (lightsPriority.get(2) != null) {lightPWM.set(lightsPriority.get(2));}
        else if (lightsPriority.get(3) != null) {lightPWM.set(lightsPriority.get(3));}
        else if (lightsPriority.get(4) != null) {lightPWM.set(lightsPriority.get(4));}
        else if (lightsPriority.get(5) != null) {lightPWM.set(lightsPriority.get(5));}
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void finished() {
        lightsPriority.remove(0);
        lightsPriority.remove(1);
        lightsPriority.remove(2);
        lightsPriority.remove(3);
        lightsPriority.remove(4);
        lightsPriority.remove(5);
    }

}
