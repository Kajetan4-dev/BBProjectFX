package at.ac.hcw.Game.Black_Jack;

public class GameStateBlackjack {

    private static BlackjackRules savedGame;

    private GameStateBlackjack() {}

    public static void saveGame(BlackjackRules game) {
        savedGame = game;
    }

    public static BlackjackRules getSavedGame() {
        return savedGame;
    }

    public static boolean hasSavedGame() {
        return savedGame != null;
    }

    public static void clear() {
        savedGame = null;
    }
}
