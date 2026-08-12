from typing import TYPE_CHECKING, List
from map.GuiMap import GuiMap

if TYPE_CHECKING:
    from map.Map import Map
    from map.GuiMap import GuiMap

class Game:
    """
    Game class to manage the game state, player position, and interactions.
    """

    # Attributs statiques pour la position du joueur (partagés par toutes les instances)
    _player_x: int = 1  # Position X du joueur (attribut de classe)
    _player_y: int = 1  # Position Y du joueur (attribut de classe)

    def __init__(self, map_obj: 'Map') -> None:
        """
        Initializes the game with a map.

        Args:
            map_obj: The map object for the game.
        """
        self._map: 'Map' = map_obj

    @property
    def player_x(self) -> int:
        """
        Returns the player's X position (instance method).

        Returns:
            The player's X position.
        """
        return Game._player_x  # Accède à l'attribut de classe

    @property
    def player_y(self) -> int:
        """
        Returns the player's Y position (instance method).

        Returns:
            The player's Y position.
        """
        return Game._player_y  # Accède à l'attribut de classe

    @staticmethod
    def get_player_x() -> int:
        """
        Returns the player's X position (static method for GuiMap).

        Returns:
            The player's X position.
        """
        return Game._player_x  # Accède à l'attribut de classe

    @staticmethod
    def get_player_y() -> int:
        """
        Returns the player's Y position (static method for GuiMap).

        Returns:
            The player's Y position.
        """
        return Game._player_y  # Accède à l'attribut de classe

    def set_player_x(self, x: int) -> None:
        """
        Sets the player's X position.

        Args:
            x: The new X position.
        """
        Game._player_x = x

    def set_player_y(self, y: int) -> None:
        """
        Sets the player's Y position.

        Args:
            y: The new Y position.
        """
        Game._player_y = y

    def move_player(self, x: int, y: int) -> None:
        """
        Moves the player by the specified x and y offsets.

        Args:
            x: The horizontal offset.
            y: The vertical offset.
        """
        new_x = Game._player_x + x
        new_y = Game._player_y + y

        # Vérifie que la nouvelle position est dans les limites de la carte
        if (
            0 <= new_x < len(self._map.get_map()[0]) and
            0 <= new_y < len(self._map.get_map())
        ):
            # Vérifie que la case n'est pas un mur (1 = mur)
            if self._map.get_map()[new_y][new_x] != 1:
                Game._player_x = new_x
                Game._player_y = new_y

    def start_game(self) -> None:
        """
        Starts the game and displays a welcome message.
        """
        print("Welcome to Minymph Project\n")
        self.main()

    def main(self) -> None:
        """
        Main method to start the GUI and display the map.
        """
        gui_map = GuiMap(self._map)
        gui_map.display()

# Exemple d'utilisation (si ce fichier est exécuté directement)
if __name__ == "__main__":
    from map.Map import Map
    map_obj = Map()  # À définir selon ton implémentation
    game = Game(map_obj)
    game.start_game()