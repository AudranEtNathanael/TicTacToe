package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

//import javax.swing.*;

public class ControllerGrid implements Initializable {

    @FXML
    private Button homeButton;

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

    private ArrayList<Object> gameState;  // contient des null, Circle circleX ou des ImageView crossX (avec x in 0..8)


    private static ControllerGrid CG;

    public static ControllerGrid getCG(){
        return CG;
    }

    public ControllerGrid(){
        CG = this;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Arrived on grille");

        pionToPlay = true;

        gameState = new ArrayList<>();
        for(int i=0; i<9; i++) {
            gameState.add(null);
        }

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
                    if (pionToPlay) {
                        crosses.get(finalI).setVisible(true);
                        gameState.set(finalI, crosses.get(finalI));
                    } else {
                        circles.get(finalI).setVisible(true);
                        gameState.set(finalI, circles.get(finalI));
                    }
                    pionToPlay = !pionToPlay;
                    displayGameState();
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



        //Creating the mouse event handler
        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                System.out.println("Hello World");
            }
        };
//Adding event Filter
    }

    private void displayGameState() {
        for(int i=0; i<gameState.size(); i++){
            if(i%3 == 0){
                System.out.print("\n");
            }
            if(gameState.get(i) == null){
                System.out.print("   ");
            }else if(gameState.get(i).getClass() == Circle.class){
                System.out.print(" O ");
            }else{
                System.out.print(" X ");
            }
        }
        System.out.print("\n");
    }


    public void changePageToPageSample() throws IOException {
        PageLoader.changePage("../view/home.fxml", this);
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
