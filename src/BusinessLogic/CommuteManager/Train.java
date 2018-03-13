package BusinessLogic.CommuteManager;

import java.io.Serializable;
import java.util.List;

public class Train implements Serializable{
    private List<Cabinet> cabinetList;
    private String departureStation;
    private String arrivalStation;
    private String departureTime;
    private String arrivalTime;
    private int availableSeats;

    private final static long serialVersionUID = 1;

    public Train(List<Cabinet> cabinets, String departureStation, String arrivalStation, String departureTime, String arrivalTime) {
        this.cabinetList = cabinets;
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }


    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public String getDepartureStation() {
        return departureStation;
    }

    public void setDepartureStation(String departureStation) {
        this.departureStation = departureStation;
    }

    public String getArrivalStation() {
        return arrivalStation;
    }

    public void setArrivalStation(String arrivalStation) {
        this.arrivalStation = arrivalStation;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public List<Cabinet> getCabinetList() {
        return cabinetList;
    }

    public void setCabinetList(List<Cabinet> cabinetList) {
        this.cabinetList = cabinetList;
    }
}
