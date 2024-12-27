package minymph_project;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

/**
 * BattleSolo - Manages a solo battle between two Minymphs.
 * Contains options for various moves, bag usage, and handling player actions.
 */
public class BattleSolo {
	private Timer battleTimer;

	/** Options for player moves presented in text form */
	private String moveOptions = 
			"1 : Attack \n" +
					"2 : Bag \n" +
					"3 : Minymph \n" +
					"4 : Surrender \n";

	/** Options for bag items presented in text form */
	private String bagOptions = 
			"1 : HP/PP \n" +
					"2 : Status \n" +
					"3 : Battle Items \n" +
					"-1 : Go back \n";

	private GUI gui;
	private AI AI;
	private Moves move;
	private static Minymph minymph1, minymph2;
	private boolean allPlayersMinymphDead = false;
	private boolean allAIMinymphDead = false;
	private static Minymph myCurrentMinymph;
	private float extraDamage = 0;
	private static Minymph myPreviousMinymph;
	private Bag bag;
	private boolean playersTurn = true;
	private boolean optionsDisplayed = false;

	/**
	 * Initializes the battle with the GUI, player's minymph, and opponent's minymph.
	 * @param gui The GUI for displaying battle messages.
	 * @param minymph1 Player's active Minymph.
	 * @param minymph2 Opponent's active Minymph.
	 */
	@SuppressWarnings("static-access")
	public BattleSolo(GUI gui, Minymph minymph1, Minymph minymph2, Minymph myCurrentMinymph) {
		this.gui = gui;
		this.minymph1 = minymph1;
		this.minymph2 = minymph2;
		this.myCurrentMinymph = minymph1;
		this.myPreviousMinymph = null;
		this.move = new Moves(gui);
		this.bag = new Bag(gui);
		AI = new AI();
	}

