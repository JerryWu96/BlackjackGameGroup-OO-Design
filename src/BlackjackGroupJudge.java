/**
 * Created by Jiatong Hao, Xiankang Wu and Lijun Chen on 9/27/2019.
 *
 * This class represents a judge in a Blackjack game or its variation
 */

public abstract class BlackjackGroupJudge<P extends BlackjackGroupPlayer, D, H extends BlackjackGroupHand> extends Judge <P, D> {
    private int dealerValue;
    private int winValue;

    public BlackjackGroupJudge(int dealerValue, int winValue) {
        this.dealerValue = dealerValue;
        this.winValue = winValue;
    }

    public int getDealerValue() {
        return dealerValue;
    }

    public void setDealerValue(int dealerValue) {
        this.dealerValue = dealerValue;
    }

    public int getWinValue() {
        return winValue;
    }

    public void setWinValue(int winValue) {
        this.winValue = winValue;
    }

    public boolean isEnoughBalance(P player, int bet) {
        return player.getBalance() >= bet;
    }

    /**
     * Tells if the current hand busts.
     *
     * @param hand the hand instance.
     * @return True if bust, false otherwise.
     */
    public abstract boolean isBust(H hand);

    public abstract int checkWinner(P player1, D player2);
}
