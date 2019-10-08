/**
 * Created by Jiatong Hao, Xiankang Wu and Lijun Chen on 10/5/2019.
 *
 * This class represents a hand of cards in a blackjack game.
 */

public class TriantaEnaHand extends BlackjackGroupHand {
    private int winValue = 31;
    private int hardValue = 10;

    public TriantaEnaHand() {
        super();
    }
    public TriantaEnaHand(BlackjackGroupCard card) {
        super(card);
    }

    /**
     * Compute the total value of cards within the current hand.
     *
     * @return total value of cards within the current hand.
     */
    public int getTotalValue() {
        int value = 0;
        int aceCount = 0;
        int cardCount = getCardCount();

        for (int i = 0; i < cardCount; i++) {
            // Add the value of each card in the hand
            BlackjackGroupCard card = (BlackjackGroupCard) getCardAt(i);
            int cardSoftValue = card.getSoftValue();
            value += cardSoftValue;

            if (card.getHardValue() == hardValue + 1) {
                aceCount += 1;
            }
        }

        // If there is only one aceCard, pick a hard value or soft value.
        if (aceCount == 1) {
            if (value + hardValue <= winValue) {
                value += hardValue;
            } else if (value + hardValue > winValue && value + 1 <= winValue) {
                value += 1;
            } else {
                value += hardValue;
            }
        }
        // If a hand has more than one Ace cards, use the hard value as long as the hand is not busted
        while (aceCount > 1) {
            value += hardValue;
            aceCount -= 1;
        }

        return value;
    }
}
