package FrontEnd.Portal;

import BusinessLogic.CommuteManager.Train;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;


class TrainView {
    TrainView() {
    }

    static Object presentTrain(Train t) {

        TabPane tabPane = new TabPane();

        return new Label(t.toString());
    }


}
