/**
 * Created by Jiatong Hao, Xiankang Wu and Lijun Chen on 9/27/2019.
 *
 * A class that represents a Blackjack game
 */
import java.util.*;
public class Blackjack extends BlackjackGroup implements BlackjackGroupActions<BlackjackHand, BlackjackPlayer>{
    public final int MAX_PLAYER = 7;
    public final int BALANCE = 100;
    private final int WIN_VAL = 21;
    private final int DEALER_VAL = 17;
    private final int MIN_DEALER_VAL = 16;
    private final int MAX_DEALER_VAL = 18;
    private final int INITIAL_CARD_NUM = 2;

    private List<BlackjackPlayer> playerList;
    private int playerCount;
    private BlackjackDealer dealer;
    private BlackjackJudge judge;
    private BlackjackLogger logger;
    private final String[] actions = {"hit", "stand", "doubleUp", "split"};

    private int winVal = WIN_VAL;
    private int dealerVal = DEALER_VAL;
    private int balance = BALANCE;

    public Blackjack() {
        super();
        logger = new BlackjackLogger();
        logger.welcomeMsg();
        setGameParams();
        setPlayerNumber();
        initGame();
    }

    /**
     * Initialize game params of a BlackJack Game: dealer stopping value, player initial balance
     */
    private void setGameParams() {
        Scanner sc = new Scanner(System.in);
        logger.displaySetDefaultParams();
        String choice = sc.nextLine();
        if (!choice.equals("y") && !choice.equals("Y")) {
            logger.msg("The game will use default parameters.\n");
            return;
        }

        setDealerParam();
        setPlayerBalance();
        logger.msg("The game will use " + dealerVal + " as dealer stopping value and " + balance + " as balance.\n");
    }

    private void setDealerParam() {
        logger.displaySetDealerParam();
        Scanner scanner = new Scanner(System.in);
        int dealerValInput = getInteger(scanner.nextLine());
        if (dealerValInput >= MIN_DEALER_VAL && dealerValInput <= MAX_DEALER_VAL) {
            this.dealerVal = dealerValInput;
        }
    }

    public void setPlayerBalance() {
        logger.displaySetBalanceParam();
        Scanner scanner = new Scanner(System.in);
        int balanceInput = getInteger(scanner.nextLine());
        if (balanceInput > 1) {
            this.balance = balanceInput;
        }
    }

    public void setPlayerNumber() {
        logger.displaySetPlayerCountInfo(MAX_PLAYER);
        Scanner scanner = new Scanner(System.in);
        boolean isPlayerValid = false;
        int playerCountInput = -1;
        while (!isPlayerValid) {
            playerCountInput = getInteger(scanner.nextLine());
            if (1 <= playerCountInput && playerCountInput <= MAX_PLAYER) {
                logger.msg("The game will have " + playerCountInput + " player(s) in the beginning.");
                isPlayerValid = true;
            } else {
                logger.displayInvalidMsgForRange(1, MAX_PLAYER);
            }
        }
        this.playerCount = playerCountInput;
    }

    private void initGame() {
        judge = new BlackjackJudge(dealerVal, winVal);
        dealer = new BlackjackDealer();
        playerList = new ArrayList<>(playerCount);
        for (int i = 0; i < playerCount; i++)
            playerList.add(new BlackjackPlayer(i+1, balance));
    }

    public void start() {
        logger.msg("\nGame starts!");
        while (!playerList.isEmpty()) {
            playARound();
            resetHands();
        }
        logger.msg("\nGame ends.");
    }

    public void playARound() {
        logger.msg("\n*****************\nRound: " + getRound());

        for (BlackjackPlayer player : playerList) {
            makeBet(player);
        }
        dealInitialCards();
        playersPlay();
        dealerPlay();
        calcRoundResult();
        setRound(getRound()+1);
    }

    /**
     * Initialize two cards to both players and dealers in alternating sequence.
     */
    private void dealInitialCards() {
        for (int idx = 0; idx < this.INITIAL_CARD_NUM; idx++) {
            for (BlackjackPlayer player : playerList) {
                BlackjackGroupCard newCard = (BlackjackGroupCard) getDeck().dealCard();
                player.getHandAt(0).addCard(newCard);
            }
            BlackjackGroupCard newCard = (BlackjackGroupCard) getDeck().dealCard();
            dealer.getHand().addCard(newCard);
        }
    }

    @Override
    public void playersPlay() {
        for (BlackjackPlayer player : playerList) {
            logger.msg("\n#################\nPlayer " + player.getId() + " starts!");
            for (int i = 0; i < player.getHandCount(); i++) {
                BlackjackHand hand = player.getHandAt(i);
                logger.playHandInfo(player.getId(), player.getBalance(), i, hand.getBet());
                logger.displayDealerCard(dealer.getVisibleCard());

                if (judge.isNaturalBlackjack(hand)) {
                    logger.displayPlayerHand(hand);
                    logger.msg("Your current hand is a Natural Blackjack! Gorgeous!!!");
                    continue;
                }

                while (!judge.isBust(hand) && !judge.isBlackjack(hand)) {
                    logger.displayPlayerHand(hand);

                    String next_action = getUserAction(player, hand);
                    playAction(player, next_action, hand);
                    if (next_action.equals("stand") || next_action.equals("doubleUp")) {
                        break;
                    }
                }

                logger.displayPlayerHand(hand);

                if (judge.isBlackjack(hand)) {
                    logger.msg("Your current hand is a Blackjack! Congrats!");
                }
                if (judge.isBust(hand)) {
                    int displayedIdx = i + 1;
                    logger.msg("Player " + player.getId() + " hand " + displayedIdx + " is Bust!");
                }
            }
        }
        logger.msg("\nAll players' terms end!");
    }

