/**
 * Created by Jiatong Hao, Xiankang Wu and Lijun Chen on 10/05/2019.
 *
 * This class represents a player in a Trianta Ena game.
 */

public class TriantaEnaPlayer extends BlackjackGroupPlayer {

    public TriantaEnaPlayer(int id, int balance) {
        super(id, balance);
        addHand(new TriantaEnaHand());
    }

    /**
     * Get the player's hand in a Trianta Ena game.
     * @return a hand instance
     */
    public TriantaEnaHand getHand() {
        return (TriantaEnaHand) getHands().get(0);
    }

    /**
     * Get the face-up card in the hand. In default it returns the second one.
     * First one is treated as face-down.
     * @return the card that is face-down.
     */
    public BlackjackGroupCard getVisibleCard() {
        return getHand().getCardAt(1);
    }
}
