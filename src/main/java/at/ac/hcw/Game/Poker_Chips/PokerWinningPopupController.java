package at.ac.hcw.Game.Poker_Chips;

import at.ac.hcw.Game.AllSoundEffects;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PokerWinningPopupController {

    @FXML private Label titleLabel;
    @FXML private Label infoLabel;
    @FXML private Label chipLabel;

    public void setResult(String winnerName, int chipsWon) {

        titleLabel.setText("WINNER!");
        infoLabel.setText(winnerName + " gewinnt den Pot");
        AllSoundEffects.win();

        animateChips(chipsWon);
    }

    private void animateChips(int chipsWon) {
        Timeline timeline = new Timeline();
        int steps = 30;
        int increment = Math.max(1, chipsWon / steps);

        for (int i = 0; i <= steps; i++) {
            int value = Math.min(chipsWon, i * increment);
            timeline.getKeyFrames().add(
                    new KeyFrame(
                            Duration.millis(i * 30),
                            e -> chipLabel.setText("+" + value + " Chips")
                    )
            );
        }
        timeline.play();
    }

    @FXML
    private void onContinue() {
        Stage stage = (Stage) chipLabel.getScene().getWindow();
        stage.close();
    }

    public void handleClose(ActionEvent actionEvent) {
    }
}