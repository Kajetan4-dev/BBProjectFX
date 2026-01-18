package at.ac.hcw.Game.Poker_Chips;

/*
 * Repräsentiert einen Spieler im Poker-Chips-Spiel.
 * Speichert Name, verfügbares Geld (Chips) und den aktuellen Einsatz (bet).
 *
 * WICHTIG:
 * - setBet(int) addiert zum bestehenden Einsatz (nicht überschreiben).
 * - setBet() ohne Parameter setzt den Einsatz wieder auf 0 (Reset pro Hand).
 */

public class PokerChipsPlayer {
    private String name; // Spielername
    private int playerMoney; // Chip-Stand des Spielers
    private int bet; // Aktueller Einsatz in der aktuellen Hand

    private int bigBlind; //Big Blind 0, 1, 2
    //0 = big blind, 1 = small blind 2 = kein blind

    /*
     * Konstruktor: erstellt Spieler mit Name und Start-Chips.
     */
    public PokerChipsPlayer(String name, int startingChips) {
        this.bet = 0;
        this.bigBlind = 2;
        this.name = name;
        this.playerMoney = startingChips;
    }
    // Gibt den aktuellen Einsatz zurück.
    public int getBet() {
        return bet;
    }

    /*
     * Erhöht den Einsatz um den übergebenen Betrag.
     */
    public void setBet(int bet) {
        this.bet += bet;
    }

    /* Setzt den Einsatz auf 0 zurück (z.B. beim Start einer neuen Hand). */
    public void setBet(){
        this.bet = 0;
    }

    /* Gibt den Namen zurück. */
    public String getName() {
        return name;
    }

    /* Gibt den aktuellen Chip-Stand zurück. */
    public int getPlayerMoney() {
        return playerMoney;
    }

    /* Setzt den Chip-Stand (z.B. nach Call/Raise oder Gewinn). */
    public void setPlayerMoney(int playerMoney) {
        this.playerMoney = playerMoney;
    }

    /* Setzt die Blind-Rolle (0/1/2). (Aktuell optional / nicht zwingend genutzt.) */
    public void setBigBlind(int bigBlind) {
        this.bigBlind = bigBlind;
    }
}
