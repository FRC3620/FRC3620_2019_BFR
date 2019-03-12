package org.usfirst.frc3620.logger;

import java.io.File;

/**
 * An interface for any data logger. A data logger is used to collect
 * numeric data at intervals while it's running. This data that (hopefully)
 * will be readable by analysis or graphing tools.
 */
public interface IDataLogger {
    /**
     * Define where we will be putting the output file.
     *
     * @param loggingDirectory a File object to the directory we will put logs into.
     */
    public void setLoggingDirectory(File loggingDirectory);

    /**
     * Set the filename <u>prefix</u> of the output file.
     * @param filename file name prefix.
     */
    public void setFilename(String filename);

    /**
     * Set how often a data point should be collected.
     * @param seconds
     */
    public void setInterval(double seconds);

    /**
     * Add a data provider to the data logger. This is called for every column you want
     * to have show up in the output file.
     * @param name name of the data item
     * @param iDataLoggerDataProvider A {@link IDataLoggerDataProvider} that can be called to get the value of the data item.
     */
    public void addDataProvider(String name, 
            IDataLoggerDataProvider iDataLoggerDataProvider);

    /**
     * Add a name/value pair to the file. This shows up just once, and should be done before {@link start()}.
     * @param s
     * @param d
     */
    public void addMetadata(String s, double d);

    /**
     * Start the data collection activity.
     * @return The path of the output file that will be written.
     */
    public String start();

}
