/**
 * Created by Jiatong Hao, Xiankang Wu and Lijun Chen on 10/7/2019.
 *
 * This class represents a judge in a Trianta Ena game that checks the winner and the actions.
 */

public class TriantaEnaJudge extends BlackjackGroupJudge<TriantaEnaPlayer, TriantaEnaPlayer, TriantaEnaHand> {
    private int INITIAL_CARD_NUM = 3;
    private int FACE_CARD_MIN_VALUE = 11;

    public TriantaEnaJudge(int bankerValue, int winValue) {
        super(bankerValue, winValue);
    }

    @Override
    public boolean isBust(TriantaEnaHand hand) {
        return hand.getTotalValue() > getWinValue();
    }

    public boolean isTriantaEna(TriantaEnaHand hand) {
        return hand.getTotalValue() == getWinValue();
    }

    public boolean isNaturalTriantaEna(TriantaEnaHand hand) {
        if (hand.getCardCount() != INITIAL_CARD_NUM)
            return false;
        int aceCardCount = 0;
        int faceCardCount = 0;
        for (BlackjackGroupCard card : hand.getHand()) {
            if (card.getValue() == 1) {
                aceCardCount++;
            } else if (card.getValue() >= FACE_CARD_MIN_VALUE) {
                faceCardCount++;
            }
        }
        return aceCardCount == 1 && faceCardCount == 2;
    }

    /**
     * Tells if the action for a specific hand of a player is valid.
     *
     * @param player current player.
     * @param hand   hand that the current player is holding.
     * @param action string represents the player action.
     * @return boolean. Returns true if the action is valid, false otherwise.
     */
    public boolean isActionValid(TriantaEnaPlayer player, TriantaEnaHand hand, String action) {
        switch (action) {
            case "hit":
                return !isBust(hand);
        }
        return true;
    }

    /**
     * Tells if the current hand is fold.
     *
     * @param hand the hand instance.
     * @return True if fold, false otherwise.
     */
    public boolean isFold(TriantaEnaHand hand) {
        return hand.getBet() == 0;
    }

    /**
     * Tells if the banker can still hit.
     *
     * @param banker banker instance.
     * @return True if the banker can hit, false otherwise.
     */
    public boolean canBankerHit(TriantaEnaPlayer banker) {
        return banker.getHand().getTotalValue() < getDealerValue();
    }

    /**
     * Compaer each hand of the player and the one of the banker.
     *
     * @param player instance of player.
     * @param banker instance of banker.
     * @return Balance that the current player wins or loses. If wins or tie, it is positive, otherwise it is negative.
     */
    public int checkWinner(TriantaEnaPlayer player, TriantaEnaPlayer banker) {
        TriantaEnaHand bankerHand = banker.getHand();
        int bankerValue = bankerHand.getTotalValue();
        int roundBalance = 0;

        if (isBust(bankerHand)) {
            // if banker is bust
            TriantaEnaHand playerHand = player.getHand();
            int bet = playerHand.getBet();
            if (!isBust(playerHand)) {
                // if not bust, player hand wins
                player.setBalance(bet * 2);
                roundBalance += playerHand.getBet();
                banker.setBalance(-bet);
            } else {
                // if this player hand bust, both player and banker lose, tie
                // if tie, player loses, and banker get the bet
                roundBalance -= bet;
                banker.setBalance(bet);
            }
        } else {
            // if banker does not bust
            TriantaEnaHand playerHand = player.getHand();
            int value = playerHand.getTotalValue();
            int bet = playerHand.getBet();

            if (isBust(playerHand)) {
                // if player hand bust, player hand loses, and banker get the bet
                roundBalance -= bet;
                banker.setBalance(bet);
            } else {
                if (value < bankerValue) { // if player hand not bust
                    // if player hand value < banker hand value, player hand loses
                    roundBalance -= bet;
                    banker.setBalance(bet);
                } else if (value == bankerValue) {
                    if (isNaturalTriantaEna(bankerHand) && isNaturalTriantaEna(playerHand)) {
                        // both banker hand & player hand is natural Trianta Ena, tie
                        // if tie, player loses
                        banker.setBalance(bet);
                        roundBalance -= bet;
                    } else if (isNaturalTriantaEna(bankerHand) && !isTriantaEna(playerHand)) {
                        // banker hand == natural Trianta Ena && player hand == Trianta Ena, player hand loses
                        banker.setBalance(bet);
                        roundBalance -= bet;
                    } else if (isTriantaEna(bankerHand) && isNaturalTriantaEna(playerHand)) {
                        // banker hand == Trianta Ena && player hand == natural Trianta Ena, player hand wins
                        // If the Player wins, they keep their bet and receive their bet * 2 from the Banker
                        player.setBalance(bet * 2);
                        roundBalance += bet;
                        banker.setBalance(-bet);
                    } else {
                        // both Trianta Ena or neither Trianta Ena, nor natural Trianta Ena, tie
                        // if tie, player loses
                        banker.setBalance(bet);
                        roundBalance -= bet;
                    }
                } else {
                    // if player hand value > banker hand value, player hand wins
                    player.setBalance(bet * 2);
                    roundBalance += bet;
                    banker.setBalance(-bet);
                }
            }
        }
        return roundBalance;
    }
}
