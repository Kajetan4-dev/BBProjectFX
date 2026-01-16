package at.ac.hcw.Game.Black_Jack;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.ArrayList;
import java.util.List;

public class BlackJackTableController {

    private BlackjackRules game;
    private int roundCounter = 1;

    @FXML private Label roundLabel;
    @FXML private HBox playerContainer;
    @FXML private Label dealerTotalLabel;
    @FXML private HBox dealerCardContainer;
    @FXML private Button newRoundButton;

    private List<Label> chipsLabels = new ArrayList<>();
    private List<Label> bidLabels = new ArrayList<>();
    private List<HBox> cardContainers = new ArrayList<>();
    private List<Label> totalLabels = new ArrayList<>();
    private List<TextField> bidFields = new ArrayList<>();
    private List<Button> setBidButtons = new ArrayList<>();
    private List<Button> hitButtons = new ArrayList<>();
    private List<Button> standButtons = new ArrayList<>();

    public void setGame(BlackjackRules game) {
        this.game = game;
        createPlayerUI();
        updateUI();
    }

    private void createPlayerUI() {
        playerContainer.getChildren().clear();
        clearLists();
        Player[] players = game.getPlayers();

        for (int i = 0; i < players.length; i++) {
            final int index = i;
            VBox pBox = new VBox(5);
            pBox.setAlignment(Pos.CENTER);
            pBox.setMinWidth(170);
            pBox.setStyle("-fx-border-color: #dcdcdc; -fx-border-width: 2; -fx-border-radius: 15; -fx-background-color: #fcfcfc; -fx-background-radius: 15; -fx-padding: 10;");

            Label name = new Label(players[index].getName());
            name.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: black;");

            Label chips = new Label("Chips: " + players[index].getChips());
            chips.setStyle("-fx-text-fill: black;");

            Label bid = new Label("Bid: 0");
            bid.setStyle("-fx-text-fill: black;");

            HBox cardsHBox = new HBox(3);
            cardsHBox.setAlignment(Pos.CENTER);
            cardsHBox.setMinHeight(60);

            Label total = new Label("Total: 0");
            total.setStyle("-fx-text-fill: black;");

            TextField bidIn = new TextField();
            bidIn.setPromptText("Einsatz...");
            bidIn.setMaxWidth(85);
            bidIn.setStyle("-fx-alignment: center; -fx-text-fill: black;");

            Button setBtn = createBlackButton("Set Bid");
            setBtn.setDisable(false);
            setBtn.setOnAction(e -> setBidForPlayer(index, bidIn));

            Button hitBtn = createBlackButton("Hit");
            Button standBtn = createBlackButton("Stand");

            hitBtn.setOnAction(e -> { game.hit(); updateUI(); checkIfRoundOver(); });
            standBtn.setOnAction(e -> { game.stand(); updateUI(); checkIfRoundOver(); });

            chipsLabels.add(chips); bidLabels.add(bid); cardContainers.add(cardsHBox);
            totalLabels.add(total); bidFields.add(bidIn); setBidButtons.add(setBtn);
            hitButtons.add(hitBtn); standButtons.add(standBtn);

            pBox.getChildren().addAll(name, chips, bid, cardsHBox, total, bidIn, setBtn, hitBtn, standBtn);
            playerContainer.getChildren().add(pBox);
        }
    }

    private void updateUI() {
        roundLabel.setText("Round: " + roundCounter);
        Player[] players = game.getPlayers();
        Dealer dealer = game.getDealer();

        for (int i = 0; i < players.length; i++) {
            chipsLabels.get(i).setText("Chips: " + players[i].getChips());
            bidLabels.get(i).setText("Bid: " + players[i].getBid());

            cardContainers.get(i).getChildren().clear();
            if (players[i].getBid() > 0) {
                for (int val : players[i].getCards()) {
                    if (val != 0) cardContainers.get(i).getChildren().add(createCardUI(val, false));
                }
                totalLabels.get(i).setText("Total: " + BlackjackRules.calculatehand(players[i].getCards()));
            }
        }

        dealerCardContainer.getChildren().clear();
        int[] dCards = dealer.getCards();
        for (int j = 0; j < dCards.length; j++) {
            if (dCards[j] != 0) {
                boolean hidden = (game.isRoundActive() && j == 1 && dealer.isCardHidden());
                dealerCardContainer.getChildren().add(createCardUI(dCards[j], hidden));
            }
        }
        dealerTotalLabel.setText(game.isRoundActive() ? "Dealer Total: ?" : "Dealer Total: " + BlackjackRules.calculatehand(dCards));

        updateButtons();
    }

