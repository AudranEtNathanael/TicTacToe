package sample;

import ai.Coup;
import ai.MultiLayerPerceptron;
import ai.SigmoidalTransferFunction;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

import static ai.Test.loadCoupsFromFile;

//import javax.swing.*;

public class Controller {

    @FXML
    private Button lauchButton;

    @FXML
    private TextArea mainTextArea;

    @FXML
    ProgressBar mainProgressBar;

    @FXML
    ProgressIndicator mainProgressIndicator;

    @FXML
    Button changePage;

    @FXML
    MenuButton difficultyButton;

    @FXML
    Rectangle mainRectangle;


    Task<Void> task = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            //Test.test(9, mainTextArea);
            /*
            final int maxIterations = 100;
            int iterations = 0;
            for (iterations = 0; iterations < maxIterations; iterations++) {
                // Arrêt de la boucle si la tâche est annulée.
                /*
                if (isCancelled()) {
                    break;
                }
                * /
                // On change l'avancement de la tâche.
                System.out.println(iterations);
                updateProgress(iterations, maxIterations);
                Thread.sleep(1000);
            }
            */
            int size = 9;

            try {

                System.out.println();
                System.out.println("START TRAINING ...");
                System.out.println();
                mainTextArea.setText("START TRAINING ...");
                //
                int[] layers = new int[]{ size, 128, size };
                //
                double error = 0.0 ;
                MultiLayerPerceptron net = new MultiLayerPerceptron(layers, 0.1, new SigmoidalTransferFunction());
                double epochs = 100000;//1000000000 ;

                System.out.println("---");
                mainTextArea.setText("---");
                System.out.println("Load data ...");
                mainTextArea.setText("Load data ...");
                HashMap<Integer, Coup> mapTrain = loadCoupsFromFile("./resources/train_dev_test/train.txt");
                HashMap<Integer, Coup> mapDev = loadCoupsFromFile("./resources/train_dev_test/dev.txt");
                HashMap<Integer, Coup> mapTest = loadCoupsFromFile("./resources/train_dev_test/test.txt");
                System.out.println("---");
                mainTextArea.setText("---");
                //TRAINING ...

                for(int i = 0; i < epochs; i++){

                    Coup c = null ;
                    while ( c == null )
                        c = mapTrain.get((int)(Math.round(Math.random() * mapTrain.size())));

                    error += net.backPropagate(c.in, c.out);

                    if ( i % 10000 == 0 ) {
                        System.out.println("Error at step "+i+" is "+ (error/(double)i));
                        //mainTextArea.setText(i*100/epochs + "% : Error at step "+i+" is "+ (error/(double)i));
                        updateMessage(i*100/epochs + "% : Error at step "+i+" is "+ (error/(double)i));
                        //progressBar.setProgress(i/epochs);*
                    }
                    updateProgress(i, epochs);
                }
                error /= epochs ;
                if ( epochs > 0 ) {
                    System.out.println("Error is " + error);
                    mainTextArea.setText("Error is " + error);
                }
                //
                System.out.println("Learning completed!");
                mainTextArea.setText("Error is " + error);

                /*
                //TEST ...
                double[] inputs = new double[]{0.0, 1.0};
                double[] output = net.forwardPropagation(inputs);

                System.out.println(inputs[0]+" or "+inputs[1]+" = "+Math.round(output[0])+" ("+output[0]+")");
                mainTextArea.setText(inputs[0]+" or "+inputs[1]+" = "+Math.round(output[0])+" ("+output[0]+")");
                */

            }
            catch (Exception e) {
                System.out.println("Test.test()");
                mainTextArea.setText("Test.test()");
                e.printStackTrace();
                System.exit(-1);
            }

            return null;
        }
    };




    public void trainExec(ActionEvent event) {
        lauchButton.setVisible(false);
        //mainRectangle.setVisible(false);

        task.messageProperty().addListener((obs, oldMsg, newMsg) -> {
            mainTextArea.setText(newMsg);
        });

        mainProgressBar.progressProperty().unbind();
        mainProgressBar.progressProperty().bind(task.progressProperty());

        mainProgressIndicator.progressProperty().unbind();
        mainProgressIndicator.progressProperty().bind(task.progressProperty());

        new Thread(task).start();

        /*
        updateText();
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                Test.test(9, mainTextArea, mainProgressBar);
            }
        });
        th.start();
        */
    }

    public void chooseEasy(){
        System.out.println("Facile");
        difficultyButton.setText("Facile");
    }

    public void chooseHard(){
        System.out.println("Difficile");
        difficultyButton.setText("Difficile");
    }
    public void updateText() {
        mainTextArea.setText("Lancement de l'apprentissage");
    }

    public void changePage() throws IOException {
        System.out.println("Vous changez de page");

        Stage stage=Main.getPrimaryStage();
        Parent root = FXMLLoader.load(getClass().getResource("../view/settings.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
