/**
 * Created by Jiatong Hao, Xiankang Wu and Lijun Chen on 10/05/2019.
 *
 * This class represents a player in a Blackjack game or its variation.
 */

public class BlackjackGroupPlayer extends Player {
    private int balance;

    /**
     * Constructor.
     * @param id      player id.
     * @param balance Initial balance for the player.
     */
    public BlackjackGroupPlayer(int id, int balance) {
        super(id);
        this.balance = balance;
    }

    public int getBalance() {
        return balance;
    }

    /**
     * Setter for player's balance
     * @param currency the amount to be changed
     */
    public void setBalance(int currency) {
        balance += currency;
    }

    public int getHandCount() {
        return getHands().size();
    }
}
