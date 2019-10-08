/**
 * Created by Jiatong Hao, Xiankang Wu and Lijun Chen on 9/27/2019.
 *
 * This class represents a logger that display info in a Blackjack game.
 */

public class BlackjackLogger extends BlackjackGroupLogger {

    public void displaySetDefaultParams() {
        msg("Before we start, do you want to change the parameters of the game?");
        msg("Default dealer stopping value is 17, and default balance for each player is 100.");
        msg("Enter Y/y to change. All other inputs means using default parameter.");
    }

    public void displaySetDealerParam() {
        msg("Please enter a number between 16 and 18 as dealer's stopping value. ");
        msg("All other inputs means using default parameter.");
    }

    public void displaySetPlayerCountInfo(int max_player) {
        msg("Please tell us how many players will join the game.");
        msg("We do not allow new players to join after the game starts.");
        msg("The max number of players allowed is " + max_player + ".");
        msg("Please enter an integer between 1 and " + max_player + ": ");
    }

    public void displayDealerCard(BlackjackGroupCard card) {
        msg("Dealer's face-up card: " + card);
    }

    public void displayDealerHand(BlackjackHand hand) {
        msg("Dealer's current hand is: \n" + hand);
        msg("Dealer's current hand has value: " + hand.getTotalValue() + "\n");
    }

    public void displayPlayerHand(BlackjackHand hand) {
        msg("Your current hand is: " + hand);
        msg("Your current hand has value: " + hand.getTotalValue() + "\n");
    }
}