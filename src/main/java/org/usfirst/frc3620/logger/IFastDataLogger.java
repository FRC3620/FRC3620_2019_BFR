package org.usfirst.frc3620.logger;

/**
 * An IDataLogger that doesn't have to run forever.
 */
public interface IFastDataLogger extends IDataLogger {
    /**
     * how to long to run. If null, then run forever!
     * @param seconds
     */
    public void setMaxLength(Double seconds);

    /**
     * call this to finish up data collection
     */
    public void done();

    /**
     * this is called to determine if collection is done
     * @return
     */
    public boolean isDone();
}
