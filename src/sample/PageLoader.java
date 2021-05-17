package sample;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Time;

public class PageLoader {

    public static void changePage(String targetPage, Object object) throws IOException {
        System.out.println("Vous changez de page ("+targetPage+")");

        Stage stage=Main.getPrimaryStage();

        Parent root = FXMLLoader.load(object.getClass().getResource(targetPage));


        stage.setScene(new Scene(root));
        stage.setTitle("TicTacToe");
        stage.show();
    }

    public static Parent generateNewRoot(String targetPage, Object object) throws IOException {
        return FXMLLoader.load(object.getClass().getResource(targetPage));
    }

    public static void changePageUsingRoot(Parent root, Object object) throws IOException {
        System.out.println("Vous changez de page");

        Stage stage=Main.getPrimaryStage();

        stage.setScene(new Scene(root));
        stage.setTitle("TicTacToe");
        stage.show();
    }

    /**
     * créer un fondu qui va de 0 à 1 en opacité
     */
    public static Timeline fadeIn(){

        DoubleProperty opacity = Main.getPrimaryStage().opacityProperty();
        Timeline fadeIn = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0)),        //opacité à 0 au debut de l'animation
                new KeyFrame(new Duration(800), new KeyValue(opacity, 1.0))    //opacité à 1 apres 1seconde
        );
        fadeIn.play();  //joue la transition
        return fadeIn;
    }

    /**
     * créer un fondu qui va de 1 à 0 en opacité
     */
    public static Timeline fadeOut(){

        DoubleProperty opacity = Main.getPrimaryStage().opacityProperty();
        Timeline fadeOut = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1)),
                new KeyFrame(new Duration(800), new KeyValue(opacity, 0))
        );
        fadeOut.play();  //joue la transition
        return fadeOut;

    }

    public static void fadeOutThenChangePage(String page, Object object){
        Timeline fadeOut=PageLoader.fadeOut();
        fadeOut.setOnFinished(e -> {
            try {
                PageLoader.changePage(page, object);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }
}
