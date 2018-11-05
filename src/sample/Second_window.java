package sample;


import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

import java.net.URL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class Second_window implements Initializable {


    @FXML
    private LineChart<String, Number> graphConso;
    @FXML
    private LineChart<String, Number> graphEnsoleillement;
    @FXML
    private BarChart<String, Number> ConsoProdGraph;

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

    @FXML
    private JFXTextField date1;

    @FXML
    private JFXTextField date2;

    @FXML
    private JFXTextField co2;

    @FXML
    private TableView<Installation> tableView;
    @FXML
    private TableColumn<Installation, Integer> numero;
    @FXML
    private TableColumn<Installation, Integer> surface;
    @FXML
    private TableColumn<Installation, Integer> inclinaison;
    @FXML
    private TableColumn<Installation, Integer> orientation;
    @FXML
    private TableColumn<Installation, Integer> puissance;
    @FXML
    private TableColumn<Installation, Integer> prodTotale;

    public ArrayList<Production> productionTotaleListe = new ArrayList<Production>();
    public ArrayList<Consommation> consommationListe = new ArrayList<Consommation>();
    public ArrayList<Production> ensoleillement = new ArrayList<Production>();
    public ObservableList<Installation> listInstallation = FXCollections.observableArrayList();

    public DateFormat dateFormat = new SimpleDateFormat("dd-MM HH:mm");


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        numero.setCellValueFactory(new PropertyValueFactory<Installation, Integer>("numero"));
        surface.setCellValueFactory(new PropertyValueFactory<Installation, Integer>("surface"));
        inclinaison.setCellValueFactory(new PropertyValueFactory<Installation, Integer>("inclinaison"));
        orientation.setCellValueFactory(new PropertyValueFactory<Installation, Integer>("orientation"));
        puissance.setCellValueFactory(new PropertyValueFactory<Installation, Integer>("puissance"));
        prodTotale.setCellValueFactory(new PropertyValueFactory<Installation, Integer>("prodTotale"));
    }

    /**
     * Cette fonction permet de récupérer les différentes list qui sont nécéssaires à l'affichage des graphiques ainsi que différentes informations
     *
     * @param productionTotale
     * @param consommation
     * @param irradiation
     * @param installation
     */
    public void initData(ArrayList<Production> productionTotale, ArrayList<Consommation> consommation, ArrayList<Production> irradiation,
                         ObservableList<Installation> installation) {
        productionTotaleListe = productionTotale;
        consommationListe = consommation;
        ensoleillement = irradiation;
        listInstallation = installation;
        puissanceTF.setText(doubleToStringFormatted(calculpuissane(listInstallation)));
        surfaceTF.setText(doubleToStringFormatted(calculsurface(listInstallation)));
        sizeChartInit();
        afficherConso();
    }


    public void sizeChartInit() {
        graphEnsoleillement.setPrefWidth(ensoleillement.size() * 2.5);
        graphConso.setPrefWidth(ensoleillement.size() * 2.5);

    }


    /**
     * Cette fonction est appelé a la fin de initData pour remplir les differents champs
     */

    @FXML
    public void afficherConso() {
        Double prodTotale = calculpotentielAnnuel();
        displayGraphConsoProd(consommationListe, productionTotaleListe);
        displayGraphEnsoleillement(ensoleillement);
        tableView.setItems(listInstallation);
        TFautoConso.setText(doubleToStringFormatted(calculTauxAutoCons()));
        TFautoprod.setText(doubleToStringFormatted(calculTauxAutoProd()));
        potentienProdAnnuel.setText(doubleToStringFormatted(prodTotale));
        co2.setText(doubleToStringFormatted(emissionCo2(prodTotale)));
        consBatAnnuel.setText(doubleToStringFormatted(consommationTotaleAnnuel()));
        date1.setText(dateFormat.format(productionTotaleListe.get(0).getDate()));
        date2.setText(dateFormat.format(productionTotaleListe.get(productionTotaleListe.size() - 1).getDate()));

    }

    /**
     * Cette fonction permet d'afficher le graphique de la production solaire et de la consommation superposées
     * elle prend en paramètre les différentes list pour ensuite les afficher
     * une première serie est construite à partie de la liste consommation
     * puis une deuxième est constituée à partir de la liste production
     * les dates des deux liste doivent concordées pour avoir une superposition significative
     * les séries sont ensuites ajoutées aux graphiques
     *
     * @param conso
     * @param prod
     */
    public void displayGraphConsoProd(ArrayList<Consommation> conso, ArrayList<Production> prod) {
        String dateconso;
        Number consommation;
        String dateprod;
        Number production;


        XYChart.Series<String, Number> seriesConso = new XYChart.Series<>();
        seriesConso.setName("Consommation en kWh");

        XYChart.Series<String, Number> seriesProd = new XYChart.Series<>();
        seriesProd.setName("Production en kWh");


        //barchart

        final String janvier = "Janvier";
        Double janvierN = 0.0;
        Double janvierP = 0.0;
        final String fevrier = "Fevrier";
        Double fevrierN = 0.0;
        Double fevrierP = 0.0;
        final String mars = "Mars";
        Double marsN = 0.0;
        Double marsP = 0.0;
        final String avril = "Avril";
        Double avrilN = 0.0;
        Double avrilP = 0.0;
        final String mai = "Mai";
        Double maiN = 0.0;
        Double maiP = 0.0;
        final String juin = "Juin";
        Double juinN = 0.0;
        Double juinP = 0.0;
        final String juillet = "Juillet";
        Double juilletN = 0.0;
        Double juilletP = 0.0;
        final String aout = "Aout";
        Double aoutN = 0.0;
        Double aoutP = 0.0;
        final String septembre = "Septembre";
        Double septembreN = 0.0;
        Double septembreP = 0.0;
        final String octobre = "Octobre";
        Double octobreN = 0.0;
        Double octobreP = 0.0;
        final String novembre = "Novembre";
        Double novembreN = 0.0;
        Double novembreP = 0.0;
        final String decembre = "Decembre";
        Double decembreN = 0.0;
        Double decembreP = 0.0;

        final XYChart.Series<String, Number> seriesConsoB =
                new XYChart.Series<String, Number>();
        seriesConsoB.setName("Consommation en KWh");


        final XYChart.Series<String, Number> seriesProdB =
                new XYChart.Series<String, Number>();
        seriesProdB.setName("Production en KWh");


        for (int i = 0; i < conso.size(); i++) {
            dateconso = dateFormat.format(conso.get(i).getDate());
            consommation = conso.get(i).getConsommation();

            seriesConso.getData().add(new XYChart.Data<String, Number>(dateconso, consommation));


            switch (conso.get(i).getDate().getMonth()) {
                case 0:
                    janvierN += conso.get(i).getConsommation();
                    break;
                case 1:
                    fevrierN += conso.get(i).getConsommation();
                    break;
                case 2:
                    marsN += conso.get(i).getConsommation();

                    break;
                case 3:
                    avrilN += conso.get(i).getConsommation();

                    break;
                case 4:
                    maiN += conso.get(i).getConsommation();

                    break;
                case 5:
                    juinN += conso.get(i).getConsommation();

                    break;
                case 6:
                    juilletN += conso.get(i).getConsommation();

                    break;
                case 7:
                    aoutN += conso.get(i).getConsommation();

                    break;
                case 8:
                    septembreN += conso.get(i).getConsommation();

                    break;
                case 9:
                    octobreN += conso.get(i).getConsommation();

                    break;
                case 10:
                    novembreN += conso.get(i).getConsommation();

                    break;
                case 11:
                    decembreN += conso.get(i).getConsommation();

                    break;
            }

        }

        for (int i = 0; i < prod.size(); i++) {
            //Line
            production = prod.get(i).getProduction();
            dateprod = dateFormat.format(prod.get(i).getDate());

            seriesProd.getData().add(new XYChart.Data<String, Number>(dateprod, production));

            //Bar

            switch (prod.get(i).getDate().getMonth()) {
                case 0:
                    janvierP += prod.get(i).getProduction();
                    break;
                case 1:
                    fevrierP += prod.get(i).getProduction();
                    break;
                case 2:
                    marsP += prod.get(i).getProduction();

                    break;
                case 3:
                    avrilP += prod.get(i).getProduction();

                    break;
                case 4:
                    maiP += prod.get(i).getProduction();

                    break;
                case 5:
                    juinP += prod.get(i).getProduction();

                    break;
                case 6:
                    juilletP += prod.get(i).getProduction();

                    break;
                case 7:
                    aoutP += prod.get(i).getProduction();

                    break;
                case 8:
                    septembreP += prod.get(i).getProduction();

                    break;
                case 9:
                    octobreP += prod.get(i).getProduction();

                    break;
                case 10:
                    novembreP += prod.get(i).getProduction();

                    break;
                case 11:
                    decembreP += prod.get(i).getProduction();

                    break;
            }


        }

        graphConso.getData().addAll(seriesProd, seriesConso);
        graphConso.setCreateSymbols(false);
        graphEnsoleillement.setCreateSymbols(false);


        seriesConsoB.getData().add(new XYChart.Data<String, Number>(janvier, janvierN));
        seriesConsoB.getData().add(new XYChart.Data<String, Number>(fevrier, fevrierN));
        seriesConsoB.getData().add(new XYChart.Data<String, Number>(mars, marsN));
        seriesConsoB.getData().add(new XYChart.Data<String, Number>(avril, avrilN));
        seriesConsoB.getData().add(new XYChart.Data<String, Number>(mai, maiN));
        seriesConsoB.getData().add(new XYChart.Data<String, Number>(juin, juinN));
        seriesConsoB.getData().add(new XYChart.Data<String, Number>(juillet, juilletN));
        seriesConsoB.getData().add(new XYChart.Data<String, Number>(aout, aoutN));
        seriesConsoB.getData().add(new XYChart.Data<String, Number>(septembre, septembreN));
        seriesConsoB.getData().add(new XYChart.Data<String, Number>(octobre, octobreN));
        seriesConsoB.getData().add(new XYChart.Data<String, Number>(novembre, novembreN));
        seriesConsoB.getData().add(new XYChart.Data<String, Number>(decembre, decembreN));

        seriesProdB.getData().add(new XYChart.Data<String, Number>(janvier, janvierP));
        seriesProdB.getData().add(new XYChart.Data<String, Number>(fevrier, fevrierP));
        seriesProdB.getData().add(new XYChart.Data<String, Number>(mars, marsP));
        seriesProdB.getData().add(new XYChart.Data<String, Number>(avril, avrilP));
        seriesProdB.getData().add(new XYChart.Data<String, Number>(mai, maiP));
        seriesProdB.getData().add(new XYChart.Data<String, Number>(juin, juinP));
        seriesProdB.getData().add(new XYChart.Data<String, Number>(juillet, juilletP));
        seriesProdB.getData().add(new XYChart.Data<String, Number>(aout, aoutP));
        seriesProdB.getData().add(new XYChart.Data<String, Number>(septembre, septembreP));
        seriesProdB.getData().add(new XYChart.Data<String, Number>(octobre, octobreP));
        seriesProdB.getData().add(new XYChart.Data<String, Number>(novembre, novembreP));
        seriesProdB.getData().add(new XYChart.Data<String, Number>(decembre, decembreP));

        ConsoProdGraph.getData().addAll(seriesProdB, seriesConsoB);

        for (XYChart.Series<String, Number> s : ConsoProdGraph.getData()) {
            for (XYChart.Data<String, Number> d : s.getData()) {
                Tooltip.install(d.getNode(), new Tooltip("Total: " + d.getYValue().toString()));
            }
        }
    }

    /**
     * Cette fonction a le même fonctionnement que la précédente mais pour le graphiquement de l'irradiation solaire
     *
     * @param list
     */

    public void displayGraphEnsoleillement(ArrayList<Production> list) {
        String date;
        Number production;
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Irradiation en kWh/m²");
        DateFormat dateFormat = new SimpleDateFormat("dd-MM HH:mm");

        for (int i = 0; i < list.size(); i++) {
            date = dateFormat.format(list.get(i).getDate());
            production = list.get(i).getProduction();
            series.getData().add(new XYChart.Data<String, Number>(date, production));
        }
        graphEnsoleillement.getData().addAll(series);
        Node line = series.getNode().lookup(".chart-series-line");

        Color color = Color.BLUE; // or any other color

        String rgb = String.format("%d, %d, %d",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
        line.setStyle("-fx-stroke: rgba(" + rgb + ", 1.0);");

    }


    /**
     * Calcule la surafece totale de l'installation à partir des surfaces ajoutés dans le tableau
     *
     * @param list
     * @return
     */

    public Double calculsurface(ObservableList<Installation> list) {
        double surf = 0;
        for (Installation inst : list) {
            surf = surf + inst.getSurface();
        }
        return surf;
    }

    /**
     * Calcule la puissance totale en Wc de l'installation à partir des puissances des installations
     *
     * @param list
     * @return
     */
    public Double calculpuissane(ObservableList<Installation> list) {
        double puissance = 0;
        double nbre = 0;
        for (Installation inst : list) {
            puissance = puissance + inst.getPuissance() * inst.getNbre();
        }
        return (puissance + nbre);
    }


    /**
     * Permet de calculer le taux d'autoconsommation de l'installation en prenant comme calcule :
     * <p>
     * (la production utilisée) / ( la production totale) *100
     * <p>
     * la production est récupérée quand elle est inférieure à la consommation du batiment
     *
     * @return
     */
    public Double calculTauxAutoCons() {
        Double utilisee = 0.0;
        Double total = 0.0;
        int taille;
        if (productionTotaleListe.size() > consommationListe.size()) {
            taille = consommationListe.size();
        } else {
            taille = productionTotaleListe.size();
        }
        for (int i = 0; i < taille; i++) {
            if (productionTotaleListe.get(i).getProduction() < consommationListe.get(i).getConsommation()) {
                utilisee = utilisee + productionTotaleListe.get(i).getProduction();
                //System.out.println("prod: "+productionTotaleListe.get(i).getProduction()+"   "+"cons: "+consommationListe.get(i).getConsommation());
            }
        }
        for (Installation inst : listInstallation) {
            total += inst.getProdTotale();
            //System.out.println(total);
        }
        double tauxAutoCons = (utilisee / total) * 100;
        // System.out.println(tauxAutoCons);
        return tauxAutoCons;
    }

    /**
     * Permet de calculer le taux d'autoproduction de l'installation
     * <p>
     * on récupère comme pour le calcul précédent la production inférieur à la consommation
     * diviser par la consommation totale du batiment
     *
     * @return
     */

    public Double calculTauxAutoProd() {
        int taille;
        Double utilisee = 0.0;
        Double total = 0.0;
        if (productionTotaleListe.size() > consommationListe.size()) {
            taille = consommationListe.size();
        } else {
            taille = productionTotaleListe.size();
        }
        for (int i = 0; i < taille; i++) {
            if (productionTotaleListe.get(i).getProduction() < consommationListe.get(i).getConsommation()) {
                utilisee = utilisee + productionTotaleListe.get(i).getProduction();
            }
        }
        for (Consommation cons : consommationListe) {
            total = total + cons.getConsommation();
        }
        double tauxAutoProd = (utilisee / total) * 100;
        return tauxAutoProd;
    }


    public Double calculpotentielAnnuel() {
        double total = 0.0;
        for (Installation inst : listInstallation) {
            total += inst.getProdTotale();
        }
        return total;
    }

    public String doubleToStringFormatted(Double number) {
        DecimalFormat df = new DecimalFormat("###.##");
        return df.format(number);
    }

    public Double consommationTotaleAnnuel() {
        double total = 0.0;
        for (Consommation cons : consommationListe) {
            total = total + cons.getConsommation();
        }
        return total;
    }

    public Double emissionCo2(Double production) {
        double emission = production * 0.027;
        return emission;
    }

}


