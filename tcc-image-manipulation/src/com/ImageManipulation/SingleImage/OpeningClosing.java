package com.ImageManipulation.SingleImage;

import image.Image;
import morphology.MorphologyConstants;

public class OpeningClosing {
    static String path = "/Users/joaomazzochin/Desktop/imageGenerated.png";

    public static void main(String[] args) throws Exception {
        Image img = new Image(path);
        Image imageb = img.convertToGray();

        Image openingImg = applyOpening(imageb.clone(), 15);
        openingImg.exportImage("/Users/joaomazzochin/Desktop/openingImg.png");
    }

    public static Image applyOpening(Image imageb, int iterations) throws Exception {
        Image imgOpening = imageb.erode(MorphologyConstants.STRUCT_SUP, iterations);
        imgOpening = imgOpening.clone().dilate(MorphologyConstants.STRUCT_SUP, iterations);

        return imgOpening;
    }

    public static Image applyClosing(Image imageb, int iterations) throws Exception {
        Image imgClosing = imageb.clone().dilate(MorphologyConstants.STRUCT_SUP, iterations);
        imgClosing = imgClosing.erode(MorphologyConstants.STRUCT_SUP, iterations);

        return imgClosing;
    }

    public static void applyDilate(Image imageb, int iterations) throws Exception {
        Image imgDilate = imageb.dilate(MorphologyConstants.STRUCT_PRIMARY, iterations);
    }

    public static void applyErode(Image imageb, int iterations) throws Exception {
        Image imgErode = imageb.erode(MorphologyConstants.STRUCT_PRIMARY, iterations);
    }
}
