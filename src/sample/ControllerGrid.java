package sample;

import ai.Coup;
import ai.MultiLayerPerceptron;
import ai.SigmoidalTransferFunction;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static sample.Settings.getFile;

//import javax.swing.*;

public class ControllerGrid implements Initializable {

    @FXML private ImageView homeButton;
    @FXML private Button buttonPlayAgain;
    @FXML private Button buttonQuit;

    @FXML private Rectangle backgroundHider;
    @FXML private VBox backgroundHiderHolder;

    @FXML private Label labelVictoryOf;
    @FXML private Label textVictoryOf;
    @FXML private Label labelAgainst;
    @FXML private Label textAgainst;

    @FXML private ImageView placeHolder0;
    @FXML private ImageView placeHolder1;
    @FXML private ImageView placeHolder2;
    @FXML private ImageView placeHolder3;
    @FXML private ImageView placeHolder4;
    @FXML private ImageView placeHolder5;
    @FXML private ImageView placeHolder6;
    @FXML private ImageView placeHolder7;
    @FXML private ImageView placeHolder8;

    private List<ImageView> placeHolders;

    @FXML private ImageView placeHolderSelected0;
    @FXML private ImageView placeHolderSelected1;
    @FXML private ImageView placeHolderSelected2;
    @FXML private ImageView placeHolderSelected3;
    @FXML private ImageView placeHolderSelected4;
    @FXML private ImageView placeHolderSelected5;
    @FXML private ImageView placeHolderSelected6;
    @FXML private ImageView placeHolderSelected7;
    @FXML private ImageView placeHolderSelected8;

    private List<ImageView> placeHoldersSelected;

    @FXML private ImageView cross0;
    @FXML private ImageView cross1;
    @FXML private ImageView cross2;
    @FXML private ImageView cross3;
    @FXML private ImageView cross4;
    @FXML private ImageView cross5;
    @FXML private ImageView cross6;
    @FXML private ImageView cross7;
    @FXML private ImageView cross8;

    private List<ImageView> crosses;

    @FXML private Circle circle0;
    @FXML private Circle circle1;
    @FXML private Circle circle2;
    @FXML private Circle circle3;
    @FXML private Circle circle4;
    @FXML private Circle circle5;
    @FXML private Circle circle6;
    @FXML private Circle circle7;
    @FXML private Circle circle8;

    private List<Circle> circles;

    private boolean pionToPlay;  // true: cross; false: circle

    private static boolean aiGameMode; // true: joue contre ia, false: joue contre un autre joueur.

    public static void setAiGameMode(boolean bool) { aiGameMode = bool; }

    private double[] gameState;  // contient des null, Circle circleX ou des ImageView crossX (avec x in 0..8)

    private int[][][] posToVerify = {{{6, 3}, {1, 2}, {4, 8}},
                                     {{0, 2}, {4, 7}},
                                     {{0, 1}, {5, 8}, {4, 6}},
                                     {{0, 6}, {4, 5}},
                                     {{1, 7}, {3, 5}, {0, 8}, {2, 6}},
                                     {{2, 8}, {3, 4}},
                                     {{0, 3}, {7, 8}, {2, 4}},
                                     {{1, 4}, {6, 8}},
                                     {{2, 5}, {6, 7}, {0, 4}}};


    private MultiLayerPerceptron ai;

    private static ControllerGrid CG;

    public static ControllerGrid getCG(){
        return CG;

    }

