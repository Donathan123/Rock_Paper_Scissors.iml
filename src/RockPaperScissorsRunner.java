import javax.swing.SwingUtilities;

/**
 * Main class for the Rock Paper Scissors game.
 * It creates the game frame on the Swing event thread and shows it.
 *
 * @author Donathan Chuon
 */
public class RockPaperScissorsRunner
{
    /**
     * Starts the game.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                RockPaperScissorsFrame frame = new RockPaperScissorsFrame();
                frame.setVisible(true);
            }
        });
    }
}
