package at.ac.hcw.Game.Poker_Chips;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PokerSetupController {

    @FXML private TextField bigBlindField;
    @FXML private TextField smallBlindField;
    @FXML private HBox playerListContainer;

    // Listen für den Zugriff auf Namen und Chips bei Spielstart
    private final List<TextField> nameInputFields = new ArrayList<>();
    private final List<TextField> chipInputFields = new ArrayList<>();

    @FXML
    public void initialize() {
        handleAddPlayer(); // Spieler 1 erstellen
        handleAddPlayer(); // Spieler 2 erstellen
    }

    @FXML
    private void handleAddPlayer() {
        if (nameInputFields.size() >= 6) return; // Limit: max 6 Spieler
        int playerNum = nameInputFields.size() + 1;

        // UI-Elemente erzeugen (Icon, Name, Chips)
        StackPane cardIcon = createCardIcon(playerNum);
        TextField nameField = createStyledTextField("Name P" + playerNum);
        TextField chipsField = createStyledTextField("Chips");
        chipsField.setText("500");

        // Felder für späteren Datenzugriff speichern
        nameInputFields.add(nameField);
        chipInputFields.add(chipsField);

        // Elemente untereinander in einer Spalte anordnen
        VBox playerColumn = new VBox(12, cardIcon, nameField, chipsField);
        playerColumn.setAlignment(Pos.CENTER);
        playerListContainer.getChildren().add(playerColumn);
    }

    @FXML
    private void handleRemovePlayer() {
        int lastIndex = playerListContainer.getChildren().size() - 1;
        if (lastIndex >= 1) { // Mindestens 2 Spieler behalten
            playerListContainer.getChildren().remove(lastIndex);
            nameInputFields.remove(lastIndex);
            chipInputFields.remove(lastIndex);
        }
    }

    // Erstellt das weiße Karten-Icon mit der Spielernummer
    private StackPane createCardIcon(int number) {
        Label label = new Label(String.valueOf(number));
        label.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        StackPane card = new StackPane(label);
        card.setPrefSize(55, 85);
        card.setMaxWidth(55);
        card.setStyle("-fx-background-color: white; -fx-border-color: #34495e; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8;");
        return card;
    }

    // Erstellt ein einheitliches Textfeld
    private TextField createStyledTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setPrefWidth(120);
        tf.setAlignment(Pos.CENTER);
        return tf;
    }

    @FXML
    private void handleStartGame() {
        try {
            // Blinds und Spielerdaten einsammeln
            int big = Integer.parseInt(bigBlindField.getText());
            int small = Integer.parseInt(smallBlindField.getText());
            int playerCount = nameInputFields.size();

            String[] names = new String[playerCount];
            int[] startingChips = new int[playerCount];

            for (int i = 0; i < playerCount; i++) {
                names[i] = nameInputFields.get(i).getText().isEmpty() ? "Player " + (i+1) : nameInputFields.get(i).getText();
                startingChips[i] = Integer.parseInt(chipInputFields.get(i).getText());
            }

            // Spiellogik initialisieren
            PokerRules pokerGame = new PokerRules(playerCount, big, small);
            pokerGame.playerSetupWithChips(names, startingChips);
            pokerGame.startHand();

            // Wechsel zum Spieltisch (poker_table.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/ac/hcw/Game/Poker_Chips/poker_table.fxml"));
            Parent root = loader.load();
            PokerTableController tableController = loader.getController();
            tableController.setGame(pokerGame);

            Stage stage = (Stage) playerListContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Poker Table");
        } catch (Exception e) { e.printStackTrace(); }
    }
}