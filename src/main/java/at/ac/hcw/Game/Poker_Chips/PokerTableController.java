package at.ac.hcw.Game.Poker_Chips;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.ArrayList;
import java.util.List;

public class PokerTableController {

    // UI-Elemente aus der FXML-Datei
    @FXML private Label potLabel;
    @FXML private Label currentBetLabel;
    @FXML private Label roundLabel;
    @FXML private HBox playerContainer;

    private PokerRules game;

    // Listen zur Verwaltung der dynamisch erstellten UI-Elemente pro Spieler
    private List<Label> moneyLabels = new ArrayList<>();
    private List<Label> betLabels = new ArrayList<>();
    private List<Label> roleLabels = new ArrayList<>();
    private List<TextField> raiseFields = new ArrayList<>();
    private List<Button> callButtons = new ArrayList<>();
    private List<Button> raiseButtons = new ArrayList<>();
    private List<Button> foldButtons = new ArrayList<>();
    private List<VBox> playerBoxes = new ArrayList<>();

    // Initialisiert das Spiel und baut die Benutzeroberfläche auf
    public void setGame(PokerRules game) {
        this.game = game;
        createPlayerUI(); // Erstellt die grafischen Karten für alle Spieler
        updateUI();       // Füllt die Karten mit den aktuellen Werten
    }

    // Erstellt dynamisch für jeden Spieler eine Anzeige-Box (Karte)
    private void createPlayerUI() {
        playerContainer.getChildren().clear(); // Container leeren
        clearLists();                          // Listen für neue Runde zurücksetzen
        PokerChipsPlayer[] players = game.getPlayers();

        for (int i = 0; i < players.length; i++) {
            // Haupt-Container der Spielerkarte
            VBox pBox = new VBox(8);
            pBox.setAlignment(Pos.CENTER);
            pBox.setMinWidth(160);
            pBox.setStyle("-fx-border-color: #dcdcdc; -fx-border-width: 2; -fx-border-radius: 15; -fx-background-color: white; -fx-background-radius: 15; -fx-padding: 10;");

            // Label für Rollen-Anzeige (D, SB, BB)
            Label roleLabel = new Label("");
            roleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: #7f8c8d; -fx-background-radius: 10; -fx-padding: 2 10;");
            roleLabel.setVisible(false);

            // Spielerinformationen
            Label name = new Label(players[i].getName());
            name.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: black;");

            Label chips = new Label("Chips: " + players[i].getPlayerMoney());
            chips.setStyle("-fx-text-fill: black;");

            Label bid = new Label("Bid: 0");
            bid.setStyle("-fx-text-fill: black;");

            // Eingabefeld für Raise-Beträge
            TextField rField = new TextField();
            rField.setPromptText("Raise...");
            rField.setMaxWidth(90);
            rField.setAlignment(Pos.CENTER);

            // Buttons erstellen und mit Spiellogik verknüpfen
            Button raiseBtn = createStyledButton("Raise");
            raiseBtn.setOnAction(e -> {
                try {
                    game.raise(Integer.parseInt(rField.getText()));
                    rField.clear();
                    updateUI();
                } catch(Exception ex){} // Ignoriert ungültige Eingaben
            });

            Button callBtn = createStyledButton("Call/Check");
            callBtn.setOnAction(e -> { game.callOrCheck(); updateUI(); });

            Button foldBtn = createStyledButton("Fold");
            foldBtn.setOnAction(e -> { game.fold(); updateUI(); });

            // Referenzen in Listen speichern für späteren Zugriff in updateUI()
            roleLabels.add(roleLabel);
            moneyLabels.add(chips); betLabels.add(bid); raiseFields.add(rField);
            callButtons.add(callBtn); raiseButtons.add(raiseBtn); foldButtons.add(foldBtn);
            playerBoxes.add(pBox);

            // Alle Elemente der Spielerbox hinzufügen
            pBox.getChildren().addAll(roleLabel, name, chips, bid, rField, raiseBtn, callBtn, foldBtn);
            playerContainer.getChildren().add(pBox);
        }
    }

    // Hilfsmethode für einheitliches Button-Design
    private Button createStyledButton(String text) {
        Button b = new Button(text);
        b.setMinWidth(110);
        b.setStyle("-fx-background-color: white; -fx-border-color: #ccc; -fx-border-radius: 5; -fx-text-fill: black; -fx-cursor: hand; -fx-font-weight: bold;");
        return b;
    }

    // Aktualisiert alle Anzeigen basierend auf dem aktuellen Spielzustand
    private void updateUI() {
        // Allgemeine Spielinfos setzen
        potLabel.setText(String.valueOf(game.getPot()));
        currentBetLabel.setText(String.valueOf(game.getCurrentBet()));
        if(roundLabel != null) roundLabel.setText("Round: " + game.getHandCount());

        PokerChipsPlayer[] players = game.getPlayers();
        int curr = game.getCurrentPlayerIndex(); // Wer ist gerade dran?
        int dIdx = game.getDIndex();             // Dealer-Index
        int bbIdx = game.getBBIndex();           // Big Blind Index
        int sbIdx = game.getSBIndex();           // Small Blind Index

        for (int i = 0; i < players.length; i++) {
            // Texte aktualisieren
            moneyLabels.get(i).setText("Chips: " + players[i].getPlayerMoney());
            betLabels.get(i).setText("Bid: " + players[i].getBet());

            // Rollen-Schilder (D, BB, SB) zuweisen
            if (i == dIdx) {
                roleLabels.get(i).setText("D");
                roleLabels.get(i).setVisible(true);
            } else if (i == bbIdx) {
                roleLabels.get(i).setText("BB");
                roleLabels.get(i).setVisible(true);
            } else if (i == sbIdx) {
                roleLabels.get(i).setText("SB");
                roleLabels.get(i).setVisible(true);
            } else {
                roleLabels.get(i).setVisible(false);
            }

            boolean isCurrent = (i == curr);
            boolean folded = game.isFolded(i);

            // Buttons sperren, wenn der Spieler nicht dran ist oder gefoldet hat
            callButtons.get(i).setDisable(!isCurrent || folded);
            raiseButtons.get(i).setDisable(!isCurrent || folded);
            foldButtons.get(i).setDisable(!isCurrent || folded);
            raiseFields.get(i).setDisable(!isCurrent || folded);

            // Visuelles Feedback
            if (folded) {
                playerBoxes.get(i).setOpacity(0.4); // Gefaltete Spieler ausgrauen
            } else if (isCurrent) {
                // Aktiver Spieler erhält einen blauen Rahmen
                playerBoxes.get(i).setStyle("-fx-border-color: #3498db; -fx-border-width: 3; -fx-background-color: white; -fx-border-radius: 15; -fx-background-radius: 15; -fx-padding: 10;");
                playerBoxes.get(i).setOpacity(1.0);
            } else {
                // Inaktive Spieler haben einen normalen grauen Rahmen
                playerBoxes.get(i).setStyle("-fx-border-color: #dcdcdc; -fx-border-width: 2; -fx-background-color: white; -fx-border-radius: 15; -fx-background-radius: 15; -fx-padding: 10;");
                playerBoxes.get(i).setOpacity(1.0);
            }
        }
    }

    // Leert alle Listen für einen sauberen Neuaufbau
    private void clearLists() {
        moneyLabels.clear(); betLabels.clear(); roleLabels.clear(); raiseFields.clear();
        callButtons.clear(); raiseButtons.clear(); foldButtons.clear(); playerBoxes.clear();
    }
}