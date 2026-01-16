package at.ac.hcw.Game.Poker_Chips;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class WinningPopupController {

    @FXML private Label winnerLabel;
    @FXML private Label chipsLabel;

    private StackPane root;

    public void setData(String winnerName, int chipsWon) {
        winnerLabel.setText(winnerName);
        chipsLabel.setText("+" + chipsWon + " Chips");
    }

    public void setRoot(StackPane root) {
        this.root = root;
    }

    @FXML
    private void handleClose() {
        ((StackPane) root.getParent()).getChildren().remove(root);
    }
}