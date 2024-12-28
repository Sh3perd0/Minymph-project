package minymph_project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * GUI - Class for the user interface of a Minymph battle game.
 * Displays game information, handles user input, and provides action buttons for interaction.
 */
@SuppressWarnings("serial")
public class GUI extends JFrame {

    // GUI components
    private JTextArea gameOutputArea;
    private JTextField userInputField;
    private JButton attackButton;
    private JButton bagButton;
    private JButton minymphButton;
    private JButton surrenderButton;
    
    // Players and Minymph
    private Player player;
    private Player enemy;
    private Minymph playerMinymph;
    private Minymph enemyMinymph;

    // Battle manager instance
    private BattleSolo battle;

    /**
     * Constructor for GUI - initializes the game window and sets up components.
     */
    public GUI() {
        // Main window configuration
        setTitle("Minymph Battle");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Game message display area
        gameOutputArea = new JTextArea(10, 50);
        gameOutputArea.setEditable(false);
        add(new JScrollPane(gameOutputArea), BorderLayout.CENTER);

        // Action panel (buttons)
        JPanel actionPanel = new JPanel(new FlowLayout());
        attackButton = new JButton("Attack");
        bagButton = new JButton("Bag");
        minymphButton = new JButton("Minymph");
        surrenderButton = new JButton("Surrender");

        actionPanel.add(attackButton);
        actionPanel.add(bagButton);
        actionPanel.add(minymphButton);
        actionPanel.add(surrenderButton);
        add(actionPanel, BorderLayout.NORTH);

        // Set up action listeners for the buttons
        setupButtonListeners();

        initializeGame();
    }

    /**
     * Disables all action buttons.
     */
    public void disableButtons() {
        attackButton.setEnabled(false);
        bagButton.setEnabled(false);
        minymphButton.setEnabled(false);
        surrenderButton.setEnabled(false);
        repaint();
    }
    
    /**
     * Disables Attack button
     */
    public void disableAttack()
    {
    	attackButton.setEnabled(false);
    	repaint();
    }
    
    /**
     * Disables Bag button
     */
    
    public void disableBag()
    {
    	bagButton.setEnabled(false);
    	repaint();
    }
    
    
    /**
     * Enables Attack button
     */
    public void enableAttack()
    {
    	attackButton.setEnabled(true);
    	repaint();
    }
    
    /**
     * Enables Bag button
     */
    
    public void enableBag()
    {
    	bagButton.setEnabled(true);
    	repaint();
    }

    /**
     * Enables all action buttons.
     */
    public void enableButtons() {
        attackButton.setEnabled(true);
        bagButton.setEnabled(true);
        minymphButton.setEnabled(true);
        surrenderButton.setEnabled(true);
    }

    /**
     * Initializes the game by creating Minymph and starting a battle instance.
     */
    private void initializeGame() {
        playerMinymph = new Minymph(this, "Oenoko", player);
        enemyMinymph = new Minymph(this, "Mangecailles", enemy);
        battle = new BattleSolo(this, playerMinymph, enemyMinymph, playerMinymph);
    }

    /**
     * Retrieves the game output area to display game messages.
     *
     * @return the game output text area
     */
    public JTextArea getGameOutputArea() {
        return this.gameOutputArea;
    }

    /**
     * Retrieves the user input text field.
     *
     * @return the user input text field
     */
    public JTextField getUserInputField() {
        return this.userInputField;
    }

    /**
     * Configures action listeners for each button.
     * Each button triggers a specific action in the BattleSolo class.
     */
    private void setupButtonListeners() {
        attackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                battle.handleUserInput("1"); // Attack
            }
        });

        bagButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                battle.handleUserInput("2"); // Open bag
            }
        });

        minymphButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                battle.handleUserInput("3"); // Switch Minymph
            }
        });

        surrenderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                battle.handleUserInput("4"); // Surrender
            }
        });
    }

    /**
     * Main entry point to launch the application and display the GUI.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI().setVisible(true));
    }
}
