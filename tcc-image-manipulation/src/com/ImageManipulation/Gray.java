package com.ImageManipulation;

import image.Image;

import java.io.File;

public class Gray {
        static String path = "/Users/joaomazzochin/Documents/University/UTFPR 5º SEMESTRE/TCC1 /Database/Troncos de Árvore - Contagem de Objetos/Ready/Dia 06-08-2021/Segmentados";
//        String path = "/Users/joaomazzochin/Documents/University/UTFPR 5º SEMESTRE/TCC1 /Database/Troncos de Árvore - Contagem de Objetos/Ready/Dia 22-08-2021/Contra o sol/Segmentados";
//        String path = "/Users/gustavotiecker/Library/Mobile Documents/com~apple~CloudDocs/TCC1 /Database/Troncos de Árvore - Contagem de Objetos/Ready/Dia 06-08-2021/Segmentados";
    static File folder = new File(path); //segmentado
    static File[] files = folder.listFiles();
    static final int TAMANHO = 4032;
    public static void main(String[] args) throws Exception {
        gray();
    }

    public static void gray() throws Exception {
        File segmentadosErode = new File(path + "/../SegmentadosGray/");
        if (!segmentadosErode.exists()) segmentadosErode.mkdir();


        for (int k = 0; k < files.length; k++) {
            if (!files[k].getAbsolutePath().contains(".png")) continue;
//            System.out.println(files[k].getAbsolutePath());
            Image img = new Image(files[k]);

            img.setCanvas(TAMANHO, TAMANHO);
            String name = files[k].getName().split("\\.")[0] + "." + files[k].getName().split("\\.")[1];
            System.out.println(path + "/../" + name + ".png");

            img.convertToGray();

            img.exportImage(path + "/../SegmentadosGray/"+name+".png", "PNG");


        }
    }

}
