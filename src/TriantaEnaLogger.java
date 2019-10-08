/**
 * Created by Jiatong Hao, Xiankang Wu and Lijun Chen on 9/27/2019.
 *
 * This class represents a logger that display info in a Trianta Ena game.
 */

public class TriantaEnaLogger extends BlackjackGroupLogger {

    public void displaySetDefaultParams() {
        msg("Before we start, do you want to change the parameters of the game?");
        msg("Default banker stopping value is 27, and default balance for each player is 100.");
        msg("Enter Y/y to change. All other inputs means using default parameter.");
    }

    public void displaySetBankerParam() {
        msg("Please enter a number between 24 and 29 as banker's stopping value. ");
        msg("All other inputs means using default parameter.");
    }

    public void displaySetPlayerCountInfo(int max_player) {
        msg("Please tell us how many players will join the game.");
        msg("We do not allow new players to join after the game starts.");
        msg("The max number of players allowed is " + max_player + ".");
        msg("Please enter an integer between 2 and " + max_player + " (include banker): ");
    }

    public void displayBankerCard(BlackjackGroupCard card) {
        msg("Banker's face-up card: " + card);
    }

    public void displayBankerHand(TriantaEnaHand hand) {
        msg("Banker's current hand is: \n" + hand);
        msg("Banker's current hand has value: " + hand.getTotalValue() + "\n");
    }

    public void displayPlayerHand(int playerId, TriantaEnaHand hand) {
        msg("Player " + playerId + ", your current hand is: " + hand);
        msg("Your current hand has value: " + hand.getTotalValue());
    }

    public void printBankerBalance(int playerId, int balance, int roundNum) {
        msg("At round " + roundNum + ", Banker " + playerId + " has balance $" + balance + ".\n");
    }
}