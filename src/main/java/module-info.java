module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    //requires org.example;
    requires javafx.graphics;
    //requires org.example;


    opens at.ac.hcw.Game to javafx.fxml;
    exports at.ac.hcw.Game;
}