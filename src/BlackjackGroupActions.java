/**
 * Created by Jiatong Hao, Xiankang Wu and Lijun Chen on 9/27/2019.
 *
 * This interface defines behaviors in a Blackjack game or its variation.
 */


public interface BlackjackGroupActions<H extends BlackjackGroupHand, P extends BlackjackGroupPlayer> {
    /**
     * Deals one card
     *
     * @param deck instance of deck
     * @param hand instance of hand
     */
    void hit(BlackjackGroupDeck deck, H hand);

    /**
     * Player stands.
     */
    void stand();

    /**
     * Ask player to make a bet
     *
     * @param player - instance of player
     */
    void makeBet(P player);

    /**
     * Ask the player if he/she would like to cash out.
     *
     * @param player - instance of player
     * @return true if player cashes out, false otherwise
     */
    boolean cashOut(P player);
}
