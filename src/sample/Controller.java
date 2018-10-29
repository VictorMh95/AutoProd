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
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

import javafx.stage.Stage;
import org.apache.poi.*;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.formula.functions.T;
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
    @FXML private TableColumn<Installation,Integer> inclinaison;
    @FXML private TableColumn<Installation,Integer> orientation;
    @FXML private TableColumn<Installation,Integer> puissance;

    private int idNumber=1;

     ObservableList<Installation> listAjout = FXCollections.observableArrayList();

    /**
     *Cette fonction initialise la fenêtre
     *Le tableau est instancié
     *puis les tools tip
     * ensuite les binding sont instanciés, cela lie les différents champs entre eux
     * lorsque l'un d'eux est vide le bouton ajouter est disable
     * pareil pour calculer la surface totale , tous les champs doivent etre renseignés pour activer le bouton
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        numero.setCellValueFactory(new PropertyValueFactory<Installation, Integer>("numero"));
        surface.setCellValueFactory(new PropertyValueFactory<Installation, Integer>("surface"));
        inclinaison.setCellValueFactory(new PropertyValueFactory<Installation, Integer>("inclinaison"));
        orientation.setCellValueFactory(new PropertyValueFactory<Installation, Integer>("orientation"));
        puissance.setCellValueFactory(new PropertyValueFactory<Installation, Integer>("puissance"));
        puissancePV.setTooltip(new Tooltip(": Indiquer la puissance crête (Wc) d’un panneau solaire. A récupérer dans les fichiers techniques de panneaux solaires disponibles online."));
        rendementTF.setTooltip(new Tooltip("Prends en compte les pertes (hors rendement du panneau photovoltaïques). Ici, on prend par défaut PR = 0.675 (= multiplication des taux présentés ci-dessous).\n" +
                "Liste des pertes :\n" +
                "-\tTempérature = 8%\n" +
                "-\tOnduleur = 10%\n" +
                "-\tPoussière = 6%\n" +
                "-\tCâblage = 1%\n" +
                "-\tTransformateur = 2%\n" +
                "-\tErreur de prévisions = 3%\n" +
                "-\tOmbrage = 4%\n" +
                "-\tAutre = 2%\n" +
                "\n" +
                "En fonction du projet, le ratio de performance reste modifiable. (Nous conseillons d’utiliser un PR compris entre 0.6 < X < 0.85).\n" +
                "\n"));
        inclinaisonTF.setTooltip(new Tooltip("Angle d'inclinaison du panneau par rapport au sol. Ex : 45°,35°"));
        orientationTF.setTooltip(new Tooltip("Orientation du panneau par rapport à la longitude en degrès." +
                "Ex: Sud = 180° Nord = 0° Est = 90° Ouest = 270 °  "));
        rendementTF.setText("65");
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

    /**
     * Lorsque le bouton est cliqué un filchooser est lancé il permet de charger un fichier de l'ordinateur
     * le filechooser ne cherchera que les fichiers en format csv
     * ensuite le path du fichier est sauvegardé dans la variable "selected file"
     * t_loc affiche le nom di fichier en prenant que son nom et non le chemin
     * ensuite on applique la méthode "Readcsv" sur ce fichier
     * @param event
     * @throws IOException
     */
    @FXML
    void bt1(ActionEvent event) throws IOException {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File("C:"));
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichier csv", "*.csv")
        );
        File selectedFile = fc.showOpenDialog(null);
        if (selectedFile != null) {
            listProduction.clear();
            t_loc.setText(selectedFile.getAbsolutePath().substring(selectedFile.getAbsolutePath().lastIndexOf("\\") + 1)
            );
            ReadCSV(selectedFile);
        } else {
            System.out.println("Le fichier n'est pas bon");
        }
    }

    /**
     * Exactement le même fonctionnement que pour le bt1
     * @param event
     * @throws IOException
     */
    @FXML
    void bt2(ActionEvent event) throws IOException {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File("C:"));
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichier xls", "*.xls")
        );
        File selectedFile2 = fc.showOpenDialog(null);
        if (selectedFile2 != null) {
            listConsommation.clear();
            t_conso.setText(selectedFile2.getAbsolutePath().substring(selectedFile2.getAbsolutePath().lastIndexOf("\\") + 1));
            Readxls(selectedFile2);
        } else {
            System.out.println("Le fichier n'est pas valide");
        }
    }

    /**
     * Lorsque le bouton run est cliqué il lance plusieurs choses:
     *  - il ouvre une nouvelle fenêtre
     *  - il vérifie qu'il y a bien les deux fichiers chargés et une installation dans le tableau
     *  - si les conditions sont vérifiés il lance la méthode "dateProductiontoConsommation" ensuite il traite
     *  la production solaire à partir du fichier de radiation solaire et des différentes installation ajoutés
     *  ensuite on lance à partir du controller de la deuxième fenêtre la récupération des différentes listes utiles à la seconde fenêtre
     *
     * @param event
     * @throws IOException
     */
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
            dateProductionToConsommation();
            traitementProduction();
            Second_window controller2 = fxmlLoader.getController();
            controller2.initData(productionTotale,listConsommation,listProduction,listAjout);
            stage.show();
        }
    }


    /**
     *
     * @param str
     * @return false | true
     * Verifie que str est n'est pas un numerique
     */

    public static boolean isNotNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return true;
        }
        return false;

    }

    /**
     * Pop up alert
     * @param alertTitle
     * @param alertHeader
     * @param alertContentText
     */
    public void alert(String alertTitle,String alertHeader, String alertContentText  ){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(alertTitle);
        alert.setHeaderText(alertHeader);
        alert.setContentText(alertContentText);
        alert.showAndWait();
    }

    /**
     *
     * @param event
     * Permet de verfier si tous les champs sont bien des numeriques et ajoute une installation dans la liste "listAjout"
     * l'élément est ensuite ajouter au tableau
     */
    @FXML
    void ajouter(ActionEvent event) {


        if (isNotNumeric(surfacePV.getText().trim())||isNotNumeric(puissancePV.getText().trim())
                ||isNotNumeric(rendementTF.getText().trim())||isNotNumeric(orientationTF.getText().trim())||isNotNumeric(inclinaisonTF.getText().trim()))
            {
                alert("Erreur","Erreur variable d'entrée","Attention, il ne doit y avoir que des valeurs numeriques dans les champs liés à l'installation ");


            }else {

            Installation installation = new Installation(idNumber++,Double.parseDouble(nbrePV.getText()), Double.parseDouble(surfacePV.getText())
                    , Double.parseDouble(puissancePV.getText()), Double.parseDouble(rendementTF.getText()), Double.parseDouble(orientationTF.getText())
                    , Double.parseDouble(inclinaisonTF.getText()),0);

            listAjout.add(installation);
            tableView.setItems(listAjout);
        }

       // System.out.println(CalculTauxOrienIncli(Double.parseDouble(orientationTF.getText()),Double.parseDouble(inclinaisonTF.getText())));


    }

    /**
     *
     * @param event
     * permet de multiplier le nombre de panneaux et leur dimension pour obtenir la surface totale
     */

    @FXML
    void resultFire(ActionEvent event) {
        if (isNotNumeric(nbrePV.getText().trim())||isNotNumeric(longPV.getText().trim())||isNotNumeric(largPV.getText().trim())){
         alert("erreur","Erreur variable d'entrée","Attention, il ne peut y avoir que des valeurs numeriques dans les champs de 'Nombre de PV'");
        }else{
            Double resultat = Double.parseDouble(nbrePV.getText())*Double.parseDouble(longPV.getText())*Double.parseDouble(largPV.getText());
            surfacePV.setText(resultat.toString());

        }
    }

    /**
     * Permet de calculer un taux à partir de deux entrées : l'orientation et l'inclinaison
     * Par default la valeur de ce taux est de 70%
     * @param orientation
     * @param inclinaison
     * @return
     */
    public double CalculTauxOrienIncli (Double orientation ,Double inclinaison)
    {
        double taux = 0.70 ;
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

    /**
     * Permet de lire le ficher météorologique en format csv
     * la lecture commence à la ligne 35
     * la lecture se fait de ligne en ligne en prenant toujours la première colonne et la deuxième pour récupérer la date puis l'heure
     * ensuite on lit la cinquième colonne pour lire l'irradiation solaire globale
     * lorsque la date est lue, celle ci est lue en format string pour ensuite etre convertie en format date
     * la radiation est divisée par 1000 pour la mettre en KW
     * lorsque les deux valeurs "date" "production solaire" sont lues un nouvelle objet est créé
     * puis ajouté dans la liste "listProduction"
     * @param file
     * @throws IOException
     */
    public void ReadCSV(File file) throws IOException {
        CSVReader csvReader = new CSVReader(new FileReader(file), ';', '\'', 35);

        String[] nextline;
        int lineNumber = 0;
        while ((nextline = csvReader.readNext()) != null) {
            lineNumber++;

            String sDate1=nextline[0];
            String Heure=nextline[1];
            try {
                SimpleDateFormat formatter1=new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date date1 = formatter1.parse(sDate1+" "+Heure);
                Production production= new Production(date1
                        ,(Double.valueOf(nextline[5])/1000));
                listProduction.add(production);
              // System.out.println(production.getDate());
            }catch (Exception e){
                e.printStackTrace();

            }
        }
    }

    /**
     * Cette fonction permet de prendre l'année du fichier consomation et la remplacer dans le
     * fichier production pour avoir une superpostion des graphs par la suite
     */
    public void dateProductionToConsommation(){

        int anneeConso=listConsommation.get(1).getDate().getYear();


        for(int i=0;i<listProduction.size();i++){
            Date date=listProduction.get(i).getDate();
            System.out.println("date avant :"+listProduction.get(i).getDate());

            date.setYear(anneeConso);
           System.out.println("date apres "+date);
            listProduction.get(i).setDate(date);
        }
        //for(Production emp: listProduction){
          //  System.out.println("date1:"+emp.getDate()+" prod:"+emp.getProduction());
        //}

    }


    public  ArrayList<Consommation> listConsommation = new ArrayList<Consommation>();

    /**
     * Cette fonction lit le fichier xls qui correpond à la consommation du batiment
     * cette fonction utilise la bibliotheque "poi-xxxx"
     * la première condition vérifie que les intitulés des colonnes soient bien "date" et "consommation"
     * puis un objet consmmation est ajouter dans la liste "listConsommation"
     * la deuxième condition vérifie que les intitulées soient bien "date", "heure" et "consommation"
     * puis un objet consommation est ajouté à la liste précédemment citée
     * @param file
     * @throws IOException
     */
    public void Readxls (File file) throws IOException {

        try
        {
            FileInputStream fis = new FileInputStream(file);
            HSSFWorkbook workbook = new HSSFWorkbook(fis);

            HSSFSheet sheet = workbook.getSheetAt(0);
            System.out.println(String.valueOf(sheet.getRow(0).getCell(0).getStringCellValue().trim()).equals("date"));

            if(String.valueOf(sheet.getRow(0).getCell(0).getStringCellValue().trim()).equals("date") && String.valueOf(sheet.getRow(0).getCell(1).getStringCellValue().trim()).equals("consommation")){
               System.out.println("date/conso");
                for(int i=sheet.getFirstRowNum()+1;i<=sheet.getPhysicalNumberOfRows()-2;i++){
                    Row ro=sheet.getRow(i);
                    Consommation e = new Consommation(ro.getCell(0).getDateCellValue(),ro.getCell(1).getNumericCellValue());
                    listConsommation.add(e);
                }
            }else if(String.valueOf(sheet.getRow(0).getCell(0).getStringCellValue().trim()).equals("date") && String.valueOf(sheet.getRow(0).getCell(1).getStringCellValue().trim()).equals("heure") && String.valueOf(sheet.getRow(0).getCell(2).getStringCellValue().trim()).equals("consommation")){
                System.out.println("date/heure/conso");
                for(int i=sheet.getFirstRowNum()+1;i<=sheet.getPhysicalNumberOfRows()-2;i++) {
                    Row ro = sheet.getRow(i);
                    Date date = new Date();
                    Date heure = new Date();
                    Double conso;

                    date = ro.getCell(0).getDateCellValue();
                    heure = ro.getCell(1).getDateCellValue();
                    conso = ro.getCell(2).getNumericCellValue();
                    date.setHours(heure.getHours());
                    date.setMinutes(heure.getMinutes());

                    Consommation e = new Consommation(date, conso);
                    listConsommation.add(e);
                }
            }else {
                alert("Erreur","Format du document","3");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Supprime un élément du tableau
     * @param actionEvent
     */
    public void supprimer(ActionEvent actionEvent) {
        tableView.getItems().removeAll(tableView.getSelectionModel().getSelectedItem());
    }

   public  ArrayList<Production> productionTotale = new ArrayList<Production>();

    /**
     * à Faire (vico)
     */
    public  void traitementProduction() {
        productionTotale.clear();
        Double tauxGlobal;

        int a = 0;

        for (Installation inst : listAjout) {
            Double nbre = inst.getNbre();
            Double surface = inst.getSurface();
            Double puissance = inst.getPuissance();
            Double perf = (inst.getPr()) / 100;
            Double incl = inst.getInclinaison();
            Double orien = inst.getOrientation();

            Double tauxOrienIncl = CalculTauxOrienIncli(orien, incl);

            tauxGlobal = tauxOrienIncl * ((nbre * puissance) / (surface * 1000)) * perf;

            // System.out.println(tauxGlobal+" "+tauxOrienIncl+" "+nbre+" "+puissance+" "+surface+" "+perf);
            Double prodInstall=0.0;

            for (int i = 0; i < listProduction.size(); i++) {
                Double energieFinale = listProduction.get(i).getProduction() * surface * tauxGlobal;

                Production production = new Production();
                if (a == 0) {
                    production.setDate(listProduction.get(i).getDate());
                    production.setProduction(energieFinale);
                    productionTotale.add(i, production);
                    prodInstall=prodInstall+energieFinale;

                } else {
                    production.setDate(listProduction.get(i).getDate());
                    production.setProduction(energieFinale + productionTotale.get(i).getProduction());
                    productionTotale.set(i, production);
                    prodInstall=prodInstall+energieFinale;
                }

            }
            inst.setProdTotale(prodInstall);
            //System.out.println("prodinstall"+inst.getProdTotale());
            a++;
            for(Production emp: productionTotale){
           //System.out.println("dateTratement:"+emp.getDate()+" prod:"+emp.getProduction());
            }
        }
    }


}

