package at.ac.hcw.Game.Poker_Chips;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PokerSetupController {

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
        int startMoney;
        int big;
        int small;
        String[] names;
        try {
             startMoney = Integer.parseInt(startingMoneyField.getText());
             big = Integer.parseInt(bigBlindField.getText());
             small = Integer.parseInt(smallBlindField.getText());

            names = nameFields.stream()
                    .map(tf -> tf.getText().isEmpty() ? tf.getPromptText() : tf.getText())
                    .toArray(String[]::new);

            // Pass data to your existing PokerRules
            PokerRules pokerGame = new PokerRules(names.length, big, small);
            pokerGame.playerSetup(names, startMoney);

            System.out.println("Starting Poker with " + names.length + " players...");
            pokerGame.startHand();

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/at/ac/hcw/Game/Poker_Chips/poker_table.fxml"));
            Parent root = loader.load();

            PokerTableController tableController = loader.getController();
            tableController.setGame(pokerGame);

            Stage stage = (Stage) startingMoneyField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (NumberFormatException e) {
            System.out.println("Please enter valid numbers for money and blinds!");
        }catch (IOException e){
            System.out.println("Could not load poker_table.fxml!");
            e.printStackTrace();
        }
    }
}