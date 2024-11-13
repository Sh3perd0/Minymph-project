package minymph_project;

/**
 * Represents a shop in the game where players can purchase items or upgrades
 * using in-game currency.
 */
public class Shop {

    private Double money;

    /**
     * Constructs a new Shop instance with default settings.
     */
    public Shop() {
    }

    /**
     * Returns the current amount of money available in the shop.
     *
     * @return the available money as a Double
     */
    public Double getMoney() {
        return money;
    }

    /**
     * Sets the amount of money available in the shop.
     *
     * @param money the amount to set as available money
     */
    public void setMoney(Double money) {
        this.money = money;
    }
}
