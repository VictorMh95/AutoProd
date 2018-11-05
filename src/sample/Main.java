package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @Author  Thomas Chevalier et Victor Mahe
 * Projet Coelab septembre-novembre 2018
 */
public class Main extends Application {


    /**
     * Point d'entrée du programme, charge le sample.fxml
     * @param primaryStage
     * @throws Exception
     */

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Auto Prod");
        primaryStage.setScene(new Scene(root, 1128, 735));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
