package org.usfirst.frc3620.misc;

import java.io.File;

import edu.wpi.first.cameraserver.CameraServer;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;

public class OperatorView {
	Logger logger = EventLogging.getLogger(getClass(), Level.INFO);

    public OperatorView() {
    }

	public void operatorViewInit() {
		File videoCamera = new File("/dev/video0");
		if (videoCamera.exists()) {
			logger.info ("/dev/video0 exists, starting the camera server");
            CameraServer.getInstance().startAutomaticCapture();
        } else {
			logger.warn ("Camera is missing");
		}
	}
}