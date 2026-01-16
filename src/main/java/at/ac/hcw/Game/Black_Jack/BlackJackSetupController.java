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

/**
 * Steuert das Setup-Fenster für Blackjack (Spieler hinzufügen/entfernen).
 */
public class BlackJackSetupController {

    // Fester Standardwert für das Startguthaben
    private static final String DEFAULT_CHIPS = "500";

    // Container für die horizontale Anzeige der Spieler-Eingabefelder
    @FXML private HBox playerListContainer;

    // Listen zum Speichern der Textfelder für Namen und Chips
    private final List<TextField> nameInputFields = new ArrayList<>();
    private final List<TextField> chipInputFields = new ArrayList<>();

    /**
     * Wird beim Laden der FXML automatisch aufgerufen; startet mit 2 Spielern.
     */
    @FXML
    public void initialize() {
        handleAddPlayer();
        handleAddPlayer();
    }

    /**
     * Erstellt die UI-Elemente für einen neuen Spieler (Icon, Name, Chips).
     */
    @FXML
    private void handleAddPlayer() {
        if (nameInputFields.size() >= 6) return; // Maximal 6 Spieler erlaubt

        int playerNum = nameInputFields.size() + 1;

        // Grafische Komponenten für die Spieler-Spalte erstellen
        StackPane cardIcon = createCardIcon(playerNum);
        TextField nameField = createStyledTextField("Name P" + playerNum);
        TextField chipsField = createStyledTextField("Chips");

        // Chips auf den Standardwert 500 setzen
        chipsField.setText(DEFAULT_CHIPS);

        // Textfelder in Listen speichern, um später die Daten auszulesen
        nameInputFields.add(nameField);
        chipInputFields.add(chipsField);

        // Icon, Name und Chips vertikal untereinander stapeln
        VBox playerColumn = new VBox(12, cardIcon, nameField, chipsField);
        playerColumn.setAlignment(Pos.CENTER);

        // Die fertige Spalte dem Haupt-Container hinzufügen
        playerListContainer.getChildren().add(playerColumn);
    }

    /**
     * Entfernt den letzten Spieler aus der Liste (mindestens 1 bleibt).
     */
    @FXML
    private void handleRemovePlayer() {
        int lastIndex = playerListContainer.getChildren().size() - 1;
        if (lastIndex >= 1) {
            playerListContainer.getChildren().remove(lastIndex);
            nameInputFields.remove(lastIndex);
            chipInputFields.remove(lastIndex);
        }
    }

    /**
     * Sammelt Spielerdaten und wechselt zum Spieltisch.
     */
    @FXML
    private void startGame() {
        try {
            Player[] players = collectPlayerData(); // Daten aus den Textfeldern sammeln
            launchTableScene(players);              // Spieltisch-Szene laden
        } catch (Exception e) {
            System.err.println("Launch error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Hilfsmethode: Erstellt ein weißes Karten-Icon mit der Spielernummer.
     */
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

    /**
     * Hilfsmethode: Erstellt ein einheitlich gestyltes Textfeld.
     */
    private TextField createStyledTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setPrefWidth(120);
        tf.setMaxWidth(120);
        tf.setAlignment(Pos.CENTER);
        tf.setStyle("-fx-background-radius: 5;");
        return tf;
    }

    /**
     * Wandelt die Texteingaben aus der UI in Player-Objekte um.
     */
    private Player[] collectPlayerData() {
        Player[] players = new Player[nameInputFields.size()];
        for (int i = 0; i < players.length; i++) {
            String name = nameInputFields.get(i).getText().trim();
            if (name.isEmpty()) name = "Player " + (i + 1); // Standardname falls leer

            int chips;
            try {
                chips = Integer.parseInt(chipInputFields.get(i).getText());
            } catch (NumberFormatException e) {
                chips = Integer.parseInt(DEFAULT_CHIPS); // Fallback auf 500 bei Fehlern
            }
            players[i] = new Player(name, chips);
        }
        return players;
    }

    /**
     * Lädt die FXML für den Spieltisch und wechselt das Fenster.
     */
    private void launchTableScene(Player[] players) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/ac/hcw/Game/Black_Jack/blackjack_table.fxml"));
        Parent root = loader.load();

        // Spielregeln mit den erstellten Spielern initialisieren
        BlackJackTableController controller = loader.getController();
        controller.setGame(new BlackjackRules(players));

        // Das aktuelle Fenster (Stage) finden und die neue Szene setzen
        Stage stage = (Stage) playerListContainer.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Blackjack Table");
    }
}