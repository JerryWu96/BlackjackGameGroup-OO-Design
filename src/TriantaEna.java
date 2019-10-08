/**
 * Created by Jiatong Hao, Xiankang Wu and Lijun Chen on 10/7/2019.
 *
 * A class that represents a TriantaEna game
 */
import java.util.*;
public class TriantaEna extends BlackjackGroup implements BlackjackGroupActions<TriantaEnaHand, TriantaEnaPlayer>{
    public final int MAX_PLAYER = 7;
    public final int BALANCE = 100;
    private final int WIN_VAL = 31;
    private final int BANKER_VAL = 27;
    private final int MIN_BANKER_VAL = 24;
    private final int MAX_BANKER_VAL = 29;
    private final int INITIAL_CARD_NUM = 3;

    private List<TriantaEnaPlayer> playerList;
    private int playerCount;
    private TriantaEnaPlayer banker;
    private TriantaEnaJudge judge;
    private TriantaEnaLogger logger;
    private final String[] actions = {"hit", "stand"};

    private int winVal = WIN_VAL;
    private int bankerVal = BANKER_VAL;
    private int balance = BALANCE;

    public TriantaEna() {
        super();
        logger = new TriantaEnaLogger();
        logger.welcomeMsg();
        setGameParams();
        setPlayerNumber();
        initGame();
    }

    /**
     * Initialize game params of TriantaEna Game: banker stopping value, player initial balance
     */
    private void setGameParams() {
        Scanner sc = new Scanner(System.in);
        logger.displaySetDefaultParams();
        String choice = sc.nextLine();
        if (!choice.equals("y") && !choice.equals("Y")) {
            logger.msg("The game will use default parameters.\n");
            return;
        }

        setBankerParam();
        setPlayerBalance();
        logger.msg("The game will use " + bankerVal + " as banker stopping value and " + balance + " as balance.\n");
    }

    private void setBankerParam() {
        logger.displaySetBankerParam();
        Scanner scanner = new Scanner(System.in);
        int bankerValInput = getInteger(scanner.nextLine());
        if (bankerValInput >= MIN_BANKER_VAL && bankerValInput <= MAX_BANKER_VAL) {
            this.bankerVal = bankerValInput;
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
            if (2 <= playerCountInput && playerCountInput <= MAX_PLAYER) {
                logger.msg("The game will have " + playerCountInput + " player(s) in the beginning.");
                isPlayerValid = true;
            } else {
                logger.displayInvalidMsgForRange(2, MAX_PLAYER);
            }
        }
        this.playerCount = playerCountInput;
    }

    private void initGame() {
        judge = new TriantaEnaJudge(bankerVal, winVal);
        banker = new TriantaEnaPlayer(playerCount, balance * 3);
        playerList = new ArrayList<>(playerCount);
        for (int i = 1; i < playerCount; i++)
            playerList.add(new TriantaEnaPlayer(i, balance));
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
        logger.msg("\n*****************\nRound: " + getRound() + "\n");

        // deal one initial card for all player and banker
        for (TriantaEnaPlayer player : playerList) {
            TriantaEnaHand hand = player.getHand();
            BlackjackGroupCard newCard = (BlackjackGroupCard) getDeck().dealCard();
            hand.addCard(newCard);
        }
        BlackjackGroupCard newCard = (BlackjackGroupCard) getDeck().dealCard();
        banker.getHand().addCard(newCard);

        // ask player to make bet or fold
        for (TriantaEnaPlayer player : playerList) {
            logger.displayPlayerHand(player.getId(), player.getHand());
            makeBet(player);
        }

        dealCards(INITIAL_CARD_NUM - 1);
        playersPlay();
        bankerPlay();
        calcRoundResult();
        rotatePlayer();
        setRound(getRound() + 1);
    }

    /**
     * Initialize two cards to both players and bankers in alternating sequence.
     */
    private void dealCards(int cardNum) {
        for (int idx = 0; idx < cardNum; idx++) {
            for (TriantaEnaPlayer player : playerList) {
                if (judge.isFold(player.getHand())) continue;
                TriantaEnaHand hand = player.getHand();
                BlackjackGroupCard newCard = (BlackjackGroupCard) getDeck().dealCard();
                hand.addCard(newCard);
            }
            BlackjackGroupCard newCard = (BlackjackGroupCard) getDeck().dealCard();
            banker.getHand().addCard(newCard);
        }
    }

