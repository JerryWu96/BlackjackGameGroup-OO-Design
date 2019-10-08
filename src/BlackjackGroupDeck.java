/**
 * Created by Jiatong Hao, Xiankang Wu and Lijun Chen on 9/27/2019.
 *
 * This class represents a deck of cards in a Blackjack game or its variation.
 */

import java.util.ArrayList;
import java.util.List;

public class BlackjackGroupDeck extends Deck{
    public void createDeck() {
        List cards = new ArrayList<BlackjackGroupCard>();
        String[] suits = new String[]{"Spade", "Heart", "Club", "Diamond"};
        for (String suit : suits) {
            cards.add(new BlackjackGroupAceCard(suit));
            for (int i = 2; i <= 10; i++) {
                cards.add(new BlackjackGroupCard(suit, i));
            }
            for (int i = 11; i <= 13; i++) {
                cards.add(new BlackjackGroupFaceCard(suit, i));
            }
        }
        setCards(cards);
    }
}
