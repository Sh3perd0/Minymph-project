package minymph_project;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;

/**
 * Represents a Bag for storing and managing items in the game.
 * Provides functionality to display, add, remove, and filter items.
 */
public class Bag {
    private static List<Objects> bag = new ArrayList<>();
    private static Scanner bagInput = new Scanner(System.in);
    private static Objects objectChosen;
    private GUIBattle gui;

    /**
     * Constructs a Bag with a GUI reference.
     *
     * @param gui the GUI reference
     */
    public Bag(GUIBattle gui) {
        this.gui = gui;
    }

    /**
     * Returns the list of items in the bag.
     *
     * @return the list of items in the bag
     */
    @SuppressWarnings("static-access")
	public List<Objects> getBag() {
        return this.bag;
    }

    /**
     * Displays items in the bag filtered by type.
     * 
     * @param param the type of items to display (e.g., Health, Status, Battle Items)
     */
    public static void showItems(String param) {
        List<Objects> filteredItems = new ArrayList<>();

        for (Objects obj : bag) {
            if (obj.getType().equals(param)) {
                filteredItems.add(obj);
            }
        }

        filteredItems.sort(Comparator.comparingInt(Objects::getId));

        Map<String, Integer> counts = new HashMap<>();
        for (Objects obj : filteredItems) {
            String name = obj.getName();
            counts.put(name, counts.getOrDefault(name, 0) + 1);
        }

        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(counts.entrySet());
        sortedEntries.sort(Comparator.comparingInt(entry -> findByName(entry.getKey()).getId()));

        for (Map.Entry<String, Integer> entry : sortedEntries) {
            Objects obj = findByName(entry.getKey());
            if (obj != null) {
                System.out.println(obj.getId() + " : " + entry.getKey() + " x" + entry.getValue());
            }
        }
        System.out.println("-1 : Go back");
    }