    private void updateButtons() {
        int curr = game.getCurrentPlayerIndex();
        boolean active = game.isRoundActive();
        Player[] players = game.getPlayers();

        for (int i = 0; i < players.length; i++) {
            if (!active) {
                hitButtons.get(i).setDisable(true);
                standButtons.get(i).setDisable(true);
            } else {
                hitButtons.get(i).setDisable(i != curr);
                standButtons.get(i).setDisable(i != curr);
            }

            boolean isBroke = players[i].getChips() <= 0;
            boolean alreadyBid = players[i].getBid() > 0;

            setBidButtons.get(i).setDisable(active || alreadyBid || isBroke);
            bidFields.get(i).setDisable(active || alreadyBid || isBroke);

            if (isBroke && !alreadyBid) {
                bidFields.get(i).setPromptText("PLEITE");
            }
        }
    }

    private void setBidForPlayer(int index, TextField field) {
        try {
            int val = Integer.parseInt(field.getText());
            Player p = game.getPlayers()[index];
            if (val > 0 && val <= p.getChips()) {
                p.setBid(val);

                boolean allReady = true;
                for (Player pl : game.getPlayers()) {
                    if (pl.getChips() > 0 && pl.getBid() <= 0) {
                        allReady = false;
                        break;
                    }
                }

                if (allReady && !game.isRoundActive()) game.startRound();
                updateUI();
            }
        } catch (Exception e) {}
    }

    private Label createCardUI(int value, boolean hidden) {
        Label card = new Label(hidden ? "?" : String.valueOf(value));
        card.setPrefSize(35, 50);
        card.setAlignment(Pos.CENTER);
        card.setStyle(hidden ?
                "-fx-background-color: #2c3e50; -fx-text-fill: white; -fx-border-color: black; -fx-border-radius: 5; -fx-background-radius: 5; -fx-font-weight: bold;" :
                "-fx-background-color: white; -fx-text-fill: black; -fx-border-color: #888; -fx-border-radius: 5; -fx-background-radius: 5; -fx-font-weight: bold;");
        return card;
    }

    private Button createBlackButton(String text) {
        Button btn = new Button(text);
        btn.setMinWidth(100);
        btn.setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-cursor: hand;");
        btn.setDisable(true);
        return btn;
    }

    private void checkIfRoundOver() {
        if (!game.isRoundActive()) {
            newRoundButton.setVisible(true);
            showWinnerAlert();
        }
    }

    private void showWinnerAlert() {
        StringBuilder msg = new StringBuilder("Round " + roundCounter + " Results:\n\n");
        int dTotal = BlackjackRules.calculatehand(game.getDealer().getCards());
        for (Player p : game.getPlayers()) {
            if (p.getBid() <= 0) continue;
            int pTotal = BlackjackRules.calculatehand(p.getCards());
            msg.append(p.getName()).append(": ");
            if (pTotal > 21) msg.append("Bust! -").append(p.getBid()).append(" Chips");
            else if (dTotal > 21 || pTotal > dTotal) msg.append("Won! +").append(p.getBid()).append(" Chips");
            else if (pTotal < dTotal) msg.append("Too Low! -").append(p.getBid()).append(" Chips");
            else msg.append("Push (Tie)");
            msg.append("\n");
        }
        showAlert("Round Over", msg.toString());
    }

    @FXML private void handleNewRound() {
        roundCounter++;
        game.resetRound();
        createPlayerUI();
        newRoundButton.setVisible(false);
        updateUI();
    }

    private void clearLists() {
        chipsLabels.clear(); bidLabels.clear(); cardContainers.clear();
        totalLabels.clear(); bidFields.clear(); setBidButtons.clear();
        hitButtons.clear(); standButtons.clear();
    }

    private void showAlert(String title, String content) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title); a.setHeaderText(null); a.setContentText(content);
        a.showAndWait();
    }
}