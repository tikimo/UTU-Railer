package BusinessLogic.CommuteManager;

import BusinessLogic.CommuteManager.Enums.Stations;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Train implements Serializable{
    private ArrayList<Cabinet> cabinetList;
    private String departureStation;
    private String arrivalStation;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private int availableSeats;

    private final static long serialVersionUID = 1;

    public Train(String departureStation, String arrivalStation, LocalTime departureTime, LocalTime arrivalTime) {
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

    public List<Cabinet> getCabinetList() {
        return cabinetList;
    }

    public void setCabinetList(ArrayList<Cabinet> cabinetList) {
        this.cabinetList = cabinetList;
    }

    public Train generateRandomTrain() {
        Train randomTrain = null;

        // Generate random cabinets with random seat types
        ArrayList<Cabinet> cabinetList = new ArrayList<>();
        for (int i = 0; i<5; i++) {
            cabinetList.add(Cabinet.generateRandomCabinet());
        }

        randomTrain.setCabinetList(cabinetList);
        randomTrain.setDepartureStation(Stations.getRandomStation().getCity());
        randomTrain.setDepartureTime(LocalTime.of(ThreadLocalRandom));

        return randomTrain;
    }
}
