/**
 * Created by Jiatong Hao, Xiankang Wu and Lijun Chen on 9/27/2019.
 *
 * This class represents a poker card in a Blackjack game or its variation.
 */

public class BlackjackGroupCard extends Card{

    public BlackjackGroupCard(String suit, int value) {
        super(suit, value);
    }

    /**
     *  Get the soft value: 10 for face card, 1 for Ace
     * @return value in int.
     */
    public int getSoftValue() {
        return getValue();
    }

    /**
     *  Get the hard value: 10 for face card, 11 for Ace
     * @return value in int.
     */
    public int getHardValue() {
        return getValue();
    }

}
