package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;


//import javax.swing.*;

public class ControllerParametres {

    @FXML
    Button retour;

    private String name="";
    private String file="";
    private int h = 0;
    private double lr = 0.0;
    private int l = 0;

    public void changePage() throws IOException {
        System.out.println("Vous changez de page");

        Stage stage=Main.getPrimaryStage();
        Parent root = FXMLLoader.load(getClass().getResource("../view/home.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("TicTacToe");

        stage.show();
    }

    public void loadFacile(){
        Settings settings=new Settings();
        settings.readConf("F");
    }
    public void loadDifficile(){
        Settings settings=new Settings();
        settings.readConf("D");
    }

    public void deleteFacile(){
        Settings settings=new Settings();
        settings.delete("F");
    }

    public void deleteDifficile(){
        Settings settings=new Settings();
        settings.delete("D");
    }

}
