package com.ImageManipulation;

import image.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static image.Image.MorphologyConstants;

public class Gradiente {
    static final int THRESHOLD = 10;
    static final int TAM_SAIDA = 256;

    public static class Vetor2D{
        private static ArrayList<Vetor2D> coordenadas = new ArrayList<Vetor2D>();
        public Vetor2D(int x, int y, boolean t){this.x = x; this.y = y;}
        public Vetor2D(int x, int y){coordenadas.add(new Vetor2D(x, y, true));}
        public int x, y;
        public double centroidX = 0, centroidY = 0;
        public int pointCounter = 0;
        public void add(int x, int y){coordenadas.add(new Vetor2D(x, y, true));}
        public double getCentroidX(){return centroidX/ pointCounter;}
        public double getCentroidY(){return centroidY/ pointCounter;}

    }

    public static void dilatar(float[][] matriz, int j, int i){
        if (j-1 >= 0){
            if (matriz[i][j-1] < matriz[i][j] && matriz[i][j-1] > 0){
                matriz[i][j-1] = matriz[i][j];
                dilatar(matriz, j-1, i);
            }
        }

        if (j+1 < matriz[0].length){
            if (matriz[i][j+1] < matriz[i][j] && matriz[i][j+1] > 0){
                matriz[i][j+1] = matriz[i][j];
                dilatar(matriz, j+1, i);
            }
        }

        if (i-1 >= 0){
            if (matriz[i-1][j] < matriz[i][j] && matriz[i-1][j] > 0){
                matriz[i-1][j] = matriz[i][j];
                dilatar(matriz, j, i-1);
            }
        }

        if (i+1 < matriz.length){
            if (matriz[i+1][j] < matriz[i][j] && matriz[i+1][j] > 0){
                matriz[i+1][j] = matriz[i][j];
                dilatar(matriz, j, i+1);
            }
        }


        //diagonal


    }

    public static void main(String[] args) throws Exception {
        String path = "/Users/joaomazzochin/Documents/University/UTFPR 5º SEMESTRE/TCC1 /Database/Troncos de Árvore - Contagem de Objetos/Ready/Dia 06-08-2021/Segmentados";
        File folder = new File(path);
        File[] files = folder.listFiles();



        for (int p=0; p<files.length; p++) {
            if (!files[p].getAbsolutePath().contains(".png")) continue;

            Image img = new Image(files[p]);
            img.convertToGray();
            img.erode(MorphologyConstants.STRUCT_SUP, 15);
            img.resize(TAM_SAIDA, TAM_SAIDA);

            //achar os clusters
            ArrayList<Vetor2D> cluster = new ArrayList<Vetor2D>();
            HashMap<Float, Vetor2D> todosClusters = new HashMap<Float, Vetor2D>(); //clusterid, respectivos pontos do cluster
            float[][] matriz = new float[img.getHeight()][img.getWidth()];

            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[0].length; j++) {
                    matriz[i][j] = (img.getPixel(j, i, 0) > THRESHOLD) ? i * matriz[0].length + j : 0; //preenchendo a matriz com um �ndice �nico para cada pixel
                }
            }

            //propagando o pixel de maior valor entre os elementos conectados (morphological reconstruction)
            for (int i = matriz.length - 1; i >= 0; i--) {
                for (int j = matriz[0].length - 1; j >= 0; j--) {
                    dilatar(matriz, j, i);
                }
            }

            //converter linearmente pra encaixar no intervalo de 0 - 255
            //achando os max e min primeiro
            float max = 0, min = Integer.MAX_VALUE;
            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[0].length; j++) {
                    if (max < matriz[i][j]) max = matriz[i][j];
                    //if (min > matriz[i][j]) min = matriz[i][j]; //assumindo min como 0
                }
            }

            //normalizando e convertendo
            int matrizInt[][] = new int[matriz.length][matriz[0].length];
            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[0].length; j++) {
                    matriz[i][j] = matriz[i][j] / (max); //normalizando
                    matriz[i][j] = matriz[i][j] * 255; //convertendo
                    matrizInt[i][j] = (int) matriz[i][j];
                }
            }


            //mostrando, nesse ponto cada cluster tem um valor "�nico" de cinza
            Image nova = new Image(matrizInt);
