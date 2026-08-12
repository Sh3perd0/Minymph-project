import random
from typing import Set, List, Optional

class TypeMinymph:
    """
    Represents the type of a Minymph character, with possible types being "grass", "fire",
    "water", and "wind". Each TypeMinymph instance can assign one of these types randomly
    and display the available types.
    """

    def __init__(self) -> None:
        """
        Constructs a new TypeMinymph instance with default possible types added.
        """
        self._grass: str = "grass"
        self._fire: str = "fire"
        self._water: str = "water"
        self._wind: str = "wind"
        self._type: Optional[str] = None
        self._possible_types: Set[str] = {"grass", "fire", "water", "wind"}  # Utilisation d'un set pour les types possibles
        self._rand = random.Random()  # Générateur aléatoire

    def set_type(self) -> str:
        """
        Randomly sets and returns a type for this TypeMinymph from the available types.

        Returns:
            The randomly assigned type as a string.
        """
        # Convertir le set en liste pour pouvoir utiliser un index aléatoire
        type_list: List[str] = list(self._possible_types)
        index: int = self._rand.randint(0, len(type_list) - 1)  # Équivalent à nextInt(list.size())
        self._type = type_list[index]
        return self._type

    def get_type(self) -> Optional[str]:
        """
        Returns the currently assigned type of this TypeMinymph.

        Returns:
            The assigned type as a string, or None if not set.
        """
        return self._type

    def print_possible_types(self) -> None:
        """
        Prints all available Minymph types to the console.
        """
        print("Current implemented Minymph types are:")
        for type_ in self._possible_types:
            print(type_)