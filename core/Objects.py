from typing import TYPE_CHECKING, Optional

if TYPE_CHECKING:
    from ..characters.Player import Player

class Objects:
    """
    Represents an in-game object with attributes such as name, type, HP (health points),
    unique identifier, side effects, and the player affected by the object.
    """

    def __init__(
        self,
        name: str = "",
        type_: str = "",
        hp: float = 0.0,
        id_: int = 0,
        side_effect: str = "",
        player_affected: Optional['Player'] = None,
        message: str = ""
    ) -> None:
        """
        Constructs an object with specified attributes.

        Args:
            name: The name of the object (optional).
            type_: The type of the object (optional).
            hp: The health points associated with the object (optional).
            id_: The unique identifier for the object (optional).
            side_effect: Any side effect associated with the object (optional).
            player_affected: The player affected by the object (optional).
            message: A message to display when the object is used (optional).
        """
        self._name: str = name
        self._hp: float = hp
        self._type: str = type_
        self._id: int = id_
        self._side_effect: str = side_effect
        self._player_affected: Optional['Player'] = player_affected
        self._message: str = message

    def get_id(self) -> int:
        """
        Returns the unique identifier of the object.

        Returns:
            The unique identifier (ID) of the object.
        """
        return self._id

    def get_message(self) -> str:
        """
        Returns the message associated with this object.

        Returns:
            The message of the object.
        """
        return self._message

    def get_player_affected(self) -> Optional['Player']:
        """
        Returns the player affected by this object.

        Returns:
            The player affected by the object.
        """
        return self._player_affected

    def get_type(self) -> str:
        """
        Returns the type of the object.

        Returns:
            The type of the object.
        """
        return self._type

    def get_side_effect(self) -> str:
        """
        Returns the side effect associated with this object.

        Returns:
            The side effect of the object.
        """
        return self._side_effect

    def get_hp(self) -> float:
        """
        Returns the health points associated with this object.

        Returns:
            The health points (HP) of the object.
        """
        return self._hp

    def get_name(self) -> str:
        """
        Returns the name of the object.

        Returns:
            The name of the object.
        """
        return self._name