package BusinessLogic.CommuteManager;

import java.util.List;

/**
 * Each cabinet of train has 50 seats no matter the type.
 */
public class Cabinet extends Train {
    private List<Seat> seatList;

    public Cabinet(List<Cabinet> cabinets, String departureStation, String arrivalStation, String departureTime, String arrivalTime) {
        super(cabinets, departureStation, arrivalStation, departureTime, arrivalTime);
    }

    public List<Seat> getSeatList() {
        return seatList;
    }

    public void setSeatList(List<Seat> seatList) {
        this.seatList = seatList;
    }
}
