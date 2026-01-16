package at.ac.hcw.Game.Black_Jack;

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

import java.util.ArrayList;
import java.util.List;

public class BlackJackSetupController {

    // Standardwert für das Startkapital, da das UI-Feld entfernt wurde
    private static final String DEFAULT_CHIPS = "500";

    @FXML private HBox playerListContainer;

    private final List<TextField> nameInputFields = new ArrayList<>();
    private final List<TextField> chipInputFields = new ArrayList<>();

    @FXML
    public void initialize() {
        // Initialisierung mit 2 Spielern
        handleAddPlayer();
        handleAddPlayer();
    }

    @FXML
    private void handleAddPlayer() {
        if (nameInputFields.size() >= 6) return; // UI Limit

        int playerNum = nameInputFields.size() + 1;

        // UI Komponenten erstellen
        StackPane cardIcon = createCardIcon(playerNum);
        TextField nameField = createStyledTextField("Name P" + playerNum);
        TextField chipsField = createStyledTextField("Chips");

        // Standard-Chips setzen
        chipsField.setText(DEFAULT_CHIPS);

        // Referenzen speichern
        nameInputFields.add(nameField);
        chipInputFields.add(chipsField);

        // In einer vertikalen Spalte gruppieren
        VBox playerColumn = new VBox(12, cardIcon, nameField, chipsField);
        playerColumn.setAlignment(Pos.CENTER);

        playerListContainer.getChildren().add(playerColumn);
    }

    @FXML
    private void handleRemovePlayer() {
        int lastIndex = playerListContainer.getChildren().size() - 1;
        if (lastIndex >= 1) { // Mindestens 1 Spieler muss bleiben
            playerListContainer.getChildren().remove(lastIndex);
            nameInputFields.remove(lastIndex);
            chipInputFields.remove(lastIndex);
        }
    }

    @FXML
    private void startGame() {
        try {
            Player[] players = collectPlayerData();
            launchTableScene(players);
        } catch (Exception e) {
            System.err.println("Launch error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private StackPane createCardIcon(int number) {
        Label label = new Label(String.valueOf(number));
        label.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        StackPane card = new StackPane(label);
        card.setPrefSize(55, 85);
        card.setMaxWidth(55);
        card.setStyle(
                "-fx-background-color: white; -fx-border-color: #34495e; -fx-border-width: 2; " +
                        "-fx-border-radius: 8; -fx-background-radius: 8; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);"
        );
        return card;
    }

    private TextField createStyledTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setPrefWidth(120);
        tf.setMaxWidth(120);
        tf.setAlignment(Pos.CENTER);
        tf.setStyle("-fx-background-radius: 5;");
        return tf;
    }

    private Player[] collectPlayerData() {
        Player[] players = new Player[nameInputFields.size()];
        for (int i = 0; i < players.length; i++) {
            String name = nameInputFields.get(i).getText().trim();
            if (name.isEmpty()) name = "Player " + (i + 1);

            int chips;
            try {
                chips = Integer.parseInt(chipInputFields.get(i).getText());
            } catch (NumberFormatException e) {
                chips = Integer.parseInt(DEFAULT_CHIPS);
            }
            players[i] = new Player(name, chips);
        }
        return players;
    }

    private void launchTableScene(Player[] players) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/ac/hcw/Game/Black_Jack/blackjack_table.fxml"));
        Parent root = loader.load();

        BlackJackTableController controller = loader.getController();
        controller.setGame(new BlackjackRules(players));

        // Zugriff auf die Stage über den playerListContainer, da startingMoneyField gelöscht wurde
        Stage stage = (Stage) playerListContainer.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Blackjack Table");
    }
}