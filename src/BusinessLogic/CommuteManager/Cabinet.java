package BusinessLogic.CommuteManager;

import BusinessLogic.CommuteManager.Enums.SeatTypes;

import java.util.ArrayList;
import java.util.List;

/**
 * Each cabinet of train has 50 seats no matter the type.
 */
public class Cabinet {
    private static ArrayList<Seat> seatList;

    public Cabinet(ArrayList<Seat> seatList) {
    }


    public ArrayList<Seat> getSeatList() {
        return seatList;
    }

    public void setSeatList(ArrayList<Seat> seatList) {
        this.seatList = seatList;
    }

    public static Cabinet generateRandomCabinet() {
        for (int i = 1; i<=50; i++) {
            seatList.add(new Seat(i, SeatTypes.getRandomSeatType().name()));
        }
        return new Cabinet(seatList);
    }
}
