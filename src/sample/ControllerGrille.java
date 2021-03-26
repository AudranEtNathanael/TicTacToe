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

public class ControllerGrille {

    @FXML
    private Button homeButton;

    public void changePageToPageSample() throws IOException {
        changePage("sample.fxml");
    }


    public void changePage(String pageName) throws IOException {
        System.out.println("Vous changez de page");

        Stage stage=Main.getPrimaryStage();
        Parent root = FXMLLoader.load(getClass().getResource(pageName));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
