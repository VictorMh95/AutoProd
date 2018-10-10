package sample;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Installation {
    private SimpleIntegerProperty numero;
    private SimpleDoubleProperty surface ;
    private SimpleIntegerProperty type ;
    private SimpleDoubleProperty puissance ;
    private SimpleDoubleProperty rendement ;
    private SimpleDoubleProperty orientation ;
    private SimpleDoubleProperty inclinaison ;

    public Installation (int numero , double surface , int type , double puissance , double rendement , int orientation
                          , int inclinaison ){
                this.numero= new SimpleIntegerProperty(numero);
                this.surface=new SimpleDoubleProperty(surface);
                this.type=new SimpleIntegerProperty(type);
                this.puissance=new SimpleDoubleProperty(puissance);
                this.rendement=new SimpleDoubleProperty(rendement);
                this.orientation=new SimpleDoubleProperty(orientation);
                this.inclinaison=new SimpleDoubleProperty(inclinaison);


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


    public int getType() {
        return type.get();
    }

    public SimpleIntegerProperty typeProperty() {
        return type;
    }

    public void setType(int type) {
        this.type.set(type);
    }

    public void setPuissance(double puissance) {
        this.puissance.set(puissance);
    }

    public void setRendement(double rendement) {
        this.rendement.set(rendement);
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

    public double getRendement() {
        return rendement.get();
    }

    public SimpleDoubleProperty rendementProperty() {
        return rendement;
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


    public void setPuissance(int puissance) {
        this.puissance.set(puissance);
    }

    public void setRendement(int rendement) {
        this.rendement.set(rendement);
    }

    public void setOrientation(int orientation) {
        this.orientation.set(orientation);
    }

    public void setInclinaison(int inclinaison) {
        this.inclinaison.set(inclinaison);
    }
}
