package BusinessLogic.CommuteManager;

import BusinessLogic.CommuteManager.Enums.SeatTypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Each cabinet of train has 60 seats no matter the type.
 */
public class Cabinet implements Serializable{
    private ArrayList<Seat> seatList;

    Cabinet(ArrayList<Seat> seats) {
        this.seatList = seats;
    }


    public ArrayList<Seat> getSeatList() {
        return seatList;
    }

    public void setSeatList(ArrayList<Seat> seatList) {
        this.seatList = seatList;
    }


    public void printCabin() {
        int i = 0;
        if (this.getSeatList() == null) {
            System.err.println("Seatlist is null!");
            return;
        }

        for (Seat seat : this.getSeatList()) {
            if (seat.isReserved()) {
                System.out.print(seat.getSeatType().substring(0,1) + " ");
            } else {
                System.out.print(seat.getSeatType().substring(0,1).toUpperCase() + " ");
            }
            if (i == 14 || i == 44) {
                System.out.println();
            }
            if (i == 29) {
                System.out.println("\n");
            }

            i++;
        }
        System.out.println();
    }
}
