package at.ac.hcw.Game.Poker_Chips;

/*
 * Enthält die Spiellogik für "Poker Chips" (vereinfachte Hand-Logik).
 * Fokus: Blinds posten, Call/Raise/Fold, Pot verwalten, Winner bestimmen wenn alle bis auf einen gefoldet haben.
 *
 * Diese Version entscheidet den Gewinner NUR über "alle bis auf einen folden".
 * (Keine Karten, kein Showdown.)
 */

public class PokerRules {

    // Variablen für Spieler, Pot-Größe
    private PokerChipsPlayer[] players; //Array aller Spieler
    private int pot; //aktueller Pot (Summe aller Einsätze)

    //Blind-Einstellungen
    private final int bigBlind;
    private final int smallBlind;

    //Betting-Status
    private int currentBet;  // aktuell höchster Einsatz, den alle "matchen" müssen
    private int currentPlayerIndex; // Wer ist gerade am Zug
    private boolean[] folded; // folded[i] == true -> Spieler i ist gefoldet

    //Rundenzähler / Dealerbutton
    private int handCount = 1; // zählt die Hände/Runden
    private int dealerIndex = 0; // Position des Dealer-Buttons

    // Infos für Winner-Popup (damit UI weiß: wer hat gewonnen + wieviel)
    private int lastWinnerIndex = -1; // -1 bedeutet: keine Runde beendet
    private int lastPotWon = 0; // wie viele Chips wurden gewonnen

    /*
     * Konstruktor: erstellt das Spiel mit Anzahl Spieler und Blind-Werten.
     * Spieler-Objekte werden später in playerSetupWithChips() erstellt.
     */
    public PokerRules(int noPlayer, int bigBlind, int smallBlind) {
        this.players = new PokerChipsPlayer[noPlayer];
        this.pot = 0;
        this.bigBlind = bigBlind;
        this.smallBlind = smallBlind;
        this.currentBet = 0;
        this.folded = new boolean[noPlayer];
    }

    /*
     * Initialisiert die Spieler mit Namen + Startgeld (Chips).
     * Danach startet sofort die erste Hand.
     */
    public void playerSetupWithChips(String[] playerName, int[] startingMoney) {
        for (int i = 0; i < players.length; i++) {
            players[i] = new PokerChipsPlayer(playerName[i], startingMoney[i]);
        }
        startHand();
    }

    /*
     * Startet eine neue Hand:
     * - Pot und CurrentBet resetten
     * - Fold-Status resetten
     * - Spielerbets resetten
     * - Blinds posten
     */
    public void startHand() {
        pot = 0;
        currentBet = 0;

        folded = new boolean[players.length]; // alles false
        for (PokerChipsPlayer p : players) p.setBet(); // Einsatz pro Spieler auf 0

        // Der Dealer beginnt die Setzrunde
        currentPlayerIndex = dealerIndex;

        // Blinds werden automatisch abgezogen
        postBlinds();
    }

    /* Zieht die Pflichteinsätze (Blinds) automatisch ab
     * bbIdx = dealer+1
     * sbIdx = dealer+2
     */
    private void postBlinds() {
        // BB ist direkt neben dem Dealer (+1), SB daneben (+2)
        int bbIdx = (dealerIndex + 1) % players.length;
        int sbIdx = (dealerIndex + 2) % players.length;

        // Big Blind abziehen
        players[bbIdx].setPlayerMoney(players[bbIdx].getPlayerMoney() - bigBlind);
        players[bbIdx].setBet(bigBlind);

        // Small Blind abziehen
        players[sbIdx].setPlayerMoney(players[sbIdx].getPlayerMoney() - smallBlind);
        players[sbIdx].setBet(smallBlind);

        pot = smallBlind + bigBlind;
        currentBet = bigBlind;
    }

