package FrontEnd;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.MotionBlur;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Login/login.fxml"));
        primaryStage.setTitle("Login to UTU-Railer");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("FrontEnd/RES/logo.png"));
        primaryStage.show();
    }


    /**
     * NOT CURRENTLY USED
     * This is a work in progress if ever to be ready. Optional extra.
     * This will animate the U-R logo.
     */
    private static void animateLogo() {
        MotionBlur motionBlur = new MotionBlur();
        motionBlur.setRadius(7);
        motionBlur.setAngle(0);

        Label logo = new Label();
        logo.setLayoutX(5);
        logo.setLayoutY(5);
        logo.setText("U-R");
        logo.setTextFill(Color.GREEN);
        logo.setFont(Font.font("Broadway", 47));
        logo.setEffect(motionBlur);

        // effect
        /*
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        final KeyValue kv = new KeyValue(motionBlur.angleProperty(), 360);
        final KeyFrame kf = new KeyFrame(Duration.millis(1000), kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();
        */
    }


    public static void main(String[] args) {
        launch(args);
    }

}
