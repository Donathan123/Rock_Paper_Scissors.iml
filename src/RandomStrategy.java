import java.util.Random;

/**
 * The Random strategy. The computer picks Rock, Paper or Scissors at random,
 * the same as a normal game. It does not need any game data, so it is an
 * external class.
 *
 * @author Donathan Chuon
 */
public class RandomStrategy implements Strategy
{
    private final Random rnd = new Random();

    /**
     * Picks a random move. The player's move is ignored.
     *
     * @param playerMove the move the player just made (not used)
     * @return "R", "P" or "S", each with the same chance
     */
    @Override
    public String getMove(String playerMove)
    {
        String computerMove = "";
        int pick = rnd.nextInt(3);

        switch (pick)
        {
            case 0:
                computerMove = "R";
                break;
            case 1:
                computerMove = "P";
                break;
            default:
                computerMove = "S";
                break;
        }

        return computerMove;
    }
}
