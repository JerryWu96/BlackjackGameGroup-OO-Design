/**
 * Created by Jiatong Hao, Xiankang Wu and Lijun Chen on 9/27/2019.
 *
 * A class that represents a Blackjack game or its variation
 */

public abstract class BlackjackGroup<P extends BlackjackGroupPlayer, L extends BlackjackGroupLogger> extends Game{

    private BlackjackGroupDeck deck;

    public BlackjackGroup() {
        setDeck();
    }

    public BlackjackGroupDeck getDeck() {
        return deck;
    }

    public void setDeck() {
        deck = new BlackjackGroupDeck();
    }

    public int getInteger(String str) {
        try {
            int res = Integer.parseInt(str);
            return res;
        } catch (Exception e) {
            return -1;
        }
    }

    public abstract void setPlayerBalance();

    public abstract void setPlayerNumber();

    /**
     * The workflow of all players selecting their actions in a single round.
     */
    public abstract void playersPlay();

    /**
     * Check winning and set balance accordingly.
     * Remove players with $0 balance, and ask other players if they would like to cash out or join the next round.
     */
    public abstract void calcRoundResult();

    /**
     * At the end of each round, reset hands of participants
     */
    public abstract void resetHands();
}
