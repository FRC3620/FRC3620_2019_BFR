package org.usfirst.frc3620.misc;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;

import edu.wpi.first.hal.can.CANJNI;

public class CANDeviceFinder {
    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);

    ArrayList<String> deviceList = new ArrayList<String>();

    private Set<Integer> pdps = new TreeSet<>();
    private Set<Integer> srxs = new TreeSet<>();
    private Set<Integer> spxs = new TreeSet<>();
    private Set<Integer> pcms = new TreeSet<>();
    private Set<Integer> maxs = new TreeSet<>();
    private set<Integer> spxs = new TeeSet<>();


    public CANDeviceFinder() {
        super();
        find();
    }

    public boolean isPDPPresent() {
        return pdps.contains(0);
    }

    public boolean isSRXPresent(int i) {
        return srxs.contains(i);
    }

    public boolean isSPXPresent(int i) {
        return spxs.contains(i);
    }

    public boolean isPCMPresent(int i) {
        return pcms.contains(i);
    }

    public boolean isMAXPresent(int i) {
        return maxs.contains(i);
    }
    
    public boolean isDevicePresent(String s) {
    	return deviceList.contains(s);
    }

    /**
     * 
     * @return ArrayList of strings holding the names of devices we've found.
     */
    public List<String> getDeviceList() {
        return deviceList;
    }

    abstract class CanFinder {
        int[] ids;
        long[] ts0;
        Set<Integer> idsPresent = new TreeSet<>();

        void pass1() {
            ts0 = new long[ids.length];
            for (int i = 0; i < ids.length; ++i) {
                ts0[i] = checkMessage(ids[i]);
                //logger.info ("pass1 looking for {} got {}", String.format("%08x", ids[i]), ts0[i]);
            }
        }

        void pass2() {
            long[] ts1 = new long[ids.length];
            for (int i = 0; i < ids.length; ++i) {
                ts1[i] = checkMessage(ids[i]);
                //logger.info ("pass2 looking for {} got {}", String.format("%08x", ids[i]), ts1[i]);
            }
            for (int i = 0; i < ids.length; ++i) {
                if (ts0[i] >= 0 && ts1[i] >= 0 && ts0[i] != ts1[i]) {
                    // logger.info ("found {}", String.format("%08x", ids[i]));
                    idsPresent.add(ids[i]);
                }
            }
        }

        abstract void report();
    }

    class DeviceFinder extends CanFinder {
        Set<Integer> deviceIds;
        List<String> deviceList;
        String deviceListPrefix;
        DeviceFinder(int devType, int mfg, int apiId, int maxDevices, Set<Integer> deviceIdSet, List<String> deviceList, String deviceListPrefix) {
            super();

            this.deviceIds = deviceIdSet;
            this.deviceList = deviceList;
            this.deviceListPrefix = deviceListPrefix;

            ids = new int[maxDevices];
            for (int i = 0; i < maxDevices; i++) {
                ids[i] = canBusId(devType, mfg, apiId, i);
            }
        }

        @Override
        void report() {
            for (int id: idsPresent) {
                int deviceId = extractDeviceId(id);
                deviceIds.add(deviceId);
                deviceList.add(deviceListPrefix + " " + Integer.toString(deviceId));
            }
        }
    }

    class APIFinder extends CanFinder {
        String label;
        APIFinder(String label, int devType, int mfg, int deviceId) {
            super();
            this.label = label;

            ids = new int[1024];
            for (int i = 0; i < 1024; i++) {
                ids[i] = canBusId(devType, mfg, i, deviceId);
            }
        }

        @Override
        void report() {
            for (int id: idsPresent) {
                int apiId = extractApiId(id);
                logger.info ("{}: API {} {} = msg {}", label, String.format("%2d", apiId), String.format("0x%03X", apiId), String.format("%08X", id));
            }
        }
    }
    
    /**
     * polls for received framing to determine if a device is present. This is
     * meant to be used once initially (and not periodically) since this steals
     * cached messages from the robot API.
     */
    public void find() {
        logger.info ("calling find()");
        deviceList.clear();
        pdps.clear();
        maxs.clear();
        spxs.clear();
        srxs.clear();
        pcms.clear();

        List<CanFinder> finders = new ArrayList<>();

        /*
         * PDPs used to be 0x08041400.
         * 2019.02.09: PDPs respond to APIs 0x50 0x51 0x52 0x59 0x5d
         */
        finders.add(new DeviceFinder(8, 4, extractApiId(0x08041400), 1, pdps, deviceList, "PDP"));

        /*
         * SRX used to be 0x02041400.

        As of 2019.02.08: (SRX @ devid 1)
         7 0x007 = 020401C1
        81 0x051 = 02041441 ** using this?
        82 0x052 = 02041481
        83 0x053 = 020414C1
        87 0x057 = 020415C1
        91 0x05B = 020416C1
        92 0x05C = 02041701
        93 0x05D = 02041741
        94 0x05E = 02041781
        */
        finders.add(new DeviceFinder(2, 4, extractApiId(0x02041441), 64, srxs, deviceList, "SRX"));

        /*
        SPX used to be 0x01041400.

        As of 2019.02.08:  (SPX @ devid 2)
         7 0x007 = 010401C2
        81 0x051 = 01041442 ** using this
        83 0x053 = 010414C2
        91 0x05B = 010416C2
        92 0x05C = 01041702
        93 0x05D = 01041742
        94 0x05E = 01041782
        */
        finders.add(new DeviceFinder(1, 4, extractApiId(0x01041442), 64, spxs, deviceList, "SPX"));

        /* we always used 0x09041400 for PCMs */
        finders.add(new DeviceFinder(9, 4, extractApiId(0x09041400), 64, pcms, deviceList, "PCM"));

        // per REV (0x02051800)
        finders.add(new DeviceFinder(2, 5, extractApiId(0x02051800), 64, maxs, deviceList, "MAX"));

        // do research
        /*
        finders.add(new APIFinder("PDP", 8, 4, 0)); // PDP
        finders.add(new APIFinder("SRX", 2, 4, 1)); // SRX #1
        finders.add(new APIFinder("SPX", 1, 4, 2)); // SPX #2
        */

        for (CanFinder finder: finders) {
            finder.pass1();
        }

        /* wait 200ms */
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (CanFinder finder: finders) {
            finder.pass2();
        }

        for (CanFinder finder: finders) {
            finder.report();
        }
    }

    /* help to calculate the CAN bus ID for a devType|mfg|api|dev.
    total of 32 bits: 8 bit devType, 8 bit mfg, 10 bit API, 6 bit device id.
    */
    boolean logCanBusIds = false;
    private int canBusId (int devType, int mfg, int apiId, int devId) {
        // TODO bounds check parameters
        int rv = ((devType & 0xff) << 24) |
          ((mfg & 0xff) << 16 ) |
          ((apiId & 0x3ff) << 6) |
          (devId & 0x3f);
        if (logCanBusIds) {
          logger.info("devType {}, mfg {}, api {}, devId {} -> {}",
            String.format ("0x%02X", devType),
            String.format ("0x%02X", mfg),
            String.format ("0x%03X", apiId),
            String.format ("0x%02X", devId),
            String.format ("0x%08X", rv)
          );
        }
        return rv;
    }

    private int extractDeviceId (int canId) {
        return canId & 0x3f;
    }

    private int extractApiId (int canId) {
        return (canId & 0xffc0) >> 6;
    }

    /** helper routine to get last received message for a given ID */
    private ByteBuffer targetID = ByteBuffer.allocateDirect(4);
    private ByteBuffer timeStamp = ByteBuffer.allocateDirect(4);
    private long checkMessage(int id) {
        try {
            targetID.clear();
            targetID.order(ByteOrder.LITTLE_ENDIAN);
            targetID.asIntBuffer().put(0, id);

            timeStamp.clear();
            timeStamp.order(ByteOrder.LITTLE_ENDIAN);
            timeStamp.asIntBuffer().put(0, 0x00000000);

            CANJNI.FRCNetCommCANSessionMuxReceiveMessage(
                    targetID.asIntBuffer(), 0x1fffffff, timeStamp);

            long retval = timeStamp.getInt();
            retval &= 0xFFFFFFFF; /* undo sign-extension */
            return retval;
        } catch (Exception e) {
            return -1;
        }
    }
}