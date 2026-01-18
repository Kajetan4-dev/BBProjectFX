package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class BlackjackWinningPopupController {

    @FXML private Label titleLabel;
    @FXML private Label infoLabel;

    public void setResult(String winner, int chipsWon, boolean blackjack) {

        if (blackjack) {
            titleLabel.setText("BLACKJACK!");
        } else {
            titleLabel.setText("ROUND WON!");
        }

        infoLabel.setText(winner + " gewinnt " + chipsWon + " Chips");
    }

    @FXML
    private void onContinue() {
        Stage stage = (Stage) titleLabel.getScene().getWindow();
        stage.close();
    }
}