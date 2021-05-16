package sample;

import ai.Coup;
import ai.MultiLayerPerceptron;
import ai.SigmoidalTransferFunction;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
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

    private static boolean sound=true;


    /**
     *
      * @return boolean
     *
     * getter for sound
     */
    public static boolean getSound(){
        return sound;
    }

    /**
     *
     * @param b
     *
     * setter for sound
     */
    public static void setSound(boolean b){
       sound=b;
    }

    /**
     *
     * @return h
     */
    public static int getH() {
        return h;
    }

    /**
     *
     * @return lr
     */
    public static double getLr() {
        return lr;
    }

    /**
     *
     * @return l
     */
    public static int getL() {
        return l;
    }

    /**
     *
     * @return file
     */
    public static String getFile(){
        return file;
    }

    /**
     *
     * @return conf in config.txt
     */
    public static HashMap<String, String[]> getConf() {
        return conf;
    }

    /**
     *
     * @param imageSound
     *
     * change imageSound en fonction du booleen sound
     */
    public static void initializeSoundIcon(ImageView imageSound)   {
        if(sound){  //si le son est activé
            try {
                Image image = new Image(new FileInputStream(System.getProperty("user.dir")+"/src/view/icon/soundOn.PNG"));
                imageSound.setImage(image);     //affiche l image SoundOn
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                Image image = new Image(new FileInputStream(System.getProperty("user.dir")+"/src/view/icon/soundOff.PNG"));
                imageSound.setImage(image);         //affiche l image SoundOff
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param imageSound
     *
     * change la valeur du booleen sound
     * change avec l image correspondante
     */
    public static void clickOnSoundIcon(ImageView imageSound){
        if(sound){              //si le son est active
            setSound(false);    //desactive le son
            try {
                Image image = new Image(new FileInputStream(System.getProperty("user.dir")+"/src/view/icon/soundOff.PNG"));
                imageSound.setImage(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else{
            setSound(true);     //active le son
            playClickSound();   //joue le son du clic
            try {
                Image image = new Image(new FileInputStream(System.getProperty("user.dir")+"/src/view/icon/soundOn.PNG"));
                imageSound.setImage(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param ressource
     * joue un son
     */
    public static void playSound(String ressource){
        if (sound){
            Media sound = new Media(new File(ressource).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
        }
    }

    /**
     * joue le son de clic
     */
    public static void playClickSound(){
        Settings.playSound("./resources/sounds/glass_001.mp3");
    }

    /**
     * joue le son d'erreur
     */
    public static void playErrorSound(){
        Settings.playSound("./resources/sounds/error_006.mp3");
    }


    /**
     * task où l'on entraine l'IA
     */
    public Task<Void> task = new Task<Void>() {


            @Override
            protected Void call() throws Exception {
                int ht=h;       //recuperation des valeurs pour l'IA
                double lrt=lr;
                int lt=l;
                String namet=name;
                String filet=file;      //fichier où ca sera sauvegarde
                int[] layers=new int[lt+2];
                layers[0]=9;
                for (int i=0; i<l;i++){
                    layers[i+1]=ht;
                }
                layers[layers.length-1]=9;

                try {
                    progressBar.setVisible(true);           //affiche la progressBar
                    progressText.setVisible(true);          //affiche le text
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
                            updateMessage(i * 100 / epochs + "%");  //modif le message
                        }
                        if (i % 10000 == 0) {
                            System.out.println("Error at step " + i + " is " + (error / (double) i));
                            //mainTextArea.setText(i*100/epochs + "% : Error at step "+i+" is "+ (error/(double)i));
                            //progressBar.setProgress(i/epochs);
                        }
                        updateProgress(i, epochs);      //met a jour la progressBar
                    }
                    updateMessage(100+"%");
                    error /= epochs;
                    if (epochs > 0) {
                        System.out.println("Error is " + error);
                    }
                    //
                    System.out.println("Learning completed!");

                    net.save(filet);                    //sauvegarde le modele
                    Thread.sleep(1000);            //attend 1 sec
                    progressBar.setVisible(false);      //cache le text affichant le pourcentage
                    progressText.setVisible(false);     //cache la progressBAr
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

    /**
     *
     * @param difficulte
     * @param progressBar
     * @param text
     * @return true si la difficulté a été trouvé et que le modele existe deja
     * lance l'entrainement de l'IA si la difficulté est trouvé
     * et si le modele n'existe pas
     */
   public boolean launchIA(String difficulte, ProgressBar progressBar, javafx.scene.text.Text text) {
       if(readConf(difficulte)){                //si la difficulté souhaité est dans config.txt
           if (new File(file).exists()){        //si le modele existe deja
               MultiLayerPerceptron net=MultiLayerPerceptron.load(file);
               System.out.println("Modele charge");
               return true;
           }
           else{            //si le modele n'existe pas
               System.out.println("Creer nouveau modele");
               progressText=text;
               task.messageProperty().addListener((obs, oldMsg, newMsg) -> {
                   progressText.setText(newMsg);        //modif le text a chaque modification du message
               });
               Settings.progressBar=progressBar;
               progressBar.progressProperty().unbind();
               progressBar.progressProperty().bind(task.progressProperty());    //lie la progressBar a la task
               Thread thread=new Thread(task);
               thread.start();      //lance l entrainement
               //thread.join();
               // new Thread(task).start();
               System.out.println("Fini");
               return false;
           }
       }
       return false;
   }

    /**
     *
     * @return la liste des difficultés
     * recupere les difficultés présentes dans config.txt
     */
   public ArrayList<String> readDifficulties(){
       try {
           File myObj = new File("./resources/config.txt");
           Scanner myReader = new Scanner(myObj);
           System.out.println("Fichier de configuration trouve");
           ArrayList<String> difficulties=new ArrayList<>();
           while (myReader.hasNextLine()) {     //lecture de chaque ligne
               String data = myReader.nextLine();
               String[] confLine=data.split(":");
               difficulties.add(confLine[0]);       //ajout de la difficulté à la liste des difficultés
           }
           myReader.close();
           return difficulties;
       } catch (FileNotFoundException e) {
           System.out.println("Fichier de configuration pas trouve");
           e.printStackTrace();
       }
       return null;
   }

    /**
     *
     * @param difficulte
     * @return true si la difficulté est trouvé
     *  recupere les valeurs de h,lr et l correspondant à la difficulté
     *
     */
    public boolean readConf(String difficulte){
        try {
            File myObj = new File("./resources/config.txt");
            conf=new HashMap<>();   //la clé est la difficulté, et contient les valeurs de h, lr, l
            Scanner myReader = new Scanner(myObj);
            System.out.println("Fichier de configuration trouve");
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] confLine=data.split(":");
                conf.put(confLine[0],confLine);
            }
            String[] searchConfig=conf.get(difficulte);     //cherche la diffuclté dans la hashmap
            myReader.close();
            if (searchConfig==null){        //si la difficulté n'est pas trouvé
                System.out.println("Config souhaite non trouve");
                return false;
            }
            else{       // si elle est trouvé
                //System.out.println(searchConfig[0]);
                h= Integer.valueOf(searchConfig[1]);        //assigne les valeurs correspondant à la difficulté
                lr= Double.valueOf(searchConfig[2]) ;
                l=Integer.valueOf(searchConfig[3]);
                name=searchConfig[0];
                System.out.println("Difficulté :"+ difficulty+","+h+","+lr+","+l);
                File dossier = new File("./resources/models/");
                boolean res = dossier.mkdir();  //cree le dossier models si il n existe pas
                if(res) {
                    System.out.println("Le dossier a été créé.");
                }
                else {
                    System.out.println("Le dossier existe déja.");
                }
                file="./resources/models/"+"mlp_"+name+"_"+h+"_"+lr+"_"+l+".srl";   //nom et emplacement du modele
                return true;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Fichier de configuration pas trouve");
            e.printStackTrace();
        }
        return false;
    }

    /**
     *
     * @param config
     * supprime un modele
     */
    public void delete(String config){
            boolean find=true;
            if (find==false){
                System.out.println("Config souhaite non trouve");
            }
            else{
                file="./resources/models/"+config;
                File fileToDelete = new File(file);         //modele à supprimer
                if (fileToDelete.delete()){         //supprime le modele
                    System.out.println(fileToDelete.getName() + " est supprimé.");

                }
                else{       //si il n existe pas
                    System.out.println("Rien a supprimer");
                }
            }

    }




}
