package BusinessLogic.CommuteManager.Enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Here are all the available seattypes you can use to build your own cabin.
 * If you want to add a specific type of seat, you can add it here.
 * UTU-Railer only uses types defined in here.
 *
 * Seattypes:
 *
 *     economy(white), allergy(green), pet(brown), disabled(orange), quiet(blue), family(red), {taken(gray)}
 *
 * Taken seat is defined in Seat class
 */
public enum SeatTypes {
    ECONOMY("economy"),
    ALLERGY("allergy"),
    PET("pet"),
    DISABLED("disabled"),
    QUIET("quiet"),
    FAMILY("family");

    private String type;

    SeatTypes(String type) {
        this.type = type;
    }

    // Class variables
    private static final List<SeatTypes> VALUES = List.of(values());
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    /**
     * Random seat
     * @return returns a random seat defined in this enum class
     */
    public static SeatTypes getRandomSeatType() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
