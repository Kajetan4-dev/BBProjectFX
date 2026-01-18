package at.ac.hcw.Game.Black_Jack;

public class Player {
    private String name;
    private int[] cards;
    private int total;
    private int aces;
    private int chips;
    private int bid;
    private boolean stand;

    public Player (String name, int chips){
        this.name = name;
        this.cards = new int[9];
        this.total = 0;
        this.aces = 0;
        this.chips = chips;
        this.stand = false;
    }
    public Player (){
        this.name = "Dealer";
        this.cards = new int[9];
        this.total = 0;
        this.aces = 0;
        this.stand = false;
    }

    // Sie sucht den ersten freien Platz (0) und legt die Karte dort ab.
    public void addCard(int cardValue) {
        for (int i = 0; i < cards.length; i++) {
            if (cards[i] == 0) {
                cards[i] = cardValue;
                break; // Karte abgelegt, fertig
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getCards() {
        return cards;
    }

    public void setCards(int[] cards){
        this.cards = cards;
    }

    public int getChips(){
        return chips;
    }

    public void setChips(int chips) {
        this.chips = chips;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getAces() {
        return aces;
    }

    public void setAces(int aces) {
        this.aces = aces;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public boolean isStand() {
        return stand;
    }

    public void setStand(boolean stand){
        this.stand = stand;
    }


    public String toString(){
        return "Name: " + this.getName() + " | Chips: " + this.getChips();
    }
}
