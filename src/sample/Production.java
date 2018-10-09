package sample;

import java.util.Date;

public class Production {
    private double production;
    private Date date;

    public Production(Date date,double production) {
        this.date=date;
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
