package FrontEnd.Admin;

import BusinessLogic.CommuteManager.Cabinet;
import BusinessLogic.CommuteManager.CommuteDatabaseManager;
import BusinessLogic.CommuteManager.Enums.Stations;
import BusinessLogic.CommuteManager.Seat;
import BusinessLogic.CommuteManager.Train;
import BusinessLogic.DatabaseManager;
import FrontEnd.Login.LoginController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * This class has very many similarities with PortalController. If something is uncommented
 * or unclear, be sure to check portal controller for more info and vice versa.
 *
 * There is a LOT of class variables because the JFXML file has many controllable objects.
 *
 * 1. First the user must select the route
 *      FROM - TO
 *      Time of leaving / Arrival time
 *      Number of passengers
 *  Program will search the database for matching trains and return them. There must be enough seats!
 *  Seat configuration and position dont matter when travelling together.
 *
 *  2. Second is the position selection
 *  Seat will have seatType. Types:
 *      Allergy(green), Pet(brown), disabled(orange), quiet(blue), family(red), taken(gray), economy(white)
 *  Whatever the type, user must be able to select the position to sit in.
 *
 *
 *  X. User settings tab will be activable at any time
 *  In this tab, user can declare:
 *      Address information
 *      new password
 *      billing information
 *
 * TO BE NOTED:
 * This is basically the same as normal portal, but without history and third pane. Here you can un-reserve seats
 *
 */
@SuppressWarnings("Duplicates")
public class adminController {

    // Class function-specific variables
    public JFXButton firstPaneSearchButton;
    private boolean settingsOpen = false;
    private String user = null;
    private DatabaseManager dbm = LoginController.getDbmanager();
    private CommuteDatabaseManager cdm = new CommuteDatabaseManager("trains");
    private ArrayList<Train> searchResults = new ArrayList<>();
    private Train selectedTrain = null;

    // FXML global specifics
    public TextField billingAddressFieldSettings;
    public TextField phoneNumberFieldSettings;
    public PasswordField oldPasswordFieldSettings;
    public PasswordField newPasswordFieldSettings;
    public Button updateBillingAddressButtonSettings;
    public Button updatePhoneNumberButtonSettings;
    public Button updatePasswordButtonSettings;
    public Label settingPropertyUpdatedText;
    public Button checkOldPassSettings;
    public Button settingsButton;
    public AnchorPane settingsAnchorPane;
    public Label settingsGreeting;
    public Pane firstPane;
    public Pane secondPane;
    public TextField creditCardFieldSettings;
    public Button updateCreditCardButton;
    public Label wrongPasswordLabel;

    // Radio buttons
    public ToggleGroup departureArrivalGroup;
    public RadioButton departureTimeRadiobutton;
    public RadioButton arrivalTimeRadiobutton;

    // Time picker
    public com.jfoenix.controls.JFXTimePicker JFXTimePicker;

    // First pane specifics
    public ChoiceBox trainCitiesFromDropDown;
    public ChoiceBox trainCitiesToDropDown;
    public Label searchFieldErrorText;
    public JFXListView trainResultListViewJFX;

    // Second pane specifics
    private int cabinSelectorIndex = 0; // index 0 = cabin 1, index 1 = cabin 2, ...
    private int seatSelectorIndex = -1;  // index 0 = seat 1 on cabin x, ...
    public Label cabinIndexIndicator;
    private GridPane cabinSeatGridpane;
    public Button prevCabinButton;
    public Button nextCabinButton;
    public ScrollPane cabinSeatScrollPane;
    public AnchorPane cabinSeatScrollpaneAnchorpane;

