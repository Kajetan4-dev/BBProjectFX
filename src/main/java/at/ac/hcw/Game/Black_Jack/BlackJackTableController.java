package at.ac.hcw.Game.Black_Jack;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class BlackJackTableController {

    private BlackjackRules game;

    // Player 1 UI Elemente
    @FXML private Label player1NameLabel;
    @FXML private Label player1ChipsLabel;
    @FXML private Label player1BidLabel;
    @FXML private Label player1TotalLabel;
    @FXML private TextField player1BidField;
    @FXML private Button player1SetBidButton;
    @FXML private Button player1HitButton;
    @FXML private Button player1StandButton;

    // Player 2 UI Elemente
    @FXML private Label player2NameLabel;
    @FXML private Label player2ChipsLabel;
    @FXML private Label player2BidLabel;
    @FXML private Label player2TotalLabel;
    @FXML private TextField player2BidField;
    @FXML private Button player2SetBidButton;
    @FXML private Button player2HitButton;
    @FXML private Button player2StandButton;

    // Dealer
    @FXML private Label dealerTotalLabel;

    // Neu: Button für neue Runde
    @FXML private Button newRoundButton;

    public void setGame(BlackjackRules game) {
        this.game = game;
        initUI();
        newRoundButton.setVisible(false); // initial unsichtbar
    }

    private void initUI() {
        Player[] players = game.getPlayers();

        if (players.length > 0) {
            player1NameLabel.setText(players[0].getName());
            player1ChipsLabel.setText("Chips: " + players[0].getChips());
            player1BidLabel.setText("Bid: " + players[0].getBid());
            player1TotalLabel.setText("Total: " + players[0].getTotal());
            player1HitButton.setDisable(true);
            player1StandButton.setDisable(true);
        }
        if (players.length > 1) {
            player2NameLabel.setText(players[1].getName());
            player2ChipsLabel.setText("Chips: " + players[1].getChips());
            player2BidLabel.setText("Bid: " + players[1].getBid());
            player2TotalLabel.setText("Total: " + players[1].getTotal());
            player2HitButton.setDisable(true);
            player2StandButton.setDisable(true);
        }

        dealerTotalLabel.setText("Dealer Total: " + game.getDealer().getTotal());
    }

    private void updateUI() {
        Player[] players = game.getPlayers();

        if (players.length > 0) {
            player1ChipsLabel.setText("Chips: " + players[0].getChips());
            player1BidLabel.setText("Bid: " + players[0].getBid());
            player1TotalLabel.setText("Total: " + players[0].getTotal());
        }
        if (players.length > 1) {
            player2ChipsLabel.setText("Chips: " + players[1].getChips());
            player2BidLabel.setText("Bid: " + players[1].getBid());
            player2TotalLabel.setText("Total: " + players[1].getTotal());
        }

        dealerTotalLabel.setText("Dealer Total: " + game.getDealer().getTotal());

        updateButtons();
    }

    private void updateButtons() {
        int currentPlayerIndex = game.getCurrentPlayerIndex();
        Player currentPlayer = game.getCurrentPlayer();

        if (currentPlayerIndex == 0) {
            player1HitButton.setDisable(currentPlayer.getBid() <= 0 || currentPlayer.isStand());
            player1StandButton.setDisable(currentPlayer.getBid() <= 0 || currentPlayer.isStand());

            player1SetBidButton.setDisable(currentPlayer.getBid() > 0);
            player1BidField.setDisable(currentPlayer.getBid() > 0);

            player2HitButton.setDisable(true);
            player2StandButton.setDisable(true);
            player2SetBidButton.setDisable(true);
            player2BidField.setDisable(true);
        } else if (currentPlayerIndex == 1) {
            player2HitButton.setDisable(currentPlayer.getBid() <= 0 || currentPlayer.isStand());
            player2StandButton.setDisable(currentPlayer.getBid() <= 0 || currentPlayer.isStand());

            player2SetBidButton.setDisable(currentPlayer.getBid() > 0);
            player2BidField.setDisable(currentPlayer.getBid() > 0);

            player1HitButton.setDisable(true);
            player1StandButton.setDisable(true);
            player1SetBidButton.setDisable(true);
            player1BidField.setDisable(true);
        }
    }

    @FXML
    private void handleSetBidPlayer1() {
        setBidForPlayer(0, player1BidField);
    }

    @FXML
    private void handleSetBidPlayer2() {
        setBidForPlayer(1, player2BidField);
    }

    private void setBidForPlayer(int playerIndex, TextField bidField) {
        try {
            int bid = Integer.parseInt(bidField.getText());
            Player player = game.getPlayers()[playerIndex];

            if (bid <= 0) {
                showAlert("Bitte eine positive Zahl für den Einsatz eingeben.");
                return;
            }
            if (bid > player.getChips()) {
                showAlert("Du hast nicht genug Chips für diesen Einsatz.");
                return;
            }

            player.setBid(bid);
            updateUI();

        } catch (NumberFormatException e) {
            showAlert("Bitte eine gültige Zahl eingeben.");
        }
    }

    @FXML
    private void handleHit() {
        game.hit();
        updateUI();
        checkIfRoundOver();
    }

    @FXML
    private void handleStand() {
        game.stand();
        updateUI();
        checkIfRoundOver();
    }

    private void checkIfRoundOver() {
        if (!game.isRoundActive()) {
            showAlert("Runde beendet!");
            // Buttons deaktivieren
            disableAllPlayerButtons();
            // Neue Runde Button anzeigen
            newRoundButton.setVisible(true);
        }
    }

    private void disableAllPlayerButtons() {
        player1HitButton.setDisable(true);
        player1StandButton.setDisable(true);
        player1SetBidButton.setDisable(true);
        player1BidField.setDisable(true);

        player2HitButton.setDisable(true);
        player2StandButton.setDisable(true);
        player2SetBidButton.setDisable(true);
        player2BidField.setDisable(true);
    }

    @FXML
    private void handleNewRound() {
        game.resetRound(); // Diese Methode muss in BlackjackRules existieren und das Spiel zurücksetzen
        game.startRound();
        updateUI();
        newRoundButton.setVisible(false);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
