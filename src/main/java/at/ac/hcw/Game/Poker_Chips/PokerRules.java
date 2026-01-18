package at.ac.hcw.Game.Poker_Chips;

public class PokerRules {
    // Variablen für Spieler, Pot-Größe und Blind-Einstellungen
    private PokerChipsPlayer[] players;
    private int pot;
    private final int bigBlind;
    private final int smallBlind;
    private int currentBet; // Der aktuell höchste Einsatz auf dem Tisch
    private int currentPlayerIndex; // Wer ist gerade am Zug
    private boolean[] folded; // Speichert, welcher Spieler ausgestiegen ist
    private int handCount = 1; // Rundenzähler
    private int dealerIndex = 0; // Position des Dealer-Buttons
    private int lastWinnerIndex = -1;
    private int lastPotWon = 0;

    public PokerRules(int noPlayer, int bigBlind, int smallBlind) {
        this.players = new PokerChipsPlayer[noPlayer];
        this.pot = 0;
        this.bigBlind = bigBlind;
        this.smallBlind = smallBlind;
        this.currentBet = 0;
        this.folded = new boolean[noPlayer];
    }

    // Initialisiert die Spieler mit Namen und Chips
    public void playerSetupWithChips(String[] playerName, int[] startingMoney) {
        for (int i = 0; i < players.length; i++) {
            players[i] = new PokerChipsPlayer(playerName[i], startingMoney[i]);
        }
        startHand();
    }

    // Bereitet eine neue Hand vor (Reset der Einsätze)
    public void startHand() {
        pot = 0;
        currentBet = 0;
        folded = new boolean[players.length];
        for (PokerChipsPlayer p : players) p.setBet();

        // Der Dealer beginnt die Setzrunde
        currentPlayerIndex = dealerIndex;
        postBlinds();
    }

    // Zieht die Pflichteinsätze (Blinds) automatisch ab
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

    // Gleicht den Einsatz an (Mitgehen)
    public void callOrCheck() {
        PokerChipsPlayer p = players[currentPlayerIndex];
        int toCall = currentBet - p.getBet(); // Differenz berechnen
        if (toCall > 0) {
            pot += toCall;
            p.setPlayerMoney(p.getPlayerMoney() - toCall);
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

    private boolean isOnlyOnePlayerLeft() {
        int active = 0;
        for (boolean f : folded) if (!f) active++;
        return active == 1;
    }

    // Überweist den Pot an den Gewinner
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

    public PokerChipsPlayer getRoundWinner() {
        if (lastWinnerIndex < 0) return null;
        return players[lastWinnerIndex];
    }

    public boolean hasRoundEnded() {
        return lastWinnerIndex != -1;
    }
    public int getLastWinnerIndex() {
        return lastWinnerIndex;
    }
    public  int getLastPotWon() {
        return lastPotWon;
    }
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