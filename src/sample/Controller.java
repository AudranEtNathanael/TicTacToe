package sample;

import ai.Coup;
import ai.MultiLayerPerceptron;
import ai.SigmoidalTransferFunction;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static ai.Test.loadCoupsFromFile;
import static sample.Settings.*;


public class Controller implements Initializable  {

    // Boutons de contrôle
    @FXML private ImageView settings;
    @FXML private ImageView playAgainstAi;
    @FXML private ImageView playWithFriend;
    @FXML private ImageView soundControl;

    List<Node> controls;

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
        Settings.initializeSoundIcon(soundControl);
        controls = Arrays.asList(settings, playAgainstAi, playWithFriend, soundControl);

        for(int i=0; i<controls.size(); i++) {
            int finalI = i;
            controls.get(finalI).addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    /*
                    ColorAdjust colorAdjust = new ColorAdjust();
                    colorAdjust.setContrast(0.3);
                    colorAdjust.setHue(-0.05);
                    colorAdjust.setBrightness(0.9);
                    colorAdjust.setSaturation(0.7);
                    homeButton.setEffect(colorAdjust);

                     */

                    final int UI_ANIMATION_TIME_MSEC = 100;

                    final double MIN_RADIUS = 0.0;
                    final double MAX_RADIUS = 2.0;
                    // Create Gaussian Blur effect with radius = 0
                    GaussianBlur blur = new GaussianBlur(MIN_RADIUS);
                    controls.get(finalI).setEffect(blur);

                    // Create animation effect
                    Timeline timeline = new Timeline();
                    KeyValue kv = new KeyValue(blur.radiusProperty(), MAX_RADIUS);
                    KeyFrame kf = new KeyFrame(Duration.millis(UI_ANIMATION_TIME_MSEC), kv);
                    timeline.getKeyFrames().add(kf);
                    timeline.play();
                }
            });

            controls.get(finalI).addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    final double RADIUS = 0.0;
                    // Create Gaussian Blur effect with radius = 0
                    GaussianBlur blur = new GaussianBlur(RADIUS);
                    controls.get(finalI).setEffect(blur);
                }
            });
        }
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
            playClickSound();
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
        playClickSound();
        System.out.println("Lancement de la partie");
        ControllerGrid.setAiGameMode(false);
        PageLoader.changePage("../view/grid.fxml", this);
    }

    public void launchGameAgainstIA() throws IOException {
        System.out.println("Joueur contre IA");
        playClickSound();
        Settings settings=new Settings();
        if(settings.launchIA(difficulty,mainProgressBar,percentText)){
            ControllerGrid.setAiGameMode(true);
            PageLoader.changePage("../view/grid.fxml", this);
        }
        else{
            System.out.println("Veuillez sélectionner une difficulté");
        }
        //System.out.println(difficulty);

    }

    public void clickSound(){
        playClickSound();
        Settings.clickOnSoundIcon(soundControl);
    }

    public void click(){
        playClickSound();
    }

    public void changePageToSettings() throws IOException {

        playClickSound();
        PageLoader.changePage("../view/settings.fxml", this);
    }

}
