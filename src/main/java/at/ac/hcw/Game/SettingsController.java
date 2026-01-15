package at.ac.hcw.Game;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;

public class SettingsController {

    @FXML
    private Slider volumeSld;

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
        // your code
    }

    @FXML
    private void zurückZumHauptmenü() {
        AllSoundEffects.button();
        // your code
    }
}
