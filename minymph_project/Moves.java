package minymph_project;

import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;
import javax.swing.*;
import java.util.Random;

/**
 * Represents a move in the Minymph game with attributes such as name, damage, PP (Power Points),
 * critical rate, and any potential side effect.
 * Moves can be chosen by players and listed for reference.
 */
public class Moves {
    
    private static List<Moves> normalMoves = new ArrayList<>();
    private GUI gui;
    private String name;
    private int damage;
    private int PP;
    private float critRate;
    private String sideEffect;
    private float accuracy;

    String moveChoice = 
            "1 : Headbutt \n" +
            "2 : Sweep \n" +
            "3 : Haze \n" +
            "4 : HyperRush \n" +
            "-1 : Go back \n";

    /**
     * Constructs a new Move with specified attributes.
     *
     * @param gui the GUI associated with the game
     * @param name the name of the move
     * @param damage the damage value of the move
     * @param PP the Power Points of the move
     * @param critRate the critical rate of the move
     * @param sideEffect any side effect the move may have
     * @param accuracy accuracy of the move
     */
    public Moves(GUI gui, String name, Integer damage, Integer PP, Float critRate, Float accuracy, String sideEffect) {
        this.gui = gui;
        this.name = name;
        this.damage = damage;
        this.PP = PP;
        this.critRate = critRate;
        this.sideEffect = sideEffect;
        this.accuracy = accuracy;
    }

    /**
     * Constructs a new Move associated with a GUI.
     *
     * @param gui the GUI associated with the game
     */
    public Moves(GUI gui) {
        this.gui = gui;
    }

    /**
     * Default constructor for creating a Move.
     */
    public Moves() {
    }

    /**
     * Returns the name of the move.
     *
     * @return the name of the move
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the damage value of the move.
     *
     * @return the damage value of the move
     */
    public int getDamage() {
        return this.damage;
    }

    /**
     * Returns the Power Points (PP) of the move.
     *
     * @return the PP of the move
     */
    public int getPP() {
        return this.PP;
    }
    
    /**
     * Returns the accuracy of the move.
     *
     * @return the accuracy of the move
     */
    public float getAccuracy() {
        return this.accuracy;
    }

    /**
     * Returns the critical rate of the move.
     *
     * @return the critical rate of the move
     */
    public float getCritRate() {
        return this.critRate;
    }

    /**
     * 
     * @param moveAccuracy the selected move accuracy 
     * @return true if the move is executed based on its accuracy
     */
	public static Boolean accuracyCheck(Float moveAccuracy)
	{
		Random random = new Random();
		int randomNumber = random.nextInt(101);
		float floatDebug = (float)(randomNumber);
		if (floatDebug<=moveAccuracy)
		{
			return true;
		}
		return false;
		
	}
	
	/**
	 * 
	 * @param moveCritRate critRate of the chosen move
	 * @return extra damage dealt by the critical move
	 */
	public Float critRateCheck(Float moveCritRate)
	{
		if (accuracyCheck(moveCritRate))// may be difficult to understand but instead of again instantiating a random number, I use the
										// one in accuracyCheck by passing in parameter the selected move critRate which has the same
										// type so thats ok
		{
			return (float)Math.ceil((this.getDamage()*(0.2))); // crit rate currently defined as 1/5 of a move's damage, this may change
		}
		
		return (float)0;
	}
    
    /**
     * Returns the side effect of the move, if any.
     *
     * @return the side effect of the move
     */
    public String getSideEffect() {
        return this.sideEffect;
    }

    /**
     * Prints the name of the move to the console.
     */
    public void printMove() {
        System.out.println(this.name);
    }

    /**
     * Prints the list of all available normal type moves to the console.
     */
    public static void printList() {
        System.out.println("Available normal type moves:");
        for (Moves mov : normalMoves) {
            System.out.println(mov.name);
        }
    }

    /**
     * Adds this move to the list of normal moves.
     */
    public void addToList() {
        normalMoves.add(this);
    }
    
    public static List<Moves> getMoves()
    {
    	return normalMoves;
    }

    /**
     * Opens a dialog for the user to choose a move from the list of normal moves.
     * Once selected, the move is passed to a callback function.
     *
     * @param callback the function to process the selected move
     */
    public void chooseMove(Consumer<Moves> callback) {
        JDialog moveDialog = new JDialog(gui, "Choose a Move", true);
        moveDialog.setSize(300, 200);
        moveDialog.setLayout(new BoxLayout(moveDialog.getContentPane(), BoxLayout.Y_AXIS));

        for (Moves move : normalMoves) {
            JButton moveButton = new JButton(move.getName());
            moveButton.addActionListener(e -> {
                callback.accept(move);
                moveDialog.dispose();
            });
            moveDialog.add(moveButton);
        }

        moveDialog.setVisible(true);
    }
}
