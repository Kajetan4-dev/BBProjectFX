package at.ac.hcw.Game;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsController {

    private static Scene previousScene;

    public static void setPreviousScene(Scene scene){
        previousScene = scene;
    }
    @FXML
    private Slider volumeSld;

    @FXML
    private Button backBtn;

    @FXML
    public void initialize() {
        volumeSld.setMin(0.0);
        volumeSld.setMax(1.0);

        // 🔗 TWO-WAY BINDING
        volumeSld.valueProperty().bindBidirectional(
                SoundManager.volumeProperty()
        );
    }

    @FXML
    private void neuesSpiel() {
        AllSoundEffects.button();

    }

    @FXML
    private void zurückZumHauptmenü() {
        AllSoundEffects.button();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/at/ac/hcw/Game/Choice.fxml"));
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Casino Game Selection");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleBack() {
        AllSoundEffects.button();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/at/ac/hcw/Game/Poker_Chips/poker_setup.fxml"));
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Poker Chips Setup");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