    @Override
    public void playersPlay() {
        for (TriantaEnaPlayer player : playerList) {
            logger.msg("\n#################\nPlayer " + player.getId() + " starts!");

            TriantaEnaHand hand = player.getHand();

            if (judge.isFold(hand)) {
                logger.msg("Player " + player.getId() + " has folded.");
                logger.displayPlayerHand(player.getId(), hand);
                logger.msg("Go to next player.");
                continue;
            }

            logger.playHandInfo(player.getId(), player.getBalance(), 0, hand.getBet());
            logger.displayBankerCard(banker.getVisibleCard());

            if (judge.isNaturalTriantaEna(hand)) {
                logger.displayPlayerHand(player.getId(), hand);
                logger.msg("Your current hand is a Natural Trianta Ena! Gorgeous!!!");
                continue;
            }

            while (!judge.isBust(hand) && !judge.isTriantaEna(hand)) {
                logger.displayPlayerHand(player.getId(), hand);

                String next_action = getUserAction(player, hand);
                playAction(player, next_action, hand);
                if (next_action.equals("stand")) {
                    break;
                }
            }

            logger.displayPlayerHand(player.getId(), hand);

            if (judge.isTriantaEna(hand) && !judge.isNaturalTriantaEna(hand)) {
                logger.msg("Your current hand is a TriantaEna! Congrats!");
            }

            if (judge.isBust(hand)) {
                logger.msg("Player " + player.getId() + " hand  busts!");
            }
        }
        logger.msg("\nAll players' terms end!");
    }

