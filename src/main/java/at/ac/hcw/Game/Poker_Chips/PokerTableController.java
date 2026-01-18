package at.ac.hcw.Game.Poker_Chips;

import at.ac.hcw.Game.AllSoundEffects;
import at.ac.hcw.Game.GameStatePoker;
import at.ac.hcw.Game.SettingsController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.StackPane;

public class PokerTableController {

    @FXML private Label potLabel;
    @FXML private Label currentBetLabel;
    @FXML private Label roundLabel;
//    @FXML private HBox playerContainer;
    @FXML private VBox leftColumn;
    @FXML private VBox rightColumn;

    private PokerRules game;

    private List<Label> nameLabels = new ArrayList<>();
    private List<Label> moneyLabels = new ArrayList<>();
    private List<Label> betLabels = new ArrayList<>();
    private List<Label> roleLabels = new ArrayList<>();
    private List<TextField> raiseFields = new ArrayList<>();
    private List<Button> callButtons = new ArrayList<>();
    private List<Button> raiseButtons = new ArrayList<>();
    private List<Button> foldButtons = new ArrayList<>();
    private List<VBox> playerBoxes = new ArrayList<>();

    public void setGame(PokerRules game) {
        this.game = game;
        createPlayerUI();
        updateUI();
    }

    private void createPlayerUI() {
        leftColumn.getChildren().clear();
        rightColumn.getChildren().clear();
        clearLists();

        PokerChipsPlayer[] players = game.getPlayers();

        for (int i = 0; i < players.length; i++) {

            VBox pBox = new VBox(10);
            pBox.setAlignment(Pos.CENTER_LEFT);
            pBox.setMinWidth(260);
            pBox.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-padding: 10; -fx-background-color: #f5f0e6;");

            Label roleLabel = new Label("");
            roleLabel.setStyle(
                    "-fx-font-weight: bold;" +
                            "-fx-text-fill: white;" +
                            "-fx-background-color: #7f8c8d;" +
                            "-fx-background-radius: 10;" +
                            "-fx-padding: 2 8;"
            );
            roleLabel.setVisible(false);

            Label name = new Label(players[i].getName());
            name.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black;");


            Label chips = new Label("Chips: " + players[i].getPlayerMoney());
            chips.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black;");

            Label bet = new Label("Bet: 0");
            bet.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: black;");


            HBox headerRow = new HBox(8, roleLabel, name);
            headerRow.setAlignment(Pos.CENTER_LEFT);

            VBox moneyAndBet = new VBox(4, chips, bet);
            moneyAndBet.setAlignment(Pos.CENTER_LEFT);

            TextField betField = new TextField();
            betField.setPromptText("Amount...");
            betField.setMaxWidth(80);
            betField.setAlignment(Pos.CENTER);

            Button raiseBtn = createStyledButton("Raise");
            Button callBtn  = createStyledButton("Call");
            Button foldBtn  = createStyledButton("Fold");

            raiseBtn.setOnAction(e -> {
                try {
                    AllSoundEffects.button();
                    game.raise(Integer.parseInt(betField.getText()));
                    betField.clear();
                    updateUI();
                } catch (Exception ex) {}
            });

            callBtn.setOnAction(e -> {AllSoundEffects.button(); game.callOrCheck(); updateUI(); });
            foldBtn.setOnAction(e -> {AllSoundEffects.button(); game.fold(); updateUI(); });

            HBox buttonsRow = new HBox(6, betField, raiseBtn, callBtn, foldBtn);
            buttonsRow.setAlignment(Pos.CENTER_LEFT);

            nameLabels.add(name);
            roleLabels.add(roleLabel);
            moneyLabels.add(chips);
            betLabels.add(bet);
            raiseFields.add(betField);
            callButtons.add(callBtn);
            raiseButtons.add(raiseBtn);
            foldButtons.add(foldBtn);
            playerBoxes.add(pBox);

            pBox.getChildren().addAll(headerRow, moneyAndBet, buttonsRow);

            if (i >= 3) {
                rightColumn.getChildren().add(pBox);
            } else {
                leftColumn.getChildren().add(pBox);
            }
        }
    }

