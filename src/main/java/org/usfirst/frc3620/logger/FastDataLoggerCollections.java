package org.usfirst.frc3620.logger;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of FastDataLoggerBase that accumulates data
 * in an ArrayList, then writes it all out when all done. This way
 * the writing of the data will not delay processing while collecting.
 *
 * TODO
 *  Having writeData() start a Thread to do the writing would be an
 *  excellent enhancement.
 */
public class FastDataLoggerCollections extends FastDataLoggerBase {
    List<Object[]> data = new ArrayList<>();
    List<Double> timestamps = new ArrayList<>();

    @Override
    void logData(double timestamp, Object[] d) {
        timestamps.add(timestamp);
        data.add(d);
    }

    @Override
    void writeData(PrintWriter w) {
        for (int i = 0; i < data.size(); i++) {
            w.print(timestamps.get(i));
            w.print(",");
            w.format("%.6f", timestamps.get(i));
            Object[] row = data.get(i);
            for (int c = 0; c < row.length; c++) {
                w.print(",");
                w.print(row[c]);
            }
            w.println();
        }
    }

}
