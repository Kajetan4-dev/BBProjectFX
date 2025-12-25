

package at.ac.hcw.Game.Black_Jack;
import java.util.Scanner;

public class BlackjackRules {

    private static Scanner scanner = new Scanner(System.in);
    private static Deck deck = new Deck();
    private static Dealer dealer = new Dealer();

    public static void startGame() {
        Player[] players = initPlayers();

        boolean running = true;
        while (running) {
            playRoundSequence(players);

            // Prüfung auf 'j/n' mit Fehlermeldung bei falscher Eingabe
            running = checkNextRound(players);
            if (running) {
                resetForNextRound(players);
            }
        }
        showFinalScore(players);
    }

    private static Player[] initPlayers() {
        int count = 0;
        // Begrenzung auf 1 bis 6 Spieler
        while (count < 1 || count > 6) {
            System.out.println("Wie viele Spieler? (max 6 Spieler) ");
            count = scanner.nextInt();
            if (count < 1 || count > 6) {
                System.out.println("Falsch! Bitte gib eine Zahl zwischen 1-6 ein.");
            }
        }

        scanner.nextLine(); // Scanner-Puffer nach Zahleneingabe leeren

        Player[] pArray = new Player[count];
        for (int i = 0; i < count; i++) {
            System.out.print("Name für Spieler " + (i + 1) + ": ");
            String name = scanner.nextLine();

            System.out.print("Start-Chips für " + name + ": ");
            int startChips = scanner.nextInt();
            scanner.nextLine();

            pArray[i] = new Player(name, startChips);
            System.out.println(pArray[i]);
        }
        return pArray;
    }

    private static void playRoundSequence(Player[] players) {
        // Sicherstellen, dass kein Spieler mehr setzt als er besitzt
        for (Player p : players) {
            if (p.getChips() > 0) {
                int einsatz = 0;
                boolean gueltig = false;

                while (!gueltig) {
                    System.out.print(p.getName() + " (Chips: " + p.getChips() + "), Einsatz: ");
                    einsatz = scanner.nextInt();

                    if (einsatz <= 0) {
                        System.out.println("Einsatz muss mindestens 1 sein!");
                    } else if (einsatz > p.getChips()) {
                        System.out.println("Zu wenig Chips! Du hast nur " + p.getChips());
                    } else {
                        gueltig = true;
                    }
                }
                p.setBid(einsatz);
            }
        }

        // Kartenverteilung: Jeder erhält 2 Karten, Dealer versteckt eine
        for (int i = 0; i < 2; i++) {
            for (Player p : players) {
                if (p.getChips() > 0) p.addCard(deck.drawCard());
            }
            dealer.addCard(deck.drawCard());
        }

        for (Player p : players) {
            if (p.getChips() > 0) handlePlayerTurn(p);
        }

        handleDealerTurn(players);
        evaluateAll(players);
    }

    private static void handlePlayerTurn(Player p) {
        System.out.println("\n>>> " + p.getName().toUpperCase() + " ist am Zug <<<");
        while (!p.isStand()) {
            p.setTotal(calculatehand(p.getCards()));

            if (dealer.isCardHidden()) {
                System.out.println("Dealer zeigt: [" + dealer.getCards()[0] + "] [?]");
            }

            // Detaillierte Kartenanzeige (z.B. 1/11 + 5) vor der Summe
            System.out.print("Deine Karten: [");
            boolean first = true;
            for (int card : p.getCards()) {
                if (card != 0) {
                    String cardDisplay = (card == 1) ? "1/11" : String.valueOf(card);
                    System.out.print((first ? "" : " + ") + cardDisplay);
                    first = false;
                }
            }
            System.out.println("] | Total: " + p.getTotal());

            if (p.getTotal() > 21) {
                System.out.println("Verloren! Du bist " + (p.getTotal() - 21) + " drüber.");
                p.setStand(true);
            } else {
                System.out.print("(h)it oder (s)tand? ");
                String wahl = scanner.next();
                if (wahl.equalsIgnoreCase("hit") || wahl.equalsIgnoreCase("h")) {
                    p.addCard(deck.drawCard());
                } else {
                    p.setStand(true);
                }
            }
        }
    }

