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
        if (this.getSeatList() == null) {
            System.err.println("Seatlist is null!");
            return;
        }

        System.out.println("\n\n");

        for (int i = 0; i<57; i += 4) {
            if (seatList.get(i).isReserved()) {
                System.out.print(seatList.get(i).getSeatType().substring(0,1).toUpperCase() + " ");
            } else {
                System.out.print(seatList.get(i).getSeatType().substring(0,1) + " ");
            }
        }
        System.out.println();
        for (int i = 1; i<58; i += 4) {
            if (seatList.get(i).isReserved()) {
                System.out.print(seatList.get(i).getSeatType().substring(0,1).toUpperCase() + " ");
            } else {
                System.out.print(seatList.get(i).getSeatType().substring(0,1) + " ");
            }
        }
        System.out.println("\n");
        for (int i = 2; i<59; i += 4) {
            if (seatList.get(i).isReserved()) {
                System.out.print(seatList.get(i).getSeatType().substring(0,1).toUpperCase() + " ");
            } else {
                System.out.print(seatList.get(i).getSeatType().substring(0,1) + " ");
            }
        }
        System.out.println();
        for (int i = 3; i<60; i += 4) {
            if (seatList.get(i).isReserved()) {
                System.out.print(seatList.get(i).getSeatType().substring(0,1).toUpperCase() + " ");
            } else {
                System.out.print(seatList.get(i).getSeatType().substring(0,1) + " ");
            }
        }
        System.out.println();


        /*
        for (Seat seat : this.getSeatList()) {

            if (seat.isReserved()) {
                System.out.print(seat.getSeatType().substring(0,1) + " ");
            } else {
                System.out.print(seat.getSeatType().substring(0,1).toUpperCase() + " ");
            }

        } */
    }
}