    /**
     * Ask the player to select the next action, and decide if it is valid.
     * @param player current player.
     * @param hand current hand the player is dealing with.
     * @return string of the action.
     */
    private String getUserAction(BlackjackPlayer player, BlackjackHand hand) {
        Scanner sc = new Scanner(System.in);
        boolean isValid = false;
        int input = -1;
        while (!isValid) {
            logger.displayActionChoices(actions);
            // if input is valid, change isValid = true
            input = getInteger(sc.nextLine());
            if (1 <= input && input <= actions.length && judge.isActionValid(player, hand, actions[input-1])) {
                logger.msg("Your action: " + actions[input - 1]);
                isValid = true;
            } else {
                logger.displayInvalidMsgForRange(1, actions.length);
            }
        }
        return actions[input - 1];
    }

    /**
     * Execute the action selected by a single player with factory design pattern.
     * @param player current player.
     * @param action current action selected by the player.
     * @param hand current hand the player is dealing with.
     */
    private void playAction(BlackjackPlayer player, String action, BlackjackHand hand) {
        switch (action) {
            case "hit":
                hit(getDeck(), hand);
                break;
            case "stand":
                stand();
                break;
            case "doubleUp":
                doubleUp(getDeck(), player, hand);
                break;
            case "split":
                split(player, hand);
                break;
        }
    }

    /**
     * Dealer's action in a round
     */
    private void dealerPlay() {
        logger.msg("\n#################\nDealer starts!");

        BlackjackHand dealerHand = dealer.getHand();

        logger.displayDealerHand(dealerHand);

        if (judge.isNaturalBlackjack(dealerHand)) {
            logger.msg("Dealer's current hand is a Natural Blackjack! Gorgeous!!!");
        }

        while (judge.canDealerHit(dealer)) {
            hit(getDeck(), dealer.getHand());
            logger.msg("Dealer hits!");
            logger.displayDealerHand(dealerHand);
        }

        if (judge.isBlackjack(dealerHand)) {
            logger.msg("Your current hand is a Blackjack! Congrats!");
        }

        if (judge.isBust(dealerHand)) {
            logger.msg("Dealer hand busts!");
        }

        logger.msg("Dealer's term ends!");
        logger.msg("#################\n");
    }

    public void calcRoundResult() {
        List<BlackjackPlayer> toRemove = new ArrayList<>();
        for (BlackjackPlayer player : playerList) {
            int roundBalance = judge.checkWinner(player, dealer);
            logger.printPlayerBalance(player.getId(), roundBalance, player.getBalance(), getRound());
            if (player.getBalance() == 0) {
                logger.playerLeaves(player.getId(), player.getBalance());
                toRemove.add(player);
            }
            else if (cashOut(player)) {
                logger.playerLeaves(player.getId(), player.getBalance());
                toRemove.add(player);
            }
        }
        for (BlackjackPlayer player: toRemove) {
            playerList.remove(player);
        }
    }

    public void resetHands() {
        dealer.clearHands();
        dealer.addHand(new BlackjackHand());
        for (BlackjackPlayer player : playerList) {
            player.clearHands();
            player.addHand(new BlackjackHand());
        }
    }

    @Override
    public void hit(BlackjackGroupDeck deck, BlackjackHand hand) {
        BlackjackGroupCard newCard = (BlackjackGroupCard) deck.dealCard();
        hand.addCard(newCard);
    }

    @Override
    public void stand() {
        return;
    }

    @Override
    public void makeBet(BlackjackPlayer player) {
        Scanner sc = new Scanner(System.in);
        int input;
        boolean isValid = false;
        logger.msg("Current balance of player " + player.getId() + " is: " + player.getBalance());
        logger.msg("Player " + player.getId() + ", please enter an integer between 1 and " + player.getBalance() + " as bet: ");

        while (!isValid) {
            input = getInteger(sc.nextLine());
            if (input >= 1 && input <= player.getBalance()) {
                isValid = true;
                player.getHandAt(0).setBet(input);
                player.setBalance(-input);
            } else {
                logger.msg("Invalid input. Please enter an integer between 1 and " + player.getBalance() + " as bet: ");
            }
        }
    }

    @Override
    public boolean cashOut(BlackjackPlayer player) {
        Scanner scanner = new Scanner(System.in);
        boolean isCashOut = false;
        logger.msg("Player " + player.getId() + ", do you want to cash out? Please enter Y/y for yes. All other input means no.");
        String choice = scanner.nextLine();
        if (choice.equals("y") || choice.equals("Y")) {
            isCashOut = true;
        }
        return isCashOut;
    }

    /**
     * The player could split into two hands, if the two cards in the current hand are of the same rank （10）
     * Cards must be same value - In most casinos, Tens, Jacks, Queens and Kings all count as ten
     * and can be considered the same for splitting rules.
     * @param player instance of player
     * @param hand instance of hand
     */
    public void split(BlackjackPlayer player, BlackjackHand hand) {
        BlackjackGroupCard card = hand.getCardAt(0);
        BlackjackHand newHand = new BlackjackHand(card);
        int handBet = hand.getBet();
        hand.removeCard(card);
        player.addHand(newHand);
        int handCount = player.getHandCount();
        player.getHandAt(handCount - 1).setBet(handBet);
        player.setBalance(-handBet);
    }

    /**
     * The player double up their bets and immediately followed by a hit and stand
     * @param deck instance of deck
     * @param hand instance of hand
     */
    public void doubleUp(BlackjackGroupDeck deck, BlackjackPlayer player, BlackjackHand hand) {
        player.setBalance(-hand.getBet());
        hand.setBet(hand.getBet() * 2);
        hit(deck, hand);
    }
}