    private static void handleDealerTurn(Player[] players) {
        // Dealer zieht nur, wenn noch aktive Spieler im Spiel sind
        boolean anyoneAlive = false;
        for (Player p : players) {
            if (p.getTotal() <= 21 && p.getBid() > 0) anyoneAlive = true;
        }

        dealer.revealCard();
        int dTotal = calculatehand(dealer.getCards());
        System.out.println("\nDealer deckt auf. Stand: " + dTotal);

        if (anyoneAlive) {
            // Dealer muss bis 17 ziehen (Standardregel)
            while (dTotal < 17) {
                int card = deck.drawCard();
                System.out.println("Dealer zieht... " + card);
                dealer.addCard(card);
                dTotal = calculatehand(dealer.getCards());
            }
            dealer.setTotal(dTotal);
            System.out.println("Dealer Endstand: " + dTotal);
        } else {
            dealer.setTotal(dTotal);
        }
    }

    private static void evaluateAll(Player[] players) {
        int dFinal = dealer.getTotal();
        System.out.println("\n--- RUNDEN-ERGEBNISSE ---");
        for (Player p : players) {
            if (p.getBid() == 0) continue;

            int pFinal = p.getTotal();
            int chipsVorher = p.getChips();
            String ergebnisText = "";

            // Vergleichs-Logik und Chip-Aktualisierung
            if (pFinal > 21) {
                ergebnisText = "Überschätzt (Verloren)";
                p.setChips(chipsVorher - p.getBid());
            } else if (dFinal > 21 || pFinal > dFinal) {
                ergebnisText = "Gewonnen!";
                p.setChips(chipsVorher + p.getBid());
            } else if (pFinal < dFinal) {
                ergebnisText = "Verloren.";
                p.setChips(chipsVorher - p.getBid());
            } else {
                ergebnisText = "Push (Unentschieden).";
            }

            System.out.println(p.getName() + " (" + pFinal + ") vs Dealer (" + dFinal + "): "
                    + ergebnisText + " [" + chipsVorher + "c -> " + p.getChips() + "c ]");
        }
    }

    private static void showFinalScore(Player[] players) {
        System.out.println("\n=================================");
        System.out.println("FINALER ENDSTAND DES SPIELS");
        for (Player p : players) {
            System.out.println(p.toString());
        }
        System.out.println("=================================");
    }

    private static boolean checkNextRound(Player[] players) {
        boolean anyoneLeft = false;
        for (Player p : players) {
            if (p.getChips() > 0) anyoneLeft = true;
        }

        if (!anyoneLeft) {
            System.out.println("Alle Spieler sind pleite!");
            return false;
        }

        // Endlosschleife zur Validierung der j/n Eingabe
        while (true) {
            System.out.print("\nNoch eine Runde? (j/n): ");
            String input = scanner.next().toLowerCase();

            if (input.equals("j") || input.equals("ja")) {
                return true;
            } else if (input.equals("n") || input.equals("nein")) {
                return false;
            } else {
                System.out.println("Fehler: Bitte gib nur 'j' für Ja oder 'n' für Nein ein!");
            }
        }
    }

    private static void resetForNextRound(Player[] players) {
        // Reset aller Hand-Werte für alle Spieler und den Dealer
        for (Player p : players) {
            p.setCards(new int[9]);
            p.setStand(false);
            p.setBid(0);
            p.setTotal(0);
        }
        dealer.setCards(new int[9]);
        dealer.setTotal(0);
        dealer.resetHideCard();
    }

    public static int calculatehand(int[] cardvalues) {
        int total = 0;
        int aces = 0;

        for (int v : cardvalues) {
            if (v == 1) { // Ass wird initial als 11 gewertet
                aces++;
                total += 11;
            } else if (v >= 10) { // Bilder zählen 10
                total += 10;
            } else if (v >= 2) {
                total += v;
            }
        }

        // Ass-Logik: 11 wird zu 1, wenn man über 21 kommt
        while (total > 21 && aces > 0) {
            total -= 10;
            aces--;
        }

        return total;
    }
}