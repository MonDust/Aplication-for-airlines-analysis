package pg.edu.pl.lsea.entities;

public class Aircraft extends Trackable{
    private String model;
    private String operator;
    private String owner;

    public Aircraft(String icao24, String model, String operator, String owner) {
        setIcao24(icao24);
        this.model = model;
        this.operator = operator;
        this.owner = owner;
    }

    public String getModel() {
        return model;
    }

    public String getOperator() {
        return operator;
    }

    public String getOwner() {
        return owner;
    }


    public void setModel(String model) {
        this.model = model;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
