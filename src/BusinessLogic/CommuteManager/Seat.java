package BusinessLogic.CommuteManager;



public class Seat {
    private int seatNumber;
    private String seatType;
    private boolean taken;

    /**
     *
     *
     * @param seatNumber    Number of seat. Invalid number is -1. Numbering should start at 1 for vision
     *                      and numbering should end in 51, since cabinet has 50 seats.
     * @param seatType      Type of seat. Types: economy, allergy, pet, disabled, quiet, family
     * @throws IllegalArgumentException To handle problems with parameters
     */
    public Seat(int seatNumber, String seatType) {

        if (seatNumber >= 1 && seatNumber <= 50) {
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

    public int getSeatNumber() {
        return seatNumber;
    }
    public String getSeatType() {
        return seatType;
    }
}
