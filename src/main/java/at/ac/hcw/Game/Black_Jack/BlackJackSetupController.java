package at.ac.hcw.Game.Black_Jack;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BlackJackSetupController {

    // ===== FXML =====
    @FXML
    private TextField startingMoneyField;

    @FXML
    private VBox playerListContainer;

    // ===== Intern =====
    private final List<TextField> nameFields = new ArrayList<>();

    // ===== Initialisierung =====
    @FXML
    public void initialize() {
        handleAddPlayer();
        handleAddPlayer(); // mind. 2 Spieler
    }

    // ===== Spieler hinzufügen =====
    @FXML
    private void handleAddPlayer() {
        if (nameFields.size() >= 6) return;

        TextField tf = new TextField();
        tf.setPromptText("Player " + (nameFields.size() + 1));
        tf.setMaxWidth(200);

        nameFields.add(tf);
        playerListContainer.getChildren().add(tf);
    }

    // ===== Spieler entfernen =====
    @FXML
    private void handleRemovePlayer() {
        if (nameFields.size() <= 2) return;

        TextField tf = nameFields.remove(nameFields.size() - 1);
        playerListContainer.getChildren().remove(tf);
    }

    // ===== Spiel starten =====
    @FXML
    private void startGame() {
        try {
            int startChips = Integer.parseInt(startingMoneyField.getText());

            if (startChips <= 0) {
                System.out.println("Startchips müssen > 0 sein!");
                return;
            }

            // ===== Spieler erstellen =====
            Player[] players = new Player[nameFields.size()];

            for (int i = 0; i < nameFields.size(); i++) {
                String name = nameFields.get(i).getText().trim();
                if (name.isEmpty()) {
                    name = nameFields.get(i).getPromptText();
                }
                players[i] = new Player(name, startChips);
            }

            // ===== Blackjack starten =====
            BlackjackRules game = new BlackjackRules(players);
            game.startRound();

            // ===== Table laden =====
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/at/ac/hcw/Game/Black_Jack/blackjack_table.fxml"
                    )
            );
            Parent root = loader.load();

            BlackJackTableController tableController = loader.getController();
            tableController.setGame(game);

            // ===== Szene wechseln =====
            Stage stage = (Stage) ((Node) startingMoneyField)
                    .getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Blackjack Tisch");
            stage.show();

        } catch (NumberFormatException e) {
            System.out.println("Bitte gültige Zahl eingeben!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
