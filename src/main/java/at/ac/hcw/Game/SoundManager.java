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

    private static final List<MediaPlayer> players = new ArrayList<>();
    private static MediaPlayer musicPlayer;

    private SoundManager() {}

    public static void playMusic(String path) {
        stopMusic();

        Media media = new Media(SoundManager.class.getResource(path).toExternalForm());
        musicPlayer = new MediaPlayer(media);
        musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        // 🔗 bind volume
        musicPlayer.volumeProperty().bind(volume);

        musicPlayer.play();
        players.add(musicPlayer);
    }

    public static void stopMusic() {
        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer.dispose();
            players.remove(musicPlayer);
            musicPlayer = null;
        }
    }

    public static void playSound(String path) {
        Media media = new Media(SoundManager.class.getResource(path).toExternalForm());
        MediaPlayer player = new MediaPlayer(media);

        // 🔗 bind volume
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

    public static double getVolume() {
        return volume.get();
    }

    public static void setVolume(double newVolume) {
        volume.set(Math.max(0, Math.min(1, newVolume)));
    }
}
