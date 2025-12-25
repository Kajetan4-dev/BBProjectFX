package at.ac.hcw.Game;

import at.ac.hcw.Game.Black_Jack.BlackjackRules;
import at.ac.hcw.Game.Poker_Chips.ChooseHowManyPlayerP;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.EventObject;
import java.util.Scanner;

public class ChoiceController {

    @FXML
    // 1. Added (ActionEvent event)
    // 2. Added throws IOException
    private void handleBlackjack(ActionEvent event) throws IOException {
        // Note: Make sure you point to your Blackjack FXML,
        // currently you are pointing to poker_setup.fxml for both!
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/ac/hcw/Game/Black_Jack/blackjack_setup.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Black Jack Setup");
        stage.show();
    }

    @FXML
    private void handlePoker(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/ac/hcw/Game/Poker_Chips/poker_setup.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Poker Setup");
        stage.show();
    }
}