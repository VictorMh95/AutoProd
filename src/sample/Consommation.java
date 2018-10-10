package sample;

public class Consommation {
    private Double date;
    private Double consommation;

    public Consommation(){}

    public Consommation(Double date, Double consommation) {
        this.date = date;
        this.consommation = consommation;
    }

    public Double getDate() {
        return date;
    }

    public void setDate(Double date) {
        this.date = date;
    }

    public Double getConsommation() {
        return consommation;
    }

    public void setConsommation(Double consommation) {
        this.consommation = consommation;
    }
}
