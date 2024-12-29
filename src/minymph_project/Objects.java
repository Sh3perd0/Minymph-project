package minymph_project;

/**
 * Represents an in-game object with attributes such as name, type, HP (health points), 
 * unique identifier, side effects, and the player affected by the object.
 */
public class Objects {
    
    private String name;
    private Double hp;
    private String type;
    private Integer id;
    private String sideEffect;
    private Player playerAffected;
    private String message;

    /**
     * Constructs an object with specified attributes.
     *
     * @param name the name of the object
     * @param type the type of the object
     * @param hp the health points associated with the object
     * @param id the unique identifier for the object
     * @param sideEffect any side effect associated with the object
     * @param playerAffected the player affected by the object
     */
    public Objects(String name, String type, Double hp, Integer id, String sideEffect, Player playerAffected, String message) {
        this.name = name;
        this.hp = hp;
        this.type = type;
        this.id = id;
        this.sideEffect = sideEffect;
        this.playerAffected = playerAffected;
        this.message=message;
    }

    /**
     * Default constructor for creating an empty object.
     */
    public Objects() {
    }

    /**
     * Returns the unique identifier of the object.
     *
     * @return the unique identifier (ID) of the object
     */
    public Integer getId() {
        return this.id;
    }
    
    public String getMessage()
    {
    	return this.message;
    }

    /**
     * Returns the player affected by this object.
     *
     * @return the player affected by the object
     */
    public Player getPlayerAffected() {
        return this.playerAffected;
    }

    /**
     * Returns the type of the object.
     *
     * @return the type of the object
     */
    public String getType() {
        return this.type;
    }

    /**
     * Returns the side effect associated with this object.
     *
     * @return the side effect of the object
     */
    public String getSideEffect() {
        return this.sideEffect;
    }

    /**
     * Returns the health points associated with this object.
     *
     * @return the health points (HP) of the object
     */
    public Double getHP() {
        return this.hp;
    }

    /**
     * Returns the name of the object.
     *
     * @return the name of the object
     */
    public String getName() {
        return this.name;
    }
}
