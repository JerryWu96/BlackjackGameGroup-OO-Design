/**
 * Created by Jiatong Hao, Xiankang Wu and Lijun Chen on 9/27/2019.
 *
 * This class represents a judge in a card game that checks the winner.
 */

public abstract class Judge<P, D> {
    public abstract int checkWinner(P playerTypeA, D playerTypeB);
}
