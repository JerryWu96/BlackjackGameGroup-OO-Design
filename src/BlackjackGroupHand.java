/**
 * Created by Jiatong Hao, Xiankang Wu and Lijun Chen on 9/27/2019.
 *
 * This class represents a hand of Blackjack cards in a blackjack game or its variation.
 */

public abstract class BlackjackGroupHand extends Hand<BlackjackGroupCard>{

    private int bet;

    public BlackjackGroupHand() {
        super();
    }

    public BlackjackGroupHand(BlackjackGroupCard card) {
        super(card);
    }

    public int getBet() {
        return bet;
    }

    public void setBet(int newBet) {
        bet = newBet;
    }

    public abstract int getTotalValue();
}

