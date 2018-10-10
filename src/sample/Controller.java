package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.opencsv.CSVReader;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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

    final ObservableList<Installation> listAjout = FXCollections.observableArrayList();


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
        stage.show();
    }


    @FXML
    void ajouter(ActionEvent event) {

        Installation installation= new Installation(idNumber++,10,12,Double.parseDouble(surfacePV.getText()),comboBox.getValue()+","+typeValue.getText()+"%"
                ,Double.parseDouble(puissancePV.getText()),Double.parseDouble(rendementTF.getText()),Integer.parseInt(orientationTF.getText())
                ,Integer.parseInt(inclinaisonTF.getText()));

        listAjout.add(installation);
        tableView.setItems(listAjout);
    }

    @FXML
    void setValuePV(ActionEvent event) {
        if (comboBox.getValue()=="Poly"){
            typeValue.setText("13");
        } else if (comboBox.getValue()=="Mono"){
            typeValue.setText("16");
        }else if (comboBox.getValue()=="Amorphe"){
            typeValue.setText("6");
        }
    }


    @FXML
    void resultFire(ActionEvent event) {

        Double resultat = Double.parseDouble(nbrePV.getText())*Double.parseDouble(longPV.getText())*Double.parseDouble(largPV.getText());
        surfacePV.setText(resultat.toString());

    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        numero.setCellValueFactory(new PropertyValueFactory<Installation,Integer>("numero"));
        surface.setCellValueFactory(new PropertyValueFactory<Installation,Integer>("surface"));
        type.setCellValueFactory(new PropertyValueFactory<Installation,String>("type"));
        inclinaison.setCellValueFactory(new PropertyValueFactory<Installation,Integer>("inclinaison"));
        orientation.setCellValueFactory(new PropertyValueFactory<Installation,Integer>("orientation"));
        puissance.setCellValueFactory(new PropertyValueFactory<Installation,Integer>("puissance"));
        comboBox.getItems().addAll("Mono","Poly","Amorphe");

    }


    ArrayList<Production> listProduction = new ArrayList<Production>();

    public void ReadCSV(File file) throws IOException {
        CSVReader csvReader = new CSVReader(new FileReader(file), ';', '\'', 35);

        String[] nextline;
        int lineNumber = 0;
        while ((nextline = csvReader.readNext()) != null) {
            lineNumber++;
            System.out.println("Line # " + lineNumber);

            Double conso = (Integer.parseInt(nextline[5])+0.01);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd");
            String dateInString=nextline[0];

            try {
                Date date = formatter.parse(dateInString);
                Production production= new Production(date
                        ,Double.valueOf(nextline[5]));
                listProduction.add(production);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            // nextLine[] is an array of values from the line


            System.out.println(nextline[0]+","+conso);
        }
    }



    public void Readxls (File file) throws IOException {

        FileInputStream fis = new FileInputStream(file);
        HSSFWorkbook workbook = new HSSFWorkbook(fis);
        HSSFSheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rowIt = sheet.iterator();

        while (rowIt.hasNext()){
            Row row = rowIt.next();

            Iterator<Cell> cellIterator = row.cellIterator();

            while (cellIterator.hasNext()){
                Cell cell = cellIterator.next();

                System.out.print(cell.toString()+";");
            }
            System.out.println();
        }

    }


    public void supprimer(ActionEvent actionEvent) {
        tableView.getItems().removeAll(tableView.getSelectionModel().getSelectedItem());

    }
}