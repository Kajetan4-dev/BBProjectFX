package at.ac.hcw.Game.Black_Jack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    private List<Integer> cards;

    public Deck() {
        cards = new ArrayList<>();
        initDeck();
        shuffle();
    }

    /* Erstellt ein Deck aus 2 Karten-Decks (104 Karten) */

    private void initDeck() {
        for (int d = 0; d < 2; d++) {           // 2 Decks
            for (int i = 0; i < 4; i++) {       // 4 Farben
                cards.add(1);                   // Ass
                for (int v = 2; v <= 9; v++) {
                    cards.add(v);               // 2–9
                }
                for (int j = 0; j < 4; j++) {
                    cards.add(10);              // 10, Bube, Dame, König
                }
            }
        }
    }

    /* Mischt das Deck zufällig */

    public void shuffle() {
        Collections.shuffle(cards);
    }

    /* Zieht eine Karte aus dem Deck */

    public int drawCard() {
        if (cards.isEmpty()) {
            initDeck();
            shuffle();
        }
        return cards.remove(0);
    }

    public int remainingCards() {
        return cards.size();
    }
}