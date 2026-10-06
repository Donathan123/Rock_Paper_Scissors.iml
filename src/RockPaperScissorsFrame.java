import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Random;

/**
 * The GUI for a Rock Paper Scissors game where the player plays against the
 * computer.
 *
 * The player clicks Rock, Paper or Scissors. The computer then picks one of
 * five strategies based on probability (Cheat, Least Used, Most Used,
 * Last Used or Random) and uses it to get its move. The result of every game
 * is added to the text area and the running totals are kept in the stats panel.
 *
 * Cheat and Random are external classes. Least Used, Most Used and Last Used
 * are inner classes because they need the record of the player's moves that
 * is kept in this frame.
 *
 * @author Donathan Chuon
 */
public class RockPaperScissorsFrame extends JFrame
{
    // GUI components
    private JPanel mainPnl;
    private JPanel topPnl;
    private JPanel titlePnl;
    private JPanel buttonPnl;
    private JPanel statsPnl;
    private JPanel resultsPnl;

    private JLabel titleLbl;

    private JButton rockBtn;
    private JButton paperBtn;
    private JButton scissorsBtn;
    private JButton quitBtn;

    private JLabel playerWinsLbl;
    private JLabel computerWinsLbl;
    private JLabel tiesLbl;
    private JTextField playerWinsTF;
    private JTextField computerWinsTF;
    private JTextField tiesTF;

    private JTextArea resultsTA;
    private JScrollPane scroller;

    // Running totals for the session
    private int playerWins = 0;
    private int computerWins = 0;
    private int ties = 0;

    // Record of the player's moves, used by the inner class strategies
    private int rockCount = 0;
    private int paperCount = 0;
    private int scissorsCount = 0;
    private String lastPlayerMove = "";

    // One instance of each strategy
    private Strategy cheat = new Cheat();
    private Strategy leastUsed = new LeastUsed();
    private Strategy mostUsed = new MostUsed();
    private Strategy lastUsed = new LastUsed();
    private Strategy random = new RandomStrategy();

    private Random rnd = new Random();

    /**
     * Builds the frame: title, button panel, stats panel and results panel.
     */
    public RockPaperScissorsFrame()
    {
        mainPnl = new JPanel();
        mainPnl.setLayout(new BorderLayout());

        topPnl = new JPanel();
        topPnl.setLayout(new BoxLayout(topPnl, BoxLayout.Y_AXIS));

        createTitlePanel();
        topPnl.add(titlePnl);

        createButtonPanel();
        topPnl.add(buttonPnl);

        createStatsPanel();
        topPnl.add(statsPnl);

        mainPnl.add(topPnl, BorderLayout.NORTH);

        createResultsPanel();
        mainPnl.add(resultsPnl, BorderLayout.CENTER);

        add(mainPnl);

        setTitle("Rock Paper Scissors Game");
        setSize(720, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // ---------------------------------------------------------------
    // GUI set up
    // ---------------------------------------------------------------

    /**
     * Creates the panel that holds the title of the game.
     */
    private void createTitlePanel()
    {
        titlePnl = new JPanel();
        titleLbl = new JLabel("Rock Paper Scissors Game", JLabel.CENTER);
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 28));
        titlePnl.add(titleLbl);
    }