	/**
	 * Starts the battle timer and displays the player's options each turn.
	 */
	public void between() 
	{

		System.out.println("Minymph currently in battle: " + myCurrentMinymph.getName());

		battleTimer = new Timer();
		battleTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (!optionsDisplayed)
				{
					gui.getGameOutputArea().append("Player's current pokemon is " + myCurrentMinymph.getName()+"\n");
					gui.getGameOutputArea().append("AI current pokemon is " + minymph2.getName()+"\n");
					gui.getGameOutputArea().append("\nYour turn!\n");
					gui.getGameOutputArea().append("What will you do?\n");
					gui.getGameOutputArea().append(moveOptions);
					optionsDisplayed = true;
				}
			}
		}, 0, 1000);
	}

	/**
	 * Returns true if it's the player's turn
	 * @return returns playersTurn variable
	 */
	public boolean getTurn()
	{
		return this.playersTurn;
	}
	
	/**
     * AI's attack method
     */
	
	public void executeAttackAI()
	{
		if (!minymph2.getStatus().equals("KO"))
		{
		playersTurn=false;
		List<Moves> movesList = Moves.getMoves();
	    Random random = new Random();
	    int randomIndex = random.nextInt(movesList.size());
	    Moves randomMove = movesList.get(randomIndex); // current AI behaviour is choosing random attack each turn. May change
	    
	    
	    if (Moves.accuracyCheck(randomMove.getAccuracy())) //check of the moves accuracy.
		{
	    	gui.getGameOutputArea().append("\n" + minymph2.getName() + " uses " + randomMove.getName() + " !\n");
	    extraDamage = randomMove.critRateCheck(randomMove.getCritRate()); // calculation of the extra damages dealt by potential critical hit (which probability is also calculated)
	    if (randomMove.getSideEffect().equals("counterblow")) {
	    	minymph2.setHp(minymph2.getHp() - randomMove.getDamage()*0.3); // current counterblow damage calculation : 1/3 of the move's damage. May change it later
			myCurrentMinymph.setHp(Math.floor((myCurrentMinymph.getHp() - randomMove.getDamage()) - extraDamage)); //adds crit damage
	    	
			if (myCurrentMinymph.getHp()<=0.0)
			{
				gui.getGameOutputArea().append("\n" + myCurrentMinymph.getName() + " now has "  + "0 HP\n");
				gui.getGameOutputArea().append("\n" + minymph2.getName() + " is hurt by recoil!\n");
			}
			else
			{
				gui.getGameOutputArea().append("\n" + myCurrentMinymph.getName() + " now has " + myCurrentMinymph.getHp() + " HP\n");
				gui.getGameOutputArea().append("\n" + minymph2.getName() + " is hurt by recoil!\n");
			}

			gui.getGameOutputArea().append("\n" + minymph2.getName() + " has " + minymph2.getHp() + " HP\n");
		}
	    else 
		{
			myCurrentMinymph.setHp(Math.floor((myCurrentMinymph.getHp() - randomMove.getDamage()) - extraDamage)); // adds crit damage
			if (myCurrentMinymph.getHp()<=0)
			{
				gui.getGameOutputArea().append("\n" + myCurrentMinymph.getName() + " now has " + "0 HP\n");
			}
			else
			{
				gui.getGameOutputArea().append("\n" + myCurrentMinymph.getName() + " now has " + myCurrentMinymph.getHp() + " HP\n");
			}
		}playersTurn=true;
		}
	    
	    else
		{
	    	gui.getGameOutputArea().append("\n" + minymph2.getName() + " uses " + randomMove.getName() + " !\n");
			gui.getGameOutputArea().append("\nBut it failed !\n");
			playersTurn=true;
		}
	    checkForKO();
	    System.out.println("on teste si ko");
		
		System.out.println("on teste si combat fini, soit si tt minymph ko");
		checkForEndBattle();
		if (allAIMinymphDead) {
			desactivateButtons();

			if (battleTimer != null) {
				battleTimer.cancel();
			}
			
		}
	}
	}
	

	/**
	 * Retrieves the current Minymph of the player.
	 * @return Current active Minymph of the player.
	 */
	public static Minymph getMyCurrentMinymph() {
		return myCurrentMinymph;
	}

	/**
	 * Retrieves the previous Minymph of the player.
	 * @return Previous Minymph if a switch occurred, otherwise the current Minymph.
	 */
	public static Minymph getMyPreviousMinymph() {
		if (myPreviousMinymph == myCurrentMinymph) {
			System.out.println("No minymph switch yet!");
			return myCurrentMinymph;
		}
		return myPreviousMinymph;
	}

	/**
	 * Handles user input for each action in the battle (attack, use bag, switch minymph, or surrender).
	 * @param input The player's choice as a string (1 for attack, 2 for bag, etc.).
	 */
	public void handleUserInput(String input) {

		if (minymph2.getStatus().equals("KO")) {
			gui.getGameOutputArea().append("You won! Congrats!\n");
			if (battleTimer != null) {
				battleTimer.cancel();
			}
			return;
		}
		if (playersTurn)
		{
			switch (input) 
			{
			case "1":
				gui.getGameOutputArea().append("\nWhich move? \n");
				move.chooseMove(moveChosen -> {
					if (moveChosen != null && !myCurrentMinymph.getStatus().equals("KO")) {
						executeAttack(moveChosen, myCurrentMinymph, minymph2);
						executeAttackAI();
					}
				});
				break;

			case "2":
				gui.getGameOutputArea().append("\nYou reach for your bag...\n");
				gui.getGameOutputArea().append(bagOptions);
				bag.chooseBag(objectChosen -> {
					if (objectChosen != null) {
						useBagItem(objectChosen);
						gui.getGameOutputArea().append("\n" + myCurrentMinymph.getOwnerName() + " uses " + objectChosen.getName()+ " !\n");
						executeAttackAI();
					} else {
						gui.getGameOutputArea().append("No item chosen or canceled.\n");
					}
				});
				break;

			case "3":
				gui.getGameOutputArea().append("Which Minymph will you choose?\n");
				minymph1.printMyMinymph();
				minymph1.choseMinymph(minymphChosen -> {
					if (minymphChosen != null && !minymphChosen.getStatus().equals("KO")) {
						myPreviousMinymph = myCurrentMinymph;
						myCurrentMinymph = minymphChosen;
						gui.getGameOutputArea().append("The minymph " + minymphChosen.getName()+ " has been chosen !\n");
						gui.enableAttack();
						gui.enableBag();
						executeAttackAI();

					}
					else
					{
						gui.getGameOutputArea().append("Impossible ! "+minymphChosen.getName()+ " is KO !\n");
					}
				});
				break;

			case "4":
				gui.getGameOutputArea().append("You're fleeing!\n");
				desactivateButtons();
				if (battleTimer != null) {
					battleTimer.cancel();
				}
				return;

			default:
				gui.getGameOutputArea().append("Unrecognized choice\n");
				break;
			}

			optionsDisplayed = false;
		}
	}


	
	/**
	 * Executes the player's chosen attack, adjusting health and checking for KO status.
	 * @param moveChosen The move chosen by the player.
	 */
	private void executeAttack(Moves moveChosen, Minymph minymph1, Minymph minymph2)
	{
		if (Moves.accuracyCheck(moveChosen.getAccuracy())) //check of the moves accuracy.
		{
			extraDamage = moveChosen.critRateCheck(moveChosen.getCritRate()); // calculation of the extra damages dealt by potential critical hit (which probability is also calculated)
			gui.getGameOutputArea().append("\n" + minymph1.getName() + " uses " + moveChosen.getName() + " !\n");
			if (Moves.accuracyCheck(moveChosen.getCritRate()))
			{
				gui.getGameOutputArea().append("\nCritical hit ! \n");
			}
			
			if (moveChosen.getSideEffect().equals("counterblow")) 
		{
			
			minymph1.setHp(minymph1.getHp() - moveChosen.getDamage()*0.3); // current counterblow damage calculation : 1/3 of the move's damage. May change it later
			minymph2.setHp(Math.floor((minymph2.getHp() - moveChosen.getDamage()) - extraDamage));
			if (minymph2.getHp()<=0.0)
			{
				gui.getGameOutputArea().append("\n" + minymph2.getName() + " now has "  + "0 HP\n");
				gui.getGameOutputArea().append("\n" + minymph1.getName() + " is hurt by recoil!\n");
			}
			else
			{
				gui.getGameOutputArea().append("\n" + minymph2.getName() + " now has " + minymph2.getHp() + " HP\n");
				gui.getGameOutputArea().append("\n" + minymph1.getName() + " is hurt by recoil!\n");
			}

			gui.getGameOutputArea().append("\n" + minymph1.getName() + " has " + minymph1.getHp() + " HP\n");
		} else 
		{
			minymph2.setHp(Math.floor((minymph2.getHp() - moveChosen.getDamage()) - extraDamage));
			if (minymph2.getHp()<=0)
			{
				gui.getGameOutputArea().append("\n" + minymph2.getName() + " now has " + "0 HP\n");
			}
			else
			{
				gui.getGameOutputArea().append("\n" + minymph2.getName() + " now has " + minymph2.getHp() + " HP\n");
			}
		}
		}
		else
		{
			gui.getGameOutputArea().append(minymph1.getName() + " uses " + moveChosen.getName() + " !\n");
			gui.getGameOutputArea().append("\nBut it failed !\n");
		}
		checkForKO();
		System.out.println("on teste si ko");
		
		System.out.println("on teste si combat fini, soit si tt minymph ko");
		checkForEndBattle();
		if (allPlayersMinymphDead) {
			desactivateButtons();

			if (battleTimer != null) {
				battleTimer.cancel();
			}
		}
		
	}
	

	
	/**
     * Calls gui disable-buttons method
     */
	private void desactivateButtons()
	{
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				gui.disableButtons(); // Désactiver les boutons
				gui.getGameOutputArea().append("\nAll your Minymphs are KO. You have lost\n");
			}
		});
	}
	/**
	 * Uses the chosen item from the bag on the player's minymph if applicable.
	 * @param objectChosen The item selected by the player from the bag.
	 */
	@SuppressWarnings("static-access")
	private void useBagItem(Objects objectChosen) {
		if (myCurrentMinymph.getHp() < myCurrentMinymph.getBaseHP() && objectChosen.getType().equals("Health")) {
			double newHp = myCurrentMinymph.getHp() + objectChosen.getHP();
			myCurrentMinymph.setHp(Math.min(newHp, myCurrentMinymph.getBaseHP()));
			gui.getGameOutputArea().append(myCurrentMinymph.getName() + " healed! New HP: " + myCurrentMinymph.getHp() + "\n");
			bag.removeFromBag(objectChosen.getName());
		} else if (objectChosen.getName().equals("Revive") && myCurrentMinymph.getStatus().equals("KO")) {
			myCurrentMinymph.setStatus("Alive");
			myCurrentMinymph.setHp(myCurrentMinymph.getBaseHP() * 0.33);
			gui.getGameOutputArea().append(myCurrentMinymph.getName() + " has been revived! HP: " + myCurrentMinymph.getHp() + "\n");
			bag.removeFromBag("Revive");
		} else if (objectChosen.getType().equals("Battle Items") && objectChosen.getPlayerAffected().getName().equals("player") && !objectChosen.getSideEffect().equals("null"))
		{
			applyEffect(objectChosen.getSideEffect());
		}
		else {
			gui.getGameOutputArea().append(myCurrentMinymph.getName() + " is already at full health or alive!\n");
		}
	}

	/**
     * Method which applies attacks side effects
     */
	private void applyEffect(String effect)
	{
		if (effect.equals("defenseBuff"))
		{
			myCurrentMinymph.setDef(myCurrentMinymph.getDef()+5);
			gui.getGameOutputArea().append("Le minymph "+myCurrentMinymph.getName()+" a gagné 5 de def\n");
			gui.getGameOutputArea().append("Le minymph "+myCurrentMinymph.getName()+ " a maintenant "+myCurrentMinymph.getDef()+" de def\n");

		}
	}

	/**
	 * "Checks the health of both players' minymph to determine if either one is KO'd. If the opponent ones are KO, end the battle"
	 */
	private void checkForKO() 
	{
		if (minymph2.getHp() <= 0.0) {
			minymph2.setHp(0.0);
			minymph2.setStatus("KO");
			gui.getGameOutputArea().append(minymph2.getName() + " is now KO!\nYou won! Congrats!\n");
			if (battleTimer != null) {
				battleTimer.cancel();
			}return;
		}
		if (myCurrentMinymph.getHp()<=0.0)
		{
			gui.disableAttack();
			gui.disableBag();
			myCurrentMinymph.setHp(0.0);
			myCurrentMinymph.setStatus("KO");
			gui.getGameOutputArea().append(myCurrentMinymph.getName() + " is now KO!\n");
			checkForEndBattle();
			if (allPlayersMinymphDead==false)
			{
				gui.getGameOutputArea().append("\nPlease choose another minymph\n");
			}
			playersTurn=true;
		}
	}

	/**
	 * Checks if all one's player minymphs are KO to end the battle.
	 */

	private void checkForEndBattle() {
		int compteur1 = 0;
		int compteur2 = 0;
		for (Minymph minymph : myCurrentMinymph.getMyMinymphs()) { //for player's minymphs
			if (minymph.getStatus().equals("KO")) {
				compteur1++;
			}
		}
		for (Minymph minymph : minymph2.getOpponentMinymphs()) //for opponents minymphs
		{
			if (minymph.getStatus().equals("KO")) {
				compteur2++;
			}
		}
		System.out.println("Compteur de KO : " + compteur1);
		System.out.println("Taille de la liste : " + myCurrentMinymph.getMyMinymphs().size());

		// If the counter corresponds to the size of list, then all the Minymph are KO
		if (compteur1 == myCurrentMinymph.getMyMinymphs().size() || compteur2 == minymph2.getOpponentMinymphs().size())
			{
				allPlayersMinymphDead=true;
			};

	}

}
