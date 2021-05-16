package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

import static sample.Settings.playClickSound;
import static sample.Settings.playErrorSound;


//import javax.swing.*;

public class ControllerParametres implements Initializable {

    @FXML
    Button retour;

    @FXML
    TextArea listFiles;

    @FXML
    Text percentText;

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

    /**
     *
     * @param location
     * @param resources
     * initialise la scene
     */
    public void initialize(URL location, ResourceBundle resources) {
        listModelFiles=new ArrayList<String>();     //initialisation de la liste de modele
        loadFiles();        //trouve les modeles existant
        makeMenuLoad();     //crée les menus grace au fichier config.txt
        makeMenuDelete();   //crée les menus grace aux modeles existant

    }


    /**
     * creer les sous menus du menu déroulant choisir la difficulté
     */
    public void makeMenuLoad(){
        Settings settings=new Settings();
        ArrayList<String> diff=settings.readDifficulties();     //les différentes difficultés du config.txt
        loadModel.getItems().clear();       //supprime les anciens sous menus
        if (diff!=null){
            for (int i=0;i<diff.size();i++){    //pour chaque difficulté
                MenuItem m=new MenuItem("Load "+diff.get(i));   //creer un sous menu menu
                m.setId(diff.get(i));               //definit l id du sous menu
                loadModel.getItems().add(m);        //ajoute le sous menu au menu deroulant
                m.setOnAction(loadMenu);            //definit l'action lorsqu un sous menu sera selectionné

            }
        }
    }

    /**
     * creer les sous menus du menu deroulant pour supprimer les modeles
     */
    public void makeMenuDelete(){
        Settings settings=new Settings();
        deleteModel.getItems().clear();     //supprime les anciens sous menus
        if (listModelFiles!=null){
            for (int i=0;i<listModelFiles.size();i++){      //pour chaque modele existant
                MenuItem m1=new MenuItem("Delete "+listModelFiles.get(i));      //creer un sous menu
                m1.setId(listModelFiles.get(i));            //definit l'id
                deleteModel.getItems().add(m1);             //ajoute le sous menu
                m1.setOnAction(deleteMenu);                 //definit l'action
            }
        }
    }

    /**
     * fonction appelé lors d'un clic sur le bouton actualiser
     * actualise les menus deroulants
     */
    public void refresh(){
        playClickSound();   //joue un son
        loadFiles();        //actualise la liste de modele
        makeMenuLoad();     //actualise le menu de difficulté
        makeMenuDelete();   //actualise le menu de suppression
    }

    /**
     *
     * @throws IOException
     * retourne à l'acceuil
     */
    public void changePageToHome() throws IOException {
        playClickSound();
        PageLoader.changePage("../view/home.fxml", this);   //change de scene
    }

    /**
     * trouve les modeles existant
     */
    public void loadFiles(){
        System.out.println("Loading files");
        try {
            File f = new File("./resources/models/");  //dossier où on va chercher

            FilenameFilter filter = new FilenameFilter() {      //definit que l'on cherche que les fichiers .srl
                @Override
                public boolean accept(File f, String name) {
                    // We want to find only .c files
                    return name.endsWith(".srl");
                }
            };

            File[] files = f.listFiles(filter);     //met les fichiers trouvés dans un tableau

            listModelFiles.clear();
            listFiles.clear();
            listFiles.appendText("Listes des modeles :\n");

            for (int i = 0; i < files.length; i++) {            //pour chaque modele trouvé
                System.out.println(files[i].getName());
                listModelFiles.add(files[i].getName());                 //ajoute le modele à la liste de modele
                listFiles.appendText(files[i].getName()+"\n");      //ajoute le fichier dans la TextArea

            }
            //System.out.println(listFiles.getText());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * clic sur un sous menu
     * choix de la difficulté
     */
    EventHandler<ActionEvent> loadMenu = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e)
        {
            playClickSound();
            System.out.println(((MenuItem)e.getSource()).getText() + " selected");
            Settings settings=new Settings();
            //progressBar.progressProperty().unbind();
            //progressBar.progressProperty().bind(settings.task.progressProperty());

            settings.launchIA(((MenuItem)e.getSource()).getId(),progressBar,percentText);   //lance l'ia avec la difficulté definit par l'ID du sous menu selectionné
            //loadFiles();
            //makeMenuDelete();
        }
    };

    /**
     * clic sur un sous menu pour supprimer un modele
     */
    EventHandler<ActionEvent>  deleteMenu= new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e)
        {
            playClickSound();
            System.out.println(((MenuItem)e.getSource()).getText() + " selected");
            Settings settings=new Settings();
            settings.delete(((MenuItem)e.getSource()).getId());     //supprime le modele correspondant
            deleteModel.getItems().remove((MenuItem)e.getSource());
            //makeMenu();
        }
    };


    /**
     * modification du fichier config.txt
     */
    public void changeConfig() {

        String difficultyTMP=difficultyField.getText();
        String hTMP=hField.getText();
        String lrTMP=lrField.getText();
        String lTMP=lField.getText();
        try{
            if (!difficultyTMP.isBlank() && !hTMP.isBlank() && !lrTMP.isBlank() && !lTMP.isBlank()){        //si tous les champs sont remplis
                playClickSound();
                String conf=difficultyTMP+":"+hTMP+":"+lrTMP+":"+lTMP;
                File file = new File("./resources/config.txt");
                if(file.exists()){      // si le fichier config.txt existe
                    Settings settings=new Settings();
                    if(settings.readConf(difficultyTMP)){       //si la difficulté existe deja
                        System.out.println("Modif");
                        FileWriter writer = new FileWriter("./resources/config.txt");
                        for (Map.Entry<String, String[]> value: Settings.getConf().entrySet()) {        //pour chaque difficulté
                            if(value.getValue()[0].equals(difficultyTMP)) {     //si la difficulté cherché est trouvé
                                writer.write(conf+"\n");    //ecriture la nouvelle ligne a la place de l ancienne
                            }
                            else{      //sinon on remet l'ancienne
                                String fic=value.getValue()[0]+":"+value.getValue()[1]+":"+value.getValue()[2]+":"+value.getValue()[3];
                                writer.write(fic+"\n");
                            }
                        }
                        //writer.write("\n"+conf);
                        writer.close();
                        System.out.println(conf);
                    }
                    else{   //si elle n existe pas
                        System.out.println("Ajout");
                        FileWriter writer = new FileWriter("./resources/config.txt",true);
                        writer.write("\n"+conf);    //ajout de la nouvelle ligne de config
                        writer.close();
                        System.out.println(conf);
                    }
                }
                else if(file.createNewFile()){  //si la création du fichier a reussi
                    System.out.println("Creation et ajout");
                    FileWriter writer = new FileWriter("./resources/config.txt");
                    writer.write(conf);     //ecris la config souhaité dans le fichier
                    writer.close();
                    System.out.println(conf);
                }
            }
            else{
                playErrorSound();
            }
        }catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    public void click(){
        playClickSound();
    }

}
