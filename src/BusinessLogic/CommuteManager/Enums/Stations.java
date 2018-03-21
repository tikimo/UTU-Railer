package BusinessLogic.CommuteManager.Enums;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

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

    public static ObservableList<String> getAllStations() {
        List<Stations> stationsAsList = Arrays.asList(Stations.values());
        List<String> stationNamesAsList = new ArrayList<>();

        for (Stations s : stationsAsList) {
            stationNamesAsList.add(s.getCity());
        }

        return FXCollections.observableArrayList(stationNamesAsList);
    }
}
