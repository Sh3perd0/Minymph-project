from typing import Optional

class Shop:
    """
    Represents a shop in the game where players can purchase items or upgrades
    using in-game currency.
    """

    def __init__(self) -> None:
        """
        Constructs a new Shop instance with default settings.
        """
        self._money: float = 0.0

    def get_money(self) -> float:
        """
        Returns the current amount of money available in the shop.

        Returns:
            The available money as a float.
        """
        return self._money

    def set_money(self, money: float) -> None:
        """
        Sets the amount of money available in the shop.

        Args:
            money: The amount to set as available money.
        """
        self._money = money