package minymph_project;

/**
 * Represents a player in the game with a name and a currently selected Minymph.
 */
public abstract class Player {
    
    private Minymph currentMinymph;
    private String name;

    /**
     * Constructs a Player with the specified name.
     *
     * @param name the name of the player
     */
    public Player(String name) {
        this.name = name;
    }
    
    public Player()
    {
    	
    }

    /**
     * Returns the name of the player.
     *
     * @return the player's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the currently active Minymph for the player.
     *
     * @param minymph the Minymph to be set as the current Minymph
     */
    public void setCurrentMinymph(Minymph minymph) {
        this.currentMinymph = minymph;
    }

    /**
     * Returns the player's currently active Minymph.
     *
     * @return the current Minymph associated with the player
     */
    public Minymph getCurrentMinymph() {
        return this.currentMinymph;
    }
    
}
