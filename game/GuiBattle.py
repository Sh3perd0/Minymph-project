import tkinter as tk
from tkinter import scrolledtext, messagebox
from typing import Optional

class GuiBattle(tk.Tk):
    """
    GUI class for the user interface of a Minymph battle game.
    Displays game information, handles user input, and provides action buttons for interaction.
    """

    def __init__(self) -> None:
        """
        Constructor for GuiBattle - initializes the game window and sets up components.
        """
        super().__init__()

        # Main window configuration
        self.title("Minymph Battle")
        self.geometry("1200x800")

        # Game message display area
        self.game_output_area = scrolledtext.ScrolledText(self, wrap=tk.WORD, height=10, width=50)
        self.game_output_area.config(state=tk.DISABLED)  # Non-editable
        self.game_output_area.pack(expand=True, fill=tk.BOTH, padx=10, pady=10)

        # Action panel (buttons)
        action_panel = tk.Frame(self)
        action_panel.pack(fill=tk.X, padx=10, pady=5)

        self.attack_button = tk.Button(action_panel, text="Attack", command=lambda: self._on_attack())
        self.bag_button = tk.Button(action_panel, text="Bag", command=lambda: self._on_bag())
        self.minymph_button = tk.Button(action_panel, text="Minymph", command=lambda: self._on_minymph())
        self.surrender_button = tk.Button(action_panel, text="Surrender", command=lambda: self._on_surrender())

        self.attack_button.pack(side=tk.LEFT, padx=5)
        self.bag_button.pack(side=tk.LEFT, padx=5)
        self.minymph_button.pack(side=tk.LEFT, padx=5)
        self.surrender_button.pack(side=tk.LEFT, padx=5)

        # Players and Minymph
        self.player: Optional['Player'] = None
        self.enemy: Optional['Player'] = None
        self.player_minymph: Optional['Minymph'] = None
        self.enemy_minymph: Optional['Minymph'] = None

        # Battle manager instance
        self.battle: Optional['BattleSolo'] = None

        # Initialize the game
        self.initialize_game()

        # Center the window
        self.center_window()

    def center_window(self) -> None:
        """
        Centers the window on the screen.
        """
        self.update_idletasks()
        width = self.winfo_width()
        height = self.winfo_height()
        screen_width = self.winfo_screenwidth()
        screen_height = self.winfo_screenheight()
        x = (screen_width - width) // 2
        y = (screen_height - height) // 2
        self.geometry(f"{width}x{height}+{x}+{y}")

    def disable_buttons(self) -> None:
        """
        Disables all action buttons.
        """
        self.attack_button.config(state=tk.DISABLED)
        self.bag_button.config(state=tk.DISABLED)
        self.minymph_button.config(state=tk.DISABLED)
        self.surrender_button.config(state=tk.DISABLED)
        self.update()

    def disable_attack(self) -> None:
        """
        Disables the Attack button.
        """
        self.attack_button.config(state=tk.DISABLED)
        self.update()

    def disable_bag(self) -> None:
        """
        Disables the Bag button.
        """
        self.bag_button.config(state=tk.DISABLED)
        self.update()

    def close_window(self) -> None:
        """
        Closes the battle window.
        """
        self.destroy()

    def enable_attack(self) -> None:
        """
        Enables the Attack button.
        """
        self.attack_button.config(state=tk.NORMAL)
        self.update()

    def enable_bag(self) -> None:
        """
        Enables the Bag button.
        """
        self.bag_button.config(state=tk.NORMAL)
        self.update()

    def enable_buttons(self) -> None:
        """
        Enables all action buttons.
        """
        self.attack_button.config(state=tk.NORMAL)
        self.bag_button.config(state=tk.NORMAL)
        self.minymph_button.config(state=tk.NORMAL)
        self.surrender_button.config(state=tk.NORMAL)

    def initialize_game(self) -> None:
        """
        Initializes the game by creating Minymph and starting a BattleSolo instance.
        """
        # Note: `player` and `enemy` are not initialized in the original Java code.
        # Assuming they are created elsewhere or passed to the constructor.
        self.player_minymph = Minymph(self, "Oenoko", self.player)
        self.enemy_minymph = Minymph(self, "Mangecailles", self.enemy)
        self.battle = BattleSolo(self, self.player_minymph, self.enemy_minymph, self.player_minymph)

    def get_game_output_area(self) -> scrolledtext.ScrolledText:
        """
        Retrieves the game output area to display game messages.

        Returns:
            The game output text area.
        """
        return self.game_output_area

    def append_to_output(self, text: str) -> None:
        """
        Appends text to the game output area.

        Args:
            text: The text to append.
        """
        self.game_output_area.config(state=tk.NORMAL)
        self.game_output_area.insert(tk.END, text)
        self.game_output_area.config(state=tk.DISABLED)
        self.game_output_area.see(tk.END)  # Scroll to the end

    def _on_attack(self) -> None:
        """
        Handles the Attack button click event.
        """
        if self.battle:
            self.battle.handle_user_input("1")  # Attack

    def _on_bag(self) -> None:
        """
        Handles the Bag button click event.
        """
        if self.battle:
            self.battle.handle_user_input("2")  # Open bag

    def _on_minymph(self) -> None:
        """
        Handles the Minymph button click event.
        """
        if self.battle:
            self.battle.handle_user_input("3")  # Switch Minymph

    def _on_surrender(self) -> None:
        """
        Handles the Surrender button click event.
        """
        if self.battle:
            self.battle.handle_user_input("4")  # Surrender

    def run(self) -> None:
        """
        Main entry point to launch the application and display the GUI.
        """
        self.mainloop()

# Exemple d'utilisation
if __name__ == "__main__":
    gui = GuiBattle()
    gui.run()