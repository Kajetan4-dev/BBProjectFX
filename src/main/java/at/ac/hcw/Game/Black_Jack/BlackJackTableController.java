package at.ac.hcw.Game.Black_Jack;
//AllSoundEffects.button(); button soud

import at.ac.hcw.Game.AllSoundEffects;
import at.ac.hcw.Game.SettingsController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.io.IOException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.Pane;
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
        if(game != null) {
            this.game = game;
        }
        else if (GameStateBlackjack.hasSavedGame()) {
            this.game = GameStateBlackjack.getSavedGame();
        }

        createPlayerUI();
        createBackground();
        updateUI();
        layoutPlayers();
        Platform.runLater(this::layoutPlayers);
    }

    // ------------------------- CHIP STACKING LOGIC -------------------------

    /**
     * Wählt das Chip-Bild basierend auf dem Wert aus.
     */
    private String getChipColorByAmount(int amount) {
        if (amount >= 500) return "/at/ac/hcw/Game/Media/images/BlackChipsTransparent.png";
        if (amount >= 100) return "/at/ac/hcw/Game/Media/images/BlueChipsTransparent.png";
        if (amount >= 50) return "/at/ac/hcw/Game/Media/images/RedChipsTransparent.png";
        return "/at/ac/hcw/Game/Media/images/GreenChipsTransparent.png";
    }

    /**
     * Erstellt einen visuellen Stapel aus Chips.
     */
    private StackPane createChipStack(int amount) {
        StackPane stack = new StackPane();
        stack.setAlignment(Pos.BOTTOM_CENTER);

        int remaining = amount;
        int chipCount = 0;

        int[] values = {500, 100, 50, 10};
        String[] paths = {
                "/at/ac/hcw/Game/Media/images/BlackChipsTransparent.png",
                "/at/ac/hcw/Game/Media/images/BlueChipsTransparent.png",
                "/at/ac/hcw/Game/Media/images/RedChipsTransparent.png",
                "/at/ac/hcw/Game/Media/images/GreenChipsTransparent.png"
        };

        for (int i = 0; i < values.length; i++) {
            while (remaining >= values[i]) {
                ImageView chip = createIcon(paths[i]);
                chip.setTranslateY(-chipCount * 3); // Versatz für Stacking-Effekt
                stack.getChildren().add(chip);
                remaining -= values[i];
                chipCount++;
                if (chipCount > 12) break; // Limit pro Stapel
            }
        }
        return stack;
    }

    private ImageView createIcon(String path) {
        try {
            Image img = new Image(getClass().getResource(path).toExternalForm());
            ImageView view = new ImageView(img);
            view.setFitWidth(25);
            view.setFitHeight(25);
            view.setPreserveRatio(true);
            return view;
        } catch (Exception e) {
            return new ImageView();
        }
    }

    // ------------------------- SETTINGS -------------------------
    @FXML
    private void handleGoToSettings() throws IOException {
        AllSoundEffects.button();
        GameStateBlackjack.saveGame(game);
        SettingsController.setFromBlackjack(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/ac/hcw/Game/Settings.fxml"));
        Parent root = loader.load();
        SettingsController controller = loader.getController();
        controller.setPBN(3);
        Stage stage = (Stage) container.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Settings");
        stage.show();
    }

    // ------------------------- BACKGROUND -------------------------
    private void createBackground() {
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

    // ------------------------- PLAYER LAYOUT -------------------------
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

    // ------------------------- PLAYER UI -------------------------
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
            name.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: black;");

            // Container für Chips-Stapel
            HBox chipsHBox = new HBox(5);
            chipsHBox.setAlignment(Pos.CENTER);
            Label chipsLabel = new Label();
            chipsLabel.setStyle("-fx-text-fill: black;");

            // Container für Bid-Stapel
            HBox bidHBox = new HBox(5);
            bidHBox.setAlignment(Pos.CENTER);
            Label bidLabel = new Label();
            bidLabel.setStyle("-fx-text-fill: black;");

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

            chipsLabels.add(chipsLabel); bidLabels.add(bidLabel); cardContainers.add(cardsHBox);
            totalLabels.add(total); bidFields.add(bidIn); setBidButtons.add(setBtn);
            hitButtons.add(hitBtn); standButtons.add(standBtn);

            // Reihenfolge: Erst Karten, dann Name/Chips, dann Buttons
            pBox.getChildren().addAll(cardsHBox, total, name, chipsHBox, bidHBox, bidIn, setBtn, hitBtn, standBtn);
            playerContainer.getChildren().add(pBox);
        }
    }

    private Button createBlackButton(String text) {
        Button btn = new Button(text);
        btn.setMinWidth(100);
        btn.setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-cursor: hand;");
        btn.setDisable(true);
        return btn;
    }

    private void updateUI() {
        roundLabel.setText("Round: " + roundCounter);
        Player[] players = game.getPlayers();
        Dealer dealer = game.getDealer();

        for (int i = 0; i < players.length; i++) {
            VBox pBox = (VBox) playerContainer.getChildren().get(i);
            HBox chipsHBox = (HBox) pBox.getChildren().get(3); // Chips-Container
            HBox bidHBox = (HBox) pBox.getChildren().get(4);   // Bid-Container

            // Chips Stapel aktualisieren
            chipsHBox.getChildren().clear();
            chipsHBox.getChildren().addAll(createChipStack(players[i].getChips()), chipsLabels.get(i));
            chipsLabels.get(i).setText("Chips: " + players[i].getChips());

            // Bid Stapel aktualisieren
            bidHBox.getChildren().clear();
            if (players[i].getBid() > 0) {
                bidHBox.getChildren().addAll(createChipStack(players[i].getBid()), bidLabels.get(i));
            }
            bidLabels.get(i).setText("Bid: " + players[i].getBid());

            cardContainers.get(i).getChildren().clear();
            if (players[i].getBid() > 0) {
                for (int val : players[i].getCards()) {
                    if (val != -1) cardContainers.get(i).getChildren().add(createCardUI(val, false));
                }
                totalLabels.get(i).setText("Total: " + BlackjackRules.calculatehand(players[i].getCards()));
            }
        }

        dealerCardContainer.getChildren().clear();
        int[] dCards = dealer.getCards();
        for (int j = 0; j < dCards.length; j++) {
            if (dCards[j] != -1) {
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
            Player p = game.getPlayers()[index];
            if (val > 0 && val <= p.getChips()) {
                p.setBid(val);

                boolean allReady = true;
                for (Player pl : game.getPlayers()) {
                    if (pl.getChips() > 0 && pl.getBid() <= 0) allReady = false;
                }
                if (allReady && !game.isRoundActive()) game.startRound();
                GameStateBlackjack.saveGame(game);
                updateUI();
            }
        } catch (Exception e) {}
    }

    private Node createCardUI(int value, boolean hidden) {
        double w = 50;
        double h = 80;

        if (hidden){
            Label card = new Label("?");
            card.setPrefSize(w,h);
            card.setAlignment(Pos.CENTER);
            card.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-border-color: #333; -fx-border-radius: 5; -fx-background-radius: 5; -fx-font-weight: bold;");
            return card;
        }

        int col = value % 13;
        int row = value / 13;

        return CardSpriteSheet.createCardView(col,row,w,h);
    }

    private Button createTransparentButton(String text) {
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