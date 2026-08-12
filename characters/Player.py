from abc import ABC
from typing import TYPE_CHECKING, Optional

if TYPE_CHECKING:
    from Minymph import Minymph

class Player(ABC):#classe abstraite
    """
    Represents a player in the game with a name and a currently selected Minymph.
    This is an abstract class and cannot be instantiated directly.
    """

    def __init__(self, name: str = "") -> None:
        """
        Constructs a Player with the specified name.

        Args:
            name: The name of the player (optional).
        """
        self._name: str = name
        self._current_minymph: Optional['Minymph'] = None

    def get_name(self) -> str:
        """
        Returns the name of the player.

        Returns:
            The player's name.
        """
        return self._name

    def set_current_minymph(self, minymph: 'Minymph') -> None:
        """
        Sets the currently active Minymph for the player.

        Args:
            minymph: The Minymph to be set as the current Minymph.
        """
        self._current_minymph = minymph

    def get_current_minymph(self) -> Optional['Minymph']:
        """
        Returns the player's currently active Minymph.

        Returns:
            The current Minymph associated with the player, or None if not set.
        """
        return self._current_minymph