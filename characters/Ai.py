from typing import List
from .Player import Player

class Ai(Player):
    """
    AI - AI opponent with name which is derived from Player
    """

    def __init__(self, name: str = None):
        super().__init__(name)