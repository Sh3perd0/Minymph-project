package minymph_project;

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
    private Moves move;
    private Minymph minymph1, minymph2;
    private static Minymph myCurrentMinymph;
    private static Minymph myPreviousMinymph;
    private Bag bag;
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
    }

    /**
     * Starts the battle timer and displays the player's options each turn.
     */
    public void between() {
        System.out.println("Minymph currently in battle: " + myCurrentMinymph.getName());
       
        battleTimer = new Timer();
        battleTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!optionsDisplayed) {
                    gui.getGameOutputArea().append("\nYour turn!\n");
                    gui.getGameOutputArea().append("What will you do?\n");
                    gui.getGameOutputArea().append(moveOptions);
                    optionsDisplayed = true;
                }
            }
        }, 0, 1000);
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
        if (minymph1.getStatus().equals("KO")) {
            gui.getGameOutputArea().append("You lost! Maybe next time...\n");
            if (battleTimer != null) {
                battleTimer.cancel();
            }
            return;
        }

        if (minymph2.getStatus().equals("KO")) {
            gui.getGameOutputArea().append("You won! Congrats!\n");
            if (battleTimer != null) {
                battleTimer.cancel();
            }
            return;
        }

        switch (input) {
            case "1":
                gui.getGameOutputArea().append("\nWhich move?\n");
                move.chooseMove(moveChosen -> {
                    if (moveChosen != null) {
                        executeAttack(moveChosen);
                    }
                });
                break;

            case "2":
                gui.getGameOutputArea().append("\nYou reach for your bag...\n");
                gui.getGameOutputArea().append(bagOptions);
                bag.chooseBag(objectChosen -> {
                    if (objectChosen != null) {
                        useBagItem(objectChosen);
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
                        gui.getGameOutputArea().append("Le minymph " + minymphChosen.getName()+ " a été choisi !\n");
                        
                    }
                    else
                    {
                    	gui.getGameOutputArea().append("Impossible ! "+minymphChosen.getName()+ " is KO !\n");
                    }
                });gui.getGameOutputArea().append(myCurrentMinymph.getHpText());
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

    
    
    /**
     * Executes the player's chosen attack, adjusting health and checking for KO status.
     * @param moveChosen The move chosen by the player.
     */
    private void executeAttack(Moves moveChosen) {
        if (moveChosen.getSideEffect().equals("counterblow")) {
        	myCurrentMinymph.setHp(myCurrentMinymph.getHp() - moveChosen.getDamage()*0.3);
            minymph2.setHp(Math.floor(minymph2.getHp() - moveChosen.getDamage()));
            if (minymph2.getHp()<=0.0)
            {
            gui.getGameOutputArea().append("\n" + minymph2.getName() + " now has "  + "0 HP\n");
            gui.getGameOutputArea().append("\n" + myCurrentMinymph.getName() + " is hurt by recoil!\n");
            }
            else
            {
            	gui.getGameOutputArea().append("\n" + minymph2.getName() + " now has " + minymph2.getHp() + " HP\n");
            	gui.getGameOutputArea().append("\n" + myCurrentMinymph.getName() + " is hurt by recoil!\n");
            }
            
            gui.getGameOutputArea().append("\n" + myCurrentMinymph.getName() + " has " + myCurrentMinymph.getHp() + " HP\n");
        } else 
        {
            minymph2.setHp(Math.floor(minymph2.getHp() - moveChosen.getDamage()));
            if (minymph2.getHp()<=0)
            {
            gui.getGameOutputArea().append(minymph2.getName() + " now has " + "0 HP\n");
            }
            else
            {
            	gui.getGameOutputArea().append(minymph2.getName() + " now has " + minymph2.getHp() + " HP\n");
            }
        }
        System.out.println("on teste si ko");
        checkForKO();
        System.out.println("on teste si combat fini, soit si tt minymph ko");
        if (checkForEndBattle()) {
            gui.getGameOutputArea().append("Combat terminé\n");
            desactivateButtons();
        
            if (battleTimer != null) {
                battleTimer.cancel();
            }
        }
            }
        	
    private void desactivateButtons()
    {
    	SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gui.disableButtons(); // Désactiver les boutons
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
    private void checkForKO() {
        if (minymph2.getHp() <= 0.0) {
            minymph2.setHp(0.0);
        	minymph2.setStatus("KO");
            gui.getGameOutputArea().append(minymph2.getName() + " is now KO!\nYou won! Congrats!\n");
            if (battleTimer != null) {
                battleTimer.cancel();
            }
        }
        if (myCurrentMinymph.getHp()<=0.0)
        {
        	myCurrentMinymph.setHp(0.0);
        	myCurrentMinymph.setStatus("KO");
            gui.getGameOutputArea().append(myCurrentMinymph.getName() + " is now KO!\n");
        }
    }
    
    /**
     * Checks if all one's player minymphs are KO to end the battle.
     */
    
    private Boolean checkForEndBattle() {
        int compteur = 0;
        for (Minymph minymph : myCurrentMinymph.getMyMinymphs()) {
            if (minymph.getStatus().equals("KO")) {
                compteur++;
            }
        }
        System.out.println("Compteur de KO : " + compteur);
        System.out.println("Taille de la liste : " + myCurrentMinymph.getMyMinymphs().size());

        // If the counter corresponds to the size of list, then all the Minymph are KO
        return compteur == myCurrentMinymph.getMyMinymphs().size();
    }

}
