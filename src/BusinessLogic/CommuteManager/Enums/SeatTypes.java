package BusinessLogic.CommuteManager.Enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Seattypes:   economy, allergy, Pet, disabled, quiet, family
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

    private static final List<SeatTypes> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static SeatTypes getRandomSeatType() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}