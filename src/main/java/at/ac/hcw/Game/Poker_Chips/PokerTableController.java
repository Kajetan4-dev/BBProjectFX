package at.ac.hcw.Game.Poker_Chips;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
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

// 🔴 NEU: Grundstil (hell)
            line.getStyleClass().add("players-label");

// 🔴 NEU: Folded-Stil
            if (game.isFolded(i)) {
                line.getStyleClass().add("players-label-folded");
            }

// 🔴 NEU: Aktueller Spieler
            if (i == game.getCurrentPlayerIndex()) {
                line.getStyleClass().add("players-label-current");
            }

            playersBox.getChildren().add(line);
        }

        if (game.hasRoundEnded()) {
            showWinningPopup();
        }
    }

    private void showWinningPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("winning_popup.fxml"));

            StackPane popup = loader.load();
            WinningPopupController controller = loader.getController();
            controller.setRoot(popup);

            int winnerIndex = game.getLastWinnerIndex();
            int chipsWon = game.getLastPotWon();

            controller.setData(game.getPlayers()[winnerIndex].getName(), chipsWon);

            StackPane root = (StackPane) playersBox.getScene().getRoot();
            root.getChildren().add(popup);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleBack(ActionEvent actionEvent) {
    }
}
