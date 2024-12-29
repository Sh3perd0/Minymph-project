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
	private GUI gui;
	private AI AI;
	private Moves move;
	private static Minymph minymph1, minymph2;
	private boolean allPlayersMinymphDead = false;
	private boolean allAIMinymphDead = false;
	private boolean moveSelected = true;
	private float AImultiplicalFactor = 1;
	private float playersMultiplicalFactor = 1;
	private static Minymph myCurrentMinymph;
	private float extraDamageAI = 0;
	private float extraDamagePlayer = 0;
	private int compteurTour = 1;
	private static Minymph myPreviousMinymph;
	private Bag bag;
	private boolean playersTurn = true;
	private boolean optionsDisplayed = false;

	/**
	 * Initializes the battle with the GUI, player's minymph, and opponent's minymph.
	 * @param gui The GUI for displaying battle messages.
	 * @param minymph1 Player's active Minymph.
	 * @param minymph2 Opponent's active Minymph.
	 * @param myCurrentMinymph the current active minymph
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
					gui.getGameOutputArea().append("\n--Tour-- 1\n");
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
		if (moveSelected=false) {return;}
		if (!minymph2.getStatus().equals("KO"))
		{
		playersTurn=false;
		List<Moves> movesList = Moves.getAIMoves();
	    Random random = new Random();
	    int randomIndex = random.nextInt(movesList.size());
	    Moves randomMove = movesList.get(randomIndex); // current AI behaviour is choosing random attack each turn. May change
	    
	    if (randomMove.getSideEffect().equals("accuracyNerf") && Moves.accuracyCheck(randomMove.getAccuracy()*(1-(1-AImultiplicalFactor))))
	    {
	    	playersMultiplicalFactor = (float) (playersMultiplicalFactor-0.33); //if the chosen move is a accuracy nerf, we adjust the multiplicalFactor. For now, it's 33%. May change.
	    	if (playersMultiplicalFactor <= 0.10f) {
	    	    playersMultiplicalFactor = 0.10f;
	    	}
	    }

	    if (Moves.accuracyCheck(randomMove.getAccuracy()*(1-(1-AImultiplicalFactor)))) //check of the moves accuracy.
		{
	    	
	    	if (randomMove.getPPai()!=0)
	    	{
	    	gui.getGameOutputArea().append("\n" + minymph2.getName() + " uses " + randomMove.getName() + " !\n");
	    	randomMove.setPPai(randomMove.getPPai()-1);
	    	if (Moves.accuracyCheck(randomMove.getCritRate())) // calculation of the extra damages dealt by potential critical hit (which probability is also calculated)
			{
				extraDamageAI = (float) Math.ceil((randomMove.getDamage()*0.15)); // currently extraDamage is defined as 15% of the move's damage
				gui.getGameOutputArea().append("\nCritical hit ! \n");
				System.out.println("\nvalue of extraDamage " + extraDamageAI+ "\n");
			}
	    	
	    
	    if (randomMove.getSideEffect().equals("counterblow")) 
	    {
	    	
	    	minymph2.setHp(minymph2.getHp() - randomMove.getDamage()*0.3); // current counterblow damage calculation : 1/3 of the move's damage. May change it later
			myCurrentMinymph.setHp(Math.floor((myCurrentMinymph.getHp() - randomMove.getDamage()) - extraDamageAI)); //adds crit damage
	    	
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
	    else if ((randomMove.getSideEffect().equals("accuracyNerf")))
	    		{
	    			gui.getGameOutputArea().append("\n" +myCurrentMinymph.getName()+" 's accuracy fell !\n"); 
	    		}
	    else 
		{
			myCurrentMinymph.setHp(Math.floor((myCurrentMinymph.getHp() - randomMove.getDamage()) - extraDamageAI)); // adds crit damage
			if (myCurrentMinymph.getHp()<=0)
			{
				gui.getGameOutputArea().append("\n" + myCurrentMinymph.getName() + " now has " + "0 HP\n");
			}
			else
			{
				gui.getGameOutputArea().append("\n" + myCurrentMinymph.getName() + " now has " + myCurrentMinymph.getHp() + " HP\n");
			}
		}playersTurn=true;
		}else
		{
			gui.getGameOutputArea().append("\n" + minymph2.getName() + " uses " + randomMove.getName() + " !\n");
			gui.getGameOutputArea().append("\nNo PP left for this move\n");
			executeAttackAI();// recursive call to choose another move until it has PP >0
		}
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
	 * Method to sleep
	 * @param value in miliseconds. Ex: 1000 = 1s
	 */
	public void sleep(Integer value)
	{
		try {
		    Thread.sleep(value); // sleep for value seconds. The try catch is mandatory: it triggers an exception if the 2s is not respected (exemple if the window is manually closed before)
		} catch (InterruptedException ex) {
		    Thread.currentThread().interrupt();
		    ex.printStackTrace();
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
				move.chooseMove(moveChosen -> {
					if (moveChosen != null && !myCurrentMinymph.getStatus().equals("KO") && moveChosen.getPP()!=0) {
						executeAttack(moveChosen, myCurrentMinymph, minymph2);
						executeAttackAI();
						compteurTour++;
						gui.getGameOutputArea().append("\n--Tour--" + compteurTour + "\n");
						
					}
				});
				break;

			case "2":
				gui.getGameOutputArea().append("\nYou reach for your bag...\n");
				bag.chooseBag(objectChosen -> {
					if (objectChosen != null && useBagItem(objectChosen)) {
						executeAttackAI();
						compteurTour++;
						gui.getGameOutputArea().append("\n--Tour--" + compteurTour + "\n");

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
						compteurTour++;
						gui.getGameOutputArea().append("\n--Tour-- " + compteurTour + "\n");
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
		
		if (moveChosen.getSideEffect().equals("accuracyNerf") && Moves.accuracyCheck(moveChosen.getAccuracy()*(1-(1-playersMultiplicalFactor))))
	    {
	    	AImultiplicalFactor = (float) (AImultiplicalFactor-0.33); //if the chosen move is a accuracy nerf, we adjust the multiplicalFactor. For now, it's 33%. May change.
	    	if (AImultiplicalFactor <= 0.10f) {
	    	    AImultiplicalFactor = 0.10f; //verification to not lower it too much
	    	}
	    }

		if (Moves.accuracyCheck(moveChosen.getAccuracy()*(1-(1-playersMultiplicalFactor)))) //check of the moves accuracy + correctiveFactor
		{

			if (moveChosen.getPP()!=0)
	    	{
		
			gui.getGameOutputArea().append("\n" + minymph1.getName() + " uses " + moveChosen.getName() + " !\n");
			moveChosen.setPP(moveChosen.getPP()-1);
			if (Moves.accuracyCheck(moveChosen.getCritRate()))
			{
				extraDamagePlayer = (float)Math.ceil(moveChosen.getDamage()*0.15); // calculation of extra damage (critical hit). for now its 15% of base move damage. may chage
				gui.getGameOutputArea().append("\nCritical hit ! \n");
			}
			
			if (moveChosen.getSideEffect().equals("counterblow")) 
		{
			
			minymph1.setHp(minymph1.getHp() - moveChosen.getDamage()*0.3); // current counterblow damage calculation : 1/3 of the move's damage. May change it later
			minymph2.setHp(Math.floor((minymph2.getHp() - moveChosen.getDamage()) - extraDamagePlayer));
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
		} 
			else if ((moveChosen.getSideEffect().equals("accuracyNerf")))
    		{
    			gui.getGameOutputArea().append("\n" +minymph2.getName()+" 's accuracy fell !\n"); 
    		}
			else 
		{
			minymph2.setHp(Math.floor((minymph2.getHp() - moveChosen.getDamage()) - extraDamagePlayer));
			if (minymph2.getHp()<=0)
			{
				gui.getGameOutputArea().append("\n" + minymph2.getName() + " now has " + "0 HP\n");
			}
			else
			{
				gui.getGameOutputArea().append("\n" + minymph2.getName() + " now has " + minymph2.getHp() + " HP\n");
			}
		}
		}else
		{
			
			gui.getGameOutputArea().append("\n" + minymph1.getName() + " uses " + moveChosen.getName() + " !\n");
			gui.getGameOutputArea().append("\nNo PP left!\n");
			moveSelected=false;
		}
			
		}
		else
		{
			gui.getGameOutputArea().append("\n" + minymph1.getName() + " uses " + moveChosen.getName() + " !\n");
			gui.getGameOutputArea().append("\nBut it failed !\n");
		}
		
		checkForKO();
		System.out.println("on teste si ko");
		
		System.out.println("on teste si combat fini, soit si tt minymph ko");
		checkForEndBattle();
		if (allPlayersMinymphDead) {
			desactivateButtons();
			gui.getGameOutputArea().append("\nAll your Minymphs are KO. You have lost\n");
			

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
				sleep(2000);
				gui.closeWindow();
				

			}
		});
	}
	/**
	 * Uses the chosen item from the bag on the player's minymph if applicable.
	 * @param objectChosen The item selected by the player from the bag.
	 */
	@SuppressWarnings("static-access")
	private boolean useBagItem(Objects objectChosen) {
		if (myCurrentMinymph.getHp() < myCurrentMinymph.getBaseHP() && objectChosen.getType().equals("Health")) {
			double newHp = myCurrentMinymph.getHp() + objectChosen.getHP();
			myCurrentMinymph.setHp(Math.min(newHp, myCurrentMinymph.getBaseHP()));
			gui.getGameOutputArea().append(myCurrentMinymph.getName() + " healed! New HP: " + myCurrentMinymph.getHp() + "\n");
			bag.removeFromBag(objectChosen.getName());
			compteurTour++;
			gui.getGameOutputArea().append("\n--Tour-- " + compteurTour + "\n");
			return true;
		} else if (objectChosen.getName().equals("Revive") && myCurrentMinymph.getStatus().equals("KO")) {
			myCurrentMinymph.setStatus("Alive");
			myCurrentMinymph.setHp(myCurrentMinymph.getBaseHP() * 0.33); // if we revive a minymph, his hps will be 1/3 of his normal hps
			gui.getGameOutputArea().append(myCurrentMinymph.getName() + " has been revived! HP: " + myCurrentMinymph.getHp() + "\n");
			bag.removeFromBag("Revive");
			compteurTour++;
			gui.getGameOutputArea().append("\n--Tour-- " + compteurTour + "\n");
			return true;
		} else if (objectChosen.getType().equals("Battle Items") && !objectChosen.getSideEffect().equals("null"))
		{
			applyEffect(objectChosen,objectChosen.getSideEffect());
			bag.removeFromBag(objectChosen.getName());
			return true;
		}
		else if (objectChosen.getType().equals("Battle Items") && objectChosen.getSideEffect().equals("null"))
		{
			applyEffect(objectChosen,objectChosen.getSideEffect());
			bag.removeFromBag(objectChosen.getName());
			gui.getGameOutputArea().append(minymph2.getName() + " has "+minymph2.getHp());
			return true;
		}
		
		else {
			gui.getGameOutputArea().append(myCurrentMinymph.getName() + " is already at full health or alive!\n");
			return false;
		}
	}

	/**
     * Method which applies attacks side effects
     */
	private void applyEffect(Objects objectChosen, String effect)
	{
		if (effect.equals("defenseBuff"))
		{
			myCurrentMinymph.setDef(myCurrentMinymph.getDef()+5);
			gui.getGameOutputArea().append("\n"+myCurrentMinymph.getName()+" defense slightly went up\n");

		}
		if (effect.equals("speedBuff"))
		{
			myCurrentMinymph.setSpeed(myCurrentMinymph.getSpeed()+5);
			gui.getGameOutputArea().append("\n"+myCurrentMinymph.getName()+" speed slightly went up\n");
		}
		if (effect.equals("null"))
		{
			minymph2.setHp(minymph2.getHp()-objectChosen.getHP());
			gui.getGameOutputArea().append("\n"+minymph2.getName()+objectChosen.getMessage());
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
			desactivateButtons();
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
			if(allPlayersMinymphDead)
			{
				gui.getGameOutputArea().append("\nAll your minymphs are KO ! You lost !\n");
				desactivateButtons();
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

		// If the counter corresponds to the size of list, then all the Minymph are KO
		if (compteur1 == myCurrentMinymph.getMyMinymphs().size())
			{
				allPlayersMinymphDead=true;
			}
		else if (compteur2 == minymph2.getOpponentMinymphs().size())
		{
			allAIMinymphDead=true;
		}

	}

}
