package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;

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

    @FXML
    public LineChart<String,Number> graphConso;

    public  ArrayList<Production> productionTotaleListe = new ArrayList<Production>();
    public  ArrayList<Consommation> consommationListe = new ArrayList<Consommation>();



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        consommationAffichage();
    }


    public void initData(ArrayList<Production> productionTotale,ArrayList<Consommation> consommation){
        productionTotaleListe = (ArrayList<Production>)productionTotale.clone();
        consommationListe = (ArrayList<Consommation>)consommation.clone();
    }

    @FXML
    void prout(ActionEvent event) {
        displayGraphConso(consommationListe);
    }

    public void consommationAffichage(){

    }

    public void displayGraphConso(ArrayList<Consommation> list){
        String date;
        Number conso;
        XYChart.Series<String,Number> series = new XYChart.Series<>();
        for (Consommation cons: list){
            date=cons.getDate().toString();
            conso = cons.getConsommation();
            series.getData().add(new XYChart.Data<String,Number>(date,conso));
        }
        System.out.println(series.getData());
        graphConso.getData().addAll(series);
    }


}
