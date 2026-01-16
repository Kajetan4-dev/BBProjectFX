module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    // FXML/Reflection
    opens at.ac.hcw.Game to javafx.fxml;
    opens at.ac.hcw.Game.Poker_Chips to javafx.fxml;

    // ✅ Damit JavaFX deine Application-Klasse starten darf
    exports at.ac.hcw.Game to javafx.graphics;

    // optional, falls du auch Poker_Chips öffentlich brauchst:
    exports at.ac.hcw.Game.Poker_Chips;
}