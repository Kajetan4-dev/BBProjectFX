package at.ac.hcw.Game.Poker_Chips;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class PokerTableController {

    @FXML private Label roundLabel;
    @FXML private Label potLabel;
    @FXML private Label currentBetLabel;
    @FXML private Label currentPlayerLabel;

    @FXML private TextField raiseField;
    @FXML private VBox playersBox;

    private PokerRules game;

    public void setGame(PokerRules game) {
        this.game = game;
        updateUI();
    }

    @FXML
    private void handleCall() {
        game.callOrCheck();
        updateUI();
    }

    @FXML
    private void handleFold() {
        game.fold();
        updateUI();
    }

    @FXML
    private void handleRaise() {
        int amount = 0;
        try { amount = Integer.parseInt(raiseField.getText().trim()); }
        catch (Exception ignored) {}

        game.raise(amount);
        raiseField.clear();
        updateUI();
    }

    private void updateUI() {
        roundLabel.setText("Round: " + game.getRoundName());
        potLabel.setText("Pot: " + game.getPot());
        currentBetLabel.setText("Current Bet: " + game.getCurrentBet());
        currentPlayerLabel.setText("Current Player: " + game.getCurrentPlayer().getName());

        // alle Spieler anzeigen
        playersBox.getChildren().clear();
        PokerChipsPlayer[] ps = game.getPlayers();

        for (int i = 0; i < ps.length; i++) {
            PokerChipsPlayer p = ps[i];

            String foldedText = game.isFolded(i) ? " (FOLDED)" : "";
            String turnMarker = (i == game.getCurrentPlayerIndex()) ? "  <--" : "";

            Label line = new Label(
                    p.getName() + " | money=" + p.getPlayerMoney() + " | bet=" + p.getBet()
                            + foldedText + turnMarker
            );

            playersBox.getChildren().add(line);
        }
    }

    public void handleBack(ActionEvent actionEvent) {
    }
}
