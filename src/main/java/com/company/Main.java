package main.java.com.company;
import static org.opencv.highgui.Highgui.imread;
import static org.opencv.highgui.Highgui.imwrite;
import static org.opencv.imgproc.Imgproc.COLOR_BGR2GRAY;
import static org.opencv.imgproc.Imgproc.MORPH_RECT;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.dilate;
import static org.opencv.imgproc.Imgproc.erode;
import static org.opencv.imgproc.Imgproc.getStructuringElement;

import java.io.File;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class Main {

     static GuiClass g;

    // Source path content images
    static String SRC_PATH = "C:\\Recognize\\java_text";
    static String TESS_DATA = "C:\\Program Files\\Tesseract-OCR\\tessdata";

    // Create tess obj
    static Tesseract tesseract = new Tesseract();

    //hold the resultString
    public static String rString;

    // Load OPENCV
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        tesseract.setDatapath(TESS_DATA);
        tesseract.setLanguage("tur");
    }

    String extractTextFromImage(Mat inputMat) {
        String result = "";
        Mat gray = new Mat();

        // Convert to gray scale
        cvtColor(inputMat, gray, COLOR_BGR2GRAY);
        imwrite(SRC_PATH + "gray.png", gray);

        //  Apply closing, opening
        //Mat element = getStructuringElement(MORPH_RECT, new Size(2, 2), new Point(1, 1));
        //dilate(gray, gray, element);
        //erode(gray, gray, element);

        //imwrite(SRC_PATH + "closeopen.png", gray);

        try {
            // Recognize text with OCR
            result = tesseract.doOCR(new File(SRC_PATH + "gray.png"));
        } catch (TesseractException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void main(String[] args) {
        System.out.println("Start recognize text from image");
        long start = System.currentTimeMillis();

        // Read image
        Mat origin = imread("C:\\Users\\Mahmut\\Desktop\\test4.jpg");

        String result = new Main().extractTextFromImage(origin);
        rString = result;
        System.out.println(result);

        System.out.println("Time");
        System.out.println(System.currentTimeMillis() - start);
        System.out.println("Done");

        g = new GuiClass();
        GuiClass.anafonk();

        

    }

}
