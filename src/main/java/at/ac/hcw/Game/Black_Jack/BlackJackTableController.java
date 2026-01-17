package at.ac.hcw.Game.Black_Jack;
//AllSoundEffects.button(); button soud

import at.ac.hcw.Game.AllSoundEffects;
import at.ac.hcw.Game.SettingsController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.Pane;
import javafx.application.Platform;
import javafx.stage.Stage;

public class BlackJackTableController {

    @FXML private VBox container;
    private BlackjackRules game;
    private int roundCounter = 1;

    @FXML private Pane playerContainer;
    @FXML private Label roundLabel;
    @FXML private Label dealerTotalLabel;
    @FXML private HBox dealerCardContainer;
    @FXML private Button newRoundButton;

    @FXML
    public void initialize() {
        playerContainer.widthProperty().addListener((obs, oldVal, newVal) -> layoutPlayers());
        playerContainer.heightProperty().addListener((obs, oldVal, newVal) -> layoutPlayers());
    }

    @FXML
    private void handleNewRound() {
        roundCounter++;
        game.resetRound();
        createPlayerUI();
        layoutPlayers(); // 👈 neu
        newRoundButton.setVisible(false);
        updateUI();
    }

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
        createBackgroud();
        updateUI();
        Platform.runLater(this::layoutPlayers);
    }

    @FXML
    private void handleGoToSettings() throws IOException {
        AllSoundEffects.button();

        SettingsController.setFromBlackjack(true);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/ac/hcw/Game/Settings.fxml"));
        Parent root = loader.load();

        SettingsController controller = loader.getController();
        controller.setPBN(2);

        Stage stage = (Stage) container.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Settings");
        stage.show();
    }

    private void createBackgroud() {
        try {
            container.setStyle(
                    "-fx-background-image: url('" +
                            getClass().getResource("/at/ac/hcw/Game/Media/images/Blackjackmatte.jpg").toExternalForm() +
                            "');" +
                            "-fx-background-repeat: no-repeat;" +
                            "-fx-background-size: cover;" +
                            "-fx-background-position: center center;"
            );
        } catch (Exception e) {
            System.err.println("Hintergrundbild fehlt.");
        }
    }

    /**
     * REPARIERTE LOGIK: Erzeugt einen sauberen Fächer-Bogen von unten.
     */
    private void layoutPlayers() {
        int n = playerContainer.getChildren().size();
        if (n == 0) return;

        double w = playerContainer.getWidth();
        double h = playerContainer.getHeight();
        if (w <= 0 || h <= 0) return;

        // Ellipse radii
        double radiusX = w * 0.4; // horizontal stretch
        double radiusY = h * -0.35; // vertical stretch

        double centerX = w / 2;
        double centerY = h / 10; // dealer is at center

        // Spread players evenly around the ellipse (semi-circle or full circle)
        double endAngle = 180 + 45; // left
        double startAngle = 360 - 45;   // right
        double step = n == 1 ? 0 : (endAngle - startAngle) / (n - 1);

        for (int i = 0; i < n; i++) {
            VBox box = (VBox) playerContainer.getChildren().get(i);
            double angle = Math.toRadians(startAngle + i * step);

            // Ellipse position around dealer
            double x = centerX + radiusX * Math.cos(angle) - box.getPrefWidth() / 2;
            double y = centerY + radiusY * Math.sin(angle) - box.getPrefHeight() / 2;

            box.setLayoutX(x);
            box.setLayoutY(y);

            // Optional rotation for a fan effect around dealer
            double rotation = -30 + 60.0 * i / (n - 1);
            box.setRotate(rotation);
        }
    }

    private void createPlayerUI() {
        playerContainer.getChildren().clear();
        clearLists();
        Player[] players = game.getPlayers();

        for (int i = 0; i < players.length; i++) {
            final int index = i;

            VBox pBox = new VBox(5);
            pBox.setAlignment(Pos.CENTER);
            pBox.setPrefSize(170, 260);
            pBox.setStyle(
                    "-fx-border-color: #dcdcdc; -fx-border-width: 2; -fx-border-radius: 15;" +
                            "-fx-background-color: #fcfcfc; -fx-background-radius: 15; -fx-padding: 10;"
            );


            // Karten oben anzeigen
            HBox cardsHBox = new HBox(3);
            cardsHBox.setAlignment(Pos.CENTER);
            cardsHBox.setMinHeight(10);

            // Name und Infos in Weiß (damit man es auf dunkler Matte sieht)
            Label name = new Label(players[i].getName());
            name.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");

            Label chips = new Label("Chips: " + players[i].getChips());
            chips.setStyle("-fx-text-fill: black;");

            Label bid = new Label("Bid: 0");
            bid.setStyle("-fx-text-fill: black;");

            Label total = new Label("Total: 0");
            total.setStyle("-fx-text-fill: black;");

            TextField bidIn = new TextField();
            bidIn.setMaxWidth(85);
            bidIn.setPromptText("Einsatz...");
            bidIn.setStyle("-fx-background-radius: 5;");

            Button setBtn = createBlackButton("Set Bid");
            setBtn.setDisable(false);
            setBtn.setOnAction(e -> setBidForPlayer(index, bidIn));

            Button hitBtn = createBlackButton("Hit");
            Button standBtn = createBlackButton("Stand");

            hitBtn.setOnAction(e -> { game.hit(); updateUI(); checkIfRoundOver(); AllSoundEffects.button();});
            standBtn.setOnAction(e -> { game.stand(); updateUI(); checkIfRoundOver(); AllSoundEffects.button();});

            chipsLabels.add(chips); bidLabels.add(bid); cardContainers.add(cardsHBox);
            totalLabels.add(total); bidFields.add(bidIn); setBidButtons.add(setBtn);
            hitButtons.add(hitBtn); standButtons.add(standBtn);

            // Reihenfolge: Erst Karten, dann Name/Chips, dann Buttons
            pBox.getChildren().addAll(cardsHBox, total, name, chips, bid, bidIn, setBtn, hitBtn, standBtn);
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