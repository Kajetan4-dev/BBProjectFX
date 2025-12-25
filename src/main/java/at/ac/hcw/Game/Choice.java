package at.ac.hcw.Game;
import java.util.Scanner;
import at.ac.hcw.Game.Poker_Chips.ChooseHowManyPlayerP;
import at.ac.hcw.Game.Black_Jack.BlackjackRules;


public class Choice {

    // Startbildschirm und Spielauswahl
    public static void start() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Start Blackjack (b) or Pokerchips (p): ");

        String input = sc.nextLine().trim().toLowerCase();

        while (!input.equals("p") && !input.equals("b")) {
            System.out.println("Bitte 'b' oder 'p' eingeben:");
            input = sc.nextLine().trim().toLowerCase();
        }

        switch (input) {
            case "p" -> new ChooseHowManyPlayerP(sc).run();
            case "b" -> BlackjackRules.startGame();
        }
    }
}
