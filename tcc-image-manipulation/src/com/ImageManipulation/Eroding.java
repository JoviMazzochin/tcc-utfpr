package com.ImageManipulation;

import image.Image;
import morphology.Morphology;

import java.io.File;

public class Eroding {
        static String path = "/Users/joaomazzochin/Documents/University/UTFPR 5º SEMESTRE/TCC1 /Database/Troncos de Árvore - Contagem de Objetos/Ready/Dia 06-08-2021/Segmentados";
//        String path = "/Users/joaomazzochin/Documents/University/UTFPR 5º SEMESTRE/TCC1 /Database/Troncos de Árvore - Contagem de Objetos/Ready/Dia 22-08-2021/Contra o sol/Segmentados";
//        String path = "/Users/gustavotiecker/Library/Mobile Documents/com~apple~CloudDocs/TCC1 /Database/Troncos de Árvore - Contagem de Objetos/Ready/Dia 06-08-2021/Segmentados";
    static File folder = new File(path); //segmentado
    static File[] files = folder.listFiles();
    static final int TAM_ORIGINAL = 4032;
    static final int TAMANHO = 4032;
    public static void main(String[] args) throws Exception {

//        erode(32);
//        erode(24);
        erode(18);
    }

    public static void erode(int erosion) throws Exception {
        File segmentadosErode = new File(path + "/../SegmentadosErode"+erosion+"/");
        if (!segmentadosErode.exists()) segmentadosErode.mkdir();


        for (int k = 0; k < files.length; k++) {
            if (!files[k].getAbsolutePath().contains(".png")) continue;
//            System.out.println(files[k].getAbsolutePath());
            Image img = new Image(files[k]);

            img.setCanvas(TAM_ORIGINAL, TAM_ORIGINAL);
            String name = files[k].getName().split("\\.")[0] + "." + files[k].getName().split("\\.")[1];
            System.out.println(path + "/../" + name + ".png");

            img.convertToGray();
            img.erode(Morphology.STRUCT_WIDER_CROSS, erosion);

            Image imgEroded = new Image(TAMANHO,TAMANHO, 3);
            imgEroded.setCanvas(TAMANHO, TAMANHO);

            System.out.println("Eroded: SegmentadosErode"+erosion+"/"+name+".png");
            for (int i=0; i<TAMANHO; i++){
                for (int j=0; j<TAMANHO; j++){
                    if (img.getPixel(i,j,0) > 100) {
                        imgEroded.setPixel(i, j, 0, img.getPixel(i,j));
                    }
                }
            }

            imgEroded.exportImage(path + "/../SegmentadosErode"+erosion+"/"+name+".png", "PNG");


        }
    }

}
