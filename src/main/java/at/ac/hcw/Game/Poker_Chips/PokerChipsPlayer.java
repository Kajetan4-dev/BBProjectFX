package at.ac.hcw.Game.Poker_Chips;
public class PokerChipsPlayer {
    private String name;
    private int playerMoney;
    private int bet;
    private int bigBlind; //Big Blind 0, 1, 2
    //0 = big blind, 1 = small blind 2 = kein blind

    public PokerChipsPlayer(String name, int startingChips) {
        this.bet = 0;
        this.bigBlind = 2;
        this.name = name;
        this.playerMoney = startingChips;
    }
    public int getBet() {
        return bet;
    }
    public void setBet(int bet) {
        this.bet += bet;
    }

    public void setBet(){
        this.bet = 0;
    }

    public String getName() {
        return name;
    }


    public int getPlayerMoney() {
        return playerMoney;
    }

    public void setPlayerMoney(int playerMoney) {
        this.playerMoney = playerMoney;
    }

    public void setBigBlind(int bigBlind) {
        this.bigBlind = bigBlind;
    }
}
