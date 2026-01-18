package at.ac.hcw.Game.Poker_Chips;
//für tim
import at.ac.hcw.Game.AllSoundEffects;
import at.ac.hcw.Game.SettingsController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * Controller für den Setup-Screen:
 * - Spieler anlegen (2 bis 6)
 * - Startgeld setzen
 * - Blinds setzen
 * - Spiel starten und zur Table-Scene wechseln
 *
 * Diese Datei ist die "einfache Setup-Version" (nur Namenfelder).
 */
public class PokerSetupController {

    @FXML private VBox leftColumn;
    @FXML private VBox rightColumn;

    @FXML private TextField bigBlindField;
    @FXML private TextField smallBlindField;

    private final List<PlayerCard> playerCards = new ArrayList<>();

    @FXML
    public void initialize() {
        // Start with 2 players by default
        handleAddPlayer();
        handleAddPlayer();
    }

    @FXML
    private void handleGoToSettings() throws IOException {
        AllSoundEffects.button();
        SettingsController.setFromBlackjack(false);
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/at/ac/hcw/Game/Settings.fxml")
        );
        Parent root = loader.load();

        SettingsController controller = loader.getController();
        controller.setPBN(0); // or 1, 2, etc.

        Stage stage = (Stage) bigBlindField.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Settings");
        stage.show();
    }


    @FXML
    private void handleAddPlayer() {
        AllSoundEffects.button();
        if (playerCards.size() >= 6) return;

        int playerNumber = playerCards.size() + 1;
        int startMoney = 500; // Fixed default starting money

        PlayerCard card = new PlayerCard(playerNumber, startMoney);
        playerCards.add(card);

        // Add to left or right column
        if (playerNumber <= 3) {
            leftColumn.getChildren().add(card.getCard());
        } else {
            rightColumn.getChildren().add(card.getCard());
        }
    }

    @FXML
    private void handleRemovePlayer() {
        AllSoundEffects.button();
        if (playerCards.size() <= 2) return;

        PlayerCard lastCard = playerCards.remove(playerCards.size() - 1);

        if (lastCard.getPlayerNumber() <= 3) {
            leftColumn.getChildren().remove(lastCard.getCard());
        } else {
            rightColumn.getChildren().remove(lastCard.getCard());
        }
    }

    @FXML
    private void handleStartGame() {
        AllSoundEffects.button();
        try {
            // 1. Collect Data from UI
            int big = Integer.parseInt(bigBlindField.getText());
            int small = Integer.parseInt(smallBlindField.getText());

            int playerCount = playerCards.size();
            String[] names = new String[playerCount];
            int[] startingChips = new int[playerCount];

            for (int i = 0; i < playerCount; i++) {
                names[i] = playerCards.get(i).getName();
                startingChips[i] = playerCards.get(i).getChips(); // Get individual chips
            }

            // 2. Initialize Game Logic
            PokerRules pokerGame = new PokerRules(playerCount, big, small);
            // Note: I modified this to accept the chips array
            pokerGame.playerSetupWithChips(names, startingChips);

            // 3. SWITCH THE SCENE (This was missing!)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/ac/hcw/Game/Poker_Chips/poker_table.fxml"));
            Parent root = loader.load();

            // Pass the game instance to the Table Controller
            PokerTableController tableController = loader.getController();
            tableController.setGame(pokerGame);

            // Get the current window (Stage) and set the new Scene
            Stage stage = (Stage) bigBlindField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Poker Table");
            stage.show();

        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter valid numbers for blinds and chips!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error: Could not find poker_table.fxml");
        }
    }

    /** Inner class for a Player Card UI component */
    private static class PlayerCard {
        private final VBox card;
        private final TextField nameField;
        private final TextField chipsField;
        private final int playerNumber;

        public PlayerCard(int playerNumber, int startMoney) {
            this.playerNumber = playerNumber;

            card = new VBox(5);
            card.setAlignment(Pos.CENTER_LEFT);
            card.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-padding: 10; -fx-background-color: #f5f0e6;");

            Label playerLabel = new Label("Player " + playerNumber);
            playerLabel.setStyle("-fx-font-weight: bold;");

            nameField = new TextField();
            nameField.setPromptText("Name");
            nameField.setMaxWidth(150);

            HBox chipsBox = new HBox(5);
            chipsBox.setAlignment(Pos.CENTER_LEFT);
            Label chipsLabel = new Label("Chips:");
            chipsField = new TextField(String.valueOf(startMoney));
            chipsField.setMaxWidth(80);
            chipsBox.getChildren().addAll(chipsLabel, chipsField);

            card.getChildren().addAll(playerLabel, nameField, chipsBox);
        }

        public VBox getCard() {
            return card;
        }

        public int getPlayerNumber() {
            return playerNumber;
        }

        public String getName() {
            return nameField.getText().isEmpty() ? "Player " + playerNumber : nameField.getText();
        }

        public int getChips() {
            try {
                return Integer.parseInt(chipsField.getText());
            } catch (NumberFormatException e) {
                return 500;
            }
        }
    }
}
