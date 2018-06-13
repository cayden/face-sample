/**
 * 
 */
package com.cayden.face;

import java.io.File;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

/**
 * @author caydencui
 *
 */
public class FaceDetector {
	/**
	 * 识别指定图片上的所有人脸
	 * 
	 * @param inImg
	 *            输入原图
	 * @param outImg
	 *            输出图片，在原图的基础上添加"识别框"
	 */

	public static void detect(String inImg, String outImg) {

		// TODO Auto-generated method stub
		System.out.println("Welcome to OpenCV " + Core.VERSION);
		File f = new File(inImg);

		// 原图片不存在直接退出
		if (!f.exists()) {
			System.out.println("\n Image File Not Found!");
			return;
		}
		// 获取原文件路径,让输出文件与原文件保存在同一路径
		String filePath = f.getAbsolutePath().substring(0, f.getAbsolutePath().indexOf(File.separator) + 1);
		// 加载OPENCV3.4本地库,必须先加载
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		System.out.println("\nRunning FaceDetector");
		CascadeClassifier faceDetector = new CascadeClassifier();
		/**
		 * 分类器，路径根据实际情况调整 haarcascade_frontalface_default 默认
		 * haarcascade_frontalface_alt 识别性能要好些
		 */
		faceDetector.load("D:\\haarcascade_frontalface_alt.xml");
		// 读取图像
		Mat image = Imgcodecs.imread(inImg);
		// 用于保存监测到的人脸
		MatOfRect faceDetections = new MatOfRect();
		// 开始监测
		faceDetector.detectMultiScale(image, faceDetections);

		System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
		int i = 0;

		for (Rect rect : faceDetections.toArray()) {
			// 循环所有监测到的人脸
			Point x = new Point(rect.x, rect.y);
			Point y = new Point(rect.x + rect.width, rect.y + rect.height);
			// 在image图片上画框，x，y可确定框的位置和大小，new Scalar(0, 255, 0)是框的颜色，自行调整
			Imgproc.rectangle(image, x, y, new Scalar(0, 255, 0));

			// 保存监测的人脸小图片
			Rect r = new Rect(x, y);
			System.out.println(r.height + ":" + r.width);
			Mat areaM = new Mat(image, r);
			// 保存监测的人脸小图片到tmp+序号的jpg文件
			String tmpFilePath = filePath + "tmp" + (i++) + ".jpg";
			Imgcodecs.imwrite(tmpFilePath, areaM);

		}
		// 保存画了方框的图片
		String filename = outImg;
		Imgcodecs.imwrite(filename, image);
		// 销毁
		image.release();
	}

	public static void main(String[] args) {
		long s = System.currentTimeMillis();
		detect("d:\\tmp_8c63e68c8918a64fef4c867f9a5e1ad8.png", "d:\\ouput1.jpg");
		long e = System.currentTimeMillis();
		System.out.println((e - s) + "ms");
	}

}
