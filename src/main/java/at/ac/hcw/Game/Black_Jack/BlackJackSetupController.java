package at.ac.hcw.Game.Black_Jack;

import at.ac.hcw.Game.Poker_Chips.PokerRules;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class BlackJackSetupController {

    @FXML private VBox playerListContainer;
    @FXML private TextField startingMoneyField;
    @FXML private TextField bigBlindField;
    @FXML private TextField smallBlindField;

    private final List<TextField> nameFields = new ArrayList<>();

    @FXML
    public void initialize() {
        // Start with 2 players by default
        handleAddPlayer();
        handleAddPlayer();
    }

    @FXML
    private void handleAddPlayer() {
        if (nameFields.size() < 6) {
            TextField newPlayer = new TextField();
            newPlayer.setPromptText("Player Name " + (nameFields.size() + 1));
            newPlayer.setMaxWidth(200);
            nameFields.add(newPlayer);
            playerListContainer.getChildren().add(newPlayer);
        }
    }

    @FXML
    private void handleRemovePlayer() {
        if (nameFields.size() > 2) {
            TextField lastField = nameFields.remove(nameFields.size() - 1);
            playerListContainer.getChildren().remove(lastField);
        }
    }

    @FXML
    private void handleStartGame() {
        try {
            int startMoney = Integer.parseInt(startingMoneyField.getText());
            int big = Integer.parseInt(bigBlindField.getText());
            int small = Integer.parseInt(smallBlindField.getText());

            String[] names = nameFields.stream()
                    .map(tf -> tf.getText().isEmpty() ? tf.getPromptText() : tf.getText())
                    .toArray(String[]::new);

            // Pass data to your existing PokerRules
            PokerRules pokerGame = new PokerRules(names.length, big, small);
            pokerGame.playerSetup(names, startMoney);

            System.out.println("Starting Poker with " + names.length + " players...");
            pokerGame.startHand();

        } catch (NumberFormatException e) {
            System.out.println("Please enter valid numbers for money and blinds!");
        }
    }
}