//        nova.showImage();


            //calcular centroides
            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[0].length; j++) {

                    if (matriz[i][j] > 0) {
                        Vetor2D nVetor = new Vetor2D(j, i);
                        if (todosClusters.containsKey(matriz[i][j])) {
                            todosClusters.get(matriz[i][j]).add(j, i);
                            nVetor = todosClusters.get(matriz[i][j]);
                        } else {
                            todosClusters.put(matriz[i][j], nVetor);
                        }
                        nVetor.centroidX += j;
                        nVetor.centroidY += i;
                        nVetor.pointCounter++;
                    }

                }
            }

            //gerar gradiente com base nos centroides
            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[0].length; j++) {

                    if (matriz[i][j] > 0) {
                        final double centroidX = todosClusters.get(matriz[i][j]).getCentroidX(),
                                centroidY = todosClusters.get(matriz[i][j]).getCentroidY();
                        double distancia = Math.sqrt(Math.pow(j - centroidX, 2) + Math.pow(i - centroidY, 2));

                        matriz[i][j] = (float) distancia; //distnacia de cada pixel pro centroid de seu respectivo cluster
                    }

                }
            }

            //gerar gradiente normalizado
            //achando os max e min primeiro
            max = 0;
            min = Integer.MAX_VALUE;
            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[0].length; j++) {
                    if (max < matriz[i][j]) max = matriz[i][j];
                    //if (min > matriz[i][j]) min = matriz[i][j]; //assumindo min como 0
                }
            }
            //normalizando e convertendo
            matrizInt = new int[matriz.length][matriz[0].length];
            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[0].length; j++) {
                    matriz[i][j] = matriz[i][j] / (max); //normalizando
                    matrizInt[i][j] = (matriz[i][j] == 0) ? 0 : (int) (255 - (matriz[i][j] * 255)); //convertendo
                }
            }

            //mostrando, nesse ponto cada cluster tem um valor "�nico" de cinza
            Image nova2 = new Image(matrizInt);
//        nova2.showImage();


            //mostrando, nesse ponto cada cluster tem um valor "�nico" de cinza
            Image nova3 = new Image(img.getWidth(), img.getHeight(), 3);
            Image nova4 = new Image(img.getWidth(), img.getHeight(), 3);
            //normalizando e convertendo (3 camadas)
            matrizInt = new int[matriz.length][matriz[0].length];
            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[0].length; j++) {
                    matriz[i][j] = (matriz[i][j] == 0) ? 0 : 465 - (matriz[i][j] * 465);
                    if (matriz[i][j] >= 310) {
                        nova3.setPixel(j, i, 0, 100 + matriz[i][j] - 310);
                        nova4.setPixel(j, i, 0, 255);
                    }
                    if (matriz[i][j] >= 155 && matriz[i][j] < 310) {
                        nova3.setPixel(j, i, 1, 100 + matriz[i][j] - 155);
                        nova4.setPixel(j, i, 1, 255);
                    }
                    if (matriz[i][j] < 155) {
                        if (matriz[i][j] == 0) continue;
                        nova3.setPixel(j, i, 2, 100 + matriz[i][j]);
                        nova4.setPixel(j, i, 2, 255);
                    }
                }
            }

//        nova3.showImage();
//        nova4.showImage();
//        img2.setCanvas(TAM_ORIGINAL, TAM_ORIGINAL);

            File juntosColorido = new File(path + "/../juntosGradienteColorido/");
            File juntosGray = new File(path + "/../juntosGradienteGray/");
            if (!juntosColorido.exists()) juntosColorido.mkdir();
            if (!juntosGray.exists()) juntosColorido.mkdir();

            String name = files[p].getName().split("\\.")[0] + "." + files[p].getName().split("\\.")[1];
            System.out.println("Salvo Colorido e Gray: " + name + ".png");
            Image img2 = new Image(new File(path + "/../" + name + ".png"));

            nova3.resize(TAM_SAIDA, TAM_SAIDA);
            nova2.resize(TAM_SAIDA, TAM_SAIDA);
            img2.resize(TAM_SAIDA, TAM_SAIDA);

//                nova2.showImage();
//                img2.showImage();

            Image imgOut = new Image(TAM_SAIDA * 2, TAM_SAIDA, 3);
            Image imgOut2 = new Image(TAM_SAIDA * 2, TAM_SAIDA, 3);

            for (int i = 0; i < TAM_SAIDA; i++) {
                for (int j = 0; j < TAM_SAIDA; j++) {
                    for (int b = 0; b < 3; b++) {
                        imgOut.setPixel(j, i, b, img2.getPixel(j, i, b));
                        imgOut2.setPixel(j, i, b, img2.getPixel(j, i, b));
                        imgOut.setPixel(j + TAM_SAIDA, i, b, nova3.getPixel(j, i, b));
                        imgOut2.setPixel(j + TAM_SAIDA, i, b, nova2.getPixel(j, i));
                    }
                }

                imgOut.exportImage(juntosColorido.getAbsolutePath() + "/" + files[p].getName());
                imgOut2.exportImage(juntosGray.getAbsolutePath() + "/" + files[p].getName());

            }
        }
    }
}
