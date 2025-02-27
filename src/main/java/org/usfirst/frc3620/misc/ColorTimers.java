package org.usfirst.frc3620.misc;

import org.usfirst.frc3620.misc.BlinkinDict.Color;

/**
 * @author Nick Zimanski (SlippStream)
 * @version 2/06/19
 * 
 * New object for dealing with light effects and their durations
 */
public class ColorTimers {
    private Integer priority;
    private Color color;
    private Double duration, startTime;

    public ColorTimers(Integer priority, Color color, Double duration, Double startTime) {
        this.priority = priority;
        this.color = color;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Double getDuration() {
        return this.duration;
    }

    public Double getStartTime() {
        return this.startTime;
    }
    public Integer getPriority() {
        return this.priority;
    }

    public Color getColor() {
        return this.color;
    }
}