package sample;

import ai.Coup;
import ai.MultiLayerPerceptron;
import ai.SigmoidalTransferFunction;
import javafx.concurrent.Task;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

import static ai.Test.loadCoupsFromFile;

public class Settings {

    public static String difficulty="F";
    private static String name="";
    private static String file="";
    private static int h = 0;
    private static double lr = 0.0;
    private static int l = 0;

   public Task<Void> task = new Task<Void>() {


            @Override
            protected Void call() throws Exception {
                int ht=h;
                double lrt=lr;
                int lt=l;
                String namet=name;
                String filet=file;

                int[] layers=new int[l+2];
                layers[0]=9;
                for (int i=0; i<l;i++){
                    layers[i+1]=h;
                }
                layers[layers.length-1]=9;

                try {

                    System.out.println();
                    System.out.println("START TRAINING ...");
                    System.out.println();
                    //
                    //
                    double error = 0.0;
                    MultiLayerPerceptron net = new MultiLayerPerceptron(layers, lrt, new SigmoidalTransferFunction());
                    double epochs = 100000;//1000000000 ;

                    System.out.println("---");
                    System.out.println("Load data ...");
                    HashMap<Integer, Coup> mapTrain = loadCoupsFromFile("./resources/train_dev_test/train.txt");
                    HashMap<Integer, Coup> mapDev = loadCoupsFromFile("./resources/train_dev_test/dev.txt");
                    HashMap<Integer, Coup> mapTest = loadCoupsFromFile("./resources/train_dev_test/test.txt");
                    System.out.println("---");
                    //TRAINING ...

                    for (int i = 0; i < epochs; i++) {

                        Coup c = null;
                        while (c == null)
                            c = mapTrain.get((int) (Math.round(Math.random() * mapTrain.size())));

                        error += net.backPropagate(c.in, c.out);

                        if (i % 10000 == 0) {
                            System.out.println("Error at step " + i + " is " + (error / (double) i));
                            //mainTextArea.setText(i*100/epochs + "% : Error at step "+i+" is "+ (error/(double)i));
                            updateMessage(i * 100 / epochs + "% : Error at step " + i + " is " + (error / (double) i));
                            //progressBar.setProgress(i/epochs);*
                        }
                        updateProgress(i, epochs);
                    }
                    error /= epochs;
                    if (epochs > 0) {
                        System.out.println("Error is " + error);
                    }
                    //
                    System.out.println("Learning completed!");

                    net.save(filet);
                /*
                //TEST ...
                double[] inputs = new double[]{0.0, 1.0};
                double[] output = net.forwardPropagation(inputs);

                System.out.println(inputs[0]+" or "+inputs[1]+" = "+Math.round(output[0])+" ("+output[0]+")");
                mainTextArea.setText(inputs[0]+" or "+inputs[1]+" = "+Math.round(output[0])+" ("+output[0]+")");
                */

                } catch (Exception e) {
                    System.out.println("Test.test()");
                    e.printStackTrace();
                    System.exit(-1);
                }

                return null;
            }
        };


    public void readConf(String difficulte){
        try {
            File myObj = new File("./resources/config.txt");
            HashMap<String,String[]> conf=new HashMap<>();
            Scanner myReader = new Scanner(myObj);
            System.out.println("Fichier de configuration trouve");
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] confLine=data.split(":");
                conf.put(confLine[0],confLine);
            }
            String[] searchConfig=conf.get(difficulte);
            myReader.close();
            if (searchConfig==null){
                System.out.println("Config souhaite non trouve");
            }
            else{
                System.out.println(searchConfig[0]);
                int h= Integer.valueOf(searchConfig[1]);
                double lr= Double.valueOf(searchConfig[2]) ;
                int l=Integer.valueOf(searchConfig[3]);
                name=searchConfig[0];
                file="./resources/models/"+"mlp_"+name+"_"+h+"_"+lr+"_"+l+".srl";
                if (new File(file).exists()){
                    MultiLayerPerceptron net=MultiLayerPerceptron.load(file);
                    System.out.println("Modele charge");
                }
                else{
                    System.out.println("Creer nouveau modele");

                    new Thread(task).start();
                    System.out.println("Fini");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Fichier de configuration pas trouve");
            e.printStackTrace();
        }
    }

    public void delete(String difficulte){
        try {
            File myObj = new File("./resources/config.txt");
            HashMap<String,String[]> conf=new HashMap<>();
            Scanner myReader = new Scanner(myObj);
            System.out.println("Fichier de configuration trouve");
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] confLine=data.split(":");
                conf.put(confLine[0],confLine);
            }
            String[] searchConfig=conf.get(difficulte);
            myReader.close();
            if (searchConfig==null){
                System.out.println("Config souhaite non trouve");
            }
            else{
                System.out.println(searchConfig[0]);
                int h= Integer.valueOf(searchConfig[1]);
                double lr= Double.valueOf(searchConfig[2]) ;
                int l=Integer.valueOf(searchConfig[3]);
                name=searchConfig[0];
                file="./resources/models/"+"mlp_"+name+"_"+h+"_"+lr+"_"+l+".srl";
                File fileToDelete = new File(file);
                if (fileToDelete.delete()){
                    System.out.println(fileToDelete.getName() + " est supprimé.");

                }
                else{
                    System.out.println("Rien a supprimer");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Fichier de configuration pas trouve");
            e.printStackTrace();
        }
    }




}
