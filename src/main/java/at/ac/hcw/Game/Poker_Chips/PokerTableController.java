package at.ac.hcw.Game.Poker_Chips;

import at.ac.hcw.Game.AllSoundEffects;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PokerTableController {

    @FXML private Label potLabel;
    @FXML private Label currentBetLabel;
    @FXML private Label currentPlayerLabel;
    @FXML private TextField raiseField;

    private PokerRules game;


    public void setGame(PokerRules game){
        this.game = game;
        updateUI();
    }

    @FXML
    private void handleCall(){
        AllSoundEffects.button();
        game.callOrCheck();
        updateUI();
    }

    @FXML
    private void handleFold(){
        AllSoundEffects.button();
        game.fold();
        updateUI();
    }

    @FXML
    private void handleRaise(){
        AllSoundEffects.button();
        int amount = 0;
        try{
            amount = Integer.parseInt(raiseField.getText());
        }catch (Exception ignored){}
        game.raise(amount);
        raiseField.clear();
        updateUI();
    }

    private void updateUI(){
        potLabel.setText("Pot: " + game.getPot());
        currentBetLabel.setText("Current Bet: " + game.getCurrentBet());
        currentPlayerLabel.setText("Current Player: " + game.getCurrentPlayer().getName());
    }
}
