package com.ContagemPorDensidade;

import com.ImageManipulation.Gradiente;
import image.Image;

import java.io.File;
import java.util.HashMap;

import static image.Image.MorphologyConstants;

public class ContagemReconstruçãoMorfológica {
    static final int THRESHOLD = 10;
    static final int TAM_SAIDA = 256;

    public static void dilatar(float[][] matriz, int j, int i) {
        if (j - 1 >= 0) {
            if (matriz[i][j - 1] < matriz[i][j] && matriz[i][j - 1] > 0) {
                matriz[i][j - 1] = matriz[i][j];
                dilatar(matriz, j - 1, i);
            }
        }

        if (j + 1 < matriz[0].length) {
            if (matriz[i][j + 1] < matriz[i][j] && matriz[i][j + 1] > 0) {
                matriz[i][j + 1] = matriz[i][j];
                dilatar(matriz, j + 1, i);
            }
        }

        if (i - 1 >= 0) {
            if (matriz[i - 1][j] < matriz[i][j] && matriz[i - 1][j] > 0) {
                matriz[i - 1][j] = matriz[i][j];
                dilatar(matriz, j, i - 1);
            }
        }

        if (i + 1 < matriz.length) {
            if (matriz[i + 1][j] < matriz[i][j] && matriz[i + 1][j] > 0) {
                matriz[i + 1][j] = matriz[i][j];
                dilatar(matriz, j, i + 1);
            }
        }
        //diagonal
    }

    public static void main(String[] args) throws Exception {
        String path = "/Users/joaomazzochin/Documents/University/TCC/Pix2Pix Results/ColortoColorErode18/20train10test/fake";
//        String path = "/Users/joaomazzochin/Documents/University/TCC/Database/Results-Training/ColortoColorErode18/20train10test/fake";
//        String path = "/Users/joaomazzochin/Documents/University/TCC/Database/Results-Training/ColortoGray/20train10test/fake";
//        String path = "/Users/joaomazzochin/Documents/University/TCC/Database/Results-Training/ColortoGrayErode18/20train10test/fake";
//        String path = "/Users/joaomazzochin/Documents/University/TCC/Database/Results-Training/GradienteColorido/20train10test/onlyRed";
//        String path = "/Users/joaomazzochin/Documents/University/TCC/Database/Results-Training/GradienteGray/20train10test/onlyGray";
//        String path = "/Users/joaomazzochin/image"; //teste
        File folder = new File(path);
        File[] files = folder.listFiles();

        for (int p = 0; p < files.length; p++) {
            if (!files[p].getAbsolutePath().contains(".png")) continue;

            Image img = new Image(files[p]);
            img.convertToGray();
            img.erode(MorphologyConstants.STRUCT_SUP, 15);
            img.resize(TAM_SAIDA, TAM_SAIDA);

            //achar os clusters
            HashMap<Float, Gradiente.Vetor2D> todosClusters = new HashMap<Float, Gradiente.Vetor2D>(); //clusterid, respectivos pontos do cluster
            float[][] matriz = new float[img.getHeight()][img.getWidth()];

            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[0].length; j++) {
                    if (img.getPixel(j, i, 0) > THRESHOLD)
                        matriz[i][j] = i * matriz[0].length + j;
                    else
                        matriz[i][j] = 0;
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

            //calcular centroides
            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[0].length; j++) {

                    if (matriz[i][j] > 0) {
                        Gradiente.Vetor2D nVetor = new Gradiente.Vetor2D(j, i);
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

            Image image1 = new Image(matrizInt);
            System.out.println("Image name: " + files[p].toString().replace(path, ""));
            System.out.println(todosClusters.size());
            image1.exportImage("/Users/joaomazzochin/image"+files[p].toString().replace(path, ""));

        }
    }
}
