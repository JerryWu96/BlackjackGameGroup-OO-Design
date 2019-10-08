/**
 * Created by Jiatong Hao, Xiankang Wu and Lijun Chen on 10/05/2019.
 *
 * This class represents a deck of cards in a card game.
 */

import java.util.Collections;
import java.util.List;

public abstract class Deck<E extends Card> {

    private List<E> cards;

    private int cardCount;

    public Deck() {
        cardCount = 52;
        createDeck();
        shuffle();
    }

    public List<E> getCards() {
        return cards;
    }

    public void setCards(List<E> cards) {
        this.cards = cards;
    }

    public int getCardCount() {
        return cardCount;
    }

    public void setCardCount(int num) {
        cardCount += num;
    }

    /**
     * Initialize the deck with cards.
     */
    abstract void createDeck();

    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Get a random card from the deck
     * @return a poker card
     */
    public E dealCard() {
        if (getCardCount() == 0) {
            createDeck();
            shuffle();
            setCardCount(52);
        }
        setCardCount(-1);
        return cards.remove(0);
    }
}
