package sample;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class Second_window implements Initializable {

    Consommation consommation = new Consommation();
    @FXML
    public LineChart<Date,Double > graphConso;

    public void DisplayGraphConso(ArrayList<Consommation> list){
        graphConso.getData().clear();
        Date date ;
        Double conso;
        XYChart.Series<Date,Double> series = new XYChart.Series<>();
        for (Consommation cons: list){
            date=cons.getDate();
            conso = cons.getConsommation();
            series.getData().add(new XYChart.Data<>(date,conso));
        }
        graphConso.getData().add(series);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }


}
