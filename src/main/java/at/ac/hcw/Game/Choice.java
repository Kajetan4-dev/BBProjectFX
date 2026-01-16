package at.ac.hcw.Game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Choice extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // This loads the FXML file you created in Step 1
        FXMLLoader fxmlLoader = new FXMLLoader(Choice.class.getResource("Choice.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Casino Game Selection");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        // This launches the JavaFX lifecycle
        launch();
    }
}