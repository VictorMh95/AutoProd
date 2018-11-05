package sample;

import java.util.Date;

/**
 * Production represente la classe du couple date et production electrique
 */

public class Production {
    private double production;
    private Date date;

    public Production() {
    }

    public Production(Date date, double production) {
        this.date = date;
        this.production = production;
    }

    public double getProduction() {
        return production;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setProduction(double production) {
        this.production = production;
    }
}










