/**
 * Created by Jiatong Hao, Xiankang Wu and Lijun Chen on 9/27/2019.
 *
 * This class presents an Ace card in a Blackjack game or its variation.
 */

public class BlackjackGroupAceCard extends BlackjackGroupCard{

    public BlackjackGroupAceCard(String suit, int value) {
        super(suit, value);
    }

    public BlackjackGroupAceCard(String suit) {
        super(suit, 1);
    }

    @Override
    public int getSoftValue() {
        return 1;
    }

    @Override
    public int getHardValue() {
        return 11;
    }

    @Override
    public String toString() {
        return getSuit() + " A";
    }
}
