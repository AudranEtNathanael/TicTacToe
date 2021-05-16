package sample;

import javafx.animation.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static sample.Settings.*;

//import javax.swing.*;

public class ControllerPlayerMenu implements Initializable {

    // ressources pour le curseur de souris
    @FXML AnchorPane baseAnchor;
    ImageCursor blueStandardMouse = new ImageCursor(new Image("view/blueStandardMouse.png"));
    ImageCursor redStandardMouse = new ImageCursor(new Image("view/redStandardMouse.png"));
    ImageCursor crossMouse = new ImageCursor(new Image("view/crossMouse.png"));
    ImageCursor circleMouse = new ImageCursor(new Image("view/circleMouse.png"));

    // Boutons de contrôle
    @FXML private ImageView homeButton;
    @FXML private ImageView soundControl;
    @FXML private ImageView validateButton;

    List<Node> controls;

    // avatars
    @FXML ImageView avatar1;
    @FXML ImageView avatar2;
    @FXML ImageView avatar3;
    @FXML ImageView avatar4;
    @FXML ImageView avatar5;

    List<Node> avatars;

    HashMap<String, ImageView> avatarsRes = new HashMap<>();

    // Titre
    @FXML
    Label textTitle;

    // Champ de saisie
    @FXML TextField pseudoField;

    public static void setCrossToChoose(boolean crossToChoose) {
        ControllerPlayerMenu.crossToChoose = crossToChoose;
    }

    private static boolean crossToChoose;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Arrived on PlayerMenu");
        initializeSoundIcon(soundControl);
        toggleCursor();

        textTitle.setText("Joueur 1");

        controls = Arrays.asList(homeButton, soundControl, validateButton, avatar1, avatar2, avatar3, avatar4, avatar5);
        avatars = Arrays.asList(avatar1, avatar2, avatar3, avatar4, avatar5);
        avatarsRes.put("avatarLambdaCircleNeg", avatar1);
        avatarsRes.put("avatarLambda", avatar2);
        avatarsRes.put("avatarLambdaCirclePos", avatar3);
        avatarsRes.put("avatarDoctor", avatar4);
        avatarsRes.put("avatarDetective", avatar5);

        try {
            if(crossToChoose){
                ControllerGrid.setAvatarCrossRes("avatarLambda");
                ControllerGrid.setAvatarCircleRes("avatarLambda");
            }
            for (String currentAvatarRes : avatarsRes.keySet()){
                avatarsRes.get(currentAvatarRes).addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        String avatarSetInGame = crossToChoose ? ControllerGrid.getAvatarCrossRes() : ControllerGrid.getAvatarCircleRes();
                        System.out.println("Res for image : view/" + avatarSetInGame + "Grey.png");
                        avatarsRes.get(avatarSetInGame).setImage(new Image("view/avatar/" + avatarSetInGame + "Grey.png"));
                        avatarsRes.get(currentAvatarRes).setImage(new Image("view/avatar/" + currentAvatarRes + (crossToChoose ? "Blue" : "Red") + ".png"));
                        if(crossToChoose) {
                            ControllerGrid.setAvatarCrossRes(currentAvatarRes);
                        }else{
                            ControllerGrid.setAvatarCircleRes(currentAvatarRes);
                        }
                    }
                });
            }
        } catch (Exception e) {
            System.out.println(e);
        }


        for(int i=0; i<controls.size(); i++) {
            int finalI = i;
            controls.get(finalI).addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {

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
        }


        validateButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("VALIDATE");
                if(crossToChoose){
                    setCrossToChoose(false);
                    ControllerGrid.setplayer1Name(pseudoField.getText() != "" ? pseudoField.getText() : "Croix");
                    textTitle.setText("Joueur 2");
                    textTitle.setTextFill(Color.web("#d7292e"));
                    pseudoField.setText("");
                    String avatarSetInGameForCross = ControllerGrid.getAvatarCrossRes();
                    avatarsRes.get(avatarSetInGameForCross).setImage(new Image("view/avatar/" + avatarSetInGameForCross + "Grey.png"));
                    String avatarSetInGameForCircle = ControllerGrid.getAvatarCircleRes();
                    avatarsRes.get(avatarSetInGameForCircle).setImage(new Image("view/avatar/" + avatarSetInGameForCircle + "Red.png"));
                }else{
                    try {
                        ControllerGrid.setplayer2Name(pseudoField.getText() != "" ? pseudoField.getText() : "Cercles");
                        changePageToGrid();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }



    private void toggleCursor(){
        baseAnchor.setCursor(crossToChoose ? blueStandardMouse : redStandardMouse);
    }

    public void changePageToPlayerMenu() throws IOException {
        playClickSound();
        PageLoader.changePage("../view/menuPlayer.fxml", this);
    }

    public void changePageToPageSample() throws IOException {
        playClickSound();
        PageLoader.changePage("../view/home.fxml", this);
    }

    public void changePageToGrid() throws IOException {
        playClickSound();
        PageLoader.changePage("../view/grid.fxml", this);
    }
}
