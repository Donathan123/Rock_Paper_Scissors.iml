/**
 * The Cheat strategy. The computer looks at the move the player already made
 * and picks the symbol that beats it, so the computer always wins.
 * It does not need any game data, so it is an external class.
 *
 * @author Donathan Chuon
 */
public class Cheat implements Strategy
{
    /**
     * Picks the symbol that beats the player's move.
     *
     * @param playerMove the move the player just made: "R", "P" or "S"
     * @return the winning move, or "X" if the player move is not valid
     */
    @Override
    public String getMove(String playerMove)
    {
        String computerMove = "";

        switch (playerMove)
        {
            case "R":
                computerMove = "P";
                break;
            case "P":
                computerMove = "S";
                break;
            case "S":
                computerMove = "R";
                break;
            default:
                computerMove = "X";
                break;
        }

        return computerMove;
    }
}
