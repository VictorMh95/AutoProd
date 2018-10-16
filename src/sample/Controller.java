package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.opencsv.CSVReader;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.event.ActionEvent;
import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javafx.stage.Stage;
import org.apache.poi.*;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.SystemOutLogger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;

public class Controller implements Initializable {

    @FXML
    private JFXButton button_excel_loc;

    @FXML
    private JFXButton button_excel_conso;

    @FXML
    private JFXTextField t_loc;

    @FXML
    private JFXTextField t_conso;

    @FXML
    public JFXTextField nbrePV ;
    @FXML
    public JFXTextField puissancePV;
    @FXML
    public JFXTextField rendementTF ;
    @FXML
    public JFXTextField inclinaisonTF;
    @FXML
    public JFXTextField orientationTF ;
    @FXML
    private JFXTextField typeValue;
    @FXML
    private JFXTextField longPV;

    @FXML
    private JFXTextField largPV;

    @FXML
    private JFXTextField surfacePV;

    @FXML
    private JFXButton resultSurface;

    @FXML
    private JFXButton ajouter ;

    @FXML private TableView<Installation> tableView;
    @FXML private TableColumn<Installation,Integer> numero;
    @FXML private TableColumn<Installation, Integer> surface;
    @FXML private TableColumn<Installation,String> type;
    @FXML private TableColumn<Installation,Integer> inclinaison;
    @FXML private TableColumn<Installation,Integer> orientation;
    @FXML private TableColumn<Installation,Integer> puissance;

    @FXML private ComboBox<String> comboBox;
    private int idNumber=1;

     ObservableList<Installation> listAjout = FXCollections.observableArrayList();


