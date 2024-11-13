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
    
    private static int IDcount = 0;
    private String name;
    private String type;
    private GUI gui;
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
    private Double baseHP = 20.0;
    private Random rand = new Random();
    private TypeMinymph typeMinymph = new TypeMinymph();
    private static List<Minymph> minymphs = new ArrayList<>();
    private static List<Minymph> availableMinymphs = new ArrayList<>();
    private static List<Minymph> myMinymphs = new ArrayList<>();

    /**
     * Constructs a new Minymph with specified GUI, name, and owner.
     */
    public Minymph(GUI gui, String name, Player owner) {
        this.name = name;
        this.owner = owner;
        this.gui = gui;
        hp = 20.0;
        id = initMinymph("id");
        xp = 0;
        lvl = 0;
        atk = initMinymph("atk");
        def = initMinymph("def");
        atkspe = initMinymph("atkspe");
        defspe = initMinymph("defspe");
        speed = initMinymph("speed");
        type = initMinymph("type");
        status = currentStatus;
        IDcount++;
    }

    /**
     * Default constructor for Minymph.
     */
    public Minymph() {}

    /**
     * Returns the owner's name of this Minymph.
     */
    public String getOwnerName() {
        return this.owner.getName();
    }

    /**
     * Returns the owner of this Minymph.
     */
    public Player getOwnerWT() {
        return this.owner;
    }

    /**
     * Returns the myMinimphs list, which is a list of all the player's one Minymphs.
     */
    @SuppressWarnings("static-access")
	public List<Minymph> getMyMinymphs()
    {
    	return this.myMinymphs;
    }
    
    /**
     * Adds this Minymph to the list of all Minymphs.
     */
    public void addToList() {
        minymphs.add(this);
    }

    /**
     * Opens a dialog to select an available Minymph for a specified action.
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
     */
    public Double getHp() {
        return this.hp;
    }

    /**
     * Returns all Minymphs.
     */
    public static List<Minymph> getAllMinymph() {
        return minymphs;
    }

    /**
     * Returns the base HP of this Minymph.
     */
    public Double getBaseHP() {
        return this.baseHP;
    }

    /**
     * Returns the name of this Minymph.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the level of this Minymph.
     */
    public Integer getLvl() {
        return this.lvl;
    }

    /**
     * Returns the type of this Minymph.
     */
    public String getType() {
        return this.type;
    }
    
    /**
     * Returns the attack stat of this Minymph, printing its value.
     */
    public Integer getAtk() {
        System.out.println("Minymph " + this.name + " currently has " + this.atk + " attack.");
        return this.atk;
    }

    /**
     * Returns the defense stat of this Minymph, printing its value.
     */
    public Integer getDef() {
        System.out.println("Minymph " + this.name + " currently has " + this.def + " defense.");
        return this.def;
    }

    /**
     * Returns the special attack stat of this Minymph, printing its value.
     */
    public Integer getAtkspe() {
        System.out.println("Minymph " + this.name + " currently has " + this.atkspe + " special attack.");
        return this.atkspe;
    }

    /**
     * Returns the special defense stat of this Minymph, printing its value.
     */
    public Integer getDefspe() {
        System.out.println("Minymph " + this.name + " currently has " + this.defspe + " special defense.");
        return this.defspe;
    }

    /**
     * Returns the speed stat of this Minymph, printing its value.
     */
    public Integer getSpeed() {
        System.out.println("Minymph " + this.name + " currently has " + this.speed + " speed.");
        return this.speed;
    }

    /**
     * Returns the status of this Minymph.
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * Returns the current HP as a formatted string.
     */
    public String getHpText() {
        String var = "Minymph " + this.name + " currently has " + this.hp + " HP\n";
        System.out.println(var);
        return var;
    }
    
    /**
     * Sets the status of this Minymph.
     * If the status is set to "KO", HP is set to 0.
     */
    public void setStatus(String status) {
        if (!this.getStatus().equals("KO")) {
            this.status = status;
        }
        this.setHp(0.0);
    }

    /**
     * Sets the HP of this Minymph, rounding down to the nearest integer.
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
     *
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
     */
    @SuppressWarnings("unchecked")
    public <T> T initMinymph(String param) {
        switch (param) {
            case "id":
                id = IDcount;
                return (T) id;
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
     */
    public String printStatsText() {
        String stats = "Base HP of " + this.name + " is: " + hp + "\n";
        stats += "ID of " + this.name + " is: " + id + "\n";
        stats += "Base attack of " + this.name + " is: " + atk + "\n";
        stats += "Base defense of " + this.name + " is: " + def + "\n";
        stats += "Base special attack of " + this.name + " is: " + atkspe + "\n";
        stats += "Base special defense of " + this.name + " is: " + defspe + "\n";
        stats += "Base speed of " + this.name + " is: " + speed + "\n";
        stats += "Type of " + this.name;
        stats += "Owner of " + this.name + " is : " + owner.getName() + "\n";
        return stats;
    }
}
