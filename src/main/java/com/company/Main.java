package main.java.com.company;

import static org.opencv.highgui.Highgui.imread;
import static org.opencv.highgui.Highgui.imwrite;
import static org.opencv.imgproc.Imgproc.COLOR_BGR2GRAY;
import static org.opencv.imgproc.Imgproc.MORPH_RECT;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.dilate;
import static org.opencv.imgproc.Imgproc.erode;
import static org.opencv.imgproc.Imgproc.getStructuringElement;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
import org.w3c.dom.ls.LSOutput;

import javax.imageio.ImageIO;

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
    public static List<String> rtun = new ArrayList<>(10);

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
    String sTarih;
    String sFisNo;
    ArrayList<String> urun = null;

    String as;

    String sirket;

    //[a-zA-Z]+[0-9]{2}

    public void scalingOfImage() {

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
        Imgproc.adaptiveThreshold(imgThreshold, imgAdaptiveThreshold, 255, CV_ADAPTIVE_THRESH_GAUSSIAN_C, CV_THRESH_BINARY_INV, 99, 50);
        Imgcodecs.imwrite("preprocess/5_imgAdaptiveThreshold.png", imgAdaptiveThreshold);


//bu bizim eskisi
        Imgproc.adaptiveThreshold(imgGaussianBlur, imgAdaptiveThreshold, 255, CV_ADAPTIVE_THRESH_MEAN_C ,CV_THRESH_BINARY, 79, 27);
        Imgcodecs.imwrite("preprocess/adaptive_threshold2.png", imgAdaptiveThreshold);


        grayscale(path);


        File imageFile = new File("preprocess/grayscale.tiff");
        ITesseract instance = new Tesseract();
        instance.setDatapath(TESS_DATA);
        instance.setLanguage("tur");

        String result = null;
        try {
            result = instance.doOCR(imageFile);
        } catch (TesseractException e) {
            e.printStackTrace();
        }
        rString = result;
        as = result;
        System.out.println(result);

//        String res = new Main().extractTextFromImage(imgAdaptiveThreshold);
//        System.out.println(res);
        //String result = new Main().extractTextFromImage(origin);
        //rString = result;
        //System.out.println(result);

        System.out.println("Time");
        System.out.println(System.currentTimeMillis() - start);
        System.out.println("Done");

        splitter();

    }


    //grayscale
    public void grayscale(String path){
        BufferedImage img = null;
        File f = null;

        //read image
        try{
            f = new File(path);
            img = ImageIO.read(f);
        }catch(IOException e){
            System.out.println(e);
        }

        //get image width and height
        int width = img.getWidth();
        int height = img.getHeight();

        //convert to grayscale
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int p = img.getRGB(x,y);

                int a = (p>>24)&0xff;
                int r = (p>>16)&0xff;
                int g = (p>>8)&0xff;
                int b = p&0xff;

                //calculate average
                int avg = (r+g+b)/3;

                //replace RGB value with avg
                p = (a<<24) | (avg<<16) | (avg<<8) | avg;

                img.setRGB(x, y, p);
            }
        }


        //write image
        try{
            f = new File("preprocess/grayscale.tiff");
            ImageIO.write(img, "tiff", f);

        }catch(IOException e){
            System.out.println(e);
        }
    }


    //String paraçalama işi burada dönüyor
    public void splitter() {

        //Şirket Adı



        //Tarih splitter
        sTarih = regexChecker("\\s*(3[01]|[12][0-9]|0?[1-9])\\.(1[012]|0?[1-9])\\.((?:19|20)\\d{2})\\s|([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}", rString);
        System.out.println("Tarih: "+sTarih);


        //Fiş no splitter
        as = clearTurkishChars(as);

        sFisNo = regexChecker("(.*?.*:?fıs.*)", as);
        if (sFisNo != null){
            sFisNo = regexChecker("[0-9]{4}", sFisNo);
            System.out.println("fiş no :" + sFisNo);
        }

        //Şirket adını Stringten çektiğimiz kısım
        sirket = regexChecker("^.*\\r?\\n(.*)", as);
        String lines[] = sirket.split("\\r?\\n");
        if (sirket.contains("tesekkurler")){
            sirket = lines[1];
        } else {
            if (lines[0].contains("a.s")){
                sirket = lines[0];
            }else {
                sirket = sirket;
            }
        }

        System.out.println(sirket);


        //Urun
        regexer("(.*?.*:?x08.*)|(.*?.*:?408.*)|(.*?.*:?(...)%08.*)|(.*?.*:?X08.*)|(.*?.*:?\\*08.*)|(.*?.*:?x18.*)|(.*?.*:?418.*)|(.*?.*:?%18.*)|(.*?.*:?X18.*)|" +
                "(.*?.*:?\\*18.*)|(.*?.*:?x01.*)|(.*?.*:?401.*)|(.*?.*:?%01.*)|(.*?.*:?X01.*)|(.*?.*:?\\*01.*)|(.*?.*:?x8.*)|(.*?.*:?48.*)|(.*?.*:?%8.*)|(.*?.*:?X8.*)|(.*?.*:?\\*8.*)", as );

        rtun.forEach((n) -> System.out.println(n));
        //KDV
//        sKDV = regexChecker("X+[0-9]{2}|x+[0-9]{2}", rString);
//        sKDV = regexChecker("[0-9]{2}", sKDV);
//        System.out.println(sKDV);
    }

    public static String clearTurkishChars(String str) {
        String ret = str;
        char[] turkishChars = new char[] {0x131, 0x130, 0xFC, 0xDC, 0xF6, 0xD6, 0x15F, 0x15E, 0xE7, 0xC7, 0x11F, 0x11E};
        char[] englishChars = new char[] {'i', 'I', 'u', 'U', 'o', 'O', 's', 'S', 'c', 'C', 'g', 'G'};
        for (int i = 0; i < turkishChars.length; i++) {
            ret = ret.replaceAll(new String(new char[]{turkishChars[i]}), new String(new char[]{englishChars[i]}));
        }
        return ret;
    }





    //Regex Checker Function
    public static String regexChecker(String theRegex, String str2Check){
        String returner = null;
        // You define your regular expression (REGEX) using Pattern

       // Pattern checkRegex = Pattern.compile(theRegex);

        str2Check = str2Check.toLowerCase();

        Pattern checkRegex = Pattern.compile(theRegex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

        // Creates a Matcher object that searches the String for
        // anything that matches the REGEX

        Matcher regexMatcher = checkRegex.matcher( str2Check );

        // Cycle through the positive matches and print them to screen
        // Make sure string isn't empty and trim off any whitespace

        while ( regexMatcher.find() ){
            if (regexMatcher.group().length() != 0){
                //System.out.println( regexMatcher.group().trim() );
                returner = regexMatcher.group().trim();
                // You can get the starting and ending indexs
            }
        }

        System.out.println();
        return returner;
    }


    //Regex Checker Function
    public static void  regexer(String theRegex, String str2Check){

        // You define your regular expression (REGEX) using Pattern

        // Pattern checkRegex = Pattern.compile(theRegex);

        str2Check = str2Check.toLowerCase();

        Pattern checkRegex = Pattern.compile(theRegex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

        // Creates a Matcher object that searches the String for
        // anything that matches the REGEX

        Matcher regexMatcher = checkRegex.matcher( str2Check );


        // Cycle through the positive matches and print them to screen
        rtun.clear();
        // Make sure string isn't empty and trim off any whitespace
        String line;
        while ( regexMatcher.find() ){
            if (regexMatcher.group().length() != 0){
                line = regexMatcher.group().trim();
                rtun.add(line);
                // You can get the starting and ending indexs
            }
        }

        System.out.println();
    }


}
