module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens at.ac.hcw.Game to javafx.fxml;
    exports at.ac.hcw.Game;
}