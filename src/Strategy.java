/**
 * A strategy the computer can use to pick its move in Rock Paper Scissors.
 * This is the Strategy design pattern: every way of choosing a move is a
 * class that implements this one method, so the game can swap between them.
 *
 * @author Donathan Chuon
 */
public interface Strategy
{
    /**
     * Picks the computer's move.
     *
     * @param playerMove the move the player just made: "R", "P" or "S"
     * @return the computer's move: "R", "P" or "S"
     */
    public String getMove(String playerMove);
}
