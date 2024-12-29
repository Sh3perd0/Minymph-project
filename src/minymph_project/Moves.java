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
    private static List<Moves> aiNormalMoves = new ArrayList<>();
    private GUIBattle gui;
    private String name;
    private int damage;
    private int PP;
    private float critRate;
    private String sideEffect;
    private float accuracy;
    private Player owner;

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
     * @param owner owner of the minymph
     */
    public Moves(GUIBattle gui, String name, Integer damage, Integer PP, Float critRate, Float accuracy, String sideEffect, Player owner) {
        this.gui = gui;
        this.name = name;
        this.damage = damage;
        this.PP = PP;
        this.critRate = critRate;
        this.sideEffect = sideEffect;
        this.accuracy = accuracy;
        this.owner = owner;
    }

    /**
     * Constructs a new Move associated with a GUI.
     *
     * @param gui the GUI associated with the game
     */
    public Moves(GUIBattle gui) {
        this.gui = gui;
    }

    /**
     * Gives the move's minymph owner (to avoid sharing move's pp)
     * @return owner of a minymph's move
     */
    public Player getOwner()
    {
    	return this.owner;
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
     * Set PP of a move
     * @param PP value to be entered
     */
    public void setPP(Integer PP)
    {
    	if (this.PP<0 && this.getOwner() instanceof ConcretePlayer)
    	{
    		this.PP = 0; // make sure the PP value is always >=0
    	}
    	this.PP = PP;
    }
    
    /**
     * get PP of ai's minymphs
     * @return pp of a specified ai's minymph
     */
    public int getPPai()
    {
    	if (this.getOwner() instanceof AI)
    	{
    		return this.PP;
    	}
    	return 0;
    }
    
    /**
     * Set PP of a move for ai (avoid sharing move's pp!)
     * @param PP value to be entered
     */
    public void setPPai(Integer PP)
    {
    	if (this.PP<0)
    	{
    		this.PP = 0; // make sure the PP value is always >=0
    	}
    	if (this.getOwner() instanceof AI)
    	{
    		this.PP = PP;
    	}
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
     * Adds this move to the player's move list
     */
    public void addToList() {
        normalMoves.add(this);
    }
    
    /**
     * Adds this move to the AI move's list.
     */
    public void addToAIList() {
        aiNormalMoves.add(this);
    }
    
    /**
     * get player's minymph moves
     * @return the player's minymph moves
     */
    public static List<Moves> getMoves()
    {
    	return normalMoves;
    }
    
    /**
     * get ai minymph moves
     * @return the ai minymph moves
     */
    public static List<Moves> getAIMoves()
    {
    	return aiNormalMoves;
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
