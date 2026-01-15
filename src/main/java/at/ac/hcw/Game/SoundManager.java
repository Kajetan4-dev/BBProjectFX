package at.ac.hcw.Game;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;
import java.util.List;

public class SoundManager {

    private static double volume = 0.5; // default 50%
    private static final List<MediaPlayer> players = new ArrayList<>();
    private static MediaPlayer musicPlayer;

    private SoundManager() {} // prevent instantiation

    /**
     * Play background music (loops indefinitely).
     * Calling this again stops previous music and starts new one.
     */
    public static void playMusic(String path) {
        stopMusic(); // stop current music if any

        Media media = new Media(SoundManager.class.getResource(path).toExternalForm());
        musicPlayer = new MediaPlayer(media);
        musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        musicPlayer.setVolume(volume);
        musicPlayer.play();

        register(musicPlayer);
    }

    /**
     * Stop background music if playing.
     */
    public static void stopMusic() {
        if (musicPlayer != null) {
            musicPlayer.stop();
            players.remove(musicPlayer);
            musicPlayer = null;
        }
    }

    /**
     * Play a short sound effect (non-looping).
     */
    public static void playSound(String path) {
        Media media = new Media(SoundManager.class.getResource(path).toExternalForm());
        MediaPlayer player = new MediaPlayer(media);
        player.setVolume(volume);
        player.setOnEndOfMedia(() -> {
            player.dispose();       // free resources after playing
            players.remove(player); // remove from list
        });
        players.add(player);
        player.play();
    }

    /**
     * Register a MediaPlayer so it reacts to volume changes.
     */
    private static void register(MediaPlayer player) {
        player.setVolume(volume);
        if (!players.contains(player)) {
            players.add(player);
        }
    }

    /** Adjust the global volume for all sounds/music */
    public static void setVolume(double newVolume) {
        volume = Math.max(0, Math.min(1, newVolume)); // clamp between 0 and 1
        for (MediaPlayer player : players) {
            player.setVolume(volume);
        }
    }

    public static double getVolume() {
        return volume;
    }
}
