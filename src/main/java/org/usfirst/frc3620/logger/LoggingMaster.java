package org.usfirst.frc3620.logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class LoggingMaster {
    private final static long SOME_TIME_AFTER_1970 = 523980000000L;
    private final static String DEFAULT_DEFAULT_LOG_LOCATION = "/home/lvuser/logs";

    // needs to be volatile: see
    // https://javarevisited.blogspot.com/2014/05/double-checked-locking-on-singleton-in-java.html
    private static volatile String _timestampString = null;
    private static volatile File _logDirectory = null;
    
    private static String defaultLogLocation = DEFAULT_DEFAULT_LOG_LOCATION;

    /**
     * Get the yyyyMMdd-HHmmss string for "right now". Return a null of the
     * system time has not been set yet.
     * <p/>
     * The thread-safety on this is not perfect, but's it has worked well enough
     * for years. wpilib is not highly threaded, so we're <i>probably</i> OK.
     *
     * See:
     * <a href="http://javarevisited.blogspot.com/2014/05/double-checked-locking-on-singleton-in-java.html">
     *     http://javarevisited.blogspot.com/2014/05/double-checked-locking-on-singleton-in-java.html
     *     </a>
     *
     * @return yyyyMMdd-HHmmss string, or null if system time not set.
     */
    public static String getTimestampString() {
        if (_timestampString == null) { // do a quick check (no overhead from
                                        // synchonized)
            synchronized (LoggingMaster.class) {
                if (_timestampString == null) { // Double checked
                    long now = System.currentTimeMillis();
                    
                    if (now > SOME_TIME_AFTER_1970) {
                        SimpleDateFormat formatName = new SimpleDateFormat(
                                "yyyyMMdd-HHmmss");
                        _timestampString = formatName.format(new Date());
                        String logMessage = String.format(
                                "timestamp for logs is %s\n", _timestampString);
                        EventLogging.writeWarningToDS(logMessage);
                    }
                }
            }
        }
        return _timestampString;
    }

    /**
     * Override the default log location (the directory that we put
     * output files into.
     *
     * TODO should the parameter for this be a File?
     *
     * @param s path of the new default log location (directory/folder)
     */
    public static void setDefaultLogLocation (String s) {
    	defaultLogLocation = s;
    }

    /**
     * return the directory that log files should be written to. Try to put
     * logs into an existing /logs directory of a flash drive if one is plugged
     * into the /logsRoboRIO,
     * otherwise use the default log location (whatever was set via
     * {@link #setDefaultLogLocation}, or "/home/lvuser/logs" if
     * setDefaultLogLocation was never called.
     * <p/>
     * The thread-safety on this is not perfect, but's it has worked well enough
     * for years. wpilib is not highly threaded, so we're <i>probably</i> OK.
     *
     * See:
     * <a href="http://javarevisited.blogspot.com/2014/05/double-checked-locking-on-singleton-in-java.html">
     *     http://javarevisited.blogspot.com/2014/05/double-checked-locking-on-singleton-in-java.html
     *     </a>
     *
     * @return directory to write logs into.
     */
    public static File getLoggingDirectory() {
        if (_logDirectory == null) { // quick check
            synchronized (LoggingMaster.class) {
                if (_logDirectory == null) {
                    // Set dataLogger and Time information
                    TimeZone.setDefault(
                            TimeZone.getTimeZone("America/Detroit"));

                    if (_logDirectory == null)
                        _logDirectory = searchForLogDirectory(new File("/u"));
                    if (_logDirectory == null)
                        _logDirectory = searchForLogDirectory(new File("/v"));
                    if (_logDirectory == null)
                        _logDirectory = searchForLogDirectory(new File("/x"));
                    if (_logDirectory == null)
                        _logDirectory = searchForLogDirectory(new File("/y"));
                    if (_logDirectory == null) {
                        _logDirectory = new File(defaultLogLocation);
                        if (!_logDirectory.exists()) {
                            _logDirectory.mkdir();
                        }
                    }
                    String logMessage = String.format("Log directory is %s\n",
                            _logDirectory);
                    System.out.print(logMessage);
                }
            }
        }
        return _logDirectory;
    }

    /**
     * Helper to search for existing logs directory on flash drives.
     * @param root path to flash drive
     * @return path to log directory if it exists on the flash drive, null
     * if not.
     */
    static private File searchForLogDirectory(File root) {
        // does the root directory exist?
        if (!root.isDirectory())
            return null;

        File logDirectory = new File(root, "logs");
        if (!logDirectory.isDirectory())
            return null;

        return logDirectory;
    }
}