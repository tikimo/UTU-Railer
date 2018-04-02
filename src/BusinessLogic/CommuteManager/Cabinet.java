package BusinessLogic.CommuteManager;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Each cabinet of train has 60 seats no matter the type. Nevertheless, a custom VIP-cabin is possible to make.
 * Implements serializable respective to Train class
 */
public class Cabinet implements Serializable{
    // Class variables
    private ArrayList<Seat> seatList;

    /**
     * Constructor
     * @param seats Cabinet must have a seatlist in oredr to be initialized
     */
    Cabinet(ArrayList<Seat> seats) {
        this.seatList = seats;
    }

    /**
     * @return returns the seatlist
     */
    public ArrayList<Seat> getSeatList() {
        return seatList;
    }

    /**
     * To fix mistakes you can set a seatlist. This is not currently used
     * @param seatList Seatlist to be set
     */
    public void setSeatList(ArrayList<Seat> seatList) {
        this.seatList = seatList;
    }

    /**
     * Prints the cabin in a commandline interface as text.
     * Letters represent the seat type. Quiet reserved = 'Q'
     * If the seat is reserved, letter representing the seat will be uppercase, otherwise lowercase
     */
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

    }
}
