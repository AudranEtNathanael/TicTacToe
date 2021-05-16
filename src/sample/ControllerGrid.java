package sample;

import ai.Coup;
import ai.MultiLayerPerceptron;
import javafx.animation.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static sample.Settings.getFile;

//import javax.swing.*;

public class ControllerGrid implements Initializable {

    // Boutons de contrôle
    @FXML private ImageView homeButton;
    @FXML private ImageView soundControl;
    @FXML private Button buttonPlayAgain;
    @FXML private Button buttonQuit;

    List<Node> controls;

    // Ecran de fin  (Sur la meme scène pour avoir la partie en arrière plan)
    @FXML private Rectangle backgroundHider;
    @FXML private VBox backgroundHiderHolder;
    @FXML private Label labelVictoryOf;
    @FXML private Label textVictoryOf;
    @FXML private Label labelAgainst;
    @FXML private Label textAgainst;

    // PlaceHolders non séléctionné pour les pions
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

    // PlaceHolders séléctionné pour les pions
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

    // Pions croix
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

    // pions cercle
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

    // Liste des cases adjacentes à vérifier pour chaque case. (évite des calculs et évite d'avoir plein de if) Si la grille étit plus grande, on procéderait autrement avec des fonction modulaires.
    private int[][][] posToVerify = {{{6, 3}, {1, 2}, {4, 8}},
                                     {{0, 2}, {4, 7}},
                                     {{0, 1}, {5, 8}, {4, 6}},
                                     {{0, 6}, {4, 5}},
                                     {{1, 7}, {3, 5}, {0, 8}, {2, 6}},
                                     {{2, 8}, {3, 4}},
                                     {{0, 3}, {7, 8}, {2, 4}},
                                     {{1, 4}, {6, 8}},
                                     {{2, 5}, {6, 7}, {0, 4}}};

    // l'ia utilisée pour jouer
    private MultiLayerPerceptron ai;


    public ControllerGrid(){
        gameState = new double[9];
        for(int i=0; i<gameState.length; i++){
            gameState[i] = Coup.EMPTY;
        }
        System.out.println("File to load for ai in Game is : " + getFile());
        ai = MultiLayerPerceptron.load(getFile());

    }


    /**
     *
     * @param url
     * @param resourceBundle
     * Méthode appellée à l'initialisation de la scène
     * Met en place les éléments de la scène et configure les eventHandlers
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Arrived on grille");

        // à ne pas mettre directement dans le constructeur : ces listes pourraient être nulle dans le corps de cette fonction (erreur testée).
        controls = Arrays.asList(homeButton, soundControl, buttonPlayAgain, buttonQuit);
        placeHolders = Arrays.asList(placeHolder0,placeHolder1, placeHolder2, placeHolder3, placeHolder4, placeHolder5, placeHolder6, placeHolder7, placeHolder8);
        placeHoldersSelected = Arrays.asList(placeHolderSelected0, placeHolderSelected1, placeHolderSelected2, placeHolderSelected3, placeHolderSelected4, placeHolderSelected5, placeHolderSelected6, placeHolderSelected7, placeHolderSelected8);
        crosses = Arrays.asList(cross0, cross1, cross2, cross3, cross4, cross5, cross6, cross7, cross8);
        circles = Arrays.asList(circle0, circle1, circle2, circle3, circle4, circle5, circle6, circle7, circle8);

        pionToPlay = true;

        // cache l'écran de fin et les pions
        hideWinningScreen();
        for (ImageView currentCross : crosses) currentCross.setVisible(false);
        for (Circle currentCircle : circles) currentCircle.setVisible(false);

        for(int i=0; i<controls.size(); i++) {
            int finalI = i;
            controls.get(finalI).addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    /*
                    ColorAdjust colorAdjust = new ColforAdjust();
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
        // un placeHolderSelected cliqué fait jouer le pion correspondant puis l'ia, le cas échéant
        for (int i=0; i<placeHoldersSelected.size(); i++){
            placeHoldersSelected.get(i).setVisible(false);

            int finalI = i;  // finalI rends la valeur de i accessible par les eventHandlers
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

                    // On joue le pion cliqué
                    hidePlaceholder(finalI);
                    if (pionToPlay) {
                        crosses.get(finalI).setVisible(true);
                        gameState[finalI] = Coup.X;
                    } else {
                        circles.get(finalI).setVisible(true);
                        gameState[finalI] = Coup.O;
                    }

                    displayGameState();

                    // On vérifie si la partie est finie. Si non, on passe fait jouer l'IA le cas échéant ou on laisse lautre joueur jouer
                    int[] isGameFinishedResult = isGameFinished(finalI);
                    if (isGameFinishedResult == null) {  // Si la partie n'est pas finie, l'autre joueur ou l'IA joue.
                        pionToPlay = !pionToPlay;
                        int aiPionPLayed = -1;
                        if (aiGameMode){
                            aiPionPLayed = playAi();
                            isGameFinishedResult = isGameFinished(aiPionPLayed);
                            if (aiPionPLayed != -1 && isGameFinishedResult != null) {  // Si on ne peut plus jouer
                                gameFinishedResult(isGameFinishedResult);
                            }
                            pionToPlay = !pionToPlay;
                        }
                    }else{  // Si on ne peut plus jouer
                        gameFinishedResult(isGameFinishedResult);
                    }
                    event.consume();
                }
            });
        }
    }

    private void gameFinishedResult(int[] isGameFinishedResult) {
        for (int i : isGameFinishedResult) {
            System.out.println(i);
            Node pion = (gameState[i] == Coup.X ? crosses : circles).get(i);

            final ScaleTransition scaleAnimation = new ScaleTransition(Duration.seconds(2), pion);
            scaleAnimation.setCycleCount(TranslateTransition.INDEFINITE);
            scaleAnimation.setAutoReverse(true);
            scaleAnimation.setFromX(0.5);
            scaleAnimation.setToX(2);
            scaleAnimation.setFromY(0.5);
            scaleAnimation.setToY(2);
            scaleAnimation.setInterpolator(Interpolator.LINEAR);
            scaleAnimation.play();
        }
        showWinningScreen(isGameFinishedResult);
    }


    /**
     * @return
     * Fais jouer l'ia
     * retourne la position du pion joué par l'ia
     */
    private int playAi() {
        System.out.println("C'est au tour de l'ia");
        hideAllPlaceholders();

        double[] gameStateIaOut = ai.forwardPropagation(gameState);
        for(int i=0; i<gameStateIaOut.length; i++){
            System.out.println(i+" : " +gameStateIaOut[i]+" "+gameState[i]);
        }

        int caseToPlay = 0;
        int i=0;
        for(; i<gameStateIaOut.length; i++){  // Recherche de la première position jouable
            if(gameState[i] == 0){
                caseToPlay = i;
                break;
            }
        }
        for(; i<gameStateIaOut.length; i++){  // Recherche de la position jouable dont le score est le plus élevé
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

    /**
     * @param posPLayed
     * @return
     *
     * Retourne la position des pions gagnants
     * Retourne null si personne n'as gagné
     * Retourne la position de toute les cases si la partie est finie et que personne n'as gagné
     */
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

        return new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8};  // Si il n'y a pas de gagnant, on retourne toute les cases pour les animer
    }

