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
        cards.clear();
        for (int d = 0; d < 2; d++) {           // 2 Decks
            for (int suit = 0; suit < 4; suit++) {       // 4 Farben
                for (int col = 0; col < 13; col++) {
                   int cardId = suit * 13 + col;
                   cards.add(cardId);

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