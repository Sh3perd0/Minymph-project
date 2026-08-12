from typing import TYPE_CHECKING, List, Dict, Optional, Callable
from collections import defaultdict

if TYPE_CHECKING:
    from .Objects import Objects
    from ..game.GuiBattle import GuiBattle

class Bag:
    """
    Represents a Bag for storing and managing items in the game.
    Provides functionality to display, add, remove, and filter items.
    """

    # Attributs statiques (équivalent des `static` en Java)
    _bag: List['Objects'] = []  # Liste des objets dans le sac
    _object_chosen: Optional['Objects'] = None  # Objet choisi par l'utilisateur

    def __init__(self, gui: 'GuiBattle'):
        """
        Constructs a Bag with a GUI reference.

        Args:
            gui: The GUI reference (GuiBattle instance).
        """
        self.gui = gui

    def get_bag(self) -> List['Objects']:
        """
        Returns the list of items in the bag.

        Returns:
            The list of items in the bag.
        """
        return self._bag

    @staticmethod
    def show_items(param: str) -> None:
        """
        Displays items in the bag filtered by type.

        Args:
            param: The type of items to display (e.g., "Health", "Status", "Battle Items").
        """
        filtered_items = [obj for obj in Bag._bag if obj.get_type() == param]
        filtered_items.sort(key=lambda obj: obj.get_id())

        # Compter les occurrences de chaque objet
        counts: Dict[str, int] = defaultdict(int)
        for obj in filtered_items:
            counts[obj.get_name()] += 1

        # Trier les entrées par ID de l'objet
        sorted_entries = sorted(
            counts.items(),
            key=lambda entry: Bag.find_by_name(entry[0]).get_id() if Bag.find_by_name(entry[0]) else 0
        )

        for name, count in sorted_entries:
            obj = Bag.find_by_name(name)
            if obj:
                print(f"{obj.get_id()} : {name} x{count}")

        print("-1 : Go back")

    @staticmethod
    def count_items_for_gui(param: str) -> None:
        """
        Counts and displays items in the bag filtered by type for GUI purposes.

        Args:
            param: The type of items to display (e.g., "Health", "Status", "Battle Items").
        """
        counts: Dict[str, int] = defaultdict(int)
        for obj in Bag._bag:
            if obj.get_type() == param:
                counts[obj.get_name()] += 1

        for name, count in counts.items():
            print(f"{name} x{count}")

    @staticmethod
    def show_all_items() -> None:
        """Displays all items in the bag."""
        for obj in Bag._bag:
            print(obj.get_name())

    @staticmethod
    def find_by_name(name: str) -> Optional['Objects']:
        """
        Finds an item by name.

        Args:
            name: The name of the item to find.

        Returns:
            The item if found, or None otherwise.
        """
        for obj in Bag._bag:
            if obj.get_name() == name:
                return obj
        return None

    @staticmethod
    def choose_item(param: str) -> Optional['Objects']:
        """
        Allows the user to choose an item based on the category.

        Args:
            param: The category of the item (e.g., "Health", "Status", "Battle Item").

        Returns:
            The chosen item if available, or None if canceled.
        """
        while True:
            try:
                user_input = int(input("Your choice : "))
            except ValueError:
                print("Invalid choice.")
                continue

            match user_input:
                case 0:
                    Bag._object_chosen = Bag.find_by_name("Small Potion")
                case 1:
                    Bag._object_chosen = Bag.find_by_name("Medium Potion")
                case 2:
                    Bag._object_chosen = Bag.find_by_name("Large Potion")
                case 3:
                    Bag._object_chosen = Bag.find_by_name("Full Potion")
                case -1:
                    return None
                case _:
                    print("Unrecognized choice")
                    continue

            if Bag._object_chosen and Bag._object_chosen in Bag._bag:
                Bag._bag.remove(Bag._object_chosen)
                return Bag._object_chosen
            else:
                print("The chosen item is not available. Please choose another item.")
                Bag.show_items(param)

    def choose_bag(self, callback: Callable[['Objects'], None]) -> None:
        """
        Opens a dialog to choose a bag category, then displays items in that category.

        Args:
            callback: The action to take with the selected item.
        """
        # En Python, on utilise une bibliothèque comme `tkinter` pour les interfaces graphiques.
        # Ici, je vais simuler le comportement avec des inputs console pour l'instant.
        print("\nChoose a category:")
        print("1: HP/PP")
        print("2: Status")
        print("3: Battle Items")
        print("4: Go Back")

        choice = input("Your choice: ")

        if choice == "1":
            Bag.show_items("Health")
            self._show_potion_items_gui(callback)
        elif choice == "2":
            Bag.show_items("Status")
            self._show_status_items_gui(callback)
        elif choice == "3":
            Bag.show_items("Battle Item")
            self._show_battle_items_gui(callback)
        elif choice == "4":
            return

    def _show_potion_items_gui(self, callback: Callable[['Objects'], None]) -> None:
        """
        Displays a dialog to choose a potion from the bag.

        Args:
            callback: The action to take with the selected potion.
        """
        potion_counts: Dict[str, int] = defaultdict(int)
        for potion in Bag._bag:
            if potion.get_type() == "Health":
                potion_counts[potion.get_name()] += 1

        print("\nChoose a potion:")
        for name, quantity in potion_counts.items():
            print(f"{name} x{quantity}")

        potion_name = input("Enter the name of the potion: ")
        selected_potion = Bag.find_by_name(potion_name)
        if selected_potion:
            callback(selected_potion)
            Bag.show_all_items()

    def _show_status_items_gui(self, callback: Callable[['Objects'], None]) -> None:
        """
        Displays a dialog to choose a status item from the bag.

        Args:
            callback: The action to take with the selected status item.
        """
        print("\nChoose a status item:")
        for item in Bag._bag:
            if item.get_type() == "Status":
                print(item.get_name())

        item_name = input("Enter the name of the status item: ")
        selected_item = Bag.find_by_name(item_name)
        if selected_item:
            callback(selected_item)

    def _show_battle_items_gui(self, callback: Callable[['Objects'], None]) -> None:
        """
        Displays a dialog to choose a battle item from the bag.

        Args:
            callback: The action to take with the selected battle item.
        """
        print("\nChoose a battle item:")
        for item in Bag._bag:
            if item.get_type() == "Battle Items":
                print(item.get_name())

        item_name = input("Enter the name of the battle item: ")
        selected_item = Bag.find_by_name(item_name)
        if selected_item:
            callback(selected_item)

    @staticmethod
    def add_to_bag(obj: 'Objects') -> None:
        """
        Adds an item to the bag.

        Args:
            obj: The item to add.
        """
        Bag._bag.append(obj)

    @staticmethod
    def remove_from_bag(obj_name: str) -> None:
        """
        Removes an item from the bag by name.

        Args:
            obj_name: The name of the item to remove.
        """
        item = Bag.find_by_name(obj_name)
        if item and item in Bag._bag:
            Bag._bag.remove(item)