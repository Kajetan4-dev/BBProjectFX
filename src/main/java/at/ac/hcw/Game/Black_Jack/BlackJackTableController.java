package at.ac.hcw.Game.Black_Jack;
//AllSoundEffects.button(); button soud

import at.ac.hcw.Game.AllSoundEffects;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.Pane;
import javafx.application.Platform;

public class BlackJackTableController {

    @FXML private VBox container;
    @FXML private Pane playerContainer;
    @FXML private Label roundLabel;
    @FXML private Label dealerTotalLabel;
    @FXML private HBox dealerCardContainer;
    @FXML private Button newRoundButton;

    private BlackjackRules game;
    private int roundCounter = 1;

    private List<Label> chipsLabels = new ArrayList<>();
    private List<Label> bidLabels = new ArrayList<>();
    private List<HBox> cardContainers = new ArrayList<>();
    private List<Label> totalLabels = new ArrayList<>();
    private List<TextField> bidFields = new ArrayList<>();
    private List<Button> setBidButtons = new ArrayList<>();
    private List<Button> hitButtons = new ArrayList<>();
    private List<Button> standButtons = new ArrayList<>();

    @FXML
    public void initialize() {
        playerContainer.widthProperty().addListener((obs, oldVal, newVal) -> layoutPlayers());
        playerContainer.heightProperty().addListener((obs, oldVal, newVal) -> layoutPlayers());
    }

    public void setGame(BlackjackRules game) {
        this.game = game;
        createPlayerUI();
        createBackgroud();
        updateUI();
        Platform.runLater(this::layoutPlayers);
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

        double centerX = w / 2;
        double centerY = h * 1.05; // Mittelpunkt knapp unter dem Fenster
        double radius = Math.min(w, h) * 1;

        double startAngle = 220;
        double endAngle = 320;
        double step = (n == 1) ? 0 : (endAngle - startAngle) / (n - 1);

        for (int i = 0; i < n; i++) {
            VBox box = (VBox) playerContainer.getChildren().get(i);
            double currentAngle = (n == 1) ? 270 : startAngle + (i * step);
            double angleRad = Math.toRadians(currentAngle);

            box.setLayoutX(centerX + radius * Math.cos(angleRad) - (box.getPrefWidth() / 2));
            box.setLayoutY(centerY + radius * Math.sin(angleRad) - (box.getPrefHeight() / 2));

            // Rotation zum Dealer hin
            box.setRotate(currentAngle - 270);
        }
    }

    private void createPlayerUI() {
        playerContainer.getChildren().clear();
        clearLists();
        Player[] players = game.getPlayers();

        for (int i = 0; i < players.length; i++) {
            final int index = i;

            // Spieler-VBox jetzt ohne weißen Hintergrund und Rahmen
            VBox pBox = new VBox(5);
            pBox.setAlignment(Pos.CENTER);
            pBox.setPrefSize(160, 240);
            pBox.setStyle("-fx-background-color: transparent;"); // 🔥 Komplett durchsichtig

            // Karten oben anzeigen
            HBox cardsHBox = new HBox(3);
            cardsHBox.setAlignment(Pos.CENTER);
            cardsHBox.setMinHeight(60);

            // Name und Infos in Weiß (damit man es auf dunkler Matte sieht)
            Label name = new Label(players[i].getName());
            name.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");

            Label chips = new Label("Chips: " + players[i].getChips());
            chips.setStyle("-fx-text-fill: white;");

            Label bid = new Label("Bid: 0");
            bid.setStyle("-fx-text-fill: white;");

            Label total = new Label("Total: 0");
            total.setStyle("-fx-text-fill: white;");

            TextField bidIn = new TextField();
            bidIn.setMaxWidth(85);
            bidIn.setPromptText("Einsatz...");
            bidIn.setStyle("-fx-background-radius: 5;");

            Button setBtn = createTransparentButton("Set Bid");
            setBtn.setDisable(false);
            setBtn.setOnAction(e -> setBidForPlayer(index, bidIn));

            Button hitBtn = createTransparentButton("Hit");
            Button standBtn = createTransparentButton("Stand");

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
        for (int i = 0; i < game.getPlayers().length; i++) {
            hitButtons.get(i).setDisable(!active || i != curr);
            standButtons.get(i).setDisable(!active || i != curr);
            boolean alreadyBid = game.getPlayers()[i].getBid() > 0;
            setBidButtons.get(i).setDisable(active || alreadyBid);
            bidFields.get(i).setDisable(active || alreadyBid);
        }
    }

    private void setBidForPlayer(int index, TextField field) {
        try {
            int val = Integer.parseInt(field.getText());
            if (val > 0 && val <= game.getPlayers()[index].getChips()) {
                game.getPlayers()[index].setBid(val);
                boolean allReady = true;
                for (Player pl : game.getPlayers()) if (pl.getChips() > 0 && pl.getBid() <= 0) allReady = false;
                if (allReady && !game.isRoundActive()) game.startRound();
                updateUI();
            }
        } catch (Exception e) {}
    }

    private Label createCardUI(int value, boolean hidden) {
        Label card = new Label(hidden ? "?" : String.valueOf(value));
        card.setPrefSize(35, 50);
        card.setAlignment(Pos.CENTER);
        // Karten bleiben weiß, damit sie sich vom grünen Hintergrund abheben
        card.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-border-color: #333; -fx-border-radius: 5; -fx-background-radius: 5; -fx-font-weight: bold;");
        return card;
    }

    private Button createTransparentButton(String text) {
        Button btn = new Button(text);
        btn.setMinWidth(100);
        // Button Design: Weißer Rand, weißer Text, durchsichtiger Hintergrund
        btn.setStyle("-fx-background-color: rgba(255,255,255,0.1); -fx-text-fill: white; -fx-border-color: white; -fx-border-radius: 5; -fx-cursor: hand; -fx-font-weight: bold;");
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
        // ... (Zusammenfassung wie gehabt)
    }

    @FXML private void handleNewRound() {
        roundCounter++;
        game.resetRound();
        createPlayerUI();
        layoutPlayers();
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