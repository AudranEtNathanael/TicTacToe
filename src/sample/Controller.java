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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import static ai.Test.loadCoupsFromFile;
import static sample.Settings.difficulty;


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

    private String name="";
    private String file="";
    private int h = 0;
    private double lr = 0.0;
    private int l = 0;



    public void chooseEasy(){
        System.out.println("Facile");
        difficultyButton.setText("Facile");
        difficulty="F";
        Settings settings=new Settings();
        settings.readConf("F");
    }

    public void chooseHard(){
        System.out.println("Difficile");
        difficultyButton.setText("Difficile");
        difficulty="D";
        Settings settings=new Settings();
        settings.readConf("D");

    }

    public void launchaGame(){
        System.out.println("Lancement de la partie");
        System.out.println(difficulty);
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
