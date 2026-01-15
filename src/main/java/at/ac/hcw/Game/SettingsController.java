package at.ac.hcw.Game;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;

public class SettingsController {

    @FXML
    private Slider volumeSld;

    @FXML
    public void initialize() {
        // Slider range 0–1 is perfect for MediaPlayer volume
        volumeSld.setMin(0.0);
        volumeSld.setMax(1.0);

        // Set slider to current volume
        volumeSld.setValue(SoundManager.getVolume());

        // Listen for changes
        volumeSld.valueProperty().addListener((obs, oldVal, newVal) -> {
            SoundManager.setVolume(newVal.doubleValue());
        });
    }

    @FXML
    private void neuesSpiel() {
        // your code
    }

    @FXML
    private void zurückZumHauptmenü() {
        // your code
    }
}
