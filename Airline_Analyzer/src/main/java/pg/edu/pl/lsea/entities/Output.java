package pg.edu.pl.lsea.entities;

public class Output extends Trackable{

    public int Value;

    public Output(String icao24, int Value) {
        setIcao24(icao24);
        this.Value = Value;
    }

    @Override
    public String toString() {
        return "Aircraft{" +
                "icao24='" + getIcao24() + "'" +
                ", Value=" + Value +
                "}";
    }


}
