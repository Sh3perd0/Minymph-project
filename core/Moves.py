import random
from typing import TYPE_CHECKING, List, Optional, Callable
import tkinter as tk
from tkinter import ttk

if TYPE_CHECKING:
    from ..game.GuiBattle import GuiBattle
    from ..characters.Player import Player
    from ..characters.ConcretePlayer import ConcretePlayer

class Moves:
    """
    Represents a move in the Minymph game with attributes such as name, damage, PP (Power Points),
    critical rate, and any potential side effect.
    Moves can be chosen by players and listed for reference.
    """

    # Attributs statiques (partagés par toutes les instances)
    _normal_moves: List['Moves'] = []  # Liste des mouvements normaux (joueur)
    _ai_normal_moves: List['Moves'] = []  # Liste des mouvements normaux (IA)

    # Texte pour le choix des mouvements
    MOVE_CHOICE: str = (
        "1 : Headbutt \n"
        "2 : Sweep \n"
        "3 : Haze \n"
        "4 : HyperRush \n"
        "-1 : Go back \n"
    )

    def __init__(
        self,
        gui: Optional['GuiBattle'] = None,
        name: str = "",
        damage: int = 0,
        pp: int = 0,
        crit_rate: float = 0.0,
        accuracy: float = 0.0,
        side_effect: str = "",
        owner: Optional['Player'] = None
    ) -> None:
        """
        Constructs a new Move with specified attributes.

        Args:
            gui: The GUI associated with the game (optional).
            name: The name of the move (optional).
            damage: The damage value of the move (optional).
            pp: The Power Points of the move (optional).
            crit_rate: The critical rate of the move (optional).
            accuracy: The accuracy of the move (optional).
            side_effect: Any side effect the move may have (optional).
            owner: The owner of the move (optional).
        """
        self.gui: Optional['GuiBattle'] = gui
        self.name: str = name
        self.damage: int = damage
        self.pp: int = pp
        self.crit_rate: float = crit_rate
        self.accuracy: float = accuracy
        self.side_effect: str = side_effect
        self.owner: Optional['Player'] = owner

    @classmethod
    def get_normal_moves(cls) -> List['Moves']:
        """
        Returns the list of all normal moves (player).

        Returns:
            The list of normal moves.
        """
        return cls._normal_moves

    @classmethod
    def get_ai_moves(cls) -> List['Moves']:
        """
        Returns the list of all AI moves.

        Returns:
            The list of AI moves.
        """
        return cls._ai_normal_moves

    def get_owner(self) -> Optional['Player']:
        """
        Returns the owner of this move.

        Returns:
            The owner Player object.
        """
        return self.owner

    def get_name(self) -> str:
        """
        Returns the name of the move.

        Returns:
            The name of the move.
        """
        return self.name

    def get_damage(self) -> int:
        """
        Returns the damage value of the move.

        Returns:
            The damage value.
        """
        return self.damage

    def get_pp(self) -> int:
        """
        Returns the Power Points (PP) of the move.

        Returns:
            The PP value.
        """
        return self.pp

    def set_pp(self, pp: int) -> None:
        """
        Sets the Power Points (PP) of the move.

        Args:
            pp: The new PP value.
        """
        if self.pp < 0 and isinstance(self.owner, ConcretePlayer):
            self.pp = 0  # Ensure PP is always >= 0 for player's moves
        self.pp = pp

    def get_pp_ai(self) -> int:
        """
        Returns the Power Points (PP) of the move for AI.

        Returns:
            The PP value if the owner is AI, otherwise 0.
        """
        if isinstance(self.owner, Ai):
            return self.pp
        return 0

    def set_pp_ai(self, pp: int) -> None:
        """
        Sets the Power Points (PP) of the move for AI.

        Args:
            pp: The new PP value.
        """
        if pp < 0:
            self.pp = 0  # Ensure PP is always >= 0
        if isinstance(self.owner, Ai):
            self.pp = pp

    def get_accuracy(self) -> float:
        """
        Returns the accuracy of the move.

        Returns:
            The accuracy value.
        """
        return self.accuracy

    def get_crit_rate(self) -> float:
        """
        Returns the critical rate of the move.

        Returns:
            The critical rate value.
        """
        return self.crit_rate

    @staticmethod
    def accuracy_check(move_accuracy: float) -> bool:
        """
        Checks if a move hits based on its accuracy.

        Args:
            move_accuracy: The accuracy of the move.

        Returns:
            True if the move hits, False otherwise.
        """
        random_number = random.randint(0, 100)
        return random_number <= move_accuracy

    def get_side_effect(self) -> str:
        """
        Returns the side effect of the move.

        Returns:
            The side effect.
        """
        return self.side_effect

    def print_move(self) -> None:
        """
        Prints the name of the move to the console.
        """
        print(self.name)

    @staticmethod
    def print_list() -> None:
        """
        Prints the list of all available normal type moves to the console.
        """
        print("Available normal type moves:")
        for move in Moves._normal_moves:
            print(move.name)

    def add_to_list(self) -> None:
        """
        Adds this move to the player's move list.
        """
        Moves._normal_moves.append(self)

    def add_to_ai_list(self) -> None:
        """
        Adds this move to the AI's move list.
        """
        Moves._ai_normal_moves.append(self)

    def choose_move(self, callback: Callable[['Moves'], None]) -> None:
        """
        Opens a dialog for the user to choose a move from the list of normal moves.
        Once selected, the move is passed to a callback function.

        Args:
            callback: The function to process the selected move.
        """
        if not self.gui:
            return

        # Créer une fenêtre de dialogue
        dialog = tk.Toplevel(self.gui)
        dialog.title("Choose a Move")
        dialog.geometry("300x200")
        dialog.transient(self.gui)  # Empêcher l'interaction avec la fenêtre parente
        dialog.grab_set()  # Capturer les événements clavier/souris

        # Disposition verticale
        frame = tk.Frame(dialog)
        frame.pack(fill=tk.BOTH, expand=True)

        for move in Moves._normal_moves:
            button = tk.Button(
                frame,
                text=move.get_name(),
                command=lambda m=move: self._on_move_selected(m, callback, dialog)
            )
            button.pack(fill=tk.X, pady=2)

    def _on_move_selected(self, move: 'Moves', callback: Callable[['Moves'], None], dialog: tk.Toplevel) -> None:
        """
        Callback pour la sélection d'un mouvement.

        Args:
            move: Le mouvement sélectionné.
            callback: Fonction à appeler avec le mouvement sélectionné.
            dialog: La fenêtre de dialogue à fermer.
        """
        callback(move)
        dialog.destroy()