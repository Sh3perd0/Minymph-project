import random
from typing import TYPE_CHECKING, List, Optional, Callable, Any
import tkinter as tk
from tkinter import ttk

if TYPE_CHECKING:
    from ..game.GuiBattle import GuiBattle
    from .Player import Player
    from ..core.TypeMinymph import TypeMinymph
    from ..game.BattleSolo import BattleSolo

class Minymph:
    """
    Represents a Minymph character with various attributes, abilities, and status.
    Each Minymph has a unique owner, type, and set of stats.
    """

    # Attributs statiques (partagés par toutes les instances)
    _id_count: int = -3  # Départ à -3 pour correspondre au code Java original
    _minymphs: List['Minymph'] = []  # Liste de tous les Minymphs
    _available_minymphs: List['Minymph'] = []  # Liste des Minymphs disponibles
    _my_minymphs: List['Minymph'] = []  # Liste des Minymphs du joueur
    _opponent_minymphs: List['Minymph'] = []  # Liste des Minymphs adverses

    def __init__(self, gui: Optional['GuiBattle'] = None, name: str = "", owner: Optional['Player'] = None) -> None:
        """
        Constructs a new Minymph with specified GUI, name, and owner.

        Args:
            gui: The GUI instance (optional).
            name: Name of the Minymph (optional).
            owner: Owner of the Minymph (optional).
        """
        self.name: str = name
        self.type: str = ""
        self.gui: Optional['GuiBattle'] = gui
        self.owner: Optional['Player'] = owner
        self.hp: float = 20.0
        self.base_hp: float = 20.0
        self.xp: int = 0
        self.lvl: int = 0
        self.atk: int = 0
        self.def_: int = 0  # 'def' est un mot-clé en Python, donc on utilise 'def_'
        self.atkspe: int = 0
        self.defspe: int = 0
        self.speed: int = 0
        self.current_status: str = "Alive"
        self.status: str = "Alive"
        self.id: int = 0
        self.type_minymph: Optional['TypeMinymph'] = None

        # Incrémenter l'ID
        Minymph._id_count += 1
        self.id = Minymph._id_count

        # Initialiser les stats aléatoires
        if name and owner:  # Si un nom et un propriétaire sont fournis
            self.atk = self._init_minymph("atk")
            self.def_ = self._init_minymph("def")
            self.atkspe = self._init_minymph("atkspe")
            self.defspe = self._init_minymph("defspe")
            self.speed = self._init_minymph("speed")
            self.type = self._init_minymph("type")

    @classmethod
    def get_id_count(cls) -> int:
        """Retourne le compteur d'ID statique."""
        return cls._id_count

    @classmethod
    def set_id_count(cls, value: int) -> None:
        """Définit le compteur d'ID statique."""
        cls._id_count = value

    def get_owner_name(self) -> str:
        """
        Returns the owner's name of this Minymph.

        Returns:
            The owner's name.
        """
        if self.owner:
            return self.owner.get_name()
        return "No owner"

    def get_owner_wt(self) -> Optional['Player']:
        """
        Returns the owner of this Minymph without additional text.

        Returns:
            The owner Player object.
        """
        return self.owner

    def get_my_minymphs(self) -> List['Minymph']:
        """
        Returns the list of all the player's Minymphs.

        Returns:
            The list of player's Minymphs.
        """
        return Minymph._my_minymphs

    def get_opponent_minymphs(self) -> List['Minymph']:
        """
        Returns the list of opponent's Minymphs.

        Returns:
            The list of opponent's Minymphs.
        """
        return Minymph._opponent_minymphs

    def add_to_list(self) -> None:
        """
        Adds this Minymph to the list of all Minymphs.
        """
        Minymph._minymphs.append(self)

    def choose_minymph(self, callback: Callable[['Minymph'], None]) -> None:
        """
        Opens a dialog to select an available Minymph for a specified action.

        Args:
            callback: Function to call with the selected Minymph.
        """
        if not self.gui:
            return

        # Créer une fenêtre de dialogue
        dialog = tk.Toplevel(self.gui)
        dialog.title("Choose Minymph")
        dialog.geometry("300x200")
        dialog.transient(self.gui)  # Empêcher l'interaction avec la fenêtre parente
        dialog.grab_set()  # Capturer les événements clavier/souris

        # Disposition verticale
        frame = tk.Frame(dialog)
        frame.pack(fill=tk.BOTH, expand=True)

        for minymph in Minymph._available_minymphs:
            button = tk.Button(
                frame,
                text=minymph.get_name(),
                command=lambda m=minymph: self._on_minymph_selected(m, callback, dialog)
            )
            button.pack(fill=tk.X, pady=2)

    def _on_minymph_selected(self, minymph: 'Minymph', callback: Callable[['Minymph'], None], dialog: tk.Toplevel) -> None:
        """
        Callback pour la sélection d'un Minymph.

        Args:
            minymph: Le Minymph sélectionné.
            callback: Fonction à appeler avec le Minymph sélectionné.
            dialog: La fenêtre de dialogue à fermer.
        """
        callback(minymph)
        dialog.destroy()
        # Mettre à jour la liste des Minymphs disponibles
        Minymph._available_minymphs.remove(minymph)
        if BattleSolo.get_my_previous_minymph():
            Minymph._available_minymphs.append(BattleSolo.get_my_previous_minymph())

    def print_my_minymph(self) -> None:
        """
        Prints all available Minymphs owned by the player and updates the available Minymphs list.
        """
        Minymph._available_minymphs.clear()  # Vider la liste avant de la remplir
        for minymph in Minymph._minymphs:
            if (minymph.get_owner_name() == "player" and
                minymph != BattleSolo.get_my_current_minymph() and
                minymph.get_status() != "KO"):
                if minymph not in Minymph._available_minymphs:
                    Minymph._available_minymphs.append(minymph)

    @staticmethod
    def print_all_available_minymph() -> None:
        """
        Prints the names of all available Minymphs.
        """
        for minymph in Minymph._available_minymphs:
            if minymph.get_status() != "KO":
                print(minymph.get_name())

    def get_hp(self) -> float:
        """
        Returns the current HP of this Minymph.

        Returns:
            The current HP.
        """
        return self.hp

    @staticmethod
    def get_all_minymphs() -> List['Minymph']:
        """
        Returns the list of all Minymphs.

        Returns:
            The list of all Minymphs.
        """
        return Minymph._minymphs

    def get_base_hp(self) -> float:
        """
        Returns the base HP of this Minymph.

        Returns:
            The base HP.
        """
        return self.base_hp

    def get_name(self) -> str:
        """
        Returns the name of this Minymph.

        Returns:
            The name.
        """
        return self.name

    def get_lvl(self) -> int:
        """
        Returns the level of this Minymph.

        Returns:
            The level.
        """
        return self.lvl

    def get_type(self) -> str:
        """
        Returns the type of this Minymph.

        Returns:
            The type.
        """
        return self.type

    def get_atk(self) -> int:
        """
        Returns the attack stat of this Minymph and prints its value.

        Returns:
            The attack stat.
        """
        print(f"Minymph {self.name} currently has {self.atk} attack.")
        return self.atk

    def get_def(self) -> int:
        """
        Returns the defense stat of this Minymph and prints its value.

        Returns:
            The defense stat.
        """
        print(f"Minymph {self.name} currently has {self.def_} defense.")
        return self.def_

    def get_atkspe(self) -> int:
        """
        Returns the special attack stat of this Minymph and prints its value.

        Returns:
            The special attack stat.
        """
        print(f"Minymph {self.name} currently has {self.atkspe} special attack.")
        return self.atkspe

    def get_defspe(self) -> int:
        """
        Returns the special defense stat of this Minymph and prints its value.

        Returns:
            The special defense stat.
        """
        print(f"Minymph {self.name} currently has {self.defspe} special defense.")
        return self.defspe

    def get_speed(self) -> int:
        """
        Returns the speed stat of this Minymph and prints its value.

        Returns:
            The speed stat.
        """
        print(f"Minymph {self.name} currently has {self.speed} speed.")
        return self.speed

    def get_status(self) -> str:
        """
        Returns the status of this Minymph.

        Returns:
            The status.
        """
        return self.status

    def get_hp_text(self) -> str:
        """
        Returns the current HP as a formatted string and prints it.

        Returns:
            A formatted string with HP information.
        """
        text = f"Minymph {self.name} currently has {self.hp} HP\n"
        print(text)
        return text

    def set_status(self, status: str) -> None:
        """
        Sets the status of this Minymph.
        If the status is set to "KO", HP is set to 0.

        Args:
            status: The new status.
        """
        if self.get_status() != "KO":
            self.status = status
        self.set_hp(0.0)

    def set_hp(self, value: float) -> None:
        """
        Sets the HP of this Minymph, rounding down to the nearest integer.

        Args:
            value: The new HP value.
        """
        self.hp = int(value)  # Équivalent à Math.floor en Python pour les entiers

    def set_type(self, type_minymph: 'TypeMinymph') -> None:
        """
        Sets the type of this Minymph.

        Args:
            type_minymph: The new TypeMinymph object.
        """
        self.type_minymph = type_minymph

    def set_name(self, name: str) -> None:
        """
        Sets the name of this Minymph.

        Args:
            name: The new name.
        """
        self.name = name

    def set_xp(self, xp: int) -> None:
        """
        Sets the experience points (XP) of this Minymph.

        Args:
            xp: The new XP value.
        """
        self.xp = xp

    def get_xp(self) -> int:
        """
        Returns the experience points (XP) of this Minymph.

        Returns:
            The XP value.
        """
        return self.xp

    def set_lvl(self, lvl: int) -> None:
        """
        Sets the level of this Minymph.

        Args:
            lvl: The new level.
        """
        self.lvl = lvl

    def set_atk(self, atk: int) -> None:
        """
        Sets the attack stat of this Minymph.

        Args:
            atk: The new attack value.
        """
        self.atk = atk

    def set_def(self, def_: int) -> None:
        """
        Sets the defense stat of this Minymph.

        Args:
            def_: The new defense value.
        """
        self.def_ = def_

    def set_atkspe(self, atkspe: int) -> None:
        """
        Sets the special attack stat of this Minymph.

        Args:
            atkspe: The new special attack value.
        """
        self.atkspe = atkspe

    def set_defspe(self, defspe: int) -> None:
        """
        Sets the special defense stat of this Minymph.

        Args:
            defspe: The new special defense value.
        """
        self.defspe = defspe

    def set_speed(self, speed: int) -> None:
        """
        Sets the speed stat of this Minymph.

        Args:
            speed: The new speed value.
        """
        self.speed = speed

    def _init_minymph(self, param: str) -> Any:
        """
        Initializes the specified attribute of this Minymph with a random value.

        Args:
            param: The attribute to initialize.

        Returns:
            The initialized value.
        """
        if param == "atk":
            self.atk = random.randint(1, 9)
            return self.atk
        elif param == "def":
            self.def_ = random.randint(1, 9)
            return self.def_
        elif param == "atkspe":
            self.atkspe = random.randint(1, 9)
            return self.atkspe
        elif param == "defspe":
            self.defspe = random.randint(1, 9)
            return self.defspe
        elif param == "speed":
            self.speed = random.randint(1, 9)
            return self.speed
        elif param == "type":
            if self.type_minymph:
                self.type = self.type_minymph.set_type()
            else:
                self.type_minymph = TypeMinymph()
                self.type = self.type_minymph.set_type()
            return self.type
        else:
            return None

    def print_stats_text(self) -> str:
        """
        Returns a formatted string of this Minymph's stats.

        Returns:
            A formatted string with all stats.
        """
        stats = (
            f"Base HP of {self.name} is: {self.hp}\n"
            f"ID of {self.name} is: {self.id}\n"
            f"Base attack of {self.name} is: {self.atk}\n"
            f"Base defense of {self.name} is: {self.def_}\n"
            f"Base special attack of {self.name} is: {self.atkspe}\n"
            f"Base special defense of {self.name} is: {self.defspe}\n"
            f"Base speed of {self.name} is: {self.speed}\n"
            f"Type of {self.name} is: {self.type}\n"
            f"Owner of {self.name} is: {self.get_owner_name()}\n"
        )
        return stats