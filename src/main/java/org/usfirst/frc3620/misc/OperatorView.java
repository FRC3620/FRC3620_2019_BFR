package org.usfirst.frc3620.misc;



import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;

import java.io.File;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;

/**
 * This is a demo program showing the use of OpenCV to do vision processing. The
 * image is acquired from the USB camera, then a rectangle is put on the image
 * and sent to the dashboard. OpenCV has many methods for different types of
 * processing.
 */
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