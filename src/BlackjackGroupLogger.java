/**
 * Created by Jiatong Hao, Xiankang Wu and Lijun Chen on 9/27/2019.
 *
 * This class represents a logger that display info in a Blackjack game or its variation
 */

public class BlackjackGroupLogger extends Logger {
    public void welcomeMsg() {
        msg("Welcome to our game!");
        msg("########\n");
    }

    public void displaySetBalanceParam() {
        msg("Please enter a number larger than 1 as balance for each player.");
        msg("All other inputs means using default parameter.");
    }

    public void displayInvalidMsgForRange(int min_number, int max_number) {
        msg("Invalid input. Please enter a number between " + min_number + " and " + max_number + ": ");
    }

    public void playHandInfo(int playerId, int playerBalance, int handIdx, int bet) {
        int displayedHandIdx = handIdx + 1;
        msg("Player " + playerId + ", Hand no." + displayedHandIdx + ", Bet for this hand = $" +
                bet + ", Balance = $" + playerBalance );
    }

    public void displayActionChoices(String[] actions) {
        msg("Please select your next action with its corresponding number (e.g., 1 to hit):");
        int idx = 1;
        for (String action : actions) {
            System.out.print(action + ": " + idx++ + "\t");
        }
        System.out.println();
    }

    public void printPlayerBalance(int playerId, int roundBalance, int playerBalance, int roundNum) {
        if (roundBalance > 0)
            msg("At round " + roundNum + ", Player " + playerId + " wins $" + roundBalance + ".");
        else if (roundBalance == 0)
            msg("At round " + roundNum + ", Player " + playerId + " is tie.");
        else
            msg("At round " + roundNum + ", Player " + playerId + " loses $" + -roundBalance + ".");
        msg("Player " + playerId + " current balance is $" + playerBalance);
    }

    public void playerLeaves(int playerId, int balance) {
        msg("Player " + playerId + " leaves the game with $" + balance + "\n");
    }
}
