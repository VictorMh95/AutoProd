package sample;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Installation {
    private SimpleIntegerProperty numero;
    private SimpleDoubleProperty nbre;
    private SimpleDoubleProperty surface ;
    private SimpleDoubleProperty puissance ;
    private SimpleDoubleProperty pr ;
    private SimpleDoubleProperty orientation ;
    private SimpleDoubleProperty inclinaison ;
    private SimpleDoubleProperty prodTotale ;

    public Installation (int numero ,double nbre, double surface , double puissance , double pr , double orientation
                          , double inclinaison,double prodTotale ){
                this.numero= new SimpleIntegerProperty(numero);
                this.nbre = new SimpleDoubleProperty(nbre);
                this.surface=new SimpleDoubleProperty(surface);
                this.puissance=new SimpleDoubleProperty(puissance);
                this.pr=new SimpleDoubleProperty(pr);
                this.orientation=new SimpleDoubleProperty(orientation);
                this.inclinaison=new SimpleDoubleProperty(inclinaison);
                this.prodTotale=new SimpleDoubleProperty(prodTotale);
    }


    public double getProdTotale() {
        return prodTotale.get();
    }

    public void setProdTotale(double prodTotale) {
        this.prodTotale.set(prodTotale);
    }

    public SimpleDoubleProperty prodTotaleProperty() {
        return prodTotale;
    }

    public void setNbre(double nbre) {
        this.nbre.set(nbre);
    }

    public double getNbre() {
        return nbre.get();
    }

    public SimpleDoubleProperty nbreProperty() {
        return nbre;
    }

    public int getNumero() {
        return numero.get();
    }

    public void setNumero(int numero) {
        this.numero.set(numero);
    }

    public SimpleIntegerProperty numeroProperty() {
        return numero;
    }

    public double getSurface() {
        return surface.get();
    }

    public SimpleDoubleProperty surfaceProperty() {
        return surface;
    }

    public void setPuissance(double puissance) {
        this.puissance.set(puissance);
    }

    public void setPr(double pr) {
        this.pr.set(pr);
    }

    public void setOrientation(double orientation) {
        this.orientation.set(orientation);
    }

    public void setInclinaison(double inclinaison) {
        this.inclinaison.set(inclinaison);
    }

    public double getPuissance() {
        return puissance.get();
    }

    public SimpleDoubleProperty puissanceProperty() {
        return puissance;
    }

    public double getPr() {
        return pr.get();
    }

    public SimpleDoubleProperty prProperty() {
        return pr;
    }

    public double getOrientation() {
        return orientation.get();
    }

    public SimpleDoubleProperty orientationProperty() {
        return orientation;
    }

    public double getInclinaison() {
        return inclinaison.get();
    }

    public SimpleDoubleProperty inclinaisonProperty() {
        return inclinaison;
    }

    public void setSurface(double surface) {
        this.surface.set(surface);
    }






}
