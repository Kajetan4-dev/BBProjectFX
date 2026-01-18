package at.ac.hcw.Game.Black_Jack;

import java.util.Arrays;

public class BlackjackRules {

    // Spielobjekte: Kartendeck, Dealer und die Liste der Spieler
    private Deck deck;
    private Dealer dealer;
    private Player[] players;

    // Statusvariablen für den aktuellen Spieler und ob die Runde läuft
    private int currentPlayerIndex = 0;
    private boolean roundActive = false;

    public BlackjackRules(Player[] players) {
        this.players = players;
        this.deck = new Deck(); // Neues Deck erstellen
        this.dealer = new Dealer(); // Dealer initialisieren
    }

    // Getter-Methoden für den Zugriff von außen
    public Player[] getPlayers() { return players; }
    public Dealer getDealer() { return dealer; }
    public boolean isRoundActive() { return roundActive; }
    public int getCurrentPlayerIndex() { return currentPlayerIndex; }

    // Gibt das Player-Objekt des Spielers zurück, der gerade am Zug ist
    public Player getCurrentPlayer() {
        if (currentPlayerIndex >= 0 && currentPlayerIndex < players.length) {
            return players[currentPlayerIndex];
        }
        return null;
    }

    // Startet eine neue Runde und teilt die ersten Karten aus
    public void startRound() {
        roundActive = true;

        // Ersten aktiven Spieler (jemand, der Chips gesetzt hat) finden
        currentPlayerIndex = -1;
        for (int i = 0; i < players.length; i++) {
            if (players[i].getBid() > 0) {
                currentPlayerIndex = i;
                break;
            }
        }

        // Sicherheitsprüfung: Wenn niemand gesetzt hat, passiert nichts
        if (currentPlayerIndex == -1) {
            roundActive = false;
            return;
        }

        // Jeweils 2 Karten an Spieler mit Gebot und den Dealer austeilen
        for (int i = 0; i < 2; i++) {
            for (Player p : players) {
                if (p.getBid() > 0) {
                    p.addCard(deck.drawCard());
                }
            }
            dealer.addCard(deck.drawCard());
        }
    }

    // Aktion "Hit": Spieler zieht eine zusätzliche Karte
    public void hit() {
        if (!roundActive) return;
        Player p = getCurrentPlayer();
        if (p == null) return;

        p.addCard(deck.drawCard()); // Karte ziehen
        p.setTotal(calculatehand(p.getCards())); // Neuen Punktwert berechnen

        // Wenn über 21 Punkte, hat der Spieler verloren (Bust) -> Nächster Spieler
        if (p.getTotal() > 21) {
            p.setStand(true);
            nextPlayer();
        }
    }

    // Aktion "Stand": Spieler bleibt bei seinem Wert stehen
    public void stand() {
        if (!roundActive) return;
        Player p = getCurrentPlayer();
        if (p != null) p.setStand(true);
        nextPlayer();
    }

    // Wechselt zum nächsten Spieler oder zum Dealer-Zug
    private void nextPlayer() {
        currentPlayerIndex++;

        // Spieler ohne Einsatz werden übersprungen
        while (currentPlayerIndex < players.length && players[currentPlayerIndex].getBid() <= 0) {
            currentPlayerIndex++;
        }

        // Wenn alle Spieler fertig sind, ist der Dealer an der Reihe
        if (currentPlayerIndex >= players.length) {
            dealerTurn();
        }
    }

    // Logik für den Dealer: Er zieht Karten, bis er mindestens 17 Punkte hat
    private void dealerTurn() {
        dealer.revealCard(); // Verdeckte Karte umdrehen
        int dTotal = calculatehand(dealer.getCards());
        while (dTotal < 17) {
            dealer.addCard(deck.drawCard());
            dTotal = calculatehand(dealer.getCards());
        }
        dealer.setTotal(dTotal);
        evaluateAll(); // Gewinner ermitteln
        roundActive = false; // Runde beendet
    }

    // Vergleicht die Werte aller Spieler mit dem Dealer und verteilt Chips
    private void evaluateAll() {
        int dFinal = dealer.getTotal();
        for (Player p : players) {
            if (p.getBid() <= 0) continue; // Nur Spieler mit Einsatz prüfen
            int pFinal = calculatehand(p.getCards());

            if (pFinal > 21) {
                // Spieler hat über 21 -> Chips verloren
                p.setChips(p.getChips() - p.getBid());
            } else if (dFinal > 21 || pFinal > dFinal) {
                // Dealer hat über 21 oder Spieler ist besser -> Chips gewonnen
                p.setChips(p.getChips() + p.getBid());
            } else if (pFinal < dFinal) {
                // Dealer ist besser -> Chips verloren
                p.setChips(p.getChips() - p.getBid());
            }
            // Bei Gleichstand (Push) passiert nichts mit den Chips
        }
    }

    // Setzt alle Werte für eine neue Runde zurück
    public void resetRound() {
        for (Player p : players) {
            int[] empty = new int[9];
            Arrays.fill(empty,-1);
            p.setCards(empty);
            p.setStand(false);
            p.setBid(0);
            p.setTotal(0);
        }
        int [] dEmpty = new int[9];
        Arrays.fill(dEmpty,-1);
        dealer.setCards(dEmpty);
        dealer.setTotal(0);
        dealer.resetHideCard();
        roundActive = false;
        currentPlayerIndex = 0;
    }

    // Berechnet den Punktwert einer Hand (Asse zählen 11 oder 1)
    public static int calculatehand(int[] cards) {
        int total = 0;
        int aces = 0;
        for (int id : cards) {
            if (id == -1) continue;

            int col = id % 13;
            int rank = col + 1;

            if (rank == 1) { aces++; total += 11; } // Ass vorerst als 11
            else if (rank >= 10) { total += 10; } // Bildkarten zählen 10
            else { total += rank; }

        }
        // Falls über 21 Punkte, werden Asse von 11 auf 1 abgewertet
        while (total > 21 && aces > 0) { total -= 10; aces--; }
        return total;
    }

    public void setCurrentPlayerIndex(int index) {
        this.currentPlayerIndex = index;
    }

    public void setRoundActive(boolean active) {
        this.roundActive = active;
    }


}