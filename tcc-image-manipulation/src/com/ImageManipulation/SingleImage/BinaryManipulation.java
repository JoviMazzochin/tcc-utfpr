package com.ImageManipulation.SingleImage;

import image.Image;
import morphology.MorphologyConstants;

import java.io.File;

public class BinaryManipulation {
    static String path = "/Users/joaomazzochin";
    static int iterations = 3;

    public static void main(String[] args) throws Exception {
        Image img = new Image(path + "/ward.png");
        Image imageb = img.convertToBinary(10);
        imageb.exportImage("/Users/joaomazzochin/ward-binary-original.png");


        applyDilate(imageb.clone());
        applyErode(imageb.clone());

        applyOpening(imageb.clone());
        applyClosing(imageb.clone());
    }

    public static void applyOpening(Image imageb) throws Exception {
        Image imgOpening = imageb.erode(MorphologyConstants.STRUCT_SUP, iterations);
        imgOpening = imgOpening.clone().dilate(MorphologyConstants.STRUCT_SUP, iterations);

        imgOpening.exportImage("/Users/joaomazzochin/ward-binary-opening.png");
    }

    public static void applyClosing(Image imageb) throws Exception {
        Image imgClosing = imageb.clone().dilate(MorphologyConstants.STRUCT_SUP, iterations);
        imgClosing = imgClosing.erode(MorphologyConstants.STRUCT_SUP, iterations);

        imgClosing.exportImage("/Users/joaomazzochin/ward-binary-closing.png");
    }

    public static void applyDilate(Image imageb) throws Exception {
        Image imgDilate = imageb.dilate(MorphologyConstants.STRUCT_PRIMARY, iterations);

        imgDilate.exportImage("/Users/joaomazzochin/ward-binary-dilation.png");
    }

    public static void applyErode(Image imageb) throws Exception {
        Image imgErode = imageb.erode(MorphologyConstants.STRUCT_PRIMARY, iterations);

        imgErode.exportImage("/Users/joaomazzochin/ward-binary-eroded.png");
    }
}
