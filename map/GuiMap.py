import tkinter as tk
from tkinter import messagebox
import random
from typing import TYPE_CHECKING, Optional, List
from PIL import Image, ImageTk 

if TYPE_CHECKING: 
    from game.Game import Game

class GuiMap(tk.Canvas):
    """
    GUI class to display the game map and handle camera movement.
    """

    TILE_SIZE = 20  # Taille en pixels d'une tuile

    def __init__(self, map_obj: 'Map', root: Optional[tk.Tk] = None) -> None:
        """
        Initializes the GUIMap with a map.

        Args:
            map_obj: The map object to display.
            root: The root Tkinter window (optional).
        """
        # Créer une fenêtre si aucune racine n'est fournie
        if root is None:
            self.root = tk.Tk()
            self.root.title("Map")
            self._setup_window()
        else:
            self.root = root

        # Initialiser le Canvas
        super().__init__(self.root, bg="white", highlightthickness=0)
        self.pack(expand=True, fill=tk.BOTH)

        # Charger les images
        self.water_1 = self._load_image("assets/water_1.png")
        self.water_2 = self._load_image("assets/water_2.png")
        self.grass = self._load_image("assets/grass.png")

        self.map = map_obj
        self.camera_x = 0  # Position X de la caméra
        self.camera_y = 0  # Position Y de la caméra

        # Configurer les bindings clavier
        self.bind("<Up>", lambda e: self.move_camera(0, -1))
        self.bind("<Down>", lambda e: self.move_camera(0, 1))
        self.bind("<Left>", lambda e: self.move_camera(-1, 0))
        self.bind("<Right>", lambda e: self.move_camera(1, 0))

        # Focus sur le Canvas pour capturer les événements clavier
        self.focus_set()

        # Dessiner la carte
        self.draw_map()

    def _setup_window(self) -> None:
        """
        Configures the window size and position.
        """
        screen_width = self.root.winfo_screenwidth()
        screen_height = self.root.winfo_screenheight()

        window_width = int(screen_width * 0.9)
        window_height = int(screen_height * 0.9)

        self.root.geometry(f"{window_width}x{window_height}")
        self.root.protocol("WM_DELETE_WINDOW", self.root.destroy)

    def _load_image(self, path: str) -> Optional[ImageTk.PhotoImage]:
        """
        Loads an image from the given path.

        Args:
            path: Path to the image file.

        Returns:
            The loaded image as a PhotoImage, or None if the image cannot be loaded.
        """
        try:
            image = Image.open(path)
            image = image.resize((self.TILE_SIZE, self.TILE_SIZE), Image.LANCZOS)
            return ImageTk.PhotoImage(image)
        except FileNotFoundError:
            print(f"Warning: Image not found at {path}. Using a placeholder.")
            return None

    def move_camera(self, dx: int, dy: int) -> None:
        """
        Moves the camera by the specified offsets.

        Args:
            dx: Horizontal offset.
            dy: Vertical offset.
        """
        max_x = len(self.map.get_map()[0]) - (self.winfo_width() // self.TILE_SIZE)
        max_y = len(self.map.get_map()) - (self.winfo_height() // self.TILE_SIZE)

        self.camera_x = max(0, min(self.camera_x + dx, max_x))
        self.camera_y = max(0, min(self.camera_y + dy, max_y))

        self.draw_map()

    def draw_map(self) -> None:
        """
        Draws the visible portion of the map.
        """
        self.delete("all")  # Effacer tout avant de redessiner

        visible_cols = self.winfo_width() // self.TILE_SIZE
        visible_rows = self.winfo_height() // self.TILE_SIZE

        for row in range(visible_rows):
            for col in range(visible_cols):
                map_row = self.camera_y + row
                map_col = self.camera_x + col

                if (
                    0 <= map_row < len(self.map.get_map()) and
                    0 <= map_col < len(self.map.get_map()[0])
                ):
                    tile_type = self.map.get_map()[map_row][map_col]

                    x = col * self.TILE_SIZE
                    y = row * self.TILE_SIZE

                    if tile_type == 0:  # Herbe
                        if self.grass:
                            self.create_image(x, y, anchor=tk.NW, image=self.grass)
                        else:
                            self.create_rectangle(x, y, x + self.TILE_SIZE, y + self.TILE_SIZE, fill="green")
                    elif tile_type == 1:  # Eau
                        if random.randint(0, 4) == 0:
                            if self.water_1:
                                self.create_image(x, y, anchor=tk.NW, image=self.water_1)
                            else:
                                self.create_rectangle(x, y, x + self.TILE_SIZE, y + self.TILE_SIZE, fill="blue")
                        else:
                            if self.water_2:
                                self.create_image(x, y, anchor=tk.NW, image=self.water_2)
                            else:
                                self.create_rectangle(x, y, x + self.TILE_SIZE, y + self.TILE_SIZE, fill="cyan")
                    elif tile_type == 2:  # Mur
                        self.create_rectangle(x, y, x + self.TILE_SIZE, y + self.TILE_SIZE, fill="black")

        # Dessiner le joueur
        from game.Game import Game  # ✅ Import dynamique de Game ici
        player_screen_x = (Game.get_player_x() - self.camera_x) * self.TILE_SIZE
        player_screen_y = (Game.get_player_y() - self.camera_y) * self.TILE_SIZE
        self.create_rectangle(
            player_screen_x, player_screen_y,
            player_screen_x + self.TILE_SIZE, player_screen_y + self.TILE_SIZE,
            fill="red"
        )

    def display(self) -> None:
        """
        Displays the map window.
        """
        if self.root is not None:
            self.root.mainloop()

# Exemple d'utilisation
if __name__ == "__main__":
    # Supposons que Map et Game sont définis ailleurs
    from game import Game
    from map import Map

    map_obj = Map()  # À définir selon ton implémentation
    gui_map = GuiMap(map_obj)
    gui_map.display()