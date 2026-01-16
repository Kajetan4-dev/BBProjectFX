package at.ac.hcw.Game;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;

public class SettingsController {
    @FXML
    private Button neuesSpielBtn;
    private int PBN;
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
    public void setPBN(int PBN) {
        this.PBN = PBN;

        // You can react immediately if needed
        if (neuesSpielBtn != null && PBN == 0) {
            ((Pane) neuesSpielBtn.getParent()).getChildren().remove(neuesSpielBtn);
        }
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

    @FXML
    private void zurückLast(){
        AllSoundEffects.button();
    }
}
