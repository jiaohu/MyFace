package function;
import java.util.Formatter;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

/*
 * Detects faces in an image, draws boxes around them, and writes the 

 results
 * to "faceDetection.png".
 */
public class DetectFaceDemo {
	public void detectface() {
		System.out.println("\nRunning DetectFaceDemo");

		// Create a face detector from the cascade file in the resources
		// directory.
		CascadeClassifier faceDetector = new CascadeClassifier(
				"./data/haarcascade_frontalface_alt.xml");
		Mat image = Imgcodecs.imread("./data/AverageMaleFace.jpg");
		//System.out.println(image.cols());
		// Detect faces in the image.
		// MatOfRect is a special container class for Rect.
		MatOfRect faceDetections = new MatOfRect();
		faceDetector.detectMultiScale(image, faceDetections);

		int i = 1;

		// Draw a bounding box around each face.
		for (Rect rect : faceDetections.toArray()) {
			//System.out.println(new Point(rect.x, rect.y));
			//System.out.println(new Point(rect.x+rect.width,rect.y+rect.height));
			
			
			//System.out.println("\n");
			Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(
					rect.x + rect.width, rect.y + rect.height), new Scalar(255,
					0, 0));
			
			Rect rect_1 = new Rect(new Point(rect.x, rect.y), new Point(
					rect.x + rect.width, rect.y + rect.height));
			Mat mat = new Mat(image,rect_1);
			
			String s  = "likai";
			Imgcodecs.imwrite(s+i+".jpg",mat);
			i++;
		}

		// Save the visualized detection.
		 String filename = "faceDetection.png";
		 //System.out.println(String.format("Writing %s", filename));
		 Imgcodecs.imwrite(filename, image);
		 
	}
	
	public void detectEyes(Mat image){
		CascadeClassifier eyesdetect = new CascadeClassifier("./data/haarcascade_eye.xml");
		
	}
}
