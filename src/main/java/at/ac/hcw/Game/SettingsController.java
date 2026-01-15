package at.ac.hcw.Game;

public class SettingsController {

    public void handleVoulume(){
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            mediaPlayer.setVolume(newVal.doubleValue());
        });
    }
}
