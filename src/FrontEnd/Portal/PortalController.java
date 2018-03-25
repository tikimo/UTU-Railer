package FrontEnd.Portal;

import BusinessLogic.CommuteManager.Cabinet;
import BusinessLogic.CommuteManager.CommuteDatabaseManager;
import BusinessLogic.CommuteManager.Enums.Stations;
import BusinessLogic.CommuteManager.Seat;
import BusinessLogic.CommuteManager.Train;
import BusinessLogic.DatabaseManager;
import FrontEnd.Login.LoginController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;

/**
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
 *  3. Payment of commute
 *  User can pay in the application or in the train.
 *  After payment, program will declare seats taken.
 *  Upon seats declared taken, other clients will be notified.
 *
 *
 *  X. User settings tab will be activable at any time
 *  In this tab, user can delcare:
 *      Address information
 *      new password
 *      billing information
 *
 * TO BE NOTED:
 * Trains in database travel every day at the same time, so datepicker is just a
 * placeholder for future expanding of application.
 *
 */
public class PortalController {

    public JFXButton firstPaneSearchButton;
    private boolean settingsOpen = false;
    private String user = null;
    private DatabaseManager dbm = LoginController.getDbmanager();
    private CommuteDatabaseManager cdm = new CommuteDatabaseManager("trains");
    private ArrayList<Train> searchResults = new ArrayList<>();
    private Train selectedTrain = null;
    private Image selectedSeatImage;


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
    public Pane thirdPane;
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
    public Label selectedSeatNumberIndicator;
    public ScrollPane cabinSeatScrollPane;
    public AnchorPane cabinSeatScrollpaneAnchorpane;

