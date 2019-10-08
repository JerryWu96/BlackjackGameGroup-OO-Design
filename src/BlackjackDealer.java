/**
 * Created by Jiatong Hao, Xiankang Wu and Lijun Chen on 9/27/2019.
 *
 * This class represents a dealer in a traditional Blackjack game.
 */

public class BlackjackDealer extends Player<BlackjackHand> {
    public BlackjackDealer() {
        super();
        addHand(new BlackjackHand());
    }

    /**
     * Get the face-up card in dealer's hand. In default it returns the second one.
     * First one is treated as face-down.
     *
     * @return the card that is face-down.
     */
    public BlackjackGroupCard getVisibleCard() {
        return (getHands().get(0)).getCardAt(1);
    }

    public BlackjackHand getHand() {
        return getHands().get(0);
    }
}
