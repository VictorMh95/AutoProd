package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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

    @FXML
    public LineChart<String,Number> graphConso;
    @FXML
    private LineChart<String, Number> graphEnsoleillement;

    @FXML private TableView<Installation> tableView;
    @FXML private TableColumn<Installation,Integer> numero;
    @FXML private TableColumn<Installation, Integer> surface;
    @FXML private TableColumn<Installation,Integer> inclinaison;
    @FXML private TableColumn<Installation,Integer> orientation;
    @FXML private TableColumn<Installation,Integer> puissance;

    public  ArrayList<Production> productionTotaleListe = new ArrayList<Production>();
    public  ArrayList<Consommation> consommationListe = new ArrayList<Consommation>();
    public ArrayList<Production> ensoleillement = new ArrayList<Production>();
    public ObservableList<Installation> listInstallation = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        consommationAffichage();
        numero.setCellValueFactory(new PropertyValueFactory<Installation, Integer>("numero"));
        surface.setCellValueFactory(new PropertyValueFactory<Installation, Integer>("surface"));
        inclinaison.setCellValueFactory(new PropertyValueFactory<Installation, Integer>("inclinaison"));
        orientation.setCellValueFactory(new PropertyValueFactory<Installation, Integer>("orientation"));
        puissance.setCellValueFactory(new PropertyValueFactory<Installation, Integer>("puissance"));
    }


    public void initData(ArrayList<Production> productionTotale,ArrayList<Consommation> consommation,ArrayList<Production> irradiation,
                         ObservableList<Installation> installation){
        productionTotaleListe = (ArrayList<Production>)productionTotale.clone();
        consommationListe = (ArrayList<Consommation>)consommation.clone();
        ensoleillement = (ArrayList<Production>)irradiation.clone();
        listInstallation = installation ;
    }

    @FXML
    void afficherEnsoleillement(ActionEvent event) {
        displayGraphEnsoleillement(ensoleillement);
        tableView.setItems(listInstallation);
    }

    @FXML
    void afficherConso(ActionEvent event) {
        displayGraphConso(consommationListe);
    }

    @FXML
    void afficherProd(ActionEvent event) {
        displayGraphProduction(productionTotaleListe);
    }

    public void consommationAffichage(){
    }

    public void displayGraphConso(ArrayList<Consommation> list){
        String date;
        Number conso;
        XYChart.Series<String,Number> series = new XYChart.Series<>();
        series.setName("Consommation en kWh");
        for (Consommation cons: list){
            date=cons.getDate().toString();
            conso = cons.getConsommation();
            series.getData().add(new XYChart.Data<String,Number>(date,conso));
        }
        System.out.println(series.getData());
        graphConso.getData().addAll(series);
    }

    public void displayGraphProduction(ArrayList<Production> list){
        String date;
        Number production;
        XYChart.Series<String,Number> series = new XYChart.Series<>();
        series.setName("Production en kWh");
        for (Production prod: list){
            date = prod.getDate().toString();
            production = prod.getProduction();
            series.getData().add(new XYChart.Data<String,Number>(date,production));
        }
        graphConso.getData().addAll(series);
    }

    public void displayGraphEnsoleillement(ArrayList<Production> list){
        String date;
        Number production;
        XYChart.Series<String,Number> series = new XYChart.Series<>();
        series.setName("Irradiation en kWh/m²");
        for (Production prod: list){
            date = prod.getDate().toString();
            production = prod.getProduction();
            series.getData().add(new XYChart.Data<String,Number>(date,production));
        }
        graphEnsoleillement.getData().addAll(series);
    }


}