    /**
     * Initializes settings for portal
     */
    public void initialize() {
        // init class variables
        user = LoginController.getAuthenticatedUser();

        // Settings tab
        Image buttonBgImage = new Image("FrontEnd/RES/hamburger.png");
        settingsButton.setGraphic(new ImageView(buttonBgImage));
        settingsAnchorPane.setVisible(settingsOpen);
        settingsGreeting.setText("Hi " + dbm.getUserName(user) + ". Here you can edit your personal info.");
        wrongPasswordLabel.setVisible(false);

        // init text fills
        settingPropertyUpdatedText.setText("");
        searchFieldErrorText.setText("");
        billingAddressFieldSettings.setText(dbm.getAddress(user));
        phoneNumberFieldSettings.setText(dbm.getPhone(user));
        creditCardFieldSettings.setText(dbm.getCard(user));

        // Make material button look cool for fun
        firstPaneSearchButton.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, new CornerRadii(2), new Insets(2))));

        // init dropdown boxes
        trainCitiesFromDropDown.setItems(Stations.getAllStations());
        trainCitiesToDropDown.setItems(Stations.getAllStations());

        // First pane
        showPane(1);
    }

    /**
     * Toggles the settings pane where user can update details
     */
    public void toggleSettings() {
        settingsAnchorPane.setVisible(!settingsOpen);
        settingsOpen = !settingsOpen;
    }

    /**
     * Updates billing information on user through database manager
     */
    public void updateBillingInfo() {
        dbm.addAddress(billingAddressFieldSettings.getText(), user);
    }

    /**
     * Updates phone number on user through database manager
     */
    public void updatePhoneNumber() {
        dbm.addPhone(phoneNumberFieldSettings.getText(), user);
    }

    /**
     * Updates CC information on user through database manager
     */
    public void updateCreditCardInfo() {
        dbm.addCard(creditCardFieldSettings.getText(), user);
    }

    /**
     * Update password button function thourgh database manager
     */
    public void updatePassword() {
        if (dbm.updatePassword(newPasswordFieldSettings.getText(), user)) {
            settingPropertyUpdatedText.setText("Password updated!");
            updatePasswordButtonSettings.setDisable(true);
            newPasswordFieldSettings.setDisable(true);
        } else {
            System.err.println("[ERROR] Problem updating password");
        }
    }

    /**
     * Button function to check old password before changing it
     */
    public void checkOldPass() {
        if (dbm.authenticate(user, oldPasswordFieldSettings.getText())) {
            System.err.println("Front-end old password check confirmed.");
            newPasswordFieldSettings.setDisable(false);
            updatePasswordButtonSettings.setDisable(false);
            wrongPasswordLabel.setVisible(false);
        } else {
            wrongPasswordLabel.setVisible(true);
        }
    }

    /**
     * Button to search trains with properties listed in textfields
     */
    public void searchTrainsByProperty() {
        searchFieldErrorText.setText("");
        try {
        String from = trainCitiesFromDropDown.getValue().toString();
        String to = trainCitiesToDropDown.getValue().toString();
        Boolean timeSettingDeparture = departureTimeRadiobutton.isSelected();
        LocalTime time = JFXTimePicker.getValue();

        // Sort trains by departure or arrival and remove that are out of scope
        ArrayList<Train> trains = cdm.getTrainsByProperty(cdm.DEPARTURE_CITY, from);
        if (timeSettingDeparture) {
            trains.sort(Comparator.comparing(Train::getDepartureTime));
            // Out of scope trim
            trains.removeIf(o -> o.getDepartureTime().isBefore(time));

        } else {
            trains.sort(Comparator.comparing(Train::getArrivalTime));
            // Out of scope trim
            trains.removeIf(t -> t.getArrivalTime().isBefore(time));
        }

        // Remove stations that are out of scope
        trains.removeIf(t -> !t.getArrivalStation().equals(to));

        // Check if trains were even found
        if (trains.size() == 0) {
            System.err.println("[WARNING] No trains were found with these settings");
            searchResults = trains;
            listTrains();
        } else {
            System.err.println(trains.size() + " train(s) found!");
        }

        searchResults = trains;
        listTrains();


        } catch (NullPointerException npe) {
            System.err.println("[ERROR] One of the fields were null!");
            searchFieldErrorText.setText("Please fill all the fields.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            searchFieldErrorText.setText("Unknown error!");
        }

    }

    /**
     * Function to list the desired trains in a listview
     */
    private void listTrains() {
        trainResultListViewJFX.getItems().remove(0, trainResultListViewJFX.getItems().size());
        for (Train t : searchResults) {
            // Train class has toString() Override, it creates an automatic new Label()
            trainResultListViewJFX.getItems().add(t);
        }

    }

    /**
     * Pick the highlighted item (train) from the listview
     */
    public void pickSelectedItemFromList() {
        int selectedItemIndex = trainResultListViewJFX.getSelectionModel().getSelectedIndex();

        try {
            selectedTrain = searchResults.get(selectedItemIndex);
            System.out.println(selectedTrain + " selected successfully!");
            loadGraphicalCabin(cabinSelectorIndex);
            showPane(2);
        } catch (IndexOutOfBoundsException e) {
            System.err.println("No train was selected!");
        }
    }

    /**
     * This method loads the cabin to the gridpane and also sets the indicator (label)
     * Seat numbering is from up to down, left to right as follows:
     *      1   5   9  ...
     *      2   6   10 ...
     *
     *      3   7   11 ...
     *      4   8   12 ...
     *
     *      and seat index = number - 1
     *
     * @param cabinIndex number of cabinet to present as index
     */
    private void loadGraphicalCabin(int cabinIndex) {
        cabinIndexIndicator.setText(cabinIndex+1 + " / " + selectedTrain.getCabinetList().size());
        Cabinet currentCabinet = selectedTrain.getCabinetList().get(cabinIndex);
        currentCabinet.printCabin();
        int currentSeatIndex;
        initNewGridPane();
        ImageView node;

        // Iterate through cabin
        for (int i = 0; i < 15; i++) {   // column
            for (int j = 0; j < 4; j++) {  // row
                currentSeatIndex = i*4 + j; // keep track of seat index
                Seat currentSeat = currentCabinet.getSeatList().get(currentSeatIndex);
                if (currentSeat.isReserved()) {
                    cabinSeatGridpane.add(new ImageView(new Image("FrontEnd/RES/Seats/taken.png")), i, j);


                    // Attach click listener to current node
                    int finalCurrentSeatIndex = currentSeatIndex;   // Expressions in lambda must be final
                    node = (ImageView) getNodeByRowColumnIndex(j, i, cabinSeatGridpane);
                    ImageView finalNode = node;
                    node.setOnMouseClicked((MouseEvent e) -> {
                        seatSelectorIndex = finalCurrentSeatIndex;
                        setSeatToAvailable(cabinIndex, finalCurrentSeatIndex, currentSeat, finalNode);
                        System.out.println("Seat with index " + seatSelectorIndex + " (" + currentSeat.getSeatType() + ")" +
                                " on cabin " + cabinIndex +" changed to available!");
                    });

                } else {
                    switch (currentSeat.getSeatType().substring(0,1)) {
                        case "a":
                            cabinSeatGridpane.add(new ImageView(new Image("FrontEnd/RES/Seats/allergy.png")), i, j);
                            break;
                        case "d":
                            cabinSeatGridpane.add(new ImageView(new Image("FrontEnd/RES/Seats/disabled.png")), i, j);
                            break;
                        case "e":
                            cabinSeatGridpane.add(new ImageView(new Image("FrontEnd/RES/Seats/economy.png")), i, j);
                            break;
                        case "f":
                            cabinSeatGridpane.add(new ImageView(new Image("FrontEnd/RES/Seats/family.png")), i, j);
                            break;
                        case "p":
                            cabinSeatGridpane.add(new ImageView(new Image("FrontEnd/RES/Seats/pet.png")), i, j);
                            break;
                        case "q":
                            cabinSeatGridpane.add(new ImageView(new Image("FrontEnd/RES/Seats/quiet.png")), i, j);
                            break;
                    }
                    node = (ImageView) getNodeByRowColumnIndex(j, i, cabinSeatGridpane);

                }

                // Set margin to present aisle
                if (j == 2) {
                    GridPane.setMargin(node, new Insets(50, 0, 0, 0));
                }
            }
        }
    }

    /**
     * Change a reserved seat to be un-reserved. Used to fix user mistakes.
     * @param cabinIndex index of cabin to have fix applied on
     * @param finalCurrentSeatIndex index of seat to have reservation freed
     * @param currentSeat seat that the modification should be applied on
     * @param finalNode Imageview to change the seat image corresponding the type.
     */
    private void setSeatToAvailable(int cabinIndex, int finalCurrentSeatIndex, Seat currentSeat, ImageView finalNode) {
        cdm.reserveSeat(selectedTrain, cabinIndex, finalCurrentSeatIndex, false);

        switch (currentSeat.getSeatType().substring(0,1)) {
            case "a":
                finalNode.setImage(new Image("FrontEnd/RES/Seats/allergy.png"));
                break;
            case "d":
                finalNode.setImage(new Image("FrontEnd/RES/Seats/disabled.png"));
                break;
            case "e":
                finalNode.setImage(new Image("FrontEnd/RES/Seats/economy.png"));
                break;
            case "f":
                finalNode.setImage(new Image("FrontEnd/RES/Seats/family.png"));
                break;
            case "p":
                finalNode.setImage(new Image("FrontEnd/RES/Seats/pet.png"));
                break;
            case "q":
                finalNode.setImage(new Image("FrontEnd/RES/Seats/quiet.png"));
                break;

        }
    }

    /**
     * Initialize a gridpane so its clickable. This is very important!
     * PS. spent many days figuring this bug out
     */
    private void initNewGridPane() {
        cabinSeatGridpane = new GridPane();
        cabinSeatGridpane.setPadding(new Insets(30,25,25,25));
        cabinSeatGridpane.setHgap(40);
        cabinSeatGridpane.setVgap(40);
        cabinSeatGridpane.setLayoutX(20);

        cabinSeatScrollpaneAnchorpane.getChildren().clear();
        cabinSeatScrollpaneAnchorpane.getChildren().add(cabinSeatGridpane);
    }

    /**
     * Displays pane that is passed as parameter
     * @param i index of pane
     */
    private void showPane(int i) {
        firstPane.setVisible(false);
        secondPane.setVisible(false);
        switch (i) {
            case 1:
                firstPane.setVisible(true);
                break;
            case 2:
                secondPane.setVisible(true);
                break;
        }
    }

    /**
     * Switch to first pane and reset seat selector index
     */
    public void switchToPane1() {
        seatSelectorIndex = -1;
        showPane(1);
    }

    /**
     * Button to control cabin selector. Updaes visual view
     */
    public void prevCabinButton() {
        cabinSelectorIndex--;
        if (cabinSelectorIndex == 0) {
            prevCabinButton.setDisable(true);
        }
        if (cabinSelectorIndex < 4) {
            nextCabinButton.setDisable(false);
        }
        loadGraphicalCabin(cabinSelectorIndex);
    }

    /**
     * Button to control cabin selectior. Updates visual view
     */
    public void nextCabinButton() {
        cabinSelectorIndex++;
        if (cabinSelectorIndex == 4) {
            nextCabinButton.setDisable(true);
        }
        if (cabinSelectorIndex > 0) {
            prevCabinButton.setDisable(false);
        }
        loadGraphicalCabin(cabinSelectorIndex);
    }

    /**
     * This is very cool. When scrolling vertically (e.g. desktop mouse) this method will translate
     * it to actually scroll horizontally!
     * @param scrollEvent scrollevent to be translated
     */
    public void changeScrollDirection(ScrollEvent scrollEvent) {
        double scrollDensity = 1400; // Smaller number is faster scrolling
        if (scrollEvent.getDeltaX() == 0 && scrollEvent.getDeltaY() != 0) {
            cabinSeatScrollPane.setHvalue(cabinSeatScrollPane.getHvalue() - scrollEvent.getDeltaY() / scrollDensity);
        }
    }

    /**
     * This function simplifies life by getting a node in a gridpane since there is no native solution :)
     * @param row row to be selected
     * @param column column to be selected
     * @param gridPane gridpane to run selection on
     * @return return the node
     */
    private Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
        Node result = null;

        for (Node node : gridPane.getChildren()) {
            if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }

        return result;
    }

    /**
     * Button to exit the applicaiton.
     */
    public void exitApplication() {
        System.exit(0);
    }

}














