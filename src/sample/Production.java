package sample;

import java.util.Date;

public class Production {
    private double production;
    private String date;

    public Production (){};

    public Production(String date,double production) {
        this.date=date;
        this.production = production;
    }

    public double getProduction() {
        return production;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setProduction(double production) {
        this.production = production;
    }
}










