package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

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
}