    @FXML
    void bt1(ActionEvent event) throws IOException {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File("C:"));
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichier csv", "*.csv")
        );
        File selectedFile = fc.showOpenDialog(null);
        if (selectedFile != null) {
            t_loc.setText(selectedFile.getAbsolutePath().substring(selectedFile.getAbsolutePath().lastIndexOf("\\") + 1)
            );
            ReadCSV(selectedFile);
        } else {
            System.out.println("Le fichier n'est pas bon");
        }
    }

    @FXML
    void bt2(ActionEvent event) throws IOException {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File("C:"));
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichier xls", "*.xls")
        );
        File selectedFile2 = fc.showOpenDialog(null);
        if (selectedFile2 != null) {
            t_conso.setText(selectedFile2.getAbsolutePath().substring(selectedFile2.getAbsolutePath().lastIndexOf("\\") + 1));
            Readxls(selectedFile2);
        } else {
            System.out.println("Le fichier n'est pas valide");
        }
    }

    @FXML
    void Run (ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("second_window.fxml"));

        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Résultats");
        stage.setScene(new Scene(root1,1259, 750));

        if(listAjout.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur tableau");
            alert.setHeaderText("Attention tableau !");
            alert.setContentText("La tableau doit avoir au moins une installation");
            alert.showAndWait();

        }else if(t_conso.getText().trim().isEmpty()||t_loc.getText().trim().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur fichier");
            alert.setHeaderText("Attention fichier !");
            alert.setContentText("Les fichiers ne sont pas chargés");
            alert.showAndWait();

        }else{
            traitementProduction();
            stage.show();
        }

    }





    @FXML
    void ajouter(ActionEvent event) {


        if (surfacePV.getText().trim().isEmpty()||puissancePV.getText().trim().isEmpty()
                ||rendementTF.getText().trim().isEmpty()||orientationTF.getText().trim().isEmpty()||inclinaisonTF.getText().trim().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur champ");
                alert.setHeaderText("Attention champ !");
                alert.setContentText("Un des champs est manquant !");
                alert.showAndWait();

            }else {

            Installation installation = new Installation(idNumber++,Double.parseDouble(nbrePV.getText()), Double.parseDouble(surfacePV.getText())
                    , Double.parseDouble(puissancePV.getText()), Double.parseDouble(rendementTF.getText()), Double.parseDouble(orientationTF.getText())
                    , Double.parseDouble(inclinaisonTF.getText()));

            listAjout.add(installation);
            tableView.setItems(listAjout);
        }

       // System.out.println(CalculTauxOrienIncli(Double.parseDouble(orientationTF.getText()),Double.parseDouble(inclinaisonTF.getText())));


    }



    @FXML
    void resultFire(ActionEvent event) {

        Double resultat = Double.parseDouble(nbrePV.getText())*Double.parseDouble(longPV.getText())*Double.parseDouble(largPV.getText());
        surfacePV.setText(resultat.toString());

    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        numero.setCellValueFactory(new PropertyValueFactory<Installation, Integer>("numero"));
        surface.setCellValueFactory(new PropertyValueFactory<Installation, Integer>("surface"));
        inclinaison.setCellValueFactory(new PropertyValueFactory<Installation, Integer>("inclinaison"));
        orientation.setCellValueFactory(new PropertyValueFactory<Installation, Integer>("orientation"));
        puissance.setCellValueFactory(new PropertyValueFactory<Installation, Integer>("puissance"));
        inclinaisonTF.setTooltip(new Tooltip("Angle d'inclinaison du panneau par rapport au sol. Ex : 45°,35°"));
        orientationTF.setTooltip(new Tooltip("Orientation du panneau par rapport à la longitude en degrès." +
                "Ex: Sud = 180° Nord = 0° Est = 90° Ouest = 270 °  "));

        BooleanBinding bb = new BooleanBinding() {
            {
                super.bind(surfacePV.textProperty(),
                        puissancePV.textProperty(),
                        rendementTF.textProperty(),
                        inclinaisonTF.textProperty(),
                        orientationTF.textProperty());
            }

            @Override
            protected boolean computeValue() {
                return (surfacePV.getText().isEmpty() ||
                        puissancePV.getText().isEmpty() ||
                        rendementTF.getText().isEmpty() ||
                        inclinaisonTF.getText().isEmpty() ||
                        orientationTF.getText().isEmpty());
            }
        };
        ajouter.disableProperty().bind(bb);

        BooleanBinding bb2 = new BooleanBinding() {
            {
                super.bind(nbrePV.textProperty(),
                        largPV.textProperty(),
                        longPV.textProperty()
                );
            }

            @Override
            protected boolean computeValue() {
                return (nbrePV.getText().isEmpty() ||
                        largPV.getText().isEmpty() ||
                        longPV.getText().isEmpty());
            }
        };
        resultSurface.disableProperty().bind(bb2);
    }



    public double CalculTauxOrienIncli (Double orientation ,Double inclinaison)
    {
        double taux = 0.95 ;
        if (90<=orientation && orientation<=270 && 0<=inclinaison && inclinaison<=20) {
            taux = 0.88 ;
        }
        if(67.5<=orientation && orientation<=112.5 && 20<=inclinaison && inclinaison<=42.5){
            taux =  0.84;
        }
        if (247.5<=orientation && orientation<=292.5 && 20<=inclinaison && inclinaison<=42.5){
            taux =  0.84;
        }
        if (112.5<=orientation && orientation<=157.5 && 20<=inclinaison && inclinaison<=60) {
            taux =  0.95;
        }
        if (202.5<=orientation && orientation<=247.5 && 20<=inclinaison && inclinaison<=47.5){
            taux =  1;
        }
        if (157.5<=orientation && orientation<=202.5 && 20<=inclinaison && inclinaison<= 47.5){
            taux =  1;
        }
        if (157.5<=orientation && orientation<=202.5 && 42.5<=inclinaison && inclinaison<=60){
            taux = 0.98;
        }
        if (67.5<=orientation && orientation<=112.5 && 42.5<=inclinaison && inclinaison<=60){
            taux = 0.77;
        }
        if (247.5<=orientation && orientation<=292.5 && 42.5<=inclinaison && inclinaison<60){
            taux =  0.76;
        }
        if (90<=orientation && orientation<=270 && 60<=inclinaison && inclinaison<=90){
            taux =  0.66;
        }
        return taux ;
    }


    ArrayList<Production> listProduction = new ArrayList<Production>();

    public void ReadCSV(File file) throws IOException {
        CSVReader csvReader = new CSVReader(new FileReader(file), ';', '\'', 35);

        String[] nextline;
        int lineNumber = 0;
        while ((nextline = csvReader.readNext()) != null) {
            lineNumber++;

            //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd");
            //String dateInString=nextline[0];

                //Date date = formatter.parse(dateInString);
                Production production= new Production(nextline[0]
                        ,(Double.valueOf(nextline[5])/1000));
                listProduction.add(production);

            // nextLine[] is an array of values from the line

        }



    }


    public  ArrayList<Consommation> listConsommation = new ArrayList<Consommation>();

    public void Readxls (File file) throws IOException {

        try
        {
            FileInputStream fis = new FileInputStream(file);
            HSSFWorkbook workbook = new HSSFWorkbook(fis);

            HSSFSheet sheet = workbook.getSheetAt(0);

            for(int i=sheet.getFirstRowNum();i<=sheet.getPhysicalNumberOfRows()-2;i++){
                Consommation e= new Consommation();
                Row ro=sheet.getRow(i);
                for(int j=0;j<=2;j++){
                    Cell ce = ro.getCell(j);
                    if(j==0){
                        e.setDate(ce.getDateCellValue());
                    }
                    if(j==1){
                       e.setConsommation(ce.getNumericCellValue());
                    }
                }
                listConsommation.add(e);
            }
            for(Consommation emp: listConsommation){
               // System.out.println("date:"+emp.getDate()+" conso:"+emp.getConsommation());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    public void supprimer(ActionEvent actionEvent) {
        tableView.getItems().removeAll(tableView.getSelectionModel().getSelectedItem());

    }

   public final ArrayList<Production> productionTotale = new ArrayList<Production>();

    public  void traitementProduction(){
            Double tauxGlobal;

        int a=0;

        for (Installation inst: listAjout){
            Double nbre = inst.getNbre();
            Double surface = inst.getSurface();
            Double puissance = inst.getPuissance();
            Double perf =  (inst.getPr())/100;
            Double incl =inst.getInclinaison();
            Double orien = inst.getOrientation();

            Double tauxOrienIncl = CalculTauxOrienIncli(orien,incl);

            tauxGlobal= tauxOrienIncl * ((nbre*puissance)/(surface*1000))*perf;

            System.out.println(tauxGlobal+" "+tauxOrienIncl+" "+nbre+" "+puissance+" "+surface+" "+perf);


            for (int i=0;i<listProduction.size();i++){
             Double energieFinale = listProduction.get(i).getProduction()*surface*tauxGlobal;

                Production production = new Production();

                if(a==0){
                    production.setDate(listProduction.get(i).getDate());
                    production.setProduction(energieFinale);
                    productionTotale.add(i,production);


                }else{
                    production.setDate(listProduction.get(i).getDate());
                    production.setProduction(energieFinale+productionTotale.get(i).getProduction());
                    productionTotale.set(i,production);

                }


             }
            a++;

        for(Production emp: productionTotale){
            System.out.println("date:"+emp.getDate()+" conso:"+emp.getProduction());
        }

        }
    }







}