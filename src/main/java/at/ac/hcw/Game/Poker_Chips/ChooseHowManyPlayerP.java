//package at.ac.hcw.Game.Poker_Chips;
////für tim
////this class is useless now I belive after PokerSetupController
//import java.util.Scanner;
//
//public class ChooseHowManyPlayerP {
//
//    private final Scanner sc;
//
//    public ChooseHowManyPlayerP(Scanner sc) {
//        this.sc = sc;   // Scanner wird von außen übergeben
//    }
//
//    public void run() {
//        System.out.println("=== POKERCHIPS WIRD GESTARTET ===");
//        int noPlayer = 2;
//        while (true) {
//            System.out.print("Wie viele Spieler? (2–6): ");
//            try {
//                noPlayer = Integer.parseInt(sc.nextLine().trim());
//            } catch (NumberFormatException e) {
//                System.out.println("Bitte eine Zahl eingeben.");
//                continue;
//            }
//            if (noPlayer < 2 || noPlayer > 6) {
//                System.out.println("Bitte zwischen 2 und 6 Spielern wählen.");
//            } else {
//                break;
//            }
//        }
//
//        String[] names = new String[noPlayer];
//        for (int i = 0; i < noPlayer; i++) {
//            System.out.print("Name von Spieler " + (i + 1) + ": ");
//            String name = sc.nextLine().trim();
//            if (name.isEmpty()) {
//                name = "Spieler" + (i + 1);
//            }
//            names[i] = name;
//        }
//
//        int startingMoney;
//        while (true) {
//            System.out.print("Startchips für alle Spieler: ");
//            try {
//                startingMoney = Integer.parseInt(sc.nextLine().trim());
//                if (startingMoney <= 0) {
//                    System.out.println("Startchips müssen > 0 sein.");
//                    continue;
//                }
//                break;
//            } catch (NumberFormatException e) {
//                System.out.println("Bitte eine gültige Zahl eingeben.");
//            }
//        }
//
//
//        System.out.println("""
//                Do you want to change the value of the big and small blind?\
//
//                (Standard big blind: 50, small blind: 20)\
//
//                (Y for Yes, N for No)""");
//        String choice = sc.next();
//
//        while (!choice.equalsIgnoreCase("Y") && !choice.equalsIgnoreCase("N")) {
//            System.out.println("Please enter Y or N:");
//            choice = sc.next();
//        }
//
//        PokerRules pokerGame;
//
//        if (choice.equalsIgnoreCase("Y")) {
//            System.out.println("Enter value for big blind: ");
//            int bigBlind = sc.nextInt();
//            System.out.println("Enter value for small blind: ");
//            int smallBlind = sc.nextInt();
//
//            pokerGame = new PokerRules(noPlayer, bigBlind, smallBlind);
//
//        } else {
//            pokerGame = new PokerRules(noPlayer, 50, 20);
//        }
//
//
//        //pokerGame.playerSetupWithChips(names, int[] startingMoney);
//        pokerGame.startHand();
//    }
//}
//
//
