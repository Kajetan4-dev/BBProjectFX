package at.ac.hcw.Game.Poker_Chips;
import java.util.Scanner;


public class PokerRules {
    PokerChipsPlayer[] players;
    private int pot;
    private final int bigBlind;
    private final int smallBlind;
    private int currentBet;

    public PokerRules(int noPlayer, int bigBlind, int smallBlind) {
        this.players = new PokerChipsPlayer[noPlayer];
        this.pot = 0;
        this.bigBlind = bigBlind;
        this.smallBlind = smallBlind;
        this.currentBet = 0;
        //big blind small blind sind falsch

    }

    // for each player enter every value, name etc. currentblind player wird zu bigblind initialisiere diesen array
    public void playerSetup(String[] playerName, int startingMoney) {
        for (int i = 0; i < players.length; i++) {
            players[i] = new PokerChipsPlayer(playerName[i], startingMoney);
        }
        players[0].setBigBlind(0);
        players[1].setBigBlind(1);
    }

    public void rounds() {
        int[] ausgeschieden = new int[players.length];
        Scanner sc = new Scanner(System.in);
        boolean preFlop = true;
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    System.out.println("=== PREFLOP ===");
                    break;
                case 1:
                    System.out.println("=== FLOP ===");
                    break;
                case 2:
                    System.out.println("=== TURN ===");
                    break;
                case 3:
                    System.out.println("=== RIVERS ===");
                    break;
            }
            int winnerIndex = bettingRound(ausgeschieden, sc, preFlop);
            preFlop = false;
            if (winnerIndex == -1){
                while (!checkEveryoneBetSame(ausgeschieden)) {
                    if (winnerIndex >= 0) {
                        break;
                    }
                    bettingRound(ausgeschieden, sc, false);
                }
            }
            if (winnerIndex > -1){
                break;
            }
        }


        int winnerIndex = checkWon(ausgeschieden);


        int newMoney = players[winnerIndex].getPlayerMoney() + pot;
        players[winnerIndex].setPlayerMoney(newMoney);
        System.out.println("Player " + players[winnerIndex].getName() + " wins the pot of " + pot + "chips!");
        pot = 0;

        swapBlinds();

        //check if a player leaves the game
        System.out.println("\\n Will a player leave the table? (yes/no)");
        String leave = sc.nextLine();

        if (leave.equalsIgnoreCase("yes")) {
            System.out.println(" Which player is leaving the table? (index 0 - " + (players.length - 1) + ")");
            int leaveIndex = sc.nextInt();
            sc.nextLine();
            if (leaveIndex >= 0 && leaveIndex < players.length) {
                PokerChipsPlayer[] newPlayers = new PokerChipsPlayer[players.length - 1];
                int idx = 0;
                for (int i = 0; i < players.length; i++) {
                    if (i != leaveIndex) {
                        newPlayers[idx] = players[i];
                        idx++;
                    }
                }
                players = newPlayers;
                System.out.println("Player removed.");
            } else {
                System.out.println("Invalid. No Player removed.");
            }
        }
        //check if a player wants to join
        System.out.println("\\nWill a new player join the table? (yes/no)");
        String add = sc.nextLine();

        if (add.equalsIgnoreCase("yes")) {
            if (players.length >= 6) {
                System.out.println("Table is full! Maximum of 6 players allowed.");
            } else {
                System.out.println("Enter new player name: ");
                String newName = sc.nextLine();

                System.out.println("Enter starting money:");
                newMoney = sc.nextInt();
                sc.nextLine();

                PokerChipsPlayer[] newPlayers = new PokerChipsPlayer[players.length + 1];

                for (int i = 0; i < players.length; i++) {
                    newPlayers[i] = players[i];
                }
                newPlayers[players.length] = new PokerChipsPlayer(newName,newMoney);
                players = newPlayers;

                System.out.println("Player added." + newName);
            }
        }
    }

    private boolean checkEveryoneBetSame(int[] ausgeschieden) {

        for (int i = 0; i < players.length - 1; i++) {
            if (ausgeschieden[i] == 1) {
                continue;
            }
            if (players[i].getBet() != currentBet) {
                return false;
            }
        }
        for (PokerChipsPlayer player : players) {
            player.setBet();
        }
        currentBet = 0;

        return true;

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

    public int checkWon(int[] ausgeschieden) {
        int counter = 0;
        for (int j : ausgeschieden) {
            counter += j;
        }
        if (ausgeschieden.length == counter + 1) {
            for (int i = 0; i < ausgeschieden.length; i++) {
                if (ausgeschieden[i] == 0) {
                    return i;
                }
            }
        }
        return -1;

    }

    private int bettingRound(int[] ausgeschieden, Scanner sc, boolean preFlop) {
        while (true) {
            for (
                    int i = 0;
                    i < players.length; i++) {

                if (ausgeschieden[i] == 1) {
                    continue;
                }
                if (preFlop && i == 0) {
                    players[i].setBet(bigBlind);
                    currentBet += bigBlind;
                    players[i].setPlayerMoney(players[i].getPlayerMoney() - bigBlind);
                    this.pot += bigBlind;
                }
                if (preFlop && i == 1) {
                    players[i].setBet(smallBlind);
                    players[i].setPlayerMoney(players[i].getPlayerMoney() - smallBlind);
                    this.pot += smallBlind;
                }
                System.out.println("\\nPlayer " + players[i].getName() + ", Money: " + players[i].getPlayerMoney());

                if (currentBet == players[i].getBet() && players[i].getBet() > currentBet) {
                    System.out.println("Do you want to Check (C), Raise (R) or Fold(F), current Bet: " + currentBet);
                } else {
                    System.out.println("Do you want to Call(C), Raise (R), Fold(F), current Bet: " + currentBet);
                }
                String CRF = "";
                while (!"C".equals(CRF) && !CRF.equals("R") && !CRF.equals("F")) CRF = sc.nextLine();


                if (CRF.equals("R")) {
                    int Bid = sc.nextInt();
                    players[i].setBet(Bid);
                    currentBet += Bid;
                    players[i].setPlayerMoney(players[i].getPlayerMoney() - (Bid));
                    this.pot += Bid;
                    System.out.println("Player " + players[i].getName() + "Increased the pot by : " + (Bid));
                    System.out.println("Pot is now: " + pot);
                }
                if (CRF.equals("F")) {
                    System.out.println("Player " + players[i].getName() + " folded");
                    ausgeschieden[i] = 1;
                    int winnerIndex = checkWon(ausgeschieden);
                    if (winnerIndex != -1) {
                        return winnerIndex;
                    }
                    continue;
                    //check folds

                }

                if (CRF.equals("C")) {
                    if (players[i].getBet() == currentBet && players[i].getBet() > currentBet) {
                        System.out.println("Player " + players[i].getName() + " checked ");
                    } else {
                        pot += currentBet - players[i].getBet();
                        players[i].setPlayerMoney(players[i].getPlayerMoney() - (currentBet - players[i].getBet()));
                        players[i].setBet(currentBet - players[i].getBet());
                        System.out.println("Player " + players[i].getName() + " called ");

                    }
                }
            }
            System.out.println("Do you want to continue betting?? (Yes, No)");
            String CB = "";
            while (!CB.equals("Yes") && !CB.equals("No")) {
                CB = sc.nextLine();
            }
            if (CB.equals("No")) {
                break;
            }
        }
        return -1;
    }
}
