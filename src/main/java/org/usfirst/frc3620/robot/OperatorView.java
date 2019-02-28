package org.usfirst.frc3620.robot;

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

public class OperatorView {
	static Logger logger = EventLogging.getLogger(OperatorView.class, Level.INFO);

	public static void operatorViewInit() {
		File videoCamera = new File("/dev/video0");
		if (videoCamera.exists()) {
			logger.info ("/dev/video0 exists, starting the camera server");
            CameraServer.getInstance().startAutomaticCapture();
		} else {
			logger.warn ("Camera is missing");
		}
	}
}