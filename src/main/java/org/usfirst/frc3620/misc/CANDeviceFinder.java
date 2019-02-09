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

    private boolean pdpIsPresent = false;
    private Set<Integer> srxs = new TreeSet<>();
    private Set<Integer> spxs = new TreeSet<>();
    private Set<Integer> pcms = new TreeSet<>();
    private Set<Integer> maxs = new TreeSet<>();

    public CANDeviceFinder() {
        super();
        find();
    }

    public boolean isPDPPresent() {
        return pdpIsPresent;
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
    
    /**
     * polls for received framing to determine if a device is present. This is
     * meant to be used once initially (and not periodically) since this steals
     * cached messages from the robot API.
     */
    public void find() {
        deviceList.clear();
        pdpIsPresent = false;
        maxs.clear();
        spxs.clear();
        srxs.clear();
        pcms.clear();

        /* get timestamp0 for each device */
        long pdp0_timeStamp0; // only look for PDP at '0'
        long[] pcm_timeStamp0 = new long[63];
        long[] srx_timeStamp0 = new long[63];
        long[] spx_timeStamp0 = new long[63];
        long[] max_timeStamp0 = new long[63];

        /*
         * 2019.02.09: PDPs respond to APIs 0x50 0x51 0x52 
         */
        pdp0_timeStamp0 = checkMessage(8, 4, 0x50, 0);

        for (int i = 0; i < 63; ++i) {
            pcm_timeStamp0[i] = checkMessage(8, 4, 0x50, i);

            /*
            SRX used to respond to API 80 0x50.

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
            srx_timeStamp0[i] = checkMessage(2, 4, 0x51, i);

            /*
            SPX used to respond to API 80 0x50.

            As of 2019.02.08:  (SPX @ devid 2)
             7 0x007 = 010401C2
            81 0x051 = 01041442 ** using this
            83 0x053 = 010414C2
            91 0x05B = 010416C2
            92 0x05C = 01041702
            93 0x05D = 01041742
            94 0x05E = 01041782
            */
            spx_timeStamp0[i] = checkMessage(1, 4, 0x51, i);

            // per REV
            max_timeStamp0[i] = checkMessage(2, 5, 0x60, i);
        }

        /* wait 200ms */
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /* get timestamp1 for each device */
        long pdp0_timeStamp1; // only look for PDP at '0'
        long[] pcm_timeStamp1 = new long[63];
        long[] srx_timeStamp1 = new long[63];
        long[] spx_timeStamp1 = new long[63];
        long[] max_timeStamp1 = new long[63];

        pdp0_timeStamp1 = checkMessage(8, 4, 0x50, 0);
        for (int i = 0; i < 63; ++i) {
            pcm_timeStamp1[i] = checkMessage(8, 4, 0x50, i);
            srx_timeStamp1[i] = checkMessage(2, 4, 0x51, i);
            spx_timeStamp1[i] = checkMessage(1, 4, 0x51, i);
            max_timeStamp1[i] = checkMessage(2, 5, 0x60, i);
        }

        /*
         * compare, if timestamp0 is good and timestamp1 is good, and they are
         * different, device is healthy
         */
        if (pdp0_timeStamp0 >= 0 && pdp0_timeStamp1 >= 0
                && pdp0_timeStamp0 != pdp0_timeStamp1) {
            deviceList.add("PDP 0");
            pdpIsPresent = true;
        }

        for (int i = 0; i < 63; ++i) {
            if (pcm_timeStamp0[i] >= 0 && pcm_timeStamp1[i] >= 0
                    && pcm_timeStamp0[i] != pcm_timeStamp1[i]) {
                deviceList.add("PCM " + i);
                pcms.add(i);
            }
        }
        for (int i = 0; i < 63; ++i) {
            if (srx_timeStamp0[i] >= 0 && srx_timeStamp1[i] >= 0
                    && srx_timeStamp0[i] != srx_timeStamp1[i]) {
                deviceList.add("SRX " + i);
                srxs.add(i);
            }
        }
        for (int i = 0; i < 63; ++i) {
            if (spx_timeStamp0[i] >= 0 && spx_timeStamp1[i] >= 0
                    && spx_timeStamp0[i] != spx_timeStamp1[i]) {
                deviceList.add("SPX " + i);
                spxs.add(i);
            }
        }
        for (int i = 0; i < 63; ++i) {
            if (max_timeStamp0[i] >= 0 && max_timeStamp1[i] >= 0
                    && max_timeStamp0[i] != max_timeStamp1[i]) {
                deviceList.add("MAX " + i);
                maxs.add(i);
            }
        }

        // searchAllApis ("PDP", 8, 4, 0); // PDP
        // searchAllApis ("SRX", 2, 4, 1); // SRX #1
        // searchAllApis ("SPX", 1, 4, 2); // SPX #2

        // logCanBusIds = true;
        // canBusId(8, 4, 0x50, 0);
    }

    private void searchAllApis (String x, int devType, int mfg, int devId) {
        logger.info ("Searching for {}", x);
        long[] xxx_timeStamp0 = new long[1024];
        long[] xxx_timeStamp1 = new long[1024];
        for (int api = 0; api < 1024; api++) {
            xxx_timeStamp0[api] = checkMessage(devType, mfg, api, devId);
        }
        logCanBusIds = false;

        /* wait 200ms */
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int api = 0; api < 1024; api++) {
            xxx_timeStamp1[api] = checkMessage(devType, mfg, api, devId);
        }

        Set<Integer> apis = new TreeSet<>();
        for (int api = 0; api < 1024; api++) {
            if (xxx_timeStamp0[api] >= 0 && xxx_timeStamp1[api] >= 0
                    && xxx_timeStamp0[api] != xxx_timeStamp1[api]) {
                apis.add(api);
            }
        }

        logger.info ("API ids = {}", apis);
        for (int apiId: apis) {
            logger.info (" {} {} = {}", String.format("%2d", apiId), String.format("0x%03X", apiId), String.format("%08X", canBusId(devType, mfg, apiId, devId)));
        }
    }

    private ByteBuffer targetID = ByteBuffer.allocateDirect(4);
    private ByteBuffer timeStamp = ByteBuffer.allocateDirect(4);

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

    /** helper routine to get last received message for a given ID */


    private long checkMessage(int devType, int mfg, int apiId, int devId) {
        int id = canBusId (devType, mfg, apiId, devId);
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