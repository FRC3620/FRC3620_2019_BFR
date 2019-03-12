package org.usfirst.frc3620.logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import edu.wpi.first.wpilibj.DriverStation;

public class EventLogging {
    private static boolean setupDone = false;
    private static boolean useDriverStation = true;

    /**
     * make some levels that correspond to the different SLF4J logging methods.
     * Have the mappings to the underlying j.u.l logging levels.
     */
    public enum Level {
        TRACE(java.util.logging.Level.FINEST), //
        DEBUG(java.util.logging.Level.FINE), //
        INFO(java.util.logging.Level.INFO), //
        WARN(java.util.logging.Level.WARNING), //
        ERROR(java.util.logging.Level.SEVERE);

        java.util.logging.Level julLevel;

        Level(java.util.logging.Level _julLevel) {
            julLevel = _julLevel;
        }
    }

    /**
     * Get an SLF4J logger for a class. Set the underlying j.u.l logger to the
     * desired level.
     *
     * @param clazz class for the logger
     * @param l     {@link Level} that we want to log at
     * @return {@link org.slf4j.Logger}
     */
    static public org.slf4j.Logger getLogger(Class<?> clazz, Level l) {
        return getLogger(clazz.getName(), l);
    }

    /**
     * Get an SLF4J logger for a name. Set the underlying j.u.l logger to the
     * desired level.
     *
     * @param sClazz name for the logger
     * @param l      Level that we want to log at
     * @return
     */
    static public org.slf4j.Logger getLogger(String sClazz, Level l) {
        setup();
        // set up the underlying logger to log at the level we want
        java.util.logging.Logger julLogger = java.util.logging.Logger
                .getLogger(sClazz);
        julLogger.setLevel(l.julLevel);

        // and return the SLF4J logger.
        org.slf4j.Logger rv = org.slf4j.LoggerFactory.getLogger(sClazz);
        return rv;
    }

    /**
     * Log command starts and stops. It looks at the traceback stack to see the
     * name of the calling method and logs that.
     *
     * @param logger logger to log to.
     */
    public static void commandMessage(org.slf4j.Logger logger) {
        Throwable t = new Throwable();
        StackTraceElement[] stackTraceElement = t.getStackTrace();
        logger.info("command {}", stackTraceElement[1].getMethodName());
    }


    /**
     * Write a warning message to the DriverStation.
     *
     * @param message Message to log.
     */
    public static final void writeWarningToDS(String message) {
        if (DriverStation.getInstance().isDSAttached()) {
            DriverStation.reportWarning(message, false);
        }
    }

    /**
     * Create a String representation of an Throwable.
     *
     * @param t the Throwable to be printed
     * @return a String representation of the Throwable. Very similar to what
     * goes into a traceback.
     */
    public static String exceptionToString(Throwable t) {
        final StackTraceElement[] stackTrace = t.getStackTrace();
        final StringBuilder message = new StringBuilder();
        final String separator = "===\n";
        final Throwable cause = t.getCause();

        message.append("Exception of type ").append(t.getClass().getName())
                .append('\n');
        message.append("Message: ").append(t.getMessage()).append('\n');
        message.append(separator);
        message.append("   ").append(stackTrace[0]).append('\n');

        for (int i = 1; i < stackTrace.length; i++) {
            message.append(" \t").append(stackTrace[i]).append('\n');
        }

        if (cause != null) {
            final StackTraceElement[] causeTrace = cause.getStackTrace();
            message.append(" \t\t").append("Caused by ")
                    .append(cause.getClass().getName()).append('\n');
            message.append(" \t\t").append("Because: ")
                    .append(cause.getMessage()).append('\n');
            message.append(" \t\t   ").append(causeTrace[0]).append('\n');
            message.append(" \t\t \t").append(causeTrace[2]).append('\n');
            message.append(" \t\t \t").append(causeTrace[3]);
        }

        return message.toString();
    }

    /**
     * Set up a j.u.l logger that will start logging to a file (with a
     * timestamped name) in the default logging directory specified in @{link
     * LoggingMaster}. The logging to the file will not start until the system
     * time is set.
     */
    public static void setup() {
        setup(LoggingMaster.getLoggingDirectory());
    }

    /**
     * Set up a j.u.l logger that will start logging to a file (with a
     * timestamped name) in the specified directory. The logging to the file
     * will not start until the system time is set.
     *
     * @param logDirectory Directory to create the logging file in
     */
    public static void setup(File logDirectory) {
        if (!setupDone) // quickly check to see if we are initialized
        {
            synchronized (EventLogging.class) // check slowly and carefully
            {
                if (!setupDone) {
                    Logger rootLogger = Logger.getLogger("");
                    // get all the existing handlers
                    Handler[] handlers = rootLogger.getHandlers();
                    // and remove them
                    for (Handler handler : handlers) {
                        rootLogger.removeHandler(handler);
                    }

                    // add the handlers we want
                    Handler h;
                    if (useDriverStation) {
                        h = new DriverStationLoggingHandler();
                        h.setLevel(Level.DEBUG.julLevel);
                        rootLogger.addHandler(h);
                    } else {
                        h = new ConsoleHandler();
                        h.setFormatter(new FormatterForFileHandler());
                        h.setLevel(Level.DEBUG.julLevel);
                        rootLogger.addHandler(h);
                    }

                    h = new MyFileHandler(logDirectory);
                    h.setFormatter(new FormatterForFileHandler());
                    h.setLevel(Level.DEBUG.julLevel);
                    rootLogger.addHandler(h);

                    setupDone = true;
                }
            }
        }
    }

    static class FormatterForFileHandler extends java.util.logging.Formatter {
        //
        // Create a DateFormat to format the logger timestamp.
        //
        private final DateFormat df = new SimpleDateFormat(
                "yyyy/MM/dd hh:mm:ss.SSS");

        public String format(LogRecord record) {
            StringBuilder builder = new StringBuilder(1000);
            // DateFormat objects are not thread-safe....
            synchronized (df) {
                builder.append(df.format(new Date(record.getMillis())))
                        .append(" ");
            }
            builder.append("[").append(record.getLoggerName()).append("] ");
            builder.append(record.getLevel()).append(" - ");
            builder.append(formatMessage(record));
            builder.append("\n");
            return builder.toString();
        }

        public String getHead(Handler h) {
            return super.getHead(h);
        }

        public String getTail(Handler h) {
            return super.getTail(h);
        }
    }

}
