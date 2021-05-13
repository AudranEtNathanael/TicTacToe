package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;


//import javax.swing.*;

public class ControllerParametres implements Initializable {

    @FXML
    Button retour;

    @FXML
    TextArea listFiles;

    @FXML
    TextField difficultyField;

    @FXML
    TextField hField;

    @FXML
    TextField lrField;

    @FXML
    TextField lField;

    @FXML
    MenuButton loadModel;

    @FXML
    MenuButton deleteModel;

    @FXML
    ProgressBar progressBar;

    @FXML
    Button refresh;

    ArrayList<String> listModelFiles;
    private String name="";
    private String file="";
    private int h = 0;
    private double lr = 0.0;
    private int l = 0;

    public void initialize(URL location, ResourceBundle resources) {
        listModelFiles=new ArrayList<String>();
        loadFiles();
        makeMenuLoad();
        makeMenuDelete();

    }

    public void makeMenuLoad(){
        Settings settings=new Settings();
        ArrayList<String> diff=settings.readDifficulties();
        loadModel.getItems().clear();
        if (diff!=null){
            for (int i=0;i<diff.size();i++){
                MenuItem m=new MenuItem("Load "+diff.get(i));
                m.setId(diff.get(i));
                loadModel.getItems().add(m);
                m.setOnAction(loadMenu);

            }
        }
    }
    public void makeMenuDelete(){
        Settings settings=new Settings();
        deleteModel.getItems().clear();
        if (listModelFiles!=null){
            for (int i=0;i<listModelFiles.size();i++){
                MenuItem m1=new MenuItem("Delete "+listModelFiles.get(i));
                m1.setId(listModelFiles.get(i));
                deleteModel.getItems().add(m1);
                m1.setOnAction(deleteMenu);
            }
        }
    }


    public void refresh(){
        loadFiles();
        makeMenuLoad();
        makeMenuDelete();
    }

    public void changePageToHome() throws IOException {
        PageLoader.changePage("../view/home.fxml", this);
    }

    public void loadFiles(){
        System.out.println("Loading files");
        try {
            File f = new File("./resources/models/");

            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File f, String name) {
                    // We want to find only .c files
                    return name.endsWith(".srl");
                }
            };

            // Note that this time we are using a File class as an array,
            // instead of String
            File[] files = f.listFiles(filter);

            // Get the names of the files by using the .getName() method
            listModelFiles.clear();
            listFiles.clear();
            listFiles.appendText("Listes des modeles :\n");

            for (int i = 0; i < files.length; i++) {
                System.out.println(files[i].getName());
                listModelFiles.add(files[i].getName());
                listFiles.appendText(files[i].getName()+"\n");

            }
            //System.out.println(listFiles.getText());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    EventHandler<ActionEvent> loadMenu = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e)
        {
            System.out.println(((MenuItem)e.getSource()).getText() + " selected");
            Settings settings=new Settings();
            //progressBar.progressProperty().unbind();
            //progressBar.progressProperty().bind(settings.task.progressProperty());
            settings.launchIA(((MenuItem)e.getSource()).getId(),progressBar);
            //loadFiles();
            //makeMenuDelete();
        }
    };

    EventHandler<ActionEvent>  deleteMenu= new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e)
        {
            System.out.println(((MenuItem)e.getSource()).getText() + " selected");
            Settings settings=new Settings();
            settings.delete(((MenuItem)e.getSource()).getId());
            deleteModel.getItems().remove((MenuItem)e.getSource());
            //makeMenu();
        }
    };



    public void changeConfig() {
        String difficultyTMP=difficultyField.getText();
        String hTMP=hField.getText();
        String lrTMP=lrField.getText();
        String lTMP=lField.getText();
        try{
            if (!difficultyTMP.isBlank() && !hTMP.isBlank() && !lrTMP.isBlank() && !lTMP.isBlank()){
                String conf=difficultyTMP+":"+hTMP+":"+lrTMP+":"+lTMP;
                File file = new File("./resources/config.txt");
                if(file.exists()){
                    Settings settings=new Settings();
                    if(settings.readConf(difficultyTMP)){
                        System.out.println("Modif");
                        FileWriter writer = new FileWriter("./resources/config.txt");
                        for (Map.Entry<String, String[]> value: Settings.getConf().entrySet()) {
                            if(value.getValue()[0].equals(difficultyTMP)) {
                                writer.write(conf+"\n");
                            }
                            else{
                                String fic=value.getValue()[0]+":"+value.getValue()[1]+":"+value.getValue()[2]+":"+value.getValue()[3];
                                writer.write(fic+"\n");
                            }
                        }
                        //writer.write("\n"+conf);
                        writer.close();
                        System.out.println(conf);
                    }
                    else{
                        System.out.println("Ajout");
                        FileWriter writer = new FileWriter("./resources/config.txt",true);
                        writer.write("\n"+conf);
                        writer.close();
                        System.out.println(conf);
                    }
                }
                else if(file.createNewFile()){
                    System.out.println("Creation et ajout");
                    FileWriter writer = new FileWriter("./resources/config.txt");
                    writer.write(conf);
                    writer.close();
                    System.out.println(conf);
                }
            }
        }catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

}
