package sample;

import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class Second_window implements Initializable {


    @FXML
    public LineChart<String,Number> graphConso;
    @FXML
    private LineChart<String, Number> graphEnsoleillement;
    @FXML
    private JFXTextField puissanceTF;

    @FXML
    private JFXTextField surfaceTF;

    @FXML
    private JFXTextField TFautoConso;

    @FXML
    private JFXTextField TFautoprod;

    @FXML
    private JFXTextField potentienProdAnnuel;

    @FXML
    private JFXTextField consBatAnnuel;

    @FXML private TableView<Installation> tableView;
    @FXML private TableColumn<Installation,Integer> numero;
    @FXML private TableColumn<Installation, Integer> surface;
    @FXML private TableColumn<Installation,Integer> inclinaison;
    @FXML private TableColumn<Installation,Integer> orientation;
    @FXML private TableColumn<Installation,Integer> puissance;
    @FXML private TableColumn<Installation,Integer> prodTotale;

    public  ArrayList<Production> productionTotaleListe = new ArrayList<Production>();
    public  ArrayList<Consommation> consommationListe = new ArrayList<Consommation>();
    public ArrayList<Production> ensoleillement = new ArrayList<Production>();
    public ObservableList<Installation> listInstallation = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        numero.setCellValueFactory(new PropertyValueFactory<Installation, Integer>("numero"));
        surface.setCellValueFactory(new PropertyValueFactory<Installation, Integer>("surface"));
        inclinaison.setCellValueFactory(new PropertyValueFactory<Installation, Integer>("inclinaison"));
        orientation.setCellValueFactory(new PropertyValueFactory<Installation, Integer>("orientation"));
        puissance.setCellValueFactory(new PropertyValueFactory<Installation, Integer>("puissance"));
        prodTotale.setCellValueFactory(new PropertyValueFactory<Installation,Integer>("prodTotale"));
    }


    public void initData(ArrayList<Production> productionTotale,ArrayList<Consommation> consommation,ArrayList<Production> irradiation,
                         ObservableList<Installation> installation){
        productionTotaleListe = (ArrayList<Production>)productionTotale;
        consommationListe = (ArrayList<Consommation>)consommation;
        ensoleillement = (ArrayList<Production>)irradiation;
        listInstallation = installation;
        puissanceTF.setText(calculpuissane(listInstallation).toString());
        surfaceTF.setText(calculsurface(listInstallation).toString());
    }

    @FXML
    void afficherConso(ActionEvent event) {
        displayGraphConsoProd(consommationListe,productionTotaleListe);
        displayGraphEnsoleillement(ensoleillement);
        tableView.setItems(listInstallation);
        consommationAffichage();
        TFautoConso.setText(String.valueOf(calculTauxAutoCons()));
        TFautoprod.setText(String.valueOf(calculTauxAutoProd()));
        potentienProdAnnuel.setText(String.valueOf(calculpotentielAnnuel()));
        consBatAnnuel.setText(String.valueOf(consommationTotaleAnnuel()));
    }


    public void consommationAffichage(){
//        long milliSecondDiff = consommationListe.get(consommationListe.size()).getDate().getTime()-consommationListe.get(0).getDate().getTime();
     //   long intervalle = milliSecondDiff/consommationListe.size();
       // int increment=0;
        //long incrementInter=0;
        //long mois=2505600000L;
       // while(incrementInter<mois){ }

    }

    public void displayGraphConsoProd(ArrayList<Consommation> conso,ArrayList<Production> prod){
        String dateconso;
        Number consommation;
        String dateprodu;
        Number production;

        XYChart.Series<String,Number> seriesConso = new XYChart.Series<>();
        seriesConso.setName("Consommation en kWh");

        XYChart.Series<String,Number> seriesProd = new XYChart.Series<>();
        seriesConso.setName("Production en kWh");

        for (Consommation cons: conso){
            dateconso=cons.getDate().toString();
            consommation = cons.getConsommation();
            seriesConso.getData().add(new XYChart.Data<String,Number>(dateconso,consommation));
        }

        for (Production pr: prod){
            dateprodu = pr.getDate().toString();
            production = pr.getProduction();
            seriesProd.getData().add(new XYChart.Data<String,Number>(dateprodu,production));
        }
        graphConso.setCreateSymbols(false);
        graphEnsoleillement.setCreateSymbols(false);
        graphConso.getData().addAll(seriesProd);
        graphConso.getData().addAll(seriesConso);

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

    public Double calculsurface(ObservableList<Installation> list) {
        double surf=0 ;
        for (Installation inst : list){
            surf = surf + inst.getSurface();
        }
        return surf;
    }

    public Double calculpuissane(ObservableList<Installation> list){
        double puissance = 0 ;
        double nbre = 0 ;
        for (Installation inst : list ){
            puissance = puissance + inst.getPuissance()*inst.getNbre();
        }
        return puissance+nbre ;
    }

    @FXML
    void savePDF(ActionEvent event) {

    }

    public double calculTauxAutoCons(){
        Double utilisée = 0.0 ;
        Double total=0.0;
        int taille;
        if (productionTotaleListe.size()>consommationListe.size()){
            taille=consommationListe.size();
        }else {
            taille=productionTotaleListe.size();
        }
        for (int i=0; i<taille ;i++ ){
            if (productionTotaleListe.get(i).getProduction()<consommationListe.get(i).getConsommation()){
                utilisée=utilisée+productionTotaleListe.get(i).getProduction();
               //System.out.println("prod: "+productionTotaleListe.get(i).getProduction()+"   "+"cons: "+consommationListe.get(i).getConsommation());
            }
        }
        for (Installation inst: listInstallation){
            total +=inst.getProdTotale();
            //System.out.println(total);
        }
         double tauxAutoCons = (utilisée/total)*100;
       // System.out.println(tauxAutoCons);
        return tauxAutoCons;
    }


    public double calculTauxAutoProd(){
        int taille;
        Double utilisée = 0.0 ;
        Double total=0.0;
        if (productionTotaleListe.size()>consommationListe.size()){
            taille=consommationListe.size();
        }else {
            taille=productionTotaleListe.size();
        }
        for (int i=0; i<taille ;i++ ){
            if (productionTotaleListe.get(i).getProduction()<consommationListe.get(i).getConsommation()){
                utilisée=utilisée+productionTotaleListe.get(i).getProduction();
            }
        }
        for (Consommation cons: consommationListe){
            total = total+cons.getConsommation();
        }
        double tauxAutoProd=(utilisée/total)*100;
        return tauxAutoProd;
    }


    public double calculpotentielAnnuel(){
        double total = 0.0;
        for (Installation inst: listInstallation){
            total +=inst.getProdTotale();
            //System.out.println(total);
        }
        return total;
    }

    public double consommationTotaleAnnuel(){
        double total = 0.0 ;
        for (Consommation cons: consommationListe){
            total = total+cons.getConsommation();
        }
       return total;
    }



}
