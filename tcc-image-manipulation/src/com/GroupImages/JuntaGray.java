package com.GroupImages;

import image.Image;

import java.io.File;

public class JuntaGray {

    public static void main(String[] args) throws Exception {

//        String path = "/Users/joaomazzochin/Documents/University/UTFPR 5º SEMESTRE/TCC1 /Database/Troncos de Árvore - Contagem de Objetos/Ready/Dia 06-08-2021/SegmentadosGray";
        String path = "/Users/joaomazzochin/Documents/University/UTFPR 5º SEMESTRE/TCC1 /Database/Troncos de Árvore - Contagem de Objetos/Ready/Dia 06-08-2021/SegmentadosGray18";
        File folder = new File(path); //segmentado
        File[] files = folder.listFiles();
        final int TAM_SAIDA = 256;
        final int TAM_ORIGINAL = 4032;

        File juntos = new File(path + "/../juntosGray18/");
        if (!juntos.exists()) juntos.mkdir();

        for (int k=0; k<files.length; k++){
            if(!files[k].getAbsolutePath().contains(".png")) continue;
//            System.out.println(files[k].getAbsolutePath());
            Image img = new Image(files[k]);
            img.resize(TAM_ORIGINAL, TAM_ORIGINAL);

            img.setCanvas(TAM_ORIGINAL, TAM_ORIGINAL);
            String name = files[k].getName().split("\\.")[0] + "." + files[k].getName().split("\\.")[1];
            System.out.println(path + "/../" + name + ".png");
            Image img2 = new Image(new File(path + "/../" + name + ".png"));
            img2.setCanvas(TAM_ORIGINAL, TAM_ORIGINAL);

            img.resize(TAM_SAIDA, TAM_SAIDA);img2.resize(TAM_SAIDA, TAM_SAIDA);

            //img.showImage(); img2.showImage();

            Image imgOut = new Image(TAM_SAIDA*2, TAM_SAIDA, 3);

            for (int i=0; i<TAM_SAIDA; i++){
                for (int j=0; j<TAM_SAIDA; j++){
                    for (int b=0; b<3; b++) {
                        imgOut.setPixel(j, i, b, img2.getPixel(j, i, b));
                        imgOut.setPixel(j + TAM_SAIDA, i, b, img.getPixel(j, i));
                    }

                }
            }
            imgOut.exportImage(juntos.getAbsolutePath() + "/" + files[k].getName());

        }

    }

}
