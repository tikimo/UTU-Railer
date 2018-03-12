package BusinessLogic.CommuteManager;

import java.util.List;

public class Train {
    private List<Cabinet> cabinetList;
    private String departureStation;
    private String arrivalStation;
    private String departureTime;
    private String arrivalTime;

    public Train(List<Cabinet> cabinets, String departureStation, String arrivalStation, String departureTime, String arrivalTime) {
        this.cabinetList = cabinets;
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }
    

}
