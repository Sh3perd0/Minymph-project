package minymph_project;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;

/**
 * Represents a Minymph character with various attributes, abilities, and status.
 * Each Minymph has a unique owner, type, and set of stats.
 */
public class Minymph {
    
    private String name;
    private String type;
    private GUIBattle gui;
    private Player owner;
    private Double hp;
    private Integer id;
    private Integer xp;
    private Integer lvl;
    private Integer atk;
    private Integer def;
    private Integer atkspe;
    private Integer defspe;
    private Integer speed;
    private String currentStatus = "Alive";
    private String status;
    private static int idCount = -3; //debug because im too lazy to actually debug why the id starts at 3 and not 0 
    private Double baseHP = 20.0;
    private Random rand = new Random();
    private TypeMinymph typeMinymph = new TypeMinymph();
    private static List<Minymph> minymphs = new ArrayList<>();
    private static List<Minymph> availableMinymphs = new ArrayList<>();
    private static List<Minymph> myMinymphs = new ArrayList<>();
    private static List<Minymph> opponentMinymphs = new ArrayList<>();

    /**
     * Constructs a new Minymph with specified GUI, name, and owner.
     * @param gui : the unique gui instance
     * @param name : name of the minymph
     * @param owner : owner of the mynimph
     */
    public Minymph(GUIBattle gui, String name, Player owner) {
        this.name = name;
        this.owner = owner;
        this.gui = gui;
        idCount++;
        id=idCount;
        hp = 20.0;
        xp = 0;
        lvl = 0;
        atk = initMinymph("atk");
        def = initMinymph("def");
        atkspe = initMinymph("atkspe");
        defspe = initMinymph("defspe");
        speed = initMinymph("speed");
        type = initMinymph("type");
        status = currentStatus;
    }

    /**
     * Default constructor for Minymph.
     */
    public Minymph() {}

    /**
     * Returns the owner's name of this Minymph.
     * @return returns owner name
     */
    public String getOwnerName() {
        return this.owner.getName();
    }

    /**
     * Returns the owner of this Minymph.
     * @return returns owner name without shown text
     */
    public Player getOwnerWT() {
        return this.owner;
    }

    /**
     * Returns the myMinimphs list, which is a list of all the player's one Minymphs.
     * @return returns list of player's minymphs
     */
    @SuppressWarnings("static-access")
	public List<Minymph> getMyMinymphs()
    {
    	return this.myMinymphs;
    }
    
    /**
     * Returns the opponents Minymphs list
     * @return returns list of opponent's minymphs
     */
    @SuppressWarnings("static-access")
    public List<Minymph> getOpponentMinymphs()
    {
    	return this.opponentMinymphs;
    }
    
    /**
     * Adds this Minymph to the list of all Minymphs.
     */
    public void addToList() {
        minymphs.add(this);
    }

    /**
     * Opens a dialog to select an available Minymph for a specified action.
     * @param callback minymph to consume
     */
    public void choseMinymph(Consumer<Minymph> callback) {
        JDialog minymphDialog = new JDialog(gui, "Choose Minymph", true);
        minymphDialog.setSize(300, 200);
        minymphDialog.setLayout(new BoxLayout(minymphDialog.getContentPane(), BoxLayout.Y_AXIS));

        for (Minymph minymph : availableMinymphs) {
            JButton statusButton = new JButton(minymph.getName());
            statusButton.addActionListener(e -> {
                callback.accept(minymph);
                minymphDialog.dispose();
                availableMinymphs.remove(minymph);
                availableMinymphs.add(BattleSolo.getMyPreviousMinymph());
            });
            minymphDialog.add(statusButton);
        }

        minymphDialog.setVisible(true);
    }

    /**
     * Prints all available Minymphs owned by the player.
     */
    public void printMyMinymph() {
        for (Minymph minymph : minymphs) {
            if (minymph.getOwnerName().equals("player") &&
                !BattleSolo.getMyCurrentMinymph().equals(minymph) &&
                !minymph.getStatus().equals("KO")) {
                if (!availableMinymphs.contains(minymph)) {
                    availableMinymphs.add(minymph);
                }
            }
        }
    }

    /**
     * Prints the names of all available Minymphs.
     */
    public static void printAllAvailableMinymph() {
        for (Minymph minymph : availableMinymphs) {
            if (!minymph.getStatus().equals("KO")) {
                System.out.println(minymph.getName());
            }
        }
    }

    /**
     * Returns the current HP of this Minymph.
     * @return returns hp of self minymph
     */
    public Double getHp() {
        return this.hp;
    }

    /**
     * Returns all Minymphs.
     * @return returns all minymphs
     */
    public static List<Minymph> getAllMinymph() {
        return minymphs;
    }

    /**
     * Returns the base HP of this Minymph.
     * @return returns base hp of self minymph
     */
    public Double getBaseHP() {
        return this.baseHP;
    }

    
    
    /**
     * Returns the name of this Minymph.
     * @return returns name of self minymph
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the level of this Minymph.
     * @return returns level of self minymph
     */
    public Integer getLvl() {
        return this.lvl;
    }

    /**
     * Returns the type of this Minymph.
     * @return returns type of self minymph
     */
    public String getType() {
        return this.type;
    }
    
    /**
     * Returns the attack stat of this Minymph, printing its value.
     * @return returns atk of self minymph
     */
    public Integer getAtk() {
        System.out.println("Minymph " + this.name + " currently has " + this.atk + " attack.");
        return this.atk;
    }