    @FXML
    private void handleGoToSettings() throws IOException {
        AllSoundEffects.button();
        //Saves Game State
        GameStatePoker.setPokerGame(game);
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/at/ac/hcw/Game/Settings.fxml")
        );
        Parent root = loader.load();

        SettingsController controller = loader.getController();
        controller.setPBN(1); // or 1, 2, etc.

        Stage stage = (Stage) currentBetLabel.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Settings");
        stage.show();
    }

    private Button createStyledButton(String text) {
        Button b = new Button(text);
        b.setMinWidth(110);
        b.setStyle("-fx-border-color: black;" +
                        "-fx-border-radius: 5;" +
                        "-fx-text-fill: black;" +
                        "-fx-cursor: hand;" +
                        "-fx-font-weight: bold;"
        );
        return b;
    }

    private void updateUI() {
        potLabel.setText(String.valueOf(game.getPot()));
        currentBetLabel.setText(String.valueOf(game.getCurrentBet()));
        if (roundLabel != null) roundLabel.setText("Round: " + game.getHandCount());

        PokerChipsPlayer[] players = game.getPlayers();
        int curr = game.getCurrentPlayerIndex();
        int dIdx = game.getDIndex();
        int bbIdx = game.getBBIndex();
        int sbIdx = game.getSBIndex();

        for (int i = 0; i < players.length; i++) {

            nameLabels.get(i).setText(players[i].getName());
            moneyLabels.get(i).setText("Chips: " + players[i].getPlayerMoney());
            betLabels.get(i).setText("Bet: " + players[i].getBet());

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

            callButtons.get(i).setDisable(!isCurrent || folded);
            raiseButtons.get(i).setDisable(!isCurrent || folded);
            foldButtons.get(i).setDisable(!isCurrent || folded);
            raiseFields.get(i).setDisable(!isCurrent || folded);

            if (folded) {
                playerBoxes.get(i).setOpacity(0.4);
            } else if (isCurrent) {
                playerBoxes.get(i).setStyle("-fx-border-color: blue; -fx-border-width: 2; -fx-padding: 10; -fx-background-color: #f5f0e6;"
                );
                playerBoxes.get(i).setOpacity(1.0);
            } else {
                playerBoxes.get(i).setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-padding: 10; -fx-background-color: #f5f0e6;");
                playerBoxes.get(i).setOpacity(1.0);
            }
        }
        if (game.hasRoundEnded()){
            showWinningPopupOverlay();
            game.clearRoundEndFlag();
        }
    }

    private void clearLists() {
        nameLabels.clear();
        moneyLabels.clear();
        betLabels.clear();
        roleLabels.clear();
        raiseFields.clear();
        callButtons.clear();
        raiseButtons.clear();
        foldButtons.clear();
        playerBoxes.clear();
    }

    private void showWinnerAlert() {

        PokerChipsPlayer winner = game.getRoundWinner();
        int pot = game.getPot();

        if (winner == null) return;

        showPokerWinningPopup(winner.getName(), pot);
    }

    private void showPokerWinningPopup(String winnerName, int chipsWon) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/at/ac/hcw/Game/Poker_Chips/poker_winning_popup.fxml"
                    )
            );
            Parent root = loader.load();

            at.ac.hcw.Game.Poker_Chips.WinningPopupController controller = loader.getController();
            controller.setResult(winnerName, chipsWon);

            Stage stage = new Stage();
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setTitle("Poker Result");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void  showWinningPopupOverlay() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/at/ac/hcw/Game/Poker_Chips/winning_popup.fxml")
            );

            StackPane popup = loader.load();
            WinningPopupController controller = loader.getController();

            int winnerIndex = game.getLastWinnerIndex();
            int chipsWon = game.getLastPotWon();
            String winnerName = game.getPlayers()[winnerIndex].getName();

            controller.setRoot(popup);
            controller.setData(winnerName, chipsWon);

            StackPane root = (StackPane) potLabel.getScene().getRoot();
            root.getChildren().add(popup);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
