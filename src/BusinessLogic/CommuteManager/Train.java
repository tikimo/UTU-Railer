package BusinessLogic.CommuteManager;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * This is the thing! This class defines train, just like Cabin class defines cabin.
 * Nothing weird magic here, just a lot of getters and setters. Many of them are
 * unnecessary, but kept for future needs.
 *
 * Most getters and setters are self-explanatory, so are not commented.
 * Feel free to contact me on github.
 *
 * NOTE: Implements serializable, just like cabin and seat, so it can be saved
 * in desired database. Another option would be json, but this is how we roll now.
 */
public class Train implements Serializable{
    private ArrayList<Cabinet> cabinetList;
    private String departureStation;
    private String arrivalStation;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private int availableSeats;

    /**
     * Required to keep track of serialization. If class is updated, trains wont be
     * fetchable anymore and a proper exception is thrown.
     */
    private final static long serialVersionUID = 1;

    /**
     * Constructor for train. It must be initialized with all properties to keep conflicts away.
     * NOTE: Take parameters from appropriate enums!!
     * @param departureStation departure station defined in enum Stations
     * @param arrivalStation arrival station defined in enum Stations
     * @param departureTime LocalTime type of departure time
     * @param arrivalTime arrival time. Remember: arrivalTime > departureTime
     */
    Train(String departureStation, String arrivalStation, LocalTime departureTime, LocalTime arrivalTime) {
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

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public ArrayList<Cabinet> getCabinetList() {
        return cabinetList;
    }

    public void setCabinetList(ArrayList<Cabinet> cabinetList) {
        this.cabinetList = cabinetList;
    }

    /**
     * A custom to-string method for presenting train info. This is pretty useful!
     * @return a formatted string containing header information of train.
     */
    @Override
    public String toString() {
        return "Train from " + departureStation + " at " + departureTime + " to " + arrivalStation + " at "
                + arrivalTime;
    }
}
