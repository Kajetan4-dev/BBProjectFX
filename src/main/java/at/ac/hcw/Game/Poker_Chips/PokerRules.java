package at.ac.hcw.Game.Poker_Chips;

public class PokerRules {

    private PokerChipsPlayer[] players;

    private int pot;
    private final int bigBlind;
    private final int smallBlind;

    private int currentBet;
    private int currentPlayerIndex;

    private boolean[] folded;

    // Runde-Logik
    private boolean[] needsAction; // wer muss in dieser Runde noch handeln?
    private int round;             // 0=Preflop,1=Flop,2=Turn,3=River

    public PokerRules(int noPlayer, int bigBlind, int smallBlind) {
        this.players = new PokerChipsPlayer[noPlayer];
        this.bigBlind = bigBlind;
        this.smallBlind = smallBlind;

        this.pot = 0;
        this.currentBet = 0;
        this.currentPlayerIndex = 0;

        this.folded = new boolean[noPlayer];
        this.needsAction = new boolean[noPlayer];
        this.round = 0;
    }

    public void playerSetup(String[] playerName, int startingMoney) {
        for (int i = 0; i < players.length; i++) {
            players[i] = new PokerChipsPlayer(playerName[i], startingMoney);
        }

        // 0 = big blind, 1 = small blind, 2 = kein blind
        players[0].setBigBlind(0);
        if (players.length > 1) players[1].setBigBlind(1);

        folded = new boolean[players.length];
        needsAction = new boolean[players.length];
        currentPlayerIndex = 0;
    }

    public void startHand() {
        pot = 0;
        currentBet = 0;
        currentPlayerIndex = 0;

        folded = new boolean[players.length];

        round = 0;
        resetNeedsActionForRound();

        for (PokerChipsPlayer p : players) {
            p.setBet(); // bet = 0
        }

        postBlinds();

        // Blinds gelten als "haben gehandelt" (damit man weiter kommt)
        needsAction[0] = false;
        if (players.length > 1) needsAction[1] = false;
    }

    private void postBlinds() {
        // Big Blind Spieler 0
        players[0].setBet(bigBlind);
        players[0].setPlayerMoney(players[0].getPlayerMoney() - bigBlind);
        pot += bigBlind;

        // Small Blind Spieler 1
        if (players.length > 1) {
            players[1].setBet(smallBlind);
            players[1].setPlayerMoney(players[1].getPlayerMoney() - smallBlind);
            pot += smallBlind;
        }

        currentBet = bigBlind;
    }

    public void callOrCheck() {
        needsAction[currentPlayerIndex] = false;

        PokerChipsPlayer p = players[currentPlayerIndex];
        int toCall = currentBet - p.getBet();

        if (toCall > 0) {
            pot += toCall;
            p.setPlayerMoney(p.getPlayerMoney() - toCall);
            p.setBet(toCall);
        }

        if (isOnlyOnePlayerLeft()) {
            awardPotToLastPlayer();
            endHandAndPrepareNext();
            return;
        }

        advanceToNextPlayer();
        handleRoundProgress(); // -> nächste Runde wenn alle dran waren
    }

    public void raise(int raiseAmount) {
        if (raiseAmount <= 0) return;

        PokerChipsPlayer p = players[currentPlayerIndex];
        int toCall = currentBet - p.getBet();
        int total = toCall + raiseAmount;

        pot += total;
        p.setPlayerMoney(p.getPlayerMoney() - total);
        p.setBet(total);

        currentBet += raiseAmount;

        // WICHTIG: Raise -> alle aktiven Spieler müssen wieder handeln
        for (int i = 0; i < players.length; i++) {
            if (!folded[i]) needsAction[i] = true;
        }
        needsAction[currentPlayerIndex] = false; // Raisender ist fertig

        if (isOnlyOnePlayerLeft()) {
            awardPotToLastPlayer();
            endHandAndPrepareNext();
            return;
        }

        advanceToNextPlayer();
        handleRoundProgress();
    }

    public void fold() {
        needsAction[currentPlayerIndex] = false;
        folded[currentPlayerIndex] = true;

        if (isOnlyOnePlayerLeft()) {
            awardPotToLastPlayer();
            endHandAndPrepareNext();
            return;
        }

        advanceToNextPlayer();
        handleRoundProgress();
    }

    private void handleRoundProgress() {
        if (!isRoundFinished()) return;

        if (round < 3) {
            nextRound();
        } else {
            // River fertig -> Hand zu Ende (für euer Projekt reicht das)
            endHandAndPrepareNext();
        }
    }

    private boolean isRoundFinished() {
        for (int i = 0; i < players.length; i++) {
            if (!folded[i] && needsAction[i]) return false;
        }
        return true;
    }

    private void nextRound() {
        round++;

        // neue Runde: alle aktiven müssen wieder handeln
        resetNeedsActionForRound();

        // neue Runde: Einsatz zurücksetzen (einfacher)
        currentBet = 0;
        for (PokerChipsPlayer p : players) p.setBet();

        // Startspieler wieder ab Index 0 suchen (einfach)
        currentPlayerIndex = 0;
        if (folded[currentPlayerIndex]) advanceToNextPlayer();
    }

    private void resetNeedsActionForRound() {
        for (int i = 0; i < players.length; i++) {
            needsAction[i] = !folded[i];
        }
    }

    private void advanceToNextPlayer() {
        for (int i = 1; i <= players.length; i++) {
            int next = (currentPlayerIndex + i) % players.length;
            if (!folded[next]) {
                currentPlayerIndex = next;
                return;
            }
        }
    }

    private boolean isOnlyOnePlayerLeft() {
        int active = 0;
        for (boolean f : folded) {
            if (!f) active++;
        }
        return active == 1;
    }

    private int getLastActivePlayerIndex() {
        for (int i = 0; i < folded.length; i++) {
            if (!folded[i]) return i;
        }
        return -1;
    }

    private void awardPotToLastPlayer() {
        int winnerIndex = getLastActivePlayerIndex();
        if (winnerIndex == -1) return;

        PokerChipsPlayer winner = players[winnerIndex];
        winner.setPlayerMoney(winner.getPlayerMoney() + pot);
        pot = 0;
    }

    public void endHandAndPrepareNext() {
        swapBlinds();

        for (PokerChipsPlayer p : players) p.setBigBlind(2);
        players[0].setBigBlind(0);
        if (players.length > 1) players[1].setBigBlind(1);

        startHand();
    }

    private void swapBlinds() {
        PokerChipsPlayer[] temp = new PokerChipsPlayer[players.length];

        for (int i = 0; i < players.length; i++) {
            if (i == 0) temp[players.length - 1] = players[i];
            else temp[i - 1] = players[i];
        }

        for (int i = 0; i < players.length; i++) {
            players[i] = temp[i];
        }
    }

    // ===== Getter für UI =====

    public int getPot() { return pot; }

    public int getCurrentBet() { return currentBet; }

    public int getCurrentPlayerIndex() { return currentPlayerIndex; }

    public PokerChipsPlayer getCurrentPlayer() { return players[currentPlayerIndex]; }

    public PokerChipsPlayer[] getPlayers() { return players; }

    public boolean isFolded(int i) { return folded[i]; }

    public int getRound() { return round; }

    public String getRoundName() {
        return switch (round) {
            case 0 -> "PREFLOP";
            case 1 -> "FLOP";
            case 2 -> "TURN";
            case 3 -> "RIVER";
            default -> "ROUND";
        };
    }
}
