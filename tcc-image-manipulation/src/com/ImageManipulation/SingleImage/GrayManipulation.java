package com.ImageManipulation.SingleImage;

import image.Image;
import morphology.MorphologyConstants;

public class GrayManipulation {
    static String path = "/Users/joaomazzochin";
    static int iterations = 3;

    public static void main(String[] args) throws Exception {
        Image img = new Image(path + "/heartstone-blue.png");
        //gray images to manipulate
        Image imgGray = img.convertToGray();

        imgGray.exportImage(path + "/heartstone-blue-original.png");

        applyDilate(imgGray.clone(),"heartstone-blue-gray.png" );
        applyErode(imgGray.clone(),"heartstone-blue-gray.png" );
        applyOpening(imgGray.clone(),"heartstone-blue-gray.png" );
        applyClosing(imgGray.clone(),"heartstone-blue-gray.png" );

    }

    public static void applyDilate(Image imageb, String name) throws Exception {
        Image imageEroded = imageb.dilate(MorphologyConstants.STRUCT_SUP, iterations);

        imageEroded.exportImage("/Users/joaomazzochin/dilate" + name);
    }

    public static void applyErode(Image imageb, String name) throws Exception {
        Image imageEroded = imageb.erode(MorphologyConstants.STRUCT_SUP, iterations);

        imageEroded.exportImage("/Users/joaomazzochin/erode" + name);
    }

    public static void applyOpening(Image imageb, String name) throws Exception {
        Image imgOpening = imageb.erode(MorphologyConstants.STRUCT_SUP, iterations);
        imgOpening = imgOpening.clone().dilate(MorphologyConstants.STRUCT_SUP, iterations);

        imgOpening.exportImage("/Users/joaomazzochin/opening" + name);
    }

    public static void applyClosing(Image imageb, String name) throws Exception {
        Image imgClosing = imageb.dilate(MorphologyConstants.STRUCT_SUP, iterations);
        imgClosing = imgClosing.clone().erode(MorphologyConstants.STRUCT_SUP, iterations);

        imgClosing.exportImage("/Users/joaomazzochin/closing" + name);
    }
}
