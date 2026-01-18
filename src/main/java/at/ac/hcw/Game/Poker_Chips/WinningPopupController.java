package at.ac.hcw.Game.Poker_Chips;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class WinningPopupController {

    @FXML private Label winnerLabel;
    @FXML private Label chipsLabel;

    private StackPane popupRoot;

    public void setRoot(StackPane popupRoot) {
        this.popupRoot = popupRoot;
    }

    public void setData(String winnerName, int chipsWon) {
        winnerLabel.setText(winnerName);
        chipsLabel.setText("+" + chipsWon + " Chips");
    }

    @FXML
    private void handleClose() {
        // Popup vom Parent entfernen (Overlay ausblenden)
        ((StackPane) popupRoot.getParent()).getChildren().remove(popupRoot);
    }

    public void setResult(String winnerName, int chipsWon) {
    }
}