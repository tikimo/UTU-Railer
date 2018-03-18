package BusinessLogic.CommuteManager;

import BusinessLogic.CommuteManager.Enums.SeatTypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Each cabinet of train has 50 seats no matter the type.
 */
public class Cabinet implements Serializable{
    private static ArrayList<Seat> seatList;

    public Cabinet() {
    }


    public ArrayList<Seat> getSeatList() {
        return seatList;
    }

    public void setSeatList(ArrayList<Seat> seatList) {
        this.seatList = seatList;
    }


}
