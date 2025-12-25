package at.ac.hcw.Game;

import at.ac.hcw.Game.Black_Jack.BlackjackRules;
import at.ac.hcw.Game.Poker_Chips.ChooseHowManyPlayerP;
import javafx.fxml.FXML;

import java.util.Scanner;

public class ChoiceController {

    @FXML
    private void handleBlackjack() {
        System.out.println("Switching to Blackjack...");
        // In JavaFX, you usually load a new FXML stage here
        BlackjackRules.startGame();
    }

    @FXML
    private void handlePoker() {
        System.out.println("Switching to Poker...");
        // Note: You may need to modify ChooseHowManyPlayerP
        // to no longer require a 'Scanner' object
        new ChooseHowManyPlayerP().run();
    }
}