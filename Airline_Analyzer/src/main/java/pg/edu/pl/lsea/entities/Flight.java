package pg.edu.pl.lsea.entities;

public class Flight {
    private String icao24;
    private int firstseen;
    private int lastseen;
    private String departureairport;
    private String arrivalairport;


    public Flight(String icao24, int firstseen, int lastseen, String departureairport, String arrivalairport) {
        this.icao24 = icao24;
        this.firstseen = firstseen;
        this.lastseen = lastseen;
        this.departureairport = departureairport;
        this.arrivalairport = arrivalairport;
    }

    public String getIcao24() {
        return icao24;
    }

    public int getFirstseen() {
        return firstseen;
    }

    public int getLastseen() {
        return lastseen;
    }

    public String getDepartureairport() {
        return departureairport;
    }

    public String getArrivalairport() {
        return arrivalairport;
    }

    public void setIcao24(String icao24) {
        this.icao24 = icao24;
    }

    public void setFirstseen(int firstseen) {
        this.firstseen = firstseen;
    }

    public void setLastseen(int lastseen) {
        this.lastseen = lastseen;
    }

    public void setDepartureairport(String departureairport) {
        this.departureairport = departureairport;
    }

    public void setArrivalairport(String arrivalairport) {
        this.arrivalairport = arrivalairport;
    }
}

