module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.graphics;



    opens at.ac.hcw.Game to javafx.fxml;
    exports at.ac.hcw.Game;
    exports at.ac.hcw.Game.Black_Jack;
    opens at.ac.hcw.Game.Black_Jack to javafx.fxml;
}