package at.ac.hcw.Game.Poker_Chips;

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
 * PokerSetupController
 *
 * Zuständig für den Setup-Screen vor dem Start des Poker-Chips-Spiels.
 *
 * Aufgaben:
 *  - Spieler hinzufügen/entfernen (min 2, max 6)
 *  - Blinds auslesen (Big/Small)
 *  - Pro Spieler: Name und Start-Chips erfassen
 *  - PokerRules-Instanz erstellen und initialisieren
 *  - Scene-Wechsel zum Poker Table Screen (poker_table.fxml)
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

    //Öffnet den Setting Screen.
    @FXML
    private void handleGoToSettings() throws IOException {
        AllSoundEffects.button();
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/at/ac/hcw/Game/Settings.fxml")
        );
        Parent root = loader.load();

        // SettingsController holen und "PBN" setzen (Navigation/Back-Logic)
        SettingsController controller = loader.getController();
        controller.setPBN(0); // or 1, 2, etc.

        // Aktuelles Stage-Fenster holen und Scene ersetzen
        Stage stage = (Stage) bigBlindField.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Settings");
        stage.show();
    }

    /*
     * Fügt einen Spieler hinzu (max 6).
     * - Erstellt eine neue PlayerCard (Name + Chips)
     * - Speichert sie in playerCards
     * - hängt sie in leftColumn oder rightColumn ein
     */
    @FXML
    private void handleAddPlayer() {
        AllSoundEffects.button(); //Klick-Sound

        //Max. 6 Spieler
        if (playerCards.size() >= 6) return;

        // Nächste Spieler-Nummer (1-basiert)
        int playerNumber = playerCards.size() + 1;
        int startMoney = 500; // Fixed default starting money

        // PlayerCard erzeugen und merken
        PlayerCard card = new PlayerCard(playerNumber, startMoney);
        playerCards.add(card);

        // In linke oder rechte Spalte einfügen
        // Spieler 1-3 links, 4-6 rechts
        if (playerNumber <= 3) {
            leftColumn.getChildren().add(card.getCard());
        } else {
            rightColumn.getChildren().add(card.getCard());
        }
    }

    /*
     * Entfernt den letzten Spieler (min 2).
     * - Entfernt das letzte PlayerCard-Element aus der Liste
     * - entfernt die UI-Karte aus der passenden VBox-Spalte
     */
    @FXML
    private void handleRemovePlayer() {
        AllSoundEffects.button(); //Klick Sound

        //Min. 2 Spieler
        if (playerCards.size() <= 2) return;

        // Letzte PlayerCard entfernen
        PlayerCard lastCard = playerCards.remove(playerCards.size() - 1);

        // UI-Karte aus richtiger Spalte entfernen
        if (lastCard.getPlayerNumber() <= 3) {
            leftColumn.getChildren().remove(lastCard.getCard());
        } else {
            rightColumn.getChildren().remove(lastCard.getCard());
        }
    }

    /*
     * Startet das Spiel:
     *  1) Blinds lesen
     *  2) Player Names + Start-Chips aus PlayerCards sammeln
     *  3) PokerRules erstellen + Setup durchführen
     *  4) poker_table.fxml laden
     *  5) PokerTableController bekommt die PokerRules Instanz (setGame)
     *  6) Scene wechseln
     */
    @FXML
    private void handleStartGame() {
        AllSoundEffects.button();
        try {
            // 1. Collect Data from UI
            int big = Integer.parseInt(bigBlindField.getText());
            int small = Integer.parseInt(smallBlindField.getText());

            // 1.5 Arrays für Namen und Start-Chips erstellen
            int playerCount = playerCards.size();
            String[] names = new String[playerCount];
            int[] startingChips = new int[playerCount];

            // PlayerCard -> Name + Chips auslesen
            for (int i = 0; i < playerCount; i++) {
                names[i] = playerCards.get(i).getName();
                startingChips[i] = playerCards.get(i).getChips(); // Get individual chips
            }

            // 2. Initialize Game Logic
            PokerRules pokerGame = new PokerRules(playerCount, big, small);
            // Note: I modified this to accept the chips array
            pokerGame.playerSetupWithChips(names, startingChips);

            // 3. SWITCH THE SCENE
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

    /*
     * Inner Class: PlayerCard
     *
     * Ein kleines UI-Komponenten-Objekt, das eine "Spieler-Karte" repräsentiert:
     * - Label "Player X"
     * - Name TextField
     * - Chips TextField
     *
     * Wird dynamisch im Setup erstellt und in leftColumn/rightColumn eingefügt.
     */
    private static class PlayerCard {

        // Root UI-Node der Karte
        private final VBox card;

        // Eingabefelder
        private final TextField nameField;
        private final TextField chipsField;

        // Spieler-Nummer (1-basiert, wichtig fürs Layout links/rechts)
        private final int playerNumber;

        /*
         * Konstruktor: baut die UI-Karte zusammen.
         *
         * @param playerNumber Spielerindex (1..6)
         * @param startMoney Default Start-Chips
         */
        public PlayerCard(int playerNumber, int startMoney) {
            this.playerNumber = playerNumber;

            // VBox als Karten-Container
            card = new VBox(5);
            card.setAlignment(Pos.CENTER_LEFT);
            card.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-padding: 10; -fx-background-color: #f5f0e6;");

            // Oben: Player Label
            Label playerLabel = new Label("Player " + playerNumber);
            playerLabel.setStyle("-fx-font-weight: bold;");

            // Name input
            nameField = new TextField();
            nameField.setPromptText("Name");
            nameField.setMaxWidth(150);

            // Chips input Zeile
            HBox chipsBox = new HBox(5);
            chipsBox.setAlignment(Pos.CENTER_LEFT);
            Label chipsLabel = new Label("Chips:");
            chipsField = new TextField(String.valueOf(startMoney));
            chipsField.setMaxWidth(80);
            chipsBox.getChildren().addAll(chipsLabel, chipsField);

            // Karte zusammensetzen
            card.getChildren().addAll(playerLabel, nameField, chipsBox);
        }
        /* Gibt die UI-Karte zurück, damit sie ins Layout eingefügt werden kann. */
        public VBox getCard() {
            return card;
        }

        /* Gibt die Spieler-Nummer zurück (für links/rechts Platzierung). */
        public int getPlayerNumber() {
            return playerNumber;
        }

        /*
         * Gibt den eingegebenen Namen zurück.
         * Wenn Name leer ist, wird "Player X" als Fallback verwendet.
         */
        public String getName() {
            return nameField.getText().isEmpty() ? "Player " + playerNumber : nameField.getText();
        }

        /*
         * Gibt den Chip-Wert zurück.
         * Wenn das Feld keine Zahl enthält, wird 500 als Fallback verwendet.
         */
        public int getChips() {
            try {
                return Integer.parseInt(chipsField.getText());
            } catch (NumberFormatException e) {
                return 500;
            }
        }
    }
}
