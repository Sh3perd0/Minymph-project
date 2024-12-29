package minymph_project;

import javax.swing.*;

/**
 * Entry point for the minymph-style battle game.
 * Initializes GUI, players, Minymphs, moves, and items, then initiates a battle.
 */
public class Main {
    public static void main(String[] args) {
    	
    	Map map = new Map();
    	Game gameInstance = new Game(map);
    	gameInstance.startGame();
    	System.out.println("Répertoire de travail actuel : " + System.getProperty("user.dir"));

    	boolean gameRunning = true;
    	boolean collision = false; // use later , triggers battle
    	
    	map.printMap();
    	
    	while (gameRunning)
    	{
    		//des trucs...
    		
    		
    		
    		
    		
    		
    		if (collision)
    		{
    			System.out.println("A wild minymph appears!");
    			
    			
    			SwingUtilities.invokeLater(new Runnable() {
    		            @Override
    		            public void run() {
    		                // Initialize GUI
    		                GUIBattle gui = new GUIBattle();
    		                gui.setVisible(true);

    		                // Create players
    		                ConcretePlayer player = new ConcretePlayer("player");
    		                AI ai = new AI("Vasco");

    		                // Create minymphs
    		                Minymph minymph1 = new Minymph(gui, "Oenoko", player);
    		                Minymph minymph2 = new Minymph(gui, "Mangecailles", ai);
    		                Minymph minymph3 = new Minymph(gui, "Tarteflute", player);

    		                // Initialize moves
    		                Moves headbutt = new Moves(gui, "Headbutt", 3, 20, (float)33,(float)85,"null",player);
    		                Moves sweep = new Moves(gui, "Sweep", 1, 20, (float)33, (float)100,"null",player);
    		                Moves haze = new Moves(gui, "Haze", 0, 20, (float)0, (float)50,"accuracyNerf",player);
    		                Moves hyperRush = new Moves(gui, "HyperRush", 6, 1, (float)0, (float)100, "counterblow",player);
    		                
    		                Moves headbuttAI = new Moves(gui, "Headbutt", 3, 20, (float)33,(float)85,"null",ai);
    		                Moves sweepAI = new Moves(gui, "Sweep", 1, 20, (float)33, (float)100,"null",ai);
    		                Moves hazeAI = new Moves(gui, "Haze", 0, 20, (float)0, (float)50,"accuracyNerf",ai);
    		                Moves hyperRushAI = new Moves(gui, "HyperRush", 6, 1, (float)0, (float)100, "counterblow",ai);

    		                // Initialize items
    		                Objects smallPotion = new Objects("Small Potion", "Health", 3.0, 0, "null", player,null);
    		                Objects mediumPotion = new Objects("Medium Potion", "Health", 5.0, 1, "null", player,null);
    		                Objects largePotion = new Objects("Large Potion", "Health", 7.0, 2, "null", player,null);
    		                Objects fullPotion = new Objects("Full Potion", "Health", minymph1.getBaseHP(), 3, "null", player,null);
    		                Objects revive = new Objects("Revive", "Status", minymph1.getBaseHP() * 0.33, 4, "null", player,null);
    		                Objects protectiveCrown = new Objects("Protective Crown", "Battle Items", 0.0, 5, "defenseBuff", player,null);
    		                Objects speedyBoots = new Objects("Speedy Boots", "Battle Items", 0.0, 6, "speedBuff", player,null);
    		                Objects caillouSaMer = new Objects("Caillou sa mer", "Battle Items", 5.0, 6, "null", player," got caillou sa mer\n\n");

    		                // Add moves to the list
    		                headbutt.addToList();
    		                sweep.addToList();
    		                haze.addToList();
    		                hyperRush.addToList();
    		                
    		                headbuttAI.addToAIList();
    		                sweepAI.addToAIList();
    		                hazeAI.addToAIList();
    		                hyperRushAI.addToAIList();
    		                

    		                // Add items to the bag
    		                Bag.addToBag(smallPotion);
    		                Bag.addToBag(mediumPotion);
    		                Bag.addToBag(mediumPotion);
    		                Bag.addToBag(largePotion);
    		                Bag.addToBag(fullPotion);
    		                Bag.addToBag(revive);
    		                Bag.addToBag(protectiveCrown);
    		                Bag.addToBag(speedyBoots);
    		                Bag.addToBag(caillouSaMer);

    		                // Set the players' active minymph
    		                player.setCurrentMinymph(minymph1);
    		                ai.setCurrentMinymph(minymph2);

    		                // Add minymph to each player’s list
    		                minymph1.addToList();
    		                minymph2.addToList();
    		                minymph3.addToList();

    		                // Display basic information in the GUI
    		                gui.getGameOutputArea().append(minymph1.printStatsText()); 
    		                gui.getGameOutputArea().append("\n");
    		                gui.getGameOutputArea().append(minymph2.printStatsText());
    		                gui.getGameOutputArea().append("\n");
    		                gui.getGameOutputArea().append(minymph3.printStatsText());
    		                gui.getGameOutputArea().append("\n");

    		                minymph1.getMyMinymphs().add(minymph3);
    		                minymph1.getMyMinymphs().add(minymph3);
    		                minymph2.getOpponentMinymphs().add(minymph2);
    		                
    		                // Start the battle with the initialized GUI
    		                BattleSolo duel = new BattleSolo(gui, minymph1, minymph2, minymph1);
    		                duel.between();
    		            }
    		        });
    		    }
    		}

    		}
    	}
    	
       