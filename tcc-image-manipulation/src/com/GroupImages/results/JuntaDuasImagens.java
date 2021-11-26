package com.GroupImages.results;

import image.Image;

import java.awt.*;
public class JuntaDuasImagens {
    //the imagens need to have the same size width and height
    static int WIDTH = 538;
    static int HEIGHT = 430;
    static Image imgOut = new Image(WIDTH*2, HEIGHT);

    static String basePath = "/Users/joaomazzochin";



    public static void main(String[] args) throws Exception {
        Image img = new Image(basePath + "/tlou-binary-original.png");
//        Image img2 = new Image(basePath + "/tlou-binary-dilation.png");
        Image img2 = new Image(basePath + "/tlou-binary-eroded.png");


        juntaBinarias(img, img2);


//        imgOut.exportImage(basePath + "/tlou-original-dilation.png");
        imgOut.exportImage(basePath + "/tlou-original-eroded.png");
    }

    public static void juntaColoridas(Image img, Image img2) {
        for (int i=0; i<WIDTH; i++){
            for (int j=0; j<HEIGHT; j++){
                for (int b=0; b<3; b++) {
                    imgOut.setPixel(i, j, b, img.getPixel(i, j, b));
                    imgOut.setPixel(i + WIDTH, j, b, img2.getPixel(i, j, b));
                    if(i+WIDTH == 501){
                        imgOut.setPixel(i + WIDTH, j, b, Color.YELLOW);
                    }
                }

            }
        }
    }

    public static void juntaBinarias(Image img, Image img2) {
        for (int i=0; i<WIDTH; i++){
            for (int j=0; j<HEIGHT; j++){
                imgOut.setPixel(i, j, img.getPixel(i, j));
                imgOut.setPixel(i + WIDTH, j, img2.getPixel(i, j));
                if(i+WIDTH == 501){
                    imgOut.setPixel(i + WIDTH, j, Color.YELLOW);
                }
            }

        }
    }
}
