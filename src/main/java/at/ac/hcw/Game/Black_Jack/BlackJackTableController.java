package at.ac.hcw.Game.Black_Jack;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BlackJackTableController {

    private BlackjackRules game;

    @FXML
    private HBox playersContainer;

    @FXML
    private Label dealerTotalLabel;

    @FXML
    private Button newRoundButton;

    // Hilfsklasse für Spieler-UI Elemente
    private static class PlayerUI {
        Label nameLabel;
        Label chipsLabel;
        Label bidLabel;
        Label totalLabel;
        TextField bidField;
        Button setBidButton;
        Button hitButton;
        Button standButton;
        HBox cardsContainer;  // *** Geändert: Container für Karten hinzugefügt
    }

    private final List<PlayerUI> playerUIs = new ArrayList<>();

    public void setGame(BlackjackRules game) {
        this.game = game;
        initUI();
        newRoundButton.setVisible(false);
    }

    private void initUI() {
        playersContainer.getChildren().clear();
        playerUIs.clear();

        Player[] players = game.getPlayers();

        for (int i = 0; i < players.length; i++) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/ac/hcw/Game/Black_Jack/player_panel.fxml"));
                VBox playerPanel = loader.load();

                PlayerUI ui = new PlayerUI();
                ui.nameLabel = (Label) playerPanel.lookup("#playerNameLabel");
                ui.chipsLabel = (Label) playerPanel.lookup("#playerChipsLabel");
                ui.bidLabel = (Label) playerPanel.lookup("#playerBidLabel");
                ui.totalLabel = (Label) playerPanel.lookup("#playerTotalLabel");
                ui.bidField = (TextField) playerPanel.lookup("#playerBidField");
                ui.setBidButton = (Button) playerPanel.lookup("#setBidButton");
                ui.hitButton = (Button) playerPanel.lookup("#hitButton");
                ui.standButton = (Button) playerPanel.lookup("#standButton");
                ui.cardsContainer = (HBox) playerPanel.lookup("#cardsContainer"); // *** Geändert: Karten-Container

                Player player = players[i];
                ui.nameLabel.setText(player.getName());
                ui.chipsLabel.setText("Chips: " + player.getChips());
                ui.bidLabel.setText("Bid: " + player.getBid());
                ui.totalLabel.setText("Total: " + player.getTotal());

                ui.hitButton.setDisable(true);
                ui.standButton.setDisable(true);

                final int playerIndex = i;

                ui.setBidButton.setOnAction(e -> setBidForPlayer(playerIndex));
                ui.hitButton.setOnAction(e -> {
                    game.hit();
                    updateUI();
                    checkIfRoundOver();
                });
                ui.standButton.setOnAction(e -> {
                    game.stand();
                    updateUI();
                    checkIfRoundOver();
                });

                playerUIs.add(ui);
                playersContainer.getChildren().add(playerPanel);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        dealerTotalLabel.setText("Dealer Total: " + game.getDealer().getTotal());
    }

    private void updateUI() {
        Player[] players = game.getPlayers();

        for (int i = 0; i < players.length; i++) {
            Player player = players[i];
            PlayerUI ui = playerUIs.get(i);

            ui.chipsLabel.setText("Chips: " + player.getChips());
            ui.bidLabel.setText("Bid: " + player.getBid());
            ui.totalLabel.setText("Total: " + player.getTotal());

            ui.cardsContainer.getChildren().clear();
            int[] cards = player.getCards();

            for (Integer card : cards) {
                if (card != null && card > 0) { // Nur echte Karten anzeigen
                    String cardStr = convertCardValue(card);
                    Label cardLabel = new Label(cardStr);
                    cardLabel.setStyle("-fx-border-color: black; -fx-padding: 5px; -fx-background-color: white; -fx-font-weight: bold;");
                    ui.cardsContainer.getChildren().add(cardLabel);
                }
            }

            int currentPlayerIndex = game.getCurrentPlayerIndex();

            if (currentPlayerIndex == i) {
                ui.hitButton.setDisable(player.getBid() <= 0 || player.isStand());
                ui.standButton.setDisable(player.getBid() <= 0 || player.isStand());
                ui.setBidButton.setDisable(player.getBid() > 0);
                ui.bidField.setDisable(player.getBid() > 0);
            } else {
                ui.hitButton.setDisable(true);
                ui.standButton.setDisable(true);
                ui.setBidButton.setDisable(true);
                ui.bidField.setDisable(true);
            }
        }

        dealerTotalLabel.setText("Dealer Total: " + game.getDealer().getTotal());
    }

    private void setBidForPlayer(int playerIndex) {
        PlayerUI ui = playerUIs.get(playerIndex);
        try {
            int bid = Integer.parseInt(ui.bidField.getText());
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

    private void checkIfRoundOver() {
        if (!game.isRoundActive()) {
            showAlert("Runde beendet!");
            disableAllPlayerButtons();
            newRoundButton.setVisible(true);
        }
    }

    private void disableAllPlayerButtons() {
        for (PlayerUI ui : playerUIs) {
            ui.hitButton.setDisable(true);
            ui.standButton.setDisable(true);
            ui.setBidButton.setDisable(true);
            ui.bidField.setDisable(true);
        }
    }

    @FXML
    private void handleNewRound() {
        game.resetRound();
        game.startRound();
        initUI();
        newRoundButton.setVisible(false);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Hilfsmethode, um Kartenzahlen in Strings zu verwandeln
    private String convertCardValue(int card) {
        switch (card) {
            case 1:  return "A";
            case 11: return "J";
            case 12: return "Q";
            case 13: return "K";
            default: return String.valueOf(card);
        }
    }
}
