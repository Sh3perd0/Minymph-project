from .Player import Player
"""
Enemy - Enemy opponent which is derived from Player.
"""

class Enemy(Player):
    """
    Enemy - Enemy opponent with a name, derived from Player.
    """

    def __init__(self, name: str) -> None:
        """
        Initializes an Enemy with a name.

        Args:
            name: The name of the enemy.
        """
        super().__init__(name)