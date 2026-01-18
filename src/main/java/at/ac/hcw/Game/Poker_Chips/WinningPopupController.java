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
    /*
     * Wird durch den Button "Weiter" ausgelöst.
     * Entfernt das Popup aus dem Parent (Overlay wird ausgeblendet).
     */
    @FXML
    private void handleClose() {
        // // PopupRoot hängt in einem Parent-StackPane (Scene-Root oder Overlay-Container)
        ((StackPane) popupRoot.getParent()).getChildren().remove(popupRoot);
    }

}