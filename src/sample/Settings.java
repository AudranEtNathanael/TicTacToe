package sample;

import ai.Coup;
import ai.MultiLayerPerceptron;
import ai.SigmoidalTransferFunction;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import static ai.Test.loadCoupsFromFile;

public class Settings {

    private static HashMap<String,String[]> conf;
    public static String difficulty="F";
    private static String name="";
    private static String file="";

    private static int h = 0;
    private static double lr = 0.0;
    private static int l = 0;
    private static ProgressBar progressBar;
    private static Text progressText;


    public static int getH() {
        return h;
    }

    public static double getLr() {
        return lr;
    }

    public static int getL() {
        return l;
    }

    public static String getFile(){
        return file;
    }
    public static HashMap<String, String[]> getConf() {
        return conf;
    }

    public Task<Void> task = new Task<Void>() {


            @Override
            protected Void call() throws Exception {
                int ht=h;
                double lrt=lr;
                int lt=l;
                String namet=name;
                String filet=file;
                int[] layers=new int[lt+2];
                layers[0]=9;
                for (int i=0; i<l;i++){
                    layers[i+1]=ht;
                }
                layers[layers.length-1]=9;

                try {
                    progressBar.setVisible(true);
                    progressText.setVisible(true);
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

                        if (i%100==0){
                            updateMessage(i * 100 / epochs + "%");
                        }
                        if (i % 10000 == 0) {
                            System.out.println("Error at step " + i + " is " + (error / (double) i));
                            //mainTextArea.setText(i*100/epochs + "% : Error at step "+i+" is "+ (error/(double)i));
                            //progressBar.setProgress(i/epochs);
                        }
                        updateProgress(i, epochs);
                    }
                    updateMessage(100+"%");
                    error /= epochs;
                    if (epochs > 0) {
                        System.out.println("Error is " + error);
                    }
                    //
                    System.out.println("Learning completed!");

                    net.save(filet);
                    Thread.sleep(1000);
                    progressBar.setVisible(false);
                    progressText.setVisible(false);
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

   public boolean launchIA(String difficulte, ProgressBar progressBar, javafx.scene.text.Text text) {
       if(readConf(difficulte)){
           if (new File(file).exists()){
               MultiLayerPerceptron net=MultiLayerPerceptron.load(file);
               System.out.println("Modele charge");
               return true;
           }
           else{
               System.out.println("Creer nouveau modele");
               progressText=text;
               task.messageProperty().addListener((obs, oldMsg, newMsg) -> {
                   progressText.setText(newMsg);
               });
               Settings.progressBar=progressBar;
               progressBar.progressProperty().unbind();
               progressBar.progressProperty().bind(task.progressProperty());
               Thread thread=new Thread(task);
               thread.start();
               //thread.join();
               // new Thread(task).start();
               System.out.println("Fini");
               return false;
           }
       }
       return false;
   }

   public ArrayList<String> readDifficulties(){
       try {
           File myObj = new File("./resources/config.txt");
           Scanner myReader = new Scanner(myObj);
           System.out.println("Fichier de configuration trouve");
           ArrayList<String> difficulties=new ArrayList<>();
           while (myReader.hasNextLine()) {
               String data = myReader.nextLine();
               String[] confLine=data.split(":");
               difficulties.add(confLine[0]);
           }
           myReader.close();
           return difficulties;
       } catch (FileNotFoundException e) {
           System.out.println("Fichier de configuration pas trouve");
           e.printStackTrace();
       }
       return null;
   }

    public boolean readConf(String difficulte){
        try {
            File myObj = new File("./resources/config.txt");
            conf=new HashMap<>();
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
                return false;
            }
            else{
                //System.out.println(searchConfig[0]);
                h= Integer.valueOf(searchConfig[1]);
                lr= Double.valueOf(searchConfig[2]) ;
                l=Integer.valueOf(searchConfig[3]);
                name=searchConfig[0];
                System.out.println("Difficulté :"+ difficulty+","+h+","+lr+","+l);
                File dossier = new File("./resources/models/");
                boolean res = dossier.mkdir();
                if(res) {
                    System.out.println("Le dossier a été créé.");
                }
                else {
                    System.out.println("Le dossier existe déja.");
                }
                file="./resources/models/"+"mlp_"+name+"_"+h+"_"+lr+"_"+l+".srl";
                return true;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Fichier de configuration pas trouve");
            e.printStackTrace();
        }
        return false;
    }

    public void delete(String config){
            boolean find=true;
            if (find==false){
                System.out.println("Config souhaite non trouve");
            }
            else{
                file="./resources/models/"+config;
                File fileToDelete = new File(file);
                if (fileToDelete.delete()){
                    System.out.println(fileToDelete.getName() + " est supprimé.");

                }
                else{
                    System.out.println("Rien a supprimer");
                }
            }

    }




}