    // Third pane specifics
    public Label orderDetailsLabel;
    public ImageView paymentWindowSeatImageView;
    public JFXRadioButton payWithCreditCardRadioButton;
    public ToggleGroup paymentMethodRadioGroup;
    public JFXTextField creditCardNumberField3;
    public JFXTextField cardHolderTextField3;
    public JFXTextField CVCTextField3;
    public JFXTextField expirationDateTextField3;
    public JFXButton payWithCreditCardButton;
    public JFXButton payInTrainReserveButton;
    public AnchorPane orderSuccessfullPane;
    public Label reservationNumberLabel;
    public Label creditCardPaymentErrorLabel;
    public AnchorPane creditCardPaymentPane;
    public AnchorPane payInTrainPane;
    public JFXButton makeANewReservationButton;


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
        payWithCreditCardButton.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, new CornerRadii(2), new Insets(2))));
        payInTrainReserveButton.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, new CornerRadii(2), new Insets(2))));
        makeANewReservationButton.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, new CornerRadii(2), new Insets(2))));


        // init dropdown boxes
        trainCitiesFromDropDown.setItems(Stations.getAllStations());
        trainCitiesToDropDown.setItems(Stations.getAllStations());

        // Third pane text fills etc.
        creditCardNumberField3.setText(dbm.getCard(user));
        cardHolderTextField3.setText(dbm.getUserName(user));
        initThirdPane();

        // First pane
        showPane(1);
    }

    /**
     * This page has to be initiated in case the user changes train or seat
     */
    private void initThirdPane() {
        orderDetailsLabel.setText("");
        orderSuccessfullPane.setVisible(false);
        creditCardPaymentErrorLabel.setVisible(false);
        payWithCreditCardButton.setDisable(true);
        payInTrainReserveButton.setDisable(true);
        payInTrainPane.setDisable(false);
        creditCardPaymentPane.setDisable(false);
    }


    /**
     * Toggles the settings pane where user can update details
     */
    public void toggleSettings() {
        settingsAnchorPane.setVisible(!settingsOpen);
        settingsOpen = !settingsOpen;
    }

    public void updateBillingInfo() {
        dbm.addAddress(billingAddressFieldSettings.getText(), user);
    }

    public void updatePhoneNumber() {
        dbm.addPhone(phoneNumberFieldSettings.getText(), user);
    }

    public void updateCreditCardInfo() {
        dbm.addCard(creditCardFieldSettings.getText(), user);
    }

    public void updatePassword() {
        if (dbm.updatePassword(newPasswordFieldSettings.getText(), user)) {
            settingPropertyUpdatedText.setText("Password updated!");
            updatePasswordButtonSettings.setDisable(true);
            newPasswordFieldSettings.setDisable(true);
        } else {

        }
    }

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

    private void listTrains() {
        trainResultListViewJFX.getItems().remove(0, trainResultListViewJFX.getItems().size());
        for (Train t : searchResults) {
            // Train class has toString() Override, it creates an automatic new Label()
            trainResultListViewJFX.getItems().add(t);
        }

    }

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
                    node = (ImageView) getNodeByRowColumnIndex(j, i, cabinSeatGridpane);
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

                    // Attach click listener to current node
                    int finalCurrentSeatIndex = currentSeatIndex;   // Expressions in lambda must be final
                    node = (ImageView) getNodeByRowColumnIndex(j, i, cabinSeatGridpane);
                    ImageView finalNode = node;
                    node.setOnMouseClicked((MouseEvent e) -> {
                        seatSelectorIndex = finalCurrentSeatIndex;
                        System.out.println("Seat with index " + seatSelectorIndex + " (" + currentSeat.getSeatType() + ")" +
                                " on cabin " + cabinIndex +" selected!");
                        selectedSeatNumberIndicator.setText((seatSelectorIndex + 1) + "");
                        selectedSeatImage = finalNode.getImage();
                    });
                }

                // Set margin to present aisle
                if (j == 2) {
                    GridPane.setMargin(node, new Insets(50, 0, 0, 0));
                }
            }
        }
    }

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
        thirdPane.setVisible(false);
        switch (i) {
            case 1:
                firstPane.setVisible(true);
                break;
            case 2:
                secondPane.setVisible(true);
                break;
            case 3:
                thirdPane.setVisible(true);
                break;
        }
    }

    public void switchToPane1() {
        seatSelectorIndex = -1;
        selectedSeatNumberIndicator.setText("");
        showPane(1);
    }

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

    public void confirmSeatSelectionButton() {
        if (seatSelectorIndex == -1) {
            System.err.println("[ERROR] No seat selected");
        } else {
            long chronoHours = ChronoUnit.HOURS
                    .between(selectedTrain.getDepartureTime(), selectedTrain.getArrivalTime());
            long chronoMins = ChronoUnit.MINUTES
                    .between(selectedTrain.getDepartureTime(), selectedTrain.getArrivalTime()) - chronoHours*60;

            // Set text to order details with precise information
            String detailText =
                    // Train info
                    "Train from " + selectedTrain.getDepartureStation() + " at " +
                    selectedTrain.getDepartureTime() + " to " + selectedTrain.getArrivalStation() + " at " +
                    // Travel time
                    selectedTrain.getArrivalTime() + "\n\nTravel time is " +
                    chronoHours + " hours and " +
                    chronoMins + " minutes. \n\n" +
                    // Seat number and type
                    "Your seat number is " + seatSelectorIndex+1 + " (" +
                    selectedTrain.getCabinetList().get(cabinSelectorIndex).getSeatList().get(seatSelectorIndex).getSeatType()
                    +") in cabin " + cabinSelectorIndex+1
                    ;
            orderDetailsLabel.setText(detailText);
            paymentWindowSeatImageView.setImage(selectedSeatImage);

            showPane(3);
        }
    }

    public void changeScrollDirection(ScrollEvent scrollEvent) {
        double scrollDensity = 1400; // Smaller number is faster scrolling
        if (scrollEvent.getDeltaX() == 0 && scrollEvent.getDeltaY() != 0) {
            cabinSeatScrollPane.setHvalue(cabinSeatScrollPane.getHvalue() - scrollEvent.getDeltaY() / scrollDensity);
        }
    }

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

    public void payWithCreditCard() {
        creditCardPaymentErrorLabel.setVisible(false);
        // Check if one of credit info fields are empty
        if (creditCardNumberField3.getText().equals("") ||
                cardHolderTextField3.getText().equals("") ||
                CVCTextField3.getText().equals("") ||
                expirationDateTextField3.getText().equals("")) {
            creditCardPaymentErrorLabel.setVisible(true);
        } else {
            // Insert payment logic here
            System.err.println("Payment successfull!");
            reserveSeat();
        }
    }

    /**
     * Reservation number is generated by 4 last chars of train serialization string,
     * cabin number and seat number.
     * @return reservation number
     */
    private String generateReservationNo() {
        String serTrain = CommuteDatabaseManager.trainToString(selectedTrain);
        return serTrain.substring(serTrain.length()-4, serTrain.length()-1) + cabinSelectorIndex+1 + seatSelectorIndex+1;
    }

    public void reserveSeat() {
        System.err.println("Trying to reserve seat...");
        cdm.reserveSeat(selectedTrain, cabinSelectorIndex, seatSelectorIndex);
        System.err.println("Reservation successfull!");
        reservationNumberLabel.setText(generateReservationNo());
        orderSuccessfullPane.setVisible(true);
        payInTrainPane.setDisable(true);
        creditCardPaymentPane.setDisable(true);
    }

    public void payWithCreditCardRadioButton() {
        payInTrainReserveButton.setDisable(true);
        payWithCreditCardButton.setDisable(false);
    }

    public void payInTrainRadioButton() {
        payInTrainReserveButton.setDisable(false);
        payWithCreditCardButton.setDisable(true);
    }

    public void switchToPane2() {
        showPane(2);
        initThirdPane();
    }

    public void exitApplication() {
        System.exit(0);
    }

    public void makeANewReservation() {
        initialize();
    }
}














