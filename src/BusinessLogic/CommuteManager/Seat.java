package BusinessLogic.CommuteManager;

import java.util.List;

public class Seat extends Cabinet {
    private int seatNumber = -1;
    private String seatType = "";


    public Seat(List<Cabinet> cabinets, String departureStation, String arrivalStation, String departureTime, String arrivalTime) {
        super(cabinets, departureStation, arrivalStation, departureTime, arrivalTime);
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }
}
