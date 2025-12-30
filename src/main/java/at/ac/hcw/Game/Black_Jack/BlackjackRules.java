package at.ac.hcw.Game.Black_Jack;

public class BlackjackRules {

    private Deck deck;
    private Dealer dealer;
    private Player[] players;

    private int currentPlayerIndex = 0;
    private boolean roundActive = false;

    // ===== KONSTRUKTOR =====
    public BlackjackRules(Player[] players) {
        this.players = players;
        this.deck = new Deck();
        this.dealer = new Dealer();
    }

    // ===== GETTER (für JavaFX / Controller) =====

    public Player[] getPlayers() {
        return players;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public boolean isRoundActive() {
        return roundActive;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public Player getCurrentPlayer() {
        return players[currentPlayerIndex];
    }

    // ===== RUNDENSTART =====
    public void startRound() {
        roundActive = true;
        currentPlayerIndex = 0;

        // Karten austeilen (2 Karten pro Spieler + Dealer)
        for (int i = 0; i < 2; i++) {
            for (Player p : players) {
                if (p.getChips() > 0) {
                    p.addCard(deck.drawCard());
                }
            }
            dealer.addCard(deck.drawCard());
        }

    }

    // ===== SPIELER-AKTIONEN =====

    public void hit() {
        if (!roundActive) return;

        Player p = getCurrentPlayer();
        p.addCard(deck.drawCard());
        p.setTotal(calculatehand(p.getCards()));

        if (p.getTotal() > 21) {
            p.setStand(true);
            nextPlayer();
        }
    }

    public void stand() {
        if (!roundActive) return;

        getCurrentPlayer().setStand(true);
        nextPlayer();
    }

    private void nextPlayer() {
        currentPlayerIndex++;

        if (currentPlayerIndex >= players.length) {
            dealerTurn();
        }
    }

    // ===== DEALER =====

    private void dealerTurn() {
        dealer.revealCard();
        int dTotal = calculatehand(dealer.getCards());

        while (dTotal < 17) {
            dealer.addCard(deck.drawCard());
            dTotal = calculatehand(dealer.getCards());
        }

        dealer.setTotal(dTotal);
        evaluateAll();
        roundActive = false;
    }

    // ===== AUSWERTUNG =====

    private void evaluateAll() {
        int dFinal = dealer.getTotal();

        for (Player p : players) {
            int pFinal = calculatehand(p.getCards());

            if (p.getBid() <= 0) continue;

            if (pFinal > 21) {
                p.setChips(p.getChips() - p.getBid());
            } else if (dFinal > 21 || pFinal > dFinal) {
                p.setChips(p.getChips() + p.getBid());
            } else if (pFinal < dFinal) {
                p.setChips(p.getChips() - p.getBid());
            }
            // Push = nichts passiert
        }
    }

    // ===== RESET FÜR NEUE RUNDE =====

    public void resetRound() {
        for (Player p : players) {
            p.setCards(new int[9]);
            p.setStand(false);
            p.setBid(0);
            p.setTotal(0);
        }

        dealer.setCards(new int[9]);
        dealer.setTotal(0);
        dealer.resetHideCard();

        roundActive = false;
        currentPlayerIndex = 0;
    }

    // ===== HILFSMETHODE =====

    public static int calculatehand(int[] cards) {
        int total = 0;
        int aces = 0;

        for (int v : cards) {
            if (v == 1) {
                aces++;
                total += 11;
            } else if (v >= 10) {
                total += 10;
            } else if (v >= 2) {
                total += v;
            }
        }

        while (total > 21 && aces > 0) {
            total -= 10;
            aces--;
        }

        return total;
    }
}
