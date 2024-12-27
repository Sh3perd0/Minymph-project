package minymph_project;

import javax.swing.*;

/**
 * Entry point for the minymph-style battle game.
 * Initializes GUI, players, Minymphs, moves, and items, then initiates a battle.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Initialize GUI
                GUI gui = new GUI();
                gui.setVisible(true);

                // Create players
                ConcretePlayer player = new ConcretePlayer("player");
                AI enemy = new AI("Vasco");

                // Create minymphs
                Minymph minymph1 = new Minymph(gui, "Oenoko", player);
                Minymph minymph2 = new Minymph(gui, "Mangecailles", enemy);
                Minymph minymph3 = new Minymph(gui, "Tarteflute", player);

                // Initialize moves
                Moves headbutt = new Moves(gui, "Headbutt", 3, 20, (float)33,(float)85,"null");
                Moves sweep = new Moves(gui, "Sweep", 1, 20, (float)33, (float)100,"null");
                Moves haze = new Moves(gui, "Haze", 0, 20, (float)0, (float)100,"speedNerf");
                Moves hyperRush = new Moves(gui, "HyperRush", 6, 5, (float)0, (float)20, "counterblow");

                // Initialize items
                Objects smallPotion = new Objects("Small Potion", "Health", 3.0, 0, "null", player);
                Objects mediumPotion = new Objects("Medium Potion", "Health", 5.0, 1, "null", player);
                Objects largePotion = new Objects("Large Potion", "Health", 7.0, 2, "null", player);
                Objects fullPotion = new Objects("Full Potion", "Health", minymph1.getBaseHP(), 3, "null", player);
                Objects revive = new Objects("Revive", "Status", minymph1.getBaseHP() * 0.33, 4, "null", player);
                Objects protectiveCrown = new Objects("Protective Crown", "Battle Items", 0.0, 5, "defenseBuff", player);
                Objects speedyBoots = new Objects("Speedy Boots", "Battle Items", 0.0, 6, "speedBuff", player);
                Objects caillouSaMer = new Objects("Caillou sa mer", "Battle Items", 1.0, 6, "null", enemy);

                // Add moves to the list
                headbutt.addToList();
                sweep.addToList();
                haze.addToList();
                hyperRush.addToList();

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
                enemy.setCurrentMinymph(minymph2);

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
