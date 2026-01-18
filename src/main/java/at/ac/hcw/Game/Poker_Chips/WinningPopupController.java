package at.ac.hcw.Game.Poker_Chips;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/*
 * Controller für das Winning Popup (winning_popup.fxml).
 * Aufgaben:
 * - Gewinnername + Chips anzeigen
 * - Popup bei "Weiter" wieder entfernen
 */
public class WinningPopupController {

    @FXML private Label winnerLabel; // zeigt den Namen des Gewinners
    @FXML private Label chipsLabel; // zeigt "+X Chips"

    // Referenz auf das geladene Popup-Root-Node, damit wir es entfernen können
    private StackPane popupRoot;

    /*
     * Übergibt dem Controller eine Referenz auf das Popup-Root,
     * damit handleClose() es später entfernen kann.
     */
    public void setRoot(StackPane popupRoot) {
        this.popupRoot = popupRoot;
    }

    //Setzt die Daten (Gewinnername, Chips) in die Labels.
    public void setData(String winnerName, int chipsWon) {
        winnerLabel.setText(winnerName);
        chipsLabel.setText("+" + chipsWon + " Chips");
    }

    @FXML
    private void handleClose() {
        // Popup vom Parent entfernen (Overlay ausblenden)
        ((StackPane) popupRoot.getParent()).getChildren().remove(popupRoot);
    }

    public void setResult(String winnerName, int chipsWon) {
    }
}