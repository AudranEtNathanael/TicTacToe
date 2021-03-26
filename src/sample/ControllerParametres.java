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
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

import static ai.Test.loadCoupsFromFile;

//import javax.swing.*;

public class ControllerParametres {

    @FXML
    Button retour;



    public void changePage() throws IOException {
        System.out.println("Vous changez de page");

        Stage stage=Main.getPrimaryStage();
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("TicTacToe");
        stage.show();
    }
}
