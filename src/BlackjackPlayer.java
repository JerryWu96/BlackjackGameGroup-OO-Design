/**
 * Created by Jiatong Hao, Xiankang Wu and Lijun Chen on 10/05/2019.
 *
 * This class represents a player in a traditional Blackjack game.
 */

public class BlackjackPlayer extends BlackjackGroupPlayer{

    public BlackjackPlayer(int id, int balance) {
        super(id, balance);
        addHand(new BlackjackHand());
    }

    public BlackjackHand getHandAt(int idx) {
        return (BlackjackHand) getHands().get(idx);
    }

}
