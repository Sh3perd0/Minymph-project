import random
from typing import List, Optional

class Map:
    """
    Represents a game map loaded from a file or generated randomly.
    Provides methods to read, update, and print the map.
    """

    def __init__(self, file_path: str = "assets/map.txt") -> None:
        """
        Initializes the map by reading it from a file.

        Args:
            file_path: Path to the file containing the map data.
        """
        self.file_path: str = file_path
        self._map: List[List[int]] = []

        try:
            self._map = self._read_matrix(self.file_path)
        except IOError as e:
            print(f"Erreur lors de la lecture du fichier {self.file_path}: {e}")
            # Si la lecture échoue, créer une carte par défaut
            self._map = self.create_square_matrix_with_borders(150)

    def _read_matrix(self, file_path: str) -> List[List[int]]:
        """
        Reads a matrix from a file.

        Args:
            file_path: Path to the file containing the matrix data.

        Returns:
            The matrix as a 2D list of integers.

        Raises:
            IOError: If the file cannot be read.
            ValueError: If the file contains invalid data.
        """
        matrix: List[List[int]] = []

        try:
            with open(file_path, 'r') as file:
                # Compter le nombre de lignes non vides
                row_count = 0
                for line in file:
                    if line.strip():
                        row_count += 1

                # Revenir au début du fichier
                file.seek(0)

                # Lire les données
                current_row = 0
                for line in file:
                    if line.strip():
                        values = line.strip().split()
                        row = []
                        for value in values:
                            try:
                                row.append(int(value))
                            except ValueError:
                                raise ValueError(f"Erreur de format dans la ligne : {line}")
                        matrix.append(row)
                        current_row += 1

        except IOError as e:
            print(f"Erreur lors de la lecture du fichier {file_path}: {e}")
            raise IOError(f"Impossible de lire le fichier {file_path}")

        return matrix

    @staticmethod
    def create_square_matrix_with_borders(size: int) -> List[List[int]]:
        """
        Creates a random map with borders, rivers, and lakes.

        Args:
            size: Size of the square matrix.

        Returns:
            A 2D list representing the map.
        """
        y_size = size + 50
        matrix: List[List[int]] = [[0 for _ in range(y_size)] for _ in range(size)]

        # Ajouter des bordures (murs)
        for i in range(size):
            for j in range(y_size):
                if i == 0 or i == size - 1 or j == 0 or j == y_size - 1:
                    matrix[i][j] = 2  # 2 = mur

        # Ajouter des rivières
        number_of_rivers = 2 + random.randint(0, 1)  # 2 ou 3 rivières
        for _ in range(number_of_rivers):
            start_row = random.randint(1, size - 2)
            start_col = random.randint(1, y_size - 2)
            length = random.randint(size // 8, size // 4 + size // 8)
            direction = random.choice([0, 1])  # 0 = vertical, 1 = horizontal
            thickness = 1 + random.randint(0, 1)  # Épaisseur de 1 ou 2

            for _ in range(length):
                for t in range(-thickness, thickness + 1):
                    adjusted_row = start_row + (0 if direction == 1 else t)
                    adjusted_col = start_col + (t if direction == 1 else 0)

                    if (1 <= adjusted_row < size - 1 and
                        1 <= adjusted_col < y_size - 1):
                        matrix[adjusted_row][adjusted_col] = 1  # 1 = eau

                if direction == 0:  # Vertical
                    start_row += random.randint(-1, 2)
                    start_col += 1
                else:  # Horizontal
                    start_col += random.randint(-1, 2)
                    start_row += 1

        # Ajouter des lacs
        number_of_lakes = 1 + random.randint(0, 1)  # 1 ou 2 lacs
        for _ in range(number_of_lakes):
            lake_center_row = random.randint(2, size - 3)
            lake_center_col = random.randint(2, y_size - 3)
            lake_radius = random.randint(10, 11)  # Rayon entre 10 et 11

            for i in range(-lake_radius, lake_radius + 1):
                for j in range(-lake_radius, lake_radius + 1):
                    if (lake_center_row + i >= 1 and lake_center_row + i < size - 1 and
                        lake_center_col + j >= 1 and lake_center_col + j < y_size - 1):
                        if (i ** 2 + j ** 2) ** 0.5 <= lake_radius + random.randint(0, 1):
                            matrix[lake_center_row + i][lake_center_col + j] = 1  # 1 = eau

        return matrix

    def get_map(self) -> List[List[int]]:
        """
        Returns the map matrix.

        Returns:
            The 2D list representing the map.
        """
        return self._map

    def update_map(self, x: int, y: int, value: int) -> None:
        """
        Updates the value at the specified coordinates.

        Args:
            x: X-coordinate.
            y: Y-coordinate.
            value: New value to set at (x, y).
        """
        if 0 <= x < len(self._map[0]) and 0 <= y < len(self._map):
            self._map[y][x] = value
        else:
            print(f"Coordonnées hors limites : ({x}, {y})")

    def print_map(self) -> None:
        """
        Prints the map to the console.
        """
        for row in self._map:
            print(" ".join(map(str, row)))

    def __str__(self) -> str:
        """
        Returns a string representation of the map.
        """
        return "\n".join(" ".join(map(str, row)) for row in self._map)