package at.ac.hcw.Game;

//Easy access for sound effects

public class AllSoundEffects {
    public static void button() {
        SoundManager.playSound("/at/ac/hcw/Game/Media/Sounds/Noises/button-press-382713.mp3");
    }

    public static void winning() {
        SoundManager.playSound("/at/ac/hcw/Game/Media/Sounds/Noises/goodresult-82807.mp3");
    }
}