    /**
     * Returns the defense stat of this Minymph, printing its value.
     * @return returns def of self minymph
     */
    public Integer getDef() {
        System.out.println("Minymph " + this.name + " currently has " + this.def + " defense.");
        return this.def;
    }

    /**
     * Returns the special attack stat of this Minymph, printing its value.
     * @return returns atkspe of self minymph
     */
    public Integer getAtkspe() {
        System.out.println("Minymph " + this.name + " currently has " + this.atkspe + " special attack.");
        return this.atkspe;
    }

    /**
     * Returns the special defense stat of this Minymph, printing its value.
     * @return returns defspe of self minymph
     */
    public Integer getDefspe() {
        System.out.println("Minymph " + this.name + " currently has " + this.defspe + " special defense.");
        return this.defspe;
    }

    /**
     * Returns the speed stat of this Minymph, printing its value.
     * @return returns speed of self minymph
     */
    public Integer getSpeed() {
        System.out.println("Minymph " + this.name + " currently has " + this.speed + " speed.");
        return this.speed;
    }

    /**
     * Returns the status of this Minymph.
     * @return returns status of self minymph
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * Returns the current HP as a formatted string.
     * @return returns hp of self minymph with log text
     */
    public String getHpText() {
        String var = "Minymph " + this.name + " currently has " + this.hp + " HP\n";
        System.out.println(var);
        return var;
    }
    
    /**
     * Sets the status of this Minymph.
     * If the status is set to "KO", HP is set to 0.
     * @param status : the new status
     */
    public void setStatus(String status) {
        if (!this.getStatus().equals("KO")) {
            this.status = status;
        }
        this.setHp(0.0);
    }

    /**
     * Sets the HP of this Minymph, rounding down to the nearest integer.
     * @param value : new hp value
     */
    public void setHp(Double value) {
        this.hp = Math.floor(value);
    }

    /**
     * Sets the type of this Minymph.
     *
     * @param typeMinymph the new type to set for this Minymph
     */
    public void setType(TypeMinymph typeMinymph) {
        this.typeMinymph = typeMinymph;
    }

    /**
     * Sets the name of this Minymph.
     *
     * @param name the new name to set for this Minymph
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the experience points (XP) of this Minymph.
     *
     * @param xp the new XP value to set for this Minymph
     */
    public void setXp(Integer xp) {
        this.xp = xp;
    }
    
    /**
     * Gets the experience points (XP) of this Minymph.
     * @return returns xp of self minymph
     */
    public Integer getXp() {
        return this.xp;
    }

    /**
     * Sets the level of this Minymph.
     *
     * @param lvl the new level to set for this Minymph
     */
    public void setLvl(Integer lvl) {
        this.lvl = lvl;
    }

    /**
     * Sets the attack stat of this Minymph.
     *
     * @param atk the new attack value to set for this Minymph
     */
    public void setAtk(Integer atk) {
        this.atk = atk;
    }

    /**
     * Sets the defense stat of this Minymph.
     *
     * @param def the new defense value to set for this Minymph
     */
    public void setDef(Integer def) {
        this.def = def;
    }

    /**
     * Sets the special attack stat of this Minymph.
     *
     * @param atkspe the new special attack value to set for this Minymph
     */
    public void setAtkspe(Integer atkspe) {
        this.atkspe = atkspe;
    }

    /**
     * Sets the special defense stat of this Minymph.
     *
     * @param defspe the new special defense value to set for this Minymph
     */
    public void setDefspe(Integer defspe) {
        this.defspe = defspe;
    }

    /**
     * Sets the speed stat of this Minymph.
     *
     * @param speed the new speed value to set for this Minymph
     */
    public void setSpeed(Integer speed) {
        this.speed = speed;
    }


    
    /**
     * Initializes the specified attribute of this Minymph.
     * Each attribute is initialized with a random value or ID as needed.
     * @param param : a minymph to initialize
     * @param <T> : type of specified param
     * @return returns specified param
     */
    @SuppressWarnings("unchecked")
    public <T> T initMinymph(String param) {
        switch (param) {
            case "atk":
                atk = rand.nextInt(9) + 1;
                return (T) atk;
            case "def":
                def = rand.nextInt(9) + 1;
                return (T) def;
            case "atkspe":
                atkspe = rand.nextInt(9) + 1;
                return (T) atkspe;
            case "defspe":
                defspe = rand.nextInt(9) + 1;
                return (T) defspe;
            case "speed":
                speed = rand.nextInt(9) + 1;
                return (T) speed;
            case "type":
                type = typeMinymph.setType();
                return (T) type;
            default:
                return null;
        }
    }

    /**
     * Returns a formatted string of this Minymph's stats.
     * @return returns a formatted string of self minymph's stats
     */
    public String printStatsText() {
        String stats = "Base HP of " + this.name + " is: " + hp + "\n";
        stats += "ID of " + this.name + " is: " + id + "\n";
        stats += "Base attack of " + this.name + " is: " + atk + "\n";
        stats += "Base defense of " + this.name + " is: " + def + "\n";
        stats += "Base special attack of " + this.name + " is: " + atkspe + "\n";
        stats += "Base special defense of " + this.name + " is: " + defspe + "\n";
        stats += "Base speed of " + this.name + " is: " + speed + "\n";
        stats += "Type of " + this.name + " is: " + this.type + "\n";
        stats += "Owner of " + this.name + " is : " + owner.getName() + "\n";
        return stats;
    }
}
