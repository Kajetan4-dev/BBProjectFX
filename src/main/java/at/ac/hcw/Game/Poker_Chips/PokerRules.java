package at.ac.hcw.Game.Poker_Chips;


public class PokerRules {
    PokerChipsPlayer[] players;
    private int pot;
    private final int bigBlind;
    private final int smallBlind;
    private int currentBet;
    private int currentPlayerIndex;
    private boolean[] folded;

    public PokerRules(int noPlayer, int bigBlind, int smallBlind) {
        this.players = new PokerChipsPlayer[noPlayer];
        this.pot = 0;
        this.bigBlind = bigBlind;
        this.smallBlind = smallBlind;
        this.currentBet = 0;
        this.folded = new boolean[noPlayer];
        this.currentPlayerIndex = 0;
        //big blind small blind sind falsch

    }

    // for each player enter every value, name etc. currentblind player wird zu bigblind initialisiere diesen array
// Change your playerSetup to accept the chips array
    public void playerSetupWithChips(String[] playerName, int[] startingMoney) {
        for (int i = 0; i < players.length; i++) {
            players[i] = new PokerChipsPlayer(playerName[i], startingMoney[i]);
        }

        // Blind roles (0 = Big, 1 = Small)
        players[0].setBigBlind(0);
        if(players.length > 1){
            players[1].setBigBlind(1);
        }

        folded = new boolean[players.length];
        currentPlayerIndex = 0;
    }


    public void startHand(){//ersetzt rounds kümmert sich um spielrunde + input + output
        pot = 0;
        currentBet = 0;
        currentPlayerIndex = 0;
        folded = new boolean[players.length];

        for(PokerChipsPlayer p : players){
            p.setBet();
        }

        postBlinds();
    }

    private void postBlinds(){
        //Big Blind Spieler 0
        players[0].setBet(bigBlind);
        players[0].setPlayerMoney(players[0].getPlayerMoney()-bigBlind);
        pot += bigBlind;

        //Small Blind Spieler 1
        if (players.length > 1){
            players[1].setBet(smallBlind);
            players[1].setPlayerMoney(players[1].getPlayerMoney()-smallBlind);
            pot += smallBlind;
        }

        currentBet = bigBlind;
    }

    public void callOrCheck(){// check wenn nichts dazuzuzahlen ist call wenn nachgezahlt werden muss dann nächster spieler
        PokerChipsPlayer p = players[currentPlayerIndex];
        int toCall = currentBet - p.getBet();//wv muss gezahlt werden um mitzuspielen

        if (toCall > 0){
            pot += toCall;
            p.setPlayerMoney(p.getPlayerMoney()-toCall);
            p.setBet(toCall);
        }
        if (isOnlyOnePlayerLeft()){
            awardPotToLastPlayer();
            endHandAndPrepareNext();
            return;
        }
        advanceToNextPlayer();
    }

    public void raise(int raiseAmount){//zahlt akutellen bet auf dann Raise currentBet steigt alle andern müssen callen
        if(raiseAmount <= 0) return;

        PokerChipsPlayer p = players[currentPlayerIndex];
        int toCall = currentBet - p.getBet();
        int total = toCall + raiseAmount;

        pot += total;
        p.setPlayerMoney(p.getPlayerMoney() - total);
        p.setBet(total);
        currentBet += raiseAmount;

        if (isOnlyOnePlayerLeft()){
            awardPotToLastPlayer();
            endHandAndPrepareNext();
            return;
        }
        advanceToNextPlayer();
    }

    public void fold(){// wenn true = spieler ist raus
        folded[currentPlayerIndex] = true;

        if (isOnlyOnePlayerLeft()){
            awardPotToLastPlayer();
            endHandAndPrepareNext();
            return;
        }
        advanceToNextPlayer();
    }

    private void advanceToNextPlayer(){
        for (int i = 1; i <= players.length; i++) {
            int next = (currentPlayerIndex + i) % players.length;// von hinten wieder bei 0

            if (!folded[next]) {
                currentPlayerIndex = next;
                return;
            }
        }
    }

    private boolean isOnlyOnePlayerLeft(){// zählt spieler die nicht gefoldet sind vorbei wenn alle bis auf einen gefoldet haben
        int active = 0;
        for(boolean f : folded){
            if (!f)active++;
        }
        return active == 1;
    }

    private int getLastActivePlayerIndex(){// "findet" den letzten aktiven spielerindex
        for (int i = 0; i < folded.length; i++){
            if (!folded[i]) return i;
        }
        return -1;
    }

    private void awardPotToLastPlayer(){ // zahlt POt an letzten Spieler
        int winnerIndex = getLastActivePlayerIndex();
        if(winnerIndex == -1)return;

        PokerChipsPlayer winner = players[winnerIndex];
        winner.setPlayerMoney(winner.getPlayerMoney() + pot);
        pot = 0;
    }

    public void endHandAndPrepareNext(){// beendet eine Hand bereitet die nächste vor
        swapBlinds();

        for (PokerChipsPlayer p : players) p.setBigBlind(2);
        players[0].setBigBlind(0);
        if (players.length > 1){
            players[1].setBigBlind(1);
        }
        startHand();
    }


    private void swapBlinds() {
        PokerChipsPlayer[] temp = new PokerChipsPlayer[players.length];

        for (int i = 0; i < players.length; i++) {
            if (i == 0) {
                temp[players.length - 1] = players[i];
            } else {
                temp[i - 1] = players[i];
            }
        }

        for (int i = 0; i < players.length; i++) {
            players[i] = temp[i];
        }
    }
    public int getPot(){
        return pot;
    }

    public int getCurrentBet() {
        return currentBet;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public PokerChipsPlayer getCurrentPlayer(){
        return players[currentPlayerIndex];
    }

    public PokerChipsPlayer[] getPlayers(){
        return players;
    }

    public boolean isFolded(int i){
        return folded[i];
    }
}
