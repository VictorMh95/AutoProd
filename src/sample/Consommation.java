package sample;

import java.util.Date;

/**
 * Consommation represente la classe du couple date et consommation electrique d'un batiment
 */
public class Consommation {
    private Date date;
    private Double consommation;

    public Consommation() {
    }

    public Consommation(Date date, Double consommation) {
        this.date = date;
        this.consommation = consommation;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getConsommation() {
        return consommation;
    }

    public void setConsommation(Double consommation) {
        this.consommation = consommation;
    }
}