    /*
     * Call oder Check:
     * - Wenn Spieler weniger gesetzt hat als currentBet -> Differenz nachzahlen
     * - Danach prüfen: Hand vorbei? sonst nächster Spieler
     */
    public void callOrCheck() {
        PokerChipsPlayer p = players[currentPlayerIndex];
        int toCall = currentBet - p.getBet(); // Differenz berechnen
        if (toCall > 0) {
            pot += toCall;
            p.setPlayerMoney(p.getPlayerMoney() - toCall);

            // WICHTIG: setBet(int) addiert, deshalb genau die Differenz addieren
            p.setBet(toCall);
        }
        checkEndOrAdvance();
    }

    // Erhöht den aktuellen Einsatz
    public void raise(int raiseAmount) {
        if (raiseAmount <= 0) return;
        PokerChipsPlayer p = players[currentPlayerIndex];
        int toCall = currentBet - p.getBet();
        int total = toCall + raiseAmount;

        pot += total;
        p.setPlayerMoney(p.getPlayerMoney() - total);
        p.setBet(total);
        currentBet += raiseAmount; // Neuen Standard setzen
        checkEndOrAdvance();
    }

    // Spieler steigt aus der aktuellen Runde aus
    public void fold() {
        folded[currentPlayerIndex] = true;
        checkEndOrAdvance();
    }

    // Prüft: Ist die Runde vorbei (nur noch einer übrig) oder kommt der nächste Spieler?
    private void checkEndOrAdvance() {
        if (isOnlyOnePlayerLeft()) {
            awardPotToLastPlayer();
            handCount++;
            dealerIndex = (dealerIndex + 1) % players.length; // Button rückt weiter
            startHand();
        } else {
            advanceToNextPlayer();
        }
    }

    // Findet den nächsten Spieler, der nicht gefoldet hat
    private void advanceToNextPlayer() {
        int next = currentPlayerIndex;
        do {
            next = (next + 1) % players.length;
        } while (folded[next]);
        currentPlayerIndex = next;
    }

    /*
     * Prüft, ob nur noch ein Spieler nicht gefoldet ist.
     */
    private boolean isOnlyOnePlayerLeft() {
        int active = 0;
        for (boolean f : folded) if (!f) active++;
        return active == 1;
    }


    /*
     * Gewinner bekommt den Pot:
     * - lastWinnerIndex und lastPotWon werden gesetzt (für UI Popup)
     * - Chips werden dem Gewinner gutgeschrieben
     */
    private void awardPotToLastPlayer() {
        for (int i = 0; i < players.length; i++) {
            if (!folded[i]) {

                lastWinnerIndex = i;
                lastPotWon = pot;

                players[i].setPlayerMoney(players[i].getPlayerMoney() + pot);
                break;
            }
        }
    }

    /* True, wenn eine Hand beendet wurde und Gewinnerdaten bereitstehen. */
    public boolean hasRoundEnded() {
        return lastWinnerIndex != -1;
    }
    /* Gewinnerindex der letzten beendeten Hand. */
    public int getLastWinnerIndex() {
        return lastWinnerIndex;
    }
    /* Potbetrag, den der Gewinner gewonnen hat. */
    public  int getLastPotWon() {
        return lastPotWon;
    }

    /*
     * Muss nach dem Anzeigen des Popups aufgerufen werden,
     * damit nicht jedes updateUI() das Popup erneut zeigt.
     */
    public void clearRoundEndFlag() {
        lastWinnerIndex = -1; lastPotWon = 0;
    }

    // Getter für das UI
    public int getDIndex() { return dealerIndex; }
    public int getBBIndex() { return (dealerIndex + 1) % players.length; }
    public int getSBIndex() { return (dealerIndex + 2) % players.length; }
    public int getPot() { return pot; }
    public int getCurrentBet() { return currentBet; }
    public int getCurrentPlayerIndex() { return currentPlayerIndex; }
    public PokerChipsPlayer[] getPlayers() { return players; }
    public boolean isFolded(int i) { return folded[i]; }
    public int getHandCount() { return handCount; }
}