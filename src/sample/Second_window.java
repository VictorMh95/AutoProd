package sample;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Second_window implements Initializable {

    @FXML
    private JFXTextField potentielProductionMenseul;

    @FXML
    private JFXTextField consommationMensuellle;

    @FXML
    private JFXTextField potentielProductionAnnuel;

    @FXML
    private JFXTextField consommationAnnuelle;




    public  ArrayList<Production> productionTotaleListe = new ArrayList<Production>();
    public  ArrayList<Consommation> ConsommationListe = new ArrayList<Consommation>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        consommationAffichage();
    }


    public void initData(ArrayList<Production> productionTotale,ArrayList<Consommation> consommation){
        productionTotaleListe = (ArrayList<Production>)productionTotale.clone();
        ConsommationListe = (ArrayList<Consommation>)consommation.clone();
    }

    public void consommationAffichage(){


    }


}
