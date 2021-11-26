package com.Indices;

import image.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static int TAMANHO = 256;
    private static double TP = 0;
    private static double TN = 0;
    private static double FP = 0;
    private static double FN = 0;
    private static final int T = 70; //0-80
    private static final String BASE_PATH = "/Users/joaomazzochin/Documents/University/TCC/Pix2Pix Results/ColortoColor/20train10test";
    private static List<Double> ac = new ArrayList<Double>();
    private static List<Double> f1 = new ArrayList<Double>();
    private static List<Double> kappa = new ArrayList<Double>();
    private static List<Double> iou = new ArrayList<Double>();

    public static void main(String[] args) throws Exception {

        String pathReal = BASE_PATH + "/real";
        File folderReal = new File(pathReal); //real images
        File[] filesReal = folderReal.listFiles();

        for (int k=0; k<filesReal.length; k++){
            TP = 0;
            TN = 0;
            FP = 0;
            FN = 0;
            if(!filesReal[k].getAbsolutePath().contains(".png")) continue;

            Image imgReal = new Image(filesReal[k]);

            String pathFake = filesReal[k].getAbsolutePath().replace("real", "fake");
            File folderFake = new File(pathFake);
            System.out.println(pathFake);
            System.out.println(folderFake.getAbsolutePath());
            Image imgFake = new Image(folderFake);

            for (int i=0; i<TAMANHO; i++){
                for (int j=0; j<TAMANHO; j++){
                    if (imgReal.getPixel(i,j,0) > imgReal.getPixel(i,j,1)// real: red
                            && imgReal.getPixel(i,j,0) > imgReal.getPixel(i,j,2)) {//(4,0,0)
                        if (imgFake.getPixel(i,j,0) > imgFake.getPixel(i,j,1) // fake: red
                                && imgFake.getPixel(i,j,0) > imgFake.getPixel(i,j,2)
                                && imgFake.getPixel(i,j,0) > T) {
                            TP++;
                        } else { //fake: !red
                            FN++;
                        }
                    }
                    if (imgReal.getPixel(i,j,0) == imgReal.getPixel(i,j,1) //real: preta
                            && imgReal.getPixel(i,j,1) == imgReal.getPixel(i,j,2)) { //(4,0,0)
                        if (imgFake.getPixel(i,j,0) <= T
                                && imgFake.getPixel(i,j,1) <= T
                                && imgFake.getPixel(i,j,2) <= T
                        ) {
                            TN++;
                        } else { //fake: !preta
                            FP++;
                        }
                    }
                }
            }

            System.out.println(
                    filesReal[k].getAbsolutePath().replace(
                            BASE_PATH+"/real/", "")
                            + " = " +
                            folderFake.getAbsolutePath().replace(
                                    BASE_PATH+"/fake/", "")
            );

            System.out.println("TP:" + TP);
            System.out.println("TN:" + TN);
            System.out.println("FP:" + FP);
            System.out.println("FN:" + FN);
            System.out.println("Total Pixels:" + (TP + TN + FP + FN));
            double aci = (TP + TN) / (TP + TN + FP + FN);
            System.out.println("ACURÁCIA: " + aci*100);

            //f1 1
            double precision = (TP/(TP+FP));
            double recall = (TP/(TP+FN));
            double f1i = (2 * precision * recall) / (precision+recall);
            System.out.println("F1: " + f1i*100);

            //kappa 1
            double po = (TP + TN) / (TP + TN + FP + FN);
            double pe = ((TP + FN) * (TP + FP) + (FP + TN) * (FN + TN)) / Math.pow((TP + TN + FP + FN),2);
            double kappai = (po - pe) / (1 - pe);
            System.out.println("KAPPA: " + kappai*100);

            //IoU
            double interOverUnion = (TP / (TP+FP+FN));
            System.out.println("IoU: " + interOverUnion*100);

            System.out.println("*****************************");
            ac.add(aci);
            f1.add(f1i);
            kappa.add(kappai);
            iou.add(interOverUnion);
        }

        System.out.println("===========================");
        double accuracy = ac.stream().mapToDouble(a -> a).sum();
        double f11 = f1.stream().mapToDouble(a -> a).sum();
        double kappaa = kappa.stream().mapToDouble(a -> a).sum();
        double iou1 = iou.stream().mapToDouble(a -> a).sum();

        System.out.println("ACCURACY total: "+ (accuracy/ac.toArray().length)*100);
        System.out.println("F1 total: "+ (f11/f1.toArray().length)*100);
        System.out.println("KAPPA total: "+ (kappaa/kappa.toArray().length)*100);
        System.out.println("IoU total: "+ (iou1/iou.toArray().length)*100);
    }

}

