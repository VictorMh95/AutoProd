package sample;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
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

    /**
     * Cette fonction permet de récupérer les différentes list qui sont nécéssaires à l'affichage des graphiques ainsi que différentes informations
     * @param productionTotale
     * @param consommation
     * @param irradiation
     * @param installation
     */
    public void initData(ArrayList<Production> productionTotale,ArrayList<Consommation> consommation,ArrayList<Production> irradiation,
                         ObservableList<Installation> installation){
        productionTotaleListe = (ArrayList<Production>)productionTotale;
        consommationListe = (ArrayList<Consommation>)consommation;
        ensoleillement = (ArrayList<Production>)irradiation;
        listInstallation = installation;
        puissanceTF.setText(calculpuissane(listInstallation).toString());
        surfaceTF.setText(calculsurface(listInstallation).toString());
    }

    /**
     *
      * @param event
     */
    @FXML
    void afficherConso(ActionEvent event) {
        displayGraphConsoProd(consommationListe,productionTotaleListe);
        displayGraphEnsoleillement(ensoleillement);
        tableView.setItems(listInstallation);
        TFautoConso.setText(String.valueOf(calculTauxAutoCons()));
        TFautoprod.setText(String.valueOf(calculTauxAutoProd()));
        potentienProdAnnuel.setText(String.valueOf(calculpotentielAnnuel()));
        consBatAnnuel.setText(String.valueOf(consommationTotaleAnnuel()));
    }

    /**
     * Cette fonction permet d'afficher le graphique de la production solaire et de la consommation superposées
     * elle prend en paramètre les différentes list pour ensuite les afficher
     * une première serie est construite à partie de la liste consommation
     * puis une deuxième est constituée à partir de la liste production
     * les dates des deux liste doivent concordées pour avoir une superposition significative
     * les séries sont ensuites ajoutées aux graphiques
     * @param conso
     * @param prod
     */
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

    /**
     * Cette fonction a le même fonctionnement que la précédente mais pour le graphiquement de l'irradiation solaire
     * @param list
     */

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

    /**
     * Calcule la surafece totale de l'installation à partir des surfaces ajoutés dans le tableau
     * @param list
     * @return
     */

    public Double calculsurface(ObservableList<Installation> list) {
        double surf=0 ;
        for (Installation inst : list){
            surf = surf + inst.getSurface();
        }
        return surf;
    }

    /**
     * Calcule la puissance totale en Wc de l'installation à partir des puissances des installations
     * @param list
     * @return
     */
    public Double calculpuissane(ObservableList<Installation> list){
        double puissance = 0 ;
        double nbre = 0 ;
        for (Installation inst : list ){
            puissance = puissance + inst.getPuissance()*inst.getNbre();
        }
        return puissance+nbre ;
    }


    /**
     * Permet de calculer le taux d'autoconsommation de l'installation en prenant comme calcule :
     *
     * (la production utilisée) / ( la production totale) *100
     *
     * la production est récupérée quand elle est inférieure à la consommation du batiment
     *
     * @return
     */
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

    /**
     * Permet de calculer le taux d'autoproduction de l'installation
     *
     * on récupère comme pour le calcul précédent la production inférieur à la consommation
     * diviser par la consommation totale du batiment
     *
     * @return
     */

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

    public static final String RESULT
            = "C:\\Users\\thomas\\Desktop\\Projet Co-elab\\test.pdf";

    @FXML
    void savePDF(ActionEvent event) throws IOException, DocumentException {
        //createPdf(RESULT);
    }
    @FXML
    private ScrollPane scrollpane;

    /**public void createPdf(String filename)
            throws DocumentException, IOException {
        BufferedImage bufImage = SwingFXUtils.fromFXImage(scrollpane.snapshot(new SnapshotParameters(), null), null);
        FileOutputStream out = new FileOutputStream(filename);
        javax.imageio.ImageIO.write(bufImage, "jpg", out);
        out.flush();
        out.close();

        com.itextpdf.text.Image image =com.itextpdf.text.Image.getInstance(filename);

        Document doc = new Document(new com.itextpdf.text.Rectangle(image.getScaledWidth(), image.getScaledHeight()));
        FileOutputStream fos = new FileOutputStream(filename);
        PdfWriter.getInstance(doc, fos);
        doc.open();
        doc.newPage();
        image.setAbsolutePosition(0, 0);
        doc.add(image);
        fos.flush();
        doc.close();
        fos.close();

    }**/


}
