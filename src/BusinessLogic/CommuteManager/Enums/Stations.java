package BusinessLogic.CommuteManager.Enums;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

/**
 * Here you can define what cities have stations.
 * UTU-Railer only uses cities defined here
 */
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

    /**
     * Get city name
     * @return Get the string defined in City enum: HELSINKI.getCity() -> "Helsinki"
     */
    public String getCity() {
        return city;
    }

    // Class variables
    private static final List<Stations> VALUES = List.of(values());
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    /**
     * Random station
     * @return returns one of the stations listed in this enum
     */
    public static Stations getRandomStation() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

    /**
     * All stations listed in this enum class
     * @return An observable list of stations
     */
    public static ObservableList<String> getAllStations() {
        Stations[] stationsAsList = Stations.values();
        List<String> stationNamesAsList = new ArrayList<>();
        for (Stations s : stationsAsList) {
            stationNamesAsList.add(s.getCity());
        }
        return FXCollections.observableArrayList(stationNamesAsList);
    }
}
