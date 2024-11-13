package minymph_project;

import java.util.Set;
import java.util.HashSet;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the type of a Minymph character, with possible types being "grass," "fire," 
 * "water," and "wind". Each TypeMinymph instance can assign one of these types randomly 
 * and display the available types.
 */
public class TypeMinymph {

    private String grass = "grass";
    private String fire = "fire";
    private String water = "water";
    private String wind = "wind";
    private String type;

    private Set<String> possibleType = new HashSet<>();
    private Random rand = new Random();

    /**
     * Constructs a new TypeMinymph instance with default possible types added.
     */
    public TypeMinymph() {
        possibleType.add(grass);
        possibleType.add(fire);
        possibleType.add(water);
        possibleType.add(wind);
    }

    /**
     * Randomly sets and returns a type for this TypeMinymph from the available types.
     *
     * @return the randomly assigned type as a String
     */
    public String setType() {
        List<String> list = new ArrayList<>(possibleType);
        int index = rand.nextInt(list.size());
        type = list.get(index);
        return type;
    }

    /**
     * Returns the currently assigned type of this TypeMinymph.
     *
     * @return the assigned type as a String
     */
    public String getType() {
        return this.type;
    }

    /**
     * Prints all available Minymph types to the console.
     */
    public void possibleType() {
        System.out.println("Current implemented Minymph types are: ");
        for (String t : possibleType) {
            System.out.println(t);
        }
    }
}
