module org.example {
    requires javafx.controls;
    requires javafx.fxml;

    opens at.ac.hcw.Game to javafx.fxml;
    opens at.ac.hcw.Game.Poker_Chips to javafx.fxml;

    // <- das hier macht’s oft endgültig stabil:
    exports at.ac.hcw.Game to javafx.fxml;
    exports at.ac.hcw.Game.Poker_Chips to javafx.fxml;
}