    /**
     * Counts and displays items in the bag filtered by type for GUI purposes.
     * 
     * @param param the type of items to display (e.g., Health, Status, Battle Items)
     */
    public static void countItemsForGUI(String param) {
        Map<String, Integer> counts = new HashMap<>();

        for (Objects obj : bag) {
            if (obj.getType().equals(param)) {
                String name = obj.getName();
                counts.put(name, counts.getOrDefault(name, 0) + 1);
            }
        }

        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            System.out.println(entry.getKey() + " x" + entry.getValue());
        }
    }

    /**
     * Displays all items in the bag.
     */
    public static void showAllItems() {
        for (Objects obj : bag) {
            System.out.println(obj.getName());
        }
    }

    /**
     * Finds an item by name.
     * 
     * @param name the name of the item to find
     * @return the item if found, or null otherwise
     */
    public static Objects findByName(String name) {
        for (Objects obj : bag) {
            if (obj.getName().equals(name)) {
                return obj;
            }
        }
        return null;
    }

    /**
     * Allows the user to choose an item based on the category.
     * 
     * @param param the category of the item (e.g., Health, Status, Battle Item)
     * @return the chosen item if available, or null if canceled
     */
    public static Objects chooseItem(String param) {
        while (true) {
            System.out.print("Your choice : ");
            while (!bagInput.hasNextInt()) {
                System.out.println("Invalid choice.");
                bagInput.nextLine();
                System.out.print("Your choice : ");
                System.out.print("\n");
            }

            Integer userInput = bagInput.nextInt();
            bagInput.nextLine();

            switch (userInput) {
                case 0 -> objectChosen = findByName("Small Potion");
                case 1 -> objectChosen = findByName("Medium Potion");
                case 2 -> objectChosen = findByName("Large Potion");
                case 3 -> objectChosen = findByName("Full Potion");
                case -1 -> {
                    return null;
                }
                default -> System.err.println("Unrecognized choice");
            }

            if (objectChosen != null && bag.contains(objectChosen)) {
                bag.remove(objectChosen);
                return objectChosen;
            } else {
                System.out.println("The chosen item is not available. Please choose another item.");
                showItems(param);
            }
        }
    }

    /**
     * Opens a dialog to choose a bag category, then displays items in that category.
     * 
     * @param callback the action to take with the selected item
     */
    public void chooseBag(Consumer<Objects> callback) {
        JDialog bagDialog = new JDialog(gui, "Choose Category", true);
        bagDialog.setSize(300, 200);
        bagDialog.setLayout(new BoxLayout(bagDialog.getContentPane(), BoxLayout.Y_AXIS));

        JButton hpButton = new JButton("HP/PP");
        JButton statusButton = new JButton("Status");
        JButton battleItemsButton = new JButton("Battle Items");
        JButton goBackButton = new JButton("Go Back");

        hpButton.addActionListener(e -> {
            showItems("Health");
            bagDialog.dispose();
            showPotionItemsGUI(callback);
        });

        statusButton.addActionListener(e -> {
            showItems("Status");
            bagDialog.dispose();
            showStatusItemsGUI(callback);
        });

        battleItemsButton.addActionListener(e -> {
            showItems("Battle Item");
            bagDialog.dispose();
            showBattleItemsGUI(callback);
        });

        goBackButton.addActionListener(e -> bagDialog.dispose());

        bagDialog.add(hpButton);
        bagDialog.add(statusButton);
        bagDialog.add(battleItemsButton);
        bagDialog.add(goBackButton);
        bagDialog.setVisible(true);
    }

    private void showPotionItemsGUI(Consumer<Objects> callback) {
        Map<String, Integer> potionCounts = new HashMap<>();

        for (Objects potion : bag) {
            if (potion.getType().equals("Health")) {
                potionCounts.put(potion.getName(), potionCounts.getOrDefault(potion.getName(), 0) + 1);
            }
        }

        JDialog potionDialog = new JDialog(gui, "Choose Potion", true);
        potionDialog.setSize(300, 200);
        potionDialog.setLayout(new BoxLayout(potionDialog.getContentPane(), BoxLayout.Y_AXIS));

        for (Map.Entry<String, Integer> entry : potionCounts.entrySet()) {
            String potionName = entry.getKey();
            int quantity = entry.getValue();

            JButton potionButton = new JButton(potionName + " x" + quantity);
            potionButton.addActionListener(e -> {
                Objects selectedPotion = findByName(potionName);
                if (selectedPotion != null) {
                    callback.accept(selectedPotion);
                    potionDialog.dispose();
                    showAllItems();
                }
            });

            potionDialog.add(potionButton);
        }

        potionDialog.setVisible(true);
    }

    private void showStatusItemsGUI(Consumer<Objects> callback) {
        JDialog statusDialog = new JDialog(gui, "Choose Status Item", true);
        statusDialog.setSize(300, 200);
        statusDialog.setLayout(new BoxLayout(statusDialog.getContentPane(), BoxLayout.Y_AXIS));

        for (Objects item : bag) {
            if (item.getType().equals("Status")) {
                JButton statusButton = new JButton(item.getName());
                statusButton.addActionListener(e -> {
                    callback.accept(item);
                    statusDialog.dispose();
                });
                statusDialog.add(statusButton);
            }
        }

        statusDialog.setVisible(true);
    }

    private void showBattleItemsGUI(Consumer<Objects> callback) {
        JDialog battleDialog = new JDialog(gui, "Choose Battle Item", true);
        battleDialog.setSize(300, 200);
        battleDialog.setLayout(new BoxLayout(battleDialog.getContentPane(), BoxLayout.Y_AXIS));

        for (Objects item : bag) {
            if (item.getType().equals("Battle Items")) {
                JButton battleButton = new JButton(item.getName());
                battleButton.addActionListener(e -> {
                    callback.accept(item);
                    battleDialog.dispose();
                });
                battleDialog.add(battleButton);
            }
        }

        battleDialog.setVisible(true);
    }

    /**
     * Adds an item to the bag.
     * 
     * @param obj the item to add
     */
    public static void addToBag(Objects obj) {
        bag.add(obj);
    }

    /**
     * Removes an item from the bag by name.
     * 
     * @param obj the name of the item to remove
     */
    public static void removeFromBag(String obj) {
        Objects item = findByName(obj);
        if (bag.contains(item)) {
            bag.remove(item);
        }
    }
}
