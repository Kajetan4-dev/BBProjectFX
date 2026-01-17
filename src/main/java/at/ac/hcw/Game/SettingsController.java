package at.ac.hcw.Game;

import at.ac.hcw.Game.Poker_Chips.PokerTableController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsController {

    @FXML
    private Button neuesSpielBtn;
    private int PBN;
    @FXML
    private Slider volumeSld;

    private static boolean fromBlackjack = false;

    public static void setFromBlackjack(boolean value) {
        fromBlackjack = value;
    }

    @FXML
    public void initialize() {
        volumeSld.setMin(0.0);
        volumeSld.setMax(1.0);

        // 🔗 TWO-WAY BINDING
        volumeSld.valueProperty().bindBidirectional(
                SoundManager.volumeProperty()
        );
    }

    public void setPBN(int PBN) {
        this.PBN = PBN;

        // You can react immediately if needed
        if (neuesSpielBtn != null && PBN == 0 || neuesSpielBtn != null && PBN == 2) {
            ((Pane) neuesSpielBtn.getParent()).getChildren().remove(neuesSpielBtn);
        }
    }

    @FXML
    private void neuesSpiel() {
        AllSoundEffects.button();
        if (PBN == 1) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/at/ac/hcw/Game/Poker_Chips/poker_setup.fxml"));
                Stage stage = (Stage) volumeSld.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Casino Game Selection");
                stage.show();

                fromBlackjack = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/at/ac/hcw/Game/Black_Jack/blackjack_setup.fxml"));
            Stage stage = (Stage) volumeSld.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Casino Game Selection");
            stage.show();

            fromBlackjack = false;
        } catch (IOException e) {
            e.printStackTrace();
        }

        // your code
    }

    @FXML
    private void zurückZumHauptmenü() {
        AllSoundEffects.button();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/at/ac/hcw/Game/Choice.fxml"));
            Stage stage = (Stage) volumeSld.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Casino Game Selection");
            stage.show();

            fromBlackjack = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void zurückLast() {
        AllSoundEffects.button();
        if (PBN == 0) {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/at/ac/hcw/Game/Poker_Chips/poker_setup.fxml"));
                    Stage stage = (Stage) volumeSld.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Casino Game Selection");
                    stage.show();

                    fromBlackjack = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        if (PBN == 1) {
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/ac/hcw/Game/Poker_Chips/poker_table.fxml"));
                Parent root = loader.load();

                PokerTableController tableController = loader.getController();
                tableController.setGame(GameStatePoker.getPokerGame());

                // Get the current window (Stage) and set the new Scene
                Stage stage = (Stage) neuesSpielBtn.getScene().getWindow();
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
        if(PBN == 2){
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/at/ac/hcw/Game/Black_Jack/blackjack_setup.fxml"));
                Stage stage = (Stage) volumeSld.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Casino Game Selection");
                stage.show();

                fromBlackjack = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
