package BusinessLogic.CommuteManager.Enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum Stations {

    HELSINKI("Helsinki"),
    KARJAA("Karjaa"),
    SALO("Salo"),
    TURKU("Turku"),
    TAMPERE("Tampere");

    private String city;

    Stations(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    private static final List<Stations> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static Stations getRandomStation() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