    /**
     * Ask the player to select the next action, and decide if it is valid.
     *
     * @param player current player.
     * @param hand   current hand the player is dealing with.
     * @return string of the action.
     */
    private String getUserAction(TriantaEnaPlayer player, TriantaEnaHand hand) {
        Scanner sc = new Scanner(System.in);
        boolean isValid = false;
        int input = -1;
        while (!isValid) {
            logger.displayActionChoices(actions);
            // if input is valid, change isValid = true
            input = getInteger(sc.nextLine());
            if (1 <= input && input <= actions.length && judge.isActionValid(player, hand, actions[input - 1])) {
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
     *
     * @param player current player.
     * @param action current action selected by the player.
     * @param hand   current hand the player is dealing with.
     */
    private void playAction(TriantaEnaPlayer player, String action, TriantaEnaHand hand) {
        switch (action) {
            case "hit":
                hit(getDeck(), hand);
                break;
            case "stand":
                stand();
                break;
        }
    }

    /**
     * Banker's action in a round
     */
    private void bankerPlay() {
        logger.msg("\n#################\nBanker starts!");

        TriantaEnaHand bankerHand = banker.getHand();

        logger.displayBankerHand(bankerHand);

        if (judge.isNaturalTriantaEna(bankerHand)) {
            logger.msg("Banker's current hand is a Natural Trianta Ena! Gorgeous!!!");
        }

        while (judge.canBankerHit(banker)) {
            hit(getDeck(), banker.getHand());
            logger.msg("Banker hits!");
            logger.displayBankerHand(bankerHand);
        }

        if (judge.isTriantaEna(bankerHand) && !judge.isNaturalTriantaEna(bankerHand)) {
            logger.msg("Your current hand is a Trianta Ena! Congrats!");
        }

        if (judge.isBust(bankerHand)) {
            logger.msg("Banker hand busts!");
        }

        logger.msg("Banker's term ends!");
        logger.msg("#################\n");
    }

    public void calcRoundResult() {
        List<TriantaEnaPlayer> toRemove = new ArrayList<>();
        for (TriantaEnaPlayer player : playerList) {
            if (!judge.isFold(player.getHand())) {
                int roundBalance = judge.checkWinner(player, banker);
                logger.printPlayerBalance(player.getId(), roundBalance, player.getBalance(), getRound());
            } else {
                logger.msg("At round " + getRound() + ", Player " + player.getId() + " chose to fold.");
                logger.msg("Player" + player.getId() + " has balance " + player.getBalance());
            }
            if (player.getBalance() == 0) {
                logger.playerLeaves(player.getId(), player.getBalance());
                toRemove.add(player);
            } else if (cashOut(player)) {
                logger.playerLeaves(player.getId(), player.getBalance());
                toRemove.add(player);
            }
        }
        logger.printBankerBalance(banker.getId(), banker.getBalance(), getRound());
        for (TriantaEnaPlayer player : toRemove) {
            playerList.remove(player);
        }
    }

    /**
     * Rotate the banker with the player who has balance larger than the dealer and agrees to rotate.
     * If they decline, the player with the next greatest amount is given the same option.
     */
    private void rotatePlayer() {
        // if banker has no balance, rotate banker, remove the previous banker from the game
        if (banker.getBalance() <= 0) {
            logger.msg("Banker " + banker.getId() + " has no balance and leaves the game.\n");
            // if only one player left, game ends
            if (playerList.size() <= 1) {
                logger.msg("Only one player left.");
                playerList.clear();
                return;
            }
            // otherwise, choose the player with the highest balance as banker
            TreeMap<Integer, Integer> sortedBalanceMap = new TreeMap<>(Collections.reverseOrder());
            // TreeMap sort the keys in descending order. Key is the playerBalance, and the value is the playerID
            for (TriantaEnaPlayer player : playerList) {
                sortedBalanceMap.put(player.getBalance(), player.getId());
            }
            int highestPlayerId = sortedBalanceMap.firstEntry().getValue();
            for (TriantaEnaPlayer player : playerList) {
                if (player.getId() == highestPlayerId) {
                    banker = player;
                    playerList.remove(player);
                    logger.msg("Player " + highestPlayerId + " has the highest balance and is now the new Banker!\n");
                    return;
                }
            }
        }

        // if banker stays, check if any player is eligible and willing to be the new banker
        TreeMap<Integer, Integer> sortedBalanceMap = new TreeMap<>(Collections.reverseOrder());
        int count = 0;

        // TreeMap sort the keys in descending order. Key is the playerBalance, and the value is the playerID
        for (TriantaEnaPlayer player : playerList) {
            sortedBalanceMap.put(player.getBalance(), player.getId());
        }

        for (Map.Entry<Integer, Integer> entry : sortedBalanceMap.entrySet()) {
            Scanner sc = new Scanner(System.in);
            Integer balance = entry.getKey();
            Integer playerID = entry.getValue();
            if (++count == 1 && balance <= banker.getBalance()) {
                // logger.msg("Current balance of banker " + banker.getId() + " is: " + banker.getBalance());
                logger.msg("No one is eligible to become the new Banker.");
                return;
            }
            if (balance <= banker.getBalance()) {
                logger.msg("Banker remains the same");
                return;
            }
            logger.msg("Player " + playerID + ", your current balance = $" + balance +
                    " exceeds that of the Banker.");
            logger.msg("If you want to become the Banker, please enter Y/y to rotate. " +
                    "Enter other inputs to decline.");
            String choice = sc.nextLine();
            if (!choice.equals("y") && !choice.equals("Y")) {
                logger.msg("Player " + playerID + " did not become the Banker.\n");
            } else {
                logger.msg("Player " + playerID + " is now the new Banker!\n");
                logger.msg("Banker " + banker.getId() + " now becomes Player " + banker.getId() + "!\n");
                playerList.add(banker);
                for (TriantaEnaPlayer player : playerList) {
                    if (player.getId() == playerID) {
                        banker = player;
                        playerList.remove(player);
                        return;
                    }
                }
            }
        }
    }

    public void resetHands() {
        banker.clearHands();
        banker.addHand(new TriantaEnaHand());
        for (TriantaEnaPlayer player : playerList) {
            player.clearHands();
            player.addHand(new TriantaEnaHand());
        }
    }

    @Override
    public void hit(BlackjackGroupDeck deck, TriantaEnaHand hand) {
        BlackjackGroupCard newCard = (BlackjackGroupCard) deck.dealCard();
        hand.addCard(newCard);
    }

    @Override
    public void stand() {
        return;
    }

    @Override
    public void makeBet(TriantaEnaPlayer player) {
        Scanner sc = new Scanner(System.in);
        int input;
        boolean isValid = false;
        logger.msg("Current balance of player " + player.getId() + " is: " + player.getBalance());
        logger.msg("Player " + player.getId() + ", please enter an integer between 1 and " + player.getBalance() + " as bet.");
        logger.msg("Enter 0 if you decide to fold.");

        while (!isValid) {
            input = getInteger(sc.nextLine());
            if (input >= 0 && input <= player.getBalance()) {
                isValid = true;
                player.getHand().setBet(input);
                player.setBalance(-input);
            } else {
                logger.msg("Invalid input. Please enter an integer between 0 and " + player.getBalance() + " as bet: ");
            }
        }
    }

    @Override
    public boolean cashOut(TriantaEnaPlayer player) {
        Scanner scanner = new Scanner(System.in);
        boolean isCashOut = false;
        logger.msg("Player " + player.getId() + ", do you want to cash out? Please enter Y/y for yes. All other input means no.");
        String choice = scanner.nextLine();
        if (choice.equals("y") || choice.equals("Y")) {
            isCashOut = true;
        }
        return isCashOut;
    }
}
