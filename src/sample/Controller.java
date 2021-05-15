package sample;

import ai.Coup;
import ai.MultiLayerPerceptron;
import ai.SigmoidalTransferFunction;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Scanner;

import static ai.Test.loadCoupsFromFile;
import static sample.Settings.difficulty;



public class Controller implements Initializable  {

    @FXML
    private Button lauchButton;

    @FXML
    Text percentText;


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

    public void initialize(URL location, ResourceBundle resources) {
        makeMenuLoad();
    }

    public void makeMenuLoad(){
        Settings settings=new Settings();
        ArrayList<String> diff=settings.readDifficulties();
        difficultyButton.getItems().clear();
        if (diff!=null){
            for (int i=0;i<diff.size();i++){
                MenuItem m=new MenuItem("Load "+diff.get(i));
                m.setId(diff.get(i));
                difficultyButton.getItems().add(m);
                m.setOnAction(loadMenu);

            }
        }
    }

    EventHandler<ActionEvent> loadMenu = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e)
        {
            System.out.println(((MenuItem)e.getSource()).getText() + " selected");
            Settings settings=new Settings();
            difficulty=((MenuItem)e.getSource()).getId();
            settings.launchIA(((MenuItem)e.getSource()).getId(),mainProgressBar,percentText);
        }
    };

    /*
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
*/

    public void launchGameAgainstPlayer() throws IOException {
        System.out.println("Joueur contre joueur");

        System.out.println("Lancement de la partie");
        ControllerGrid.setAiGameMode(false);
        PageLoader.changePage("../view/InGame/grid.fxml", this);
    }

    public void launchGameAgainstIA() throws IOException {
        System.out.println("Joueur contre IA");
        Settings settings=new Settings();
        if(settings.launchIA(difficulty,mainProgressBar,percentText)){
            ControllerGrid.setAiGameMode(true);
            PageLoader.changePage("../view/InGame/grid.fxml", this);
        }
        else{
            System.out.println("Veuillez sélectionner une difficulté");
        }
        //System.out.println(difficulty);

    }



    public void changePageToSettings() throws IOException {
        Media sound = new Media(new File("./resources/sounds/glass_001.mp3").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
        PageLoader.changePage("../view/settings.fxml", this);
    }

}
