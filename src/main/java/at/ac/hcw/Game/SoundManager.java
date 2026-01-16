package at.ac.hcw.Game;

import javafx.scene.media.MediaPlayer;
import java.util.ArrayList;
import java.util.List;

public class SoundManager {

    private static double volume = 0.5; // default 50%
    private static final List<MediaPlayer> players = new ArrayList<>();

    private SoundManager() {}

    public static void register(MediaPlayer player) {
        player.setVolume(volume);
        players.add(player);
    }

    public static void setVolume(double newVolume) {
        volume = newVolume;
        for (MediaPlayer player : players) {
            player.setVolume(volume);
        }
    }

    public static double getVolume() {
        return volume;
    }
}
