package com.GroupImages.results;

import image.Image;

import java.io.File;

public class JuntaResultsGradienteColorido {
    public static void main(String[] args) throws Exception {
        String path = "/Users/joaomazzochin/Documents/University/UTFPR 5º SEMESTRE/TCC1 /Database/GradienteColorido/20train10test/fake";
        File folder = new File(path); //fake
        File[] files = folder.listFiles();
        final int TAM = 256;

        File juntos = new File(path + "/../juntosGradienteColorido/");
        if (!juntos.exists()) juntos.mkdir();

        for (int k=0; k<files.length; k++){
            if(!files[k].getAbsolutePath().contains(".png")) continue;
//            System.out.println(files[k].getAbsolutePath());
            Image img = new Image(files[k]);

            String name = files[k].getName().split("\\.")[0] + "." + files[k].getName().split("\\.")[1].replace("fake","real");
            System.out.println(path + "/../real/" + name + ".png");
            Image img2 = new Image(new File(path + "/../real/" + name + ".png"));



            img.resize(TAM, TAM);img2.resize(TAM, TAM);

            //img.showImage(); img2.showImage();

            Image imgOut = new Image(TAM*2, TAM, 3);

            for (int i=0; i<TAM; i++){
                for (int j=0; j<TAM; j++){
                        imgOut.setPixel(j, i, 0, img2.getPixel(j, i, 0));
                        imgOut.setPixel(j + TAM, i, 0, img.getPixel(j, i, 0));
                }
            }


            imgOut.exportImage(juntos.getAbsolutePath() + "/" + files[k].getName());

        }

    }
}
