package at.ac.hcw.Game;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;
import java.util.List;

public class SoundManager {

    // Global volume property (0.0 – 1.0)
    private static final DoubleProperty volume = new SimpleDoubleProperty(0.5);

    //private Variables that store the different sounds
    private static final List<MediaPlayer> players = new ArrayList<>();
    //holds background music
    private static MediaPlayer musicPlayer;

    //Constructor that prevents from creating instances of sound manager
    private SoundManager() {}

    //plays music in a loop
    public static void playMusic(String path) {
        //Removes last music
        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer.volumeProperty().unbind();
            musicPlayer.dispose();
            players.remove(musicPlayer);
        }

        //Creates a media player to play the music in
        Media media = new Media(SoundManager.class.getResource(path).toExternalForm());
        musicPlayer = new MediaPlayer(media);

        //Loops music forever
        musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        //Any change to the volume updates the music instantly
        musicPlayer.volumeProperty().bind(volume);

        //plays music
        musicPlayer.play();
        players.add(musicPlayer);
    }

    //plays a single sound effect
    public static void playSound(String path) {
        Media media = new Media(SoundManager.class.getResource(path).toExternalForm());
        MediaPlayer player = new MediaPlayer(media);

        player.volumeProperty().bind(volume);

        player.setOnEndOfMedia(() -> {
            player.dispose();
            players.remove(player);
        });

        players.add(player);
        player.play();
    }

    // Expose volume property
    public static DoubleProperty volumeProperty() {
        return volume;
    }

}
