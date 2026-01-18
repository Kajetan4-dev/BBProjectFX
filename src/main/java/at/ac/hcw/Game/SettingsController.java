package at.ac.hcw.Game;

import at.ac.hcw.Game.Black_Jack.BlackJackTableController;
import at.ac.hcw.Game.Black_Jack.GameStateBlackjack;
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
    //Variable PBN is the parameter that shows the Settings in which Screen whe are on,
    //0 = Poker choose players
    //1 = Poker Game
    //2 = Black Jack choose players
    //3 = Black Jack Game
    private int PBN;
    @FXML
    private Slider volumeSld;


    //Initializes the sound
    @FXML
    public void initialize() {
        volumeSld.setMin(0.0);
        volumeSld.setMax(1.0);

        volumeSld.valueProperty().bindBidirectional(SoundManager.volumeProperty());
    }

    //Allows the PBN to be set
    public void setPBN(int PBN) {
        this.PBN = PBN;

        //This removes new game when not in a game
        if (neuesSpielBtn != null && PBN == 0 || neuesSpielBtn != null && PBN == 2) {
            ((Pane) neuesSpielBtn.getParent()).getChildren().remove(neuesSpielBtn);
        }
    }

    @FXML
    private void neuesSpiel() {
        AllSoundEffects.button();

        //Logic for starting a new game for Poker Chips
        if (PBN == 1) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/at/ac/hcw/Game/Poker_Chips/poker_setup.fxml"));
                Stage stage = (Stage) volumeSld.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Casino Game Selection");
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Logik for starting a new game in Black Jack
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/at/ac/hcw/Game/Black_Jack/blackjack_setup.fxml"));
            Stage stage = (Stage) neuesSpielBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Casino Game Selection");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Logic for going back to the selection of Poker and Black Jack
    @FXML
    private void zurückZumHauptmenü() {
        AllSoundEffects.button();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/at/ac/hcw/Game/Choice.fxml"));
            Stage stage = (Stage) volumeSld.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Casino Game Selection");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Logic for the "normal" Zurück btn
    @FXML
    private void zurückLast() {
        AllSoundEffects.button();

        //for the player selection in Poker
        if (PBN == 0) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/at/ac/hcw/Game/Poker_Chips/poker_setup.fxml"));
                Stage stage = (Stage) volumeSld.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Casino Game Selection");
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //for going back to the game Poker Chips
        if (PBN == 1) {
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/ac/hcw/Game/Poker_Chips/poker_table.fxml"));
                Parent root = loader.load();

                PokerTableController tableController = loader.getController();
                //Very impoetant Grabs the last saved state in the game and then
                //reuses it
                tableController.setGame(GameStatePoker.getPokerGame());


                Stage stage = (Stage) neuesSpielBtn.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Poker Table");
                stage.show();

            }catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error: Could not find poker_table.fxml");
            }
        }
        //Goes back to the Black Jack player selection Screen
        if (PBN == 2) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/at/ac/hcw/Game/Black_Jack/blackjack_setup.fxml"));
                Stage stage = (Stage) volumeSld.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Casino Game Selection");
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Goes Back to Black Jack
        if (PBN == 3) {
            if (GameStateBlackjack.hasSavedGame()) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/ac/hcw/Game/Black_Jack/blackjack_table.fxml"));
                    Parent root = loader.load();

                    // Get controller and restore game
                    BlackJackTableController controller = loader.getController();
                    controller.setGame(GameStateBlackjack.getSavedGame());

                    Stage stage = (Stage) neuesSpielBtn.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Blackjack Table");
                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
