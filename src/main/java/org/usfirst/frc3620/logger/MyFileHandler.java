package org.usfirst.frc3620.logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

/**
 * A {@link java.util.logging.StreamHandler} that writes to a local file.
 * Very similar to the {@link java.util.logging.FileHandler}, except this
 * take care of file naming itself, and it won't log anything until the
 * system time has been set (since the name is derived from the system
 * time.
 */
public class MyFileHandler extends StreamHandler {
    File logDirectory;
    volatile FileOutputStream fileOutputStream = null;

    public MyFileHandler(File logDirectory) {
        super();
        this.logDirectory = logDirectory;
    }

    @Override
    public void publish(LogRecord arg0) {
        /*
         * if the file is not open, see if we can open it.
         */
        if (fileOutputStream == null) // quick check
        {
            synchronized (this) // take some overhead for the good check
            {
                if (fileOutputStream == null) // deliberate check
                {
                    // we'll get a null here if the clock is not yet set
                    String timestampString = LoggingMaster
                            .getTimestampString();

                    if (timestampString != null) {
                        // cool, let's make a file to log to!
                        File logFile = new File(logDirectory,
                                timestampString + ".log");
                        try {
                            fileOutputStream = new FileOutputStream(
                                    logFile);
                            setOutputStream(fileOutputStream);
                        } catch (IOException ex) {
                            ex.printStackTrace(System.err);
                        }

                    }
                }
            }
        }

        // only log if we have a place to log to
        if (fileOutputStream != null) {
            super.publish(arg0);
            flush();
        }
    }

}