    /**
     * Affiche l'état du jeu
     */
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


    /**
     * @param gameFinishedResult
     * Affiche l'écran de fin avec le bon texte
     */
    private void showWinningScreen(int[] gameFinishedResult) {
        hideAllPlaceholders();
        backgroundHiderHolder.setVisible(true);
        backgroundHider.setVisible(true);

        if(gameFinishedResult.length != 9) {
            System.out.println("les " + (pionToPlay ? "Croix" : "Cercles") + " ont gagné");
            textVictoryOf.setText(pionToPlay ? "Croix" : "Cercles");
            textAgainst.setText(pionToPlay ? "Cercles" : "Croix");
        }else{System.out.println("Persone n'as gagné");
            textVictoryOf.setText("Personne");
            textAgainst.setText("Le destin..");
        }
        textVictoryOf.setTextFill(Color.web(pionToPlay ? "#3481eb" : "#d7292e"));
        textAgainst.setTextFill(Color.web(pionToPlay ? "#d7292e" : "#3481eb"));
        labelVictoryOf.setVisible(true);
        labelAgainst.setVisible(true);
        textVictoryOf.setVisible(true);
        textAgainst.setVisible(true);

        buttonPlayAgain.setVisible(true);
        buttonQuit.setVisible(true);
    }

    /**
     * Cache l'écran de fin
     */
    private void hideWinningScreen() {
        backgroundHiderHolder.setVisible(false);
        backgroundHider.setVisible(false);
        labelVictoryOf.setVisible(false);
        textVictoryOf.setVisible(false);
        labelAgainst.setVisible(false);
        textAgainst.setVisible(false);

        buttonPlayAgain.setVisible(false);
        buttonQuit.setVisible(false);
    }

    /**
     * Affiche tous les placeHolder pour lesquels aucun pion n'as été posé
     */
    public void showPlaceholders(){
        for (int i=0; i<placeHolders.size(); i++) {
            if(gameState[i] == Coup.EMPTY) {
                placeHolders.get(i).setVisible(true);
            }
        }
    }

    /**
     * @param placeHolderNumber
     * Cache le placeHolder placeHolderNumber
     */
    private void hidePlaceholder(int placeHolderNumber) {
        placeHoldersSelected.get(placeHolderNumber).setVisible(false);
        placeHolders.get(placeHolderNumber).setVisible(false);
    }

    /**
     * Cache tous les placeHolder
     */
    public void hideAllPlaceholders(){
        for (int i=0; i<placeHoldersSelected.size(); i++) {
            placeHoldersSelected.get(i).setVisible(false);
        }
        for (int i=0; i<placeHolders.size(); i++) {
            placeHolders.get(i).setVisible(false);
        }
    }

    public void changePageToPageSample() throws IOException {
        PageLoader.changePage("../view/home.fxml", this);
    }

    public void changePageToGrid() throws IOException {
        PageLoader.changePage("../view/grid.fxml", this);
    }
}
