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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.tess4j.ITesseract;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;


import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.imgproc.Imgproc;

public class Main {

    private static final int CV_ADAPTIVE_THRESH_MEAN_C = 1;
    private static final int CV_THRESH_BINARY = 0;
    private static final int CV_THRESH_OTSU = 8;
    private static final int CV_ADAPTIVE_THRESH_GAUSSIAN_C = 1;
    private static final int CV_THRESH_BINARY_INV = 1;
    public static GuiClass g;


    // Source path content images
    static String SRC_PATH = "C:\\Recognize\\java_text";
    static String TESS_DATA = "C:\\Program Files\\Tesseract-OCR\\tessdata";

    // Create tess obj
    static Tesseract tesseract = new Tesseract();

    //hold the resultString
    public static String rString;

    // Load OPENCV
    static {
        String libPath = System.getProperty("C:\\opencv\\build\\java\\x64");

        String libraryName = "opencv_java411";
        //System.out.println("Trying to load '" + libraryName + "'");
        System.loadLibrary(libraryName);
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

    public Main(){

    }


    public void baslat() {

        g = new GuiClass();
        g.anafonk();

    }
    public void tesseract(String path) {


        System.out.println("Start recognize text from image");
        long start = System.currentTimeMillis();

        System.out.println(path);
        // Read image
        //Mat origin = imread(path);

        //Mahmut tries something and it doesn't work
        Mat img = Imgcodecs.imread(path);
        Imgcodecs.imwrite("preprocess/True_Image.png", img);
        //grayScale
        Mat imgGray = new Mat();
        Imgproc.cvtColor(img, imgGray, Imgproc.COLOR_BGR2GRAY);
        Imgcodecs.imwrite("preprocess/Gray.png", imgGray);
        //Gaussian Blur
        Mat imgGaussianBlur = new Mat();
        Imgproc.GaussianBlur(imgGray,imgGaussianBlur,new Size(3, 3),0);
        Imgcodecs.imwrite("preprocess/gaussian_blur.png", imgGaussianBlur);
/*      //Sobel
        Mat imgSobel = new Mat();
        Imgproc.Sobel(imgGaussianBlur, imgSobel, -1, 1, 0);
        Imgcodecs.imwrite("preprocess/4_imgSobel.png", imgSobel);


    Sobel bak moruk
*/
        //Step5
        Mat imgThreshold = new Mat();
        Imgproc.threshold(imgGaussianBlur, imgThreshold, 0, 255,  CV_THRESH_OTSU + CV_THRESH_BINARY);
        Imgcodecs.imwrite("preprocess/5_imgThreshold.png", imgThreshold);

        //Adaptive Threshold
        Mat imgAdaptiveThreshold = new Mat();
        Imgproc.adaptiveThreshold(imgThreshold, imgAdaptiveThreshold, 255, CV_ADAPTIVE_THRESH_GAUSSIAN_C, CV_THRESH_BINARY_INV, 75, 50);
        Imgcodecs.imwrite("preprocess/5_imgAdaptiveThreshold.png", imgAdaptiveThreshold);


//bu bizim eskisi
        Imgproc.adaptiveThreshold(imgGaussianBlur, imgAdaptiveThreshold, 255, CV_ADAPTIVE_THRESH_MEAN_C ,CV_THRESH_BINARY, 79, 27);
        Imgcodecs.imwrite("preprocess/adaptive_threshold2.png", imgAdaptiveThreshold);

        File imageFile = new File("preprocess/adaptive_threshold2.png");
        ITesseract instance = new Tesseract();
        instance.setDatapath(TESS_DATA);
        instance.setLanguage("tur");
        //instance.setTessVariable("tessedit_char_whitelist", "acekopxyABCEHKMOPTXY0123456789");
        String result = null;
        try {
            result = instance.doOCR(imageFile);
        } catch (TesseractException e) {
            e.printStackTrace();
        }
        System.out.println(result);

//        String res = new Main().extractTextFromImage(imgAdaptiveThreshold);
//        System.out.println(res);
        //String result = new Main().extractTextFromImage(origin);
        //rString = result;
        //System.out.println(result);

        System.out.println("Time");
        System.out.println(System.currentTimeMillis() - start);
        System.out.println("Done");

    }


    //Regex Checker Function to
    public static void regexChecker(String theRegex, String str2Check){

        // You define your regular expression (REGEX) using Pattern

        Pattern checkRegex = Pattern.compile(theRegex);

        // Creates a Matcher object that searches the String for
        // anything that matches the REGEX

        Matcher regexMatcher = checkRegex.matcher( str2Check );

        // Cycle through the positive matches and print them to screen
        // Make sure string isn't empty and trim off any whitespace

        while ( regexMatcher.find() ){
            if (regexMatcher.group().length() != 0){
                System.out.println( regexMatcher.group().trim() );

                // You can get the starting and ending indexs

                System.out.println( "Start Index: " + regexMatcher.start());
                System.out.println( "Start Index: " + regexMatcher.end());
            }
        }

        System.out.println();
    }


}
