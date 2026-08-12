from .Player import Player
"""
ConcretePlayer - Concrete player which is derived from Player. This is the human player.
"""

class ConcretePlayer(Player): #mettre en paranthèses : héritage
    """
    ConcretePlayer - Concrete player with a name, derived from Player. This is the human player.
    """

    def __init__(self, name: str) -> None:
        """
        Initializes a ConcretePlayer with a name.

        Args:
            name: The name of the player.
        """
        super().__init__(name)