package sample;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;

public class Main extends Application {


    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage=primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("../view/home.fxml"));
        primaryStage.setTitle("TicTacToe");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.getIcons().setAll(
                new Image("view/appIcon/appIcon_16.png"),
                new Image("view/appIcon/appIcon_24.png"),
                new Image("view/appIcon/appIcon_32.png"),
                new Image("view/appIcon/appIcon_48.png"),
                new Image("view/appIcon/appIcon_128.png"),
                new Image("view/appIcon/appIcon_256.png"));
        primaryStage.show();

    }


    public static Stage getPrimaryStage(){
        return primaryStage;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
