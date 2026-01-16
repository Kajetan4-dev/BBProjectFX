module at.ac.hcw.game {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens at.ac.hcw.Game to javafx.fxml;
    opens at.ac.hcw.Game.Black_Jack to javafx.fxml;
    opens at.ac.hcw.Game.Poker_Chips to javafx.fxml;

    exports at.ac.hcw.Game;
}
