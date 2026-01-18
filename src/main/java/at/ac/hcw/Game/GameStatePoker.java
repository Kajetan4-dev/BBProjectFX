package at.ac.hcw.Game;

import at.ac.hcw.Game.Poker_Chips.PokerRules;

//Saves the game state in poker

public class GameStatePoker {

    private static PokerRules pokerGame;

    public static PokerRules getPokerGame() {
        return pokerGame;
    }

    public static void setPokerGame(PokerRules game) {
        pokerGame = game;
    }

}