    /**
     * Creates the bordered panel with the Rock, Paper, Scissors and Quit
     * buttons. Rock, Paper and Scissors all share one ActionListener.
     */
    private void createButtonPanel()
    {
        buttonPnl = new JPanel();
        buttonPnl.setLayout(new GridLayout(1, 4, 10, 10));
        buttonPnl.setBorder(new TitledBorder(new EtchedBorder(), "Pick your move"));

        rockBtn = createGameButton("Rock", "rock.png");
        paperBtn = createGameButton("Paper", "paper.png");
        scissorsBtn = createGameButton("Scissors", "scissors.png");
        quitBtn = createGameButton("Quit", "quit.png");

        // A single listener for the three move buttons
        MoveListener moveListener = new MoveListener();
        rockBtn.addActionListener(moveListener);
        paperBtn.addActionListener(moveListener);
        scissorsBtn.addActionListener(moveListener);

        quitBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                quitGame();
            }
        });

        buttonPnl.add(rockBtn);
        buttonPnl.add(paperBtn);
        buttonPnl.add(scissorsBtn);
        buttonPnl.add(quitBtn);
    }

    /**
     * Creates one button with its text under its picture.
     *
     * @param text      the text shown on the button
     * @param imageFile the name of the image file in the src directory
     * @return the finished button
     */
    private JButton createGameButton(String text, String imageFile)
    {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));

        URL imageURL = getClass().getResource("/" + imageFile);
        if (imageURL != null)
        {
            button.setIcon(new ImageIcon(imageURL));
        }

        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setFocusPainted(false);
        return button;
    }

    /**
     * Creates the stats panel with a label and a read only text field for
     * the player wins, the computer wins and the ties.
     */
    private void createStatsPanel()
    {
        statsPnl = new JPanel();
        statsPnl.setBorder(new TitledBorder(new EtchedBorder(), "Stats"));

        Font statsFont = new Font("SansSerif", Font.PLAIN, 16);

        playerWinsLbl = new JLabel("Player Wins:");
        playerWinsLbl.setFont(statsFont);
        playerWinsTF = new JTextField("0", 4);
        playerWinsTF.setFont(statsFont);
        playerWinsTF.setEditable(false);
        playerWinsTF.setHorizontalAlignment(JTextField.CENTER);

        computerWinsLbl = new JLabel("Computer Wins:");
        computerWinsLbl.setFont(statsFont);
        computerWinsTF = new JTextField("0", 4);
        computerWinsTF.setFont(statsFont);
        computerWinsTF.setEditable(false);
        computerWinsTF.setHorizontalAlignment(JTextField.CENTER);

        tiesLbl = new JLabel("Ties:");
        tiesLbl.setFont(statsFont);
        tiesTF = new JTextField("0", 4);
        tiesTF.setFont(statsFont);
        tiesTF.setEditable(false);
        tiesTF.setHorizontalAlignment(JTextField.CENTER);

        statsPnl.add(playerWinsLbl);
        statsPnl.add(playerWinsTF);
        statsPnl.add(Box.createHorizontalStrut(20));
        statsPnl.add(computerWinsLbl);
        statsPnl.add(computerWinsTF);
        statsPnl.add(Box.createHorizontalStrut(20));
        statsPnl.add(tiesLbl);
        statsPnl.add(tiesTF);
    }

    /**
     * Creates the results panel: a text area in a scroll pane that shows
     * one line for every game played in the session.
     */
    private void createResultsPanel()
    {
        resultsPnl = new JPanel();
        resultsPnl.setLayout(new BorderLayout());
        resultsPnl.setBorder(new TitledBorder(new EtchedBorder(), "Results"));

        resultsTA = new JTextArea(12, 50);
        resultsTA.setFont(new Font("Monospaced", Font.PLAIN, 14));
        resultsTA.setEditable(false);

        scroller = new JScrollPane(resultsTA);
        resultsPnl.add(scroller, BorderLayout.CENTER);
    }

    /**
     * The single listener for the Rock, Paper and Scissors buttons.
     * It uses the ActionEvent to find out which button was clicked and
     * plays a game with that move.
     */
    private class MoveListener implements ActionListener
    {
        /**
         * Plays one game using the button that was clicked as the player move.
         *
         * @param ae the event from the button that was clicked
         */
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            Object source = ae.getSource();

            if (source == rockBtn)
            {
                playGame("R");
            }
            else if (source == paperBtn)
            {
                playGame("P");
            }
            else if (source == scissorsBtn)
            {
                playGame("S");
            }
        }
    }

    // ---------------------------------------------------------------
    // Game play
    // ---------------------------------------------------------------

    /**
     * Plays one game. Picks the computer's strategy from a random number
     * between 1 and 100, gets the computer's move from that strategy, works
     * out who won, then updates the stats and the results display.
     *
     * 1 - 10 Cheat, 11 - 30 Least Used, 31 - 50 Most Used,
     * 51 - 70 Last Used, 71 - 100 Random
     *
     * @param playerMove the player's move: "R", "P" or "S"
     */
    private void playGame(String playerMove)
    {
        Strategy strategy;
        String strategyName;

        int probability = rnd.nextInt(100) + 1;

        if (probability <= 10)
        {
            strategy = cheat;
            strategyName = "Cheat";
        }
        else if (probability <= 30)
        {
            strategy = leastUsed;
            strategyName = "Least Used";
        }
        else if (probability <= 50)
        {
            strategy = mostUsed;
            strategyName = "Most Used";
        }
        else if (probability <= 70 && !lastPlayerMove.equals(""))
        {
            strategy = lastUsed;
            strategyName = "Last Used";
        }
        else
        {
            // 71 - 100, and also Last Used on the first round when
            // there is no last move yet
            strategy = random;
            strategyName = "Random";
        }

        String computerMove = strategy.getMove(playerMove);

        String resultMsg;

        if (playerMove.equals(computerMove))
        {
            ties++;
            resultMsg = getSymbolName(playerMove) + " ties " + getSymbolName(computerMove)
                    + ". (Tie! Computer: " + strategyName + ")";
        }
        else if (getWinningMove(computerMove).equals(playerMove))
        {
            playerWins++;
            resultMsg = getWinPhrase(playerMove)
                    + ". (Player wins! Computer: " + strategyName + ")";
        }
        else
        {
            computerWins++;
            resultMsg = getWinPhrase(computerMove)
                    + ". (Computer wins! Computer: " + strategyName + ")";
        }

        // Record the player's move for the strategies to use next time.
        // This is done after the computer has moved so only Cheat ever
        // gets to use the current move.
        switch (playerMove)
        {
            case "R":
                rockCount++;
                break;
            case "P":
                paperCount++;
                break;
            case "S":
                scissorsCount++;
                break;
        }
        lastPlayerMove = playerMove;

        playerWinsTF.setText("" + playerWins);
        computerWinsTF.setText("" + computerWins);
        tiesTF.setText("" + ties);

        resultsTA.append(resultMsg + "\n");
        resultsTA.setCaretPosition(resultsTA.getDocument().getLength());
    }

    /**
     * Asks the player to confirm and then ends the program.
     */
    private void quitGame()
    {
        int answer = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to quit?",
                "Quit", JOptionPane.YES_NO_OPTION);

        if (answer == JOptionPane.YES_OPTION)
        {
            System.exit(0);
        }
    }

    /**
     * Gets the symbol that beats the symbol passed in.
     *
     * @param move "R", "P" or "S"
     * @return the move that wins against it
     */
    private String getWinningMove(String move)
    {
        switch (move)
        {
            case "R":
                return "P";
            case "P":
                return "S";
            default:
                return "R";
        }
    }

    /**
     * Gets the full name of a symbol for the display.
     *
     * @param move "R", "P" or "S"
     * @return "Rock", "Paper" or "Scissors"
     */
    private String getSymbolName(String move)
    {
        switch (move)
        {
            case "R":
                return "Rock";
            case "P":
                return "Paper";
            default:
                return "Scissors";
        }
    }

    /**
     * Gets the phrase that describes how the winning symbol wins.
     *
     * @param winningMove the move that won: "R", "P" or "S"
     * @return for example "Rock breaks Scissors"
     */
    private String getWinPhrase(String winningMove)
    {
        switch (winningMove)
        {
            case "R":
                return "Rock breaks Scissors";
            case "P":
                return "Paper covers Rock";
            default:
                return "Scissors cuts Paper";
        }
    }

    // ---------------------------------------------------------------
    // Inner class strategies - these need the player's move record
    // ---------------------------------------------------------------

    /**
     * The Least Used strategy. It assumes the player is most likely to use
     * the symbol they have used the least, so the computer picks the symbol
     * that beats it. An inner class so it can read the player symbol counts.
     */
    private class LeastUsed implements Strategy
    {
        /**
         * Picks the move that beats the player's least used symbol.
         *
         * @param playerMove the move the player just made (not used)
         * @return the computer's move: "R", "P" or "S"
         */
        @Override
        public String getMove(String playerMove)
        {
            String least = "R";
            int leastCount = rockCount;

            if (paperCount < leastCount)
            {
                least = "P";
                leastCount = paperCount;
            }
            if (scissorsCount < leastCount)
            {
                least = "S";
            }

            return getWinningMove(least);
        }
    }

    /**
     * The Most Used strategy. It assumes the player will keep picking the
     * symbol they use the most, so the computer picks the symbol that beats
     * it. An inner class so it can read the player symbol counts.
     */
    private class MostUsed implements Strategy
    {
        /**
         * Picks the move that beats the player's most used symbol.
         *
         * @param playerMove the move the player just made (not used)
         * @return the computer's move: "R", "P" or "S"
         */
        @Override
        public String getMove(String playerMove)
        {
            String most = "R";
            int mostCount = rockCount;

            if (paperCount > mostCount)
            {
                most = "P";
                mostCount = paperCount;
            }
            if (scissorsCount > mostCount)
            {
                most = "S";
            }

            return getWinningMove(most);
        }
    }

    /**
     * The Last Used strategy. The computer uses the symbol the player used
     * on the last round. An inner class so it can read the player's last
     * move. It is never picked on the first round.
     */
    private class LastUsed implements Strategy
    {
        /**
         * Returns the symbol the player used on the last round.
         *
         * @param playerMove the move the player just made (not used)
         * @return the player's move from the last round
         */
        @Override
        public String getMove(String playerMove)
        {
            if (lastPlayerMove.equals(""))
            {
                // Safety check: no last move yet, so play a random one
                return random.getMove(playerMove);
            }
            return lastPlayerMove;
        }
    }
}