    public ControllerGrid(){
        CG = this;

        gameState = new double[9];
        for(int i=0; i<9; i++){
            gameState[i] = Coup.EMPTY;
        }

        int[] layers = new int[Settings.getL()+2];
        layers[0]=9;
        for (int i=0; i<Settings.getL(); i++){
            layers[i+1] = Settings.getH();
        }
        layers[layers.length-1]=9;

        //ai = new MultiLayerPerceptron(layers, Settings.getLr(), new SigmoidalTransferFunction());
        System.out.println("File for ai is : " + getFile());
        ai = MultiLayerPerceptron.load(getFile());

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Arrived on grille");

        backgroundHiderHolder.setVisible(false);
        backgroundHider.setVisible(false);
        labelVictoryOf.setVisible(false);
        textVictoryOf.setVisible(false);
        labelAgainst.setVisible(false);
        textAgainst.setVisible(false);

        buttonPlayAgain.setVisible(false);
        buttonQuit.setVisible(false);

        pionToPlay = true;

        placeHolders = Arrays.asList(placeHolder0,placeHolder1, placeHolder2, placeHolder3, placeHolder4, placeHolder5, placeHolder6, placeHolder7, placeHolder8);
        placeHoldersSelected = Arrays.asList(placeHolderSelected0, placeHolderSelected1, placeHolderSelected2, placeHolderSelected3, placeHolderSelected4, placeHolderSelected5, placeHolderSelected6, placeHolderSelected7, placeHolderSelected8);
        crosses = Arrays.asList(cross0, cross1, cross2, cross3, cross4, cross5, cross6, cross7, cross8);
        circles = Arrays.asList(circle0, circle1, circle2, circle3, circle4, circle5, circle6, circle7, circle8);


        // un placeHolder survolé affiche le placeHolderSelected correspondant
        for (int i=0; i<placeHolders.size(); i++){
            int finalI = i;
            placeHolders.get(i).addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    System.out.println("PlaceHolder n°" + String.valueOf(finalI) + " selected");
                    placeHoldersSelected.get(finalI).setVisible(true);
                    event.consume();
                }
            });
        }

        // placeHoldersSelected caché par defaut
        // un placeHolderSelected non survolé se cache à nouveau
        // un placeHolderSelected cliqué affiche le bon pion correspondant, se cache et cache le placeHolder correspondant
        for (int i=0; i<placeHoldersSelected.size(); i++){
            placeHoldersSelected.get(i).setVisible(false);

            int finalI = i;
            placeHoldersSelected.get(i).addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    System.out.println("PlaceHolder n°" + String.valueOf(finalI) + " deselected");
                    placeHoldersSelected.get(finalI).setVisible(false);
                    event.consume();
                }
            });

            placeHoldersSelected.get(i).addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    System.out.println("PlaceHolder n°" + String.valueOf(finalI) + " clicked");
                    placeHoldersSelected.get(finalI).setVisible(false);
                    placeHolders.get(finalI).setVisible(false);
                    if (aiGameMode) {
                        crosses.get(finalI).setVisible(true);
                        gameState[finalI] = Coup.X;
                    } else {
                        if (pionToPlay) {
                            crosses.get(finalI).setVisible(true);
                            gameState[finalI] = Coup.X;
                        } else {
                            circles.get(finalI).setVisible(true);
                            gameState[finalI] = Coup.O;
                        }
                    }

                    displayGameState();
                    System.out.println("pion to play : " + (pionToPlay ? "Croix" : "Cercles"));
                    if (isGameFinished(finalI) == null) {  // Si la partie n'est pas finie, l'autre joueur ou l'IA joue.
                        pionToPlay = !pionToPlay;
                        int aiPionPLayed = -1;
                        if (aiGameMode){
                            aiPionPLayed = playAi();
                            if (aiPionPLayed != -1 && isGameFinished(aiPionPLayed) != null) {  // Si on ne peut plus jouer
                                for (int i : isGameFinished(aiPionPLayed)) {
                                    System.out.println(i);
                                }
                                hidePlaceholders();
                                backgroundHiderHolder.setVisible(true);
                                backgroundHider.setVisible(true);
                                System.out.println("les " + (pionToPlay ? "croix" : "cercles") + " (IA) ont gagné");
                            }
                            pionToPlay = !pionToPlay;
                        }


                    }else{  // Si on ne peut plus jouer
                        for(int i : isGameFinished(finalI)){
                            System.out.println(i);
                        }
                        hidePlaceholders();
                        backgroundHiderHolder.setVisible(true);
                        backgroundHider.setVisible(true);
                        System.out.println("les " + (pionToPlay ? "Croix" : "Cercles") + " ont gagné");
                        textVictoryOf.setText(pionToPlay ? "Croix" : "Cercles");
                        textAgainst.setText(pionToPlay ? "Cercles" : "Croix");
                        textVictoryOf.setTextFill(Color.web(pionToPlay ? "#3481eb" : "#d7292e"));
                        textAgainst.setTextFill(Color.web(pionToPlay ? "#d7292e" : "#3481eb"));
                        labelVictoryOf.setVisible(true);
                        labelAgainst.setVisible(true);
                        textVictoryOf.setVisible(true);
                        textAgainst.setVisible(true);

                        buttonPlayAgain.setVisible(true);
                        buttonQuit.setVisible(true);
                    }
                    event.consume();
                }
            });
        }

        for (ImageView currentCross : crosses){
            currentCross.setVisible(false);
        }

        for (Circle currentCircle : circles){
            currentCircle.setVisible(false);
        }
    }

    public void hidePlaceholders(){
        for (int i=0; i<placeHoldersSelected.size(); i++) {
            placeHoldersSelected.get(i).setVisible(false);
        }
        for (int i=0; i<placeHolders.size(); i++) {
            placeHolders.get(i).setVisible(false);
        }
    }

    public void showPlaceholders(){
        for (int i=0; i<placeHolders.size(); i++) {
            if(gameState[i] == Coup.EMPTY) {
                placeHolders.get(i).setVisible(true);
            }
        }
    }

    private int playAi() {
        System.out.println("C'est au tour de l'ia");
        hidePlaceholders();

        double[] gameStateIaOut = ai.forwardPropagation(gameState);
        for(int i=0; i<gameStateIaOut.length; i++){
            System.out.println(i+" : " +gameStateIaOut[i]+" "+gameState[i]);
        }

        int caseToPlay = 0;
        int i=0;
        for(; i<gameStateIaOut.length; i++){
            if(gameState[i] == 0){
                caseToPlay = i;
                break;
            }
        }
        for(; i<gameStateIaOut.length; i++){
            if ((gameStateIaOut[i] > gameStateIaOut[caseToPlay]) && (gameState[i] == 0)){
                caseToPlay = i;
            }
        }
        System.out.println("Max is  " + caseToPlay);
        gameState[caseToPlay] = Coup.O;
        circles.get(caseToPlay).setVisible(true);

        showPlaceholders();

        return caseToPlay;
    }

    private void displayGameState() {
        for(int i=0; i<gameState.length; i++){
            if(i%3 == 0){
                System.out.print("\n");
            }
            if(gameState[i] == 0){
                System.out.print("   ");
            }else if(gameState[i] == Coup.O){
                System.out.print(" O ");
            }else{
                System.out.print(" X ");
            }
        }
        System.out.print("\n");
    }



    private int[] isGameFinished(int posPLayed){
        int intPionToPlay = pionToPlay ? Coup.X : Coup.O;
        for(int[] currentPosToVerify : posToVerify[posPLayed]){
            System.out.println(intPionToPlay + " " + gameState[currentPosToVerify[0]] + " " + gameState[currentPosToVerify[1]]);
            if(gameState[currentPosToVerify[0]] == intPionToPlay && gameState[currentPosToVerify[1]] == intPionToPlay){
                return new int[]{posPLayed, currentPosToVerify[0], currentPosToVerify[1]};
            }
        }

        for(int i=0; i<gameState.length; i++){
            if(gameState[i] == Coup.EMPTY) return null;  // Si on peut encore jouer
        }

        return new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};  // Si il n'y a pas de gagnant, on retourne toute les cases pour les animer
    }


    public void changePageToPageSample() throws IOException {
        PageLoader.changePage("../view/home.fxml", this);
    }

    public void changePageToGrid() throws IOException {
        PageLoader.changePage("../view/InGame/grid.fxml", this);
    }


    public void addCircle() throws IOException {
        System.out.println("Vous posez un pion Cercle");
        placeHoldersSelected.get(0).setVisible(true);
    }

    public void unselectPlaceHolder() throws IOException {
        System.out.println("placeHolder désélectionné");
        placeHoldersSelected.get(0).setVisible(false);
    }
}
