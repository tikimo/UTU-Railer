package BusinessLogic.CommuteManager;


import java.io.Serializable;

/**
 * Actual class of seat. You should check the enum Seat also!
 * basically this only defines seat with getters and setters and whatnot.
 */
public class Seat implements Serializable{
    private int seatNumber;
    private String seatType;
    private boolean reserved;

    /**
     * Constructor for seat
     *
     * @param seatNumber    Number of seat. Invalid number is -1. Numbering should start at 1 (index 0) for vision
     *                      and numbering should end in 60, since cabinet has 60 seats.
     * @param seatType      Type of seat. Types: economy, allergy, pet, disabled, quiet, family
     * @throws IllegalArgumentException To handle problems with parameters
     */
    Seat(int seatNumber, String seatType) {
        if (seatNumber >= 1 && seatNumber <= 60) {
        this.seatNumber = seatNumber;
        } else {
            throw new IllegalArgumentException("Seat number is out of range!");
        }

        if (seatType.equals("economy") || seatType.equals("allergy") || seatType.equals("pet") ||
                seatType.equals("disabled") || seatType.equals("quiet") || seatType.equals("family")) {
            this.seatType = seatType;
        } else {
            throw new IllegalArgumentException("Seat type is not defined");
        }
    }

    /**
     * Getter for seat number. No setter is needed
     * @return seat number
     */
    public int getSeatNumber() {
        return seatNumber;
    }

    /**
     * Getter for seat type. No setter is needed
     * @return type of seat defined in this enum
     */
    public String getSeatType() {
        return seatType;
    }

    /**
     * Check the reservation status of a seat
     * @return boolean true if seat is reserved
     */
    public boolean isReserved() {
        return reserved;
    }

    /**
     * sets a seat to be reserved
     * @param reserved boolean to indicate reservation status
     */
    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

}
