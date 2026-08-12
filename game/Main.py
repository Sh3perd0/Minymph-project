import os
import tkinter as tk
from typing import TYPE_CHECKING, List, Optional
from map.Map import Map
from game.Game import Game
from game.GuiBattle import GuiBattle
from characters.ConcretePlayer import ConcretePlayer
from characters.Minymph import Minymph
from core.Moves import Moves
from core.Objects import Objects
from core.Bag import Bag
from game.BattleSolo import BattleSolo
from characters.Ai import Ai


class Main:
    """
    Entry point for the Minymph-style battle game.
    Initializes GUI, players, Minymphs, moves, and items, then initiates a battle.
    """

    def __init__(self) -> None:
        self.run()

    def run(self) -> None:
        # Initialize the map and game
        map_obj = Map()
        game_instance = Game(map_obj)
        game_instance.start_game()

        print(f"Répertoire de travail actuel : {os.getcwd()}")

        # Afficher la carte
        #map_obj.print_map()

        # Initialiser la GUI et le combat
        root = tk.Tk()
        gui = GuiBattle(root)

        # Créer les joueurs
        player = ConcretePlayer("player")
        ai = Ai("Vasco")

        # Créer les Minymphs
        minymph1 = Minymph(gui, "Oenoko", player)
        minymph2 = Minymph(gui, "Mangecailles", ai)
        minymph3 = Minymph(gui, "Tarteflute", player)

        # Initialiser les mouvements
        headbutt = Moves(gui, "Headbutt", 3, 20, 33.0, 85.0, "null", player)
        sweep = Moves(gui, "Sweep", 1, 20, 33.0, 100.0, "null", player)
        haze = Moves(gui, "Haze", 0, 20, 0.0, 50.0, "accuracyNerf", player)
        hyper_rush = Moves(gui, "HyperRush", 6, 1, 0.0, 100.0, "counterblow", player)

        headbutt_ai = Moves(gui, "Headbutt", 3, 20, 33.0, 85.0, "null", ai)
        sweep_ai = Moves(gui, "Sweep", 1, 20, 33.0, 100.0, "null", ai)
        haze_ai = Moves(gui, "Haze", 0, 20, 0.0, 50.0, "accuracyNerf", ai)
        hyper_rush_ai = Moves(gui, "HyperRush", 6, 1, 0.0, 100.0, "counterblow", ai)

        # Initialiser les objets
        small_potion = Objects("Small Potion", "Health", 3.0, 0, "null", player, None)
        medium_potion = Objects("Medium Potion", "Health", 5.0, 1, "null", player, None)
        large_potion = Objects("Large Potion", "Health", 7.0, 2, "null", player, None)
        full_potion = Objects("Full Potion", "Health", minymph1.get_base_hp(), 3, "null", player, None)
        revive = Objects("Revive", "Status", minymph1.get_base_hp() * 0.33, 4, "null", player, None)
        protective_crown = Objects("Protective Crown", "Battle Items", 0.0, 5, "defenseBuff", player, None)
        speedy_boots = Objects("Speedy Boots", "Battle Items", 0.0, 6, "speedBuff", player, None)
        caillou_sa_mer = Objects("Caillou sa mer", "Battle Items", 5.0, 6, "null", player, " got caillou sa mer\n\n")

        # Ajouter les mouvements à la liste
        headbutt.add_to_list()
        sweep.add_to_list()
        haze.add_to_list()
        hyper_rush.add_to_list()

        headbutt_ai.add_to_ai_list()
        sweep_ai.add_to_ai_list()
        haze_ai.add_to_ai_list()
        hyper_rush_ai.add_to_ai_list()

        # Ajouter les objets au sac
        Bag.add_to_bag(small_potion)
        Bag.add_to_bag(medium_potion)
        Bag.add_to_bag(medium_potion)
        Bag.add_to_bag(large_potion)
        Bag.add_to_bag(full_potion)
        Bag.add_to_bag(revive)
        Bag.add_to_bag(protective_crown)
        Bag.add_to_bag(speedy_boots)
        Bag.add_to_bag(caillou_sa_mer)

        # Définir les Minymphs actifs
        player.set_current_minymph(minymph1)
        ai.set_current_minymph(minymph2)

        # Ajouter les Minymphs à la liste
        minymph1.add_to_list()
        minymph2.add_to_list()
        minymph3.add_to_list()

        # Ajouter les Minymphs aux listes
        minymph1.get_my_minymphs().append(minymph3)
        minymph2.get_opponent_minymphs().append(minymph2)

        # Afficher les informations dans la GUI
        gui.get_game_output_area().insert(tk.END, minymph1.print_stats_text() + "\n\n")
        gui.get_game_output_area().insert(tk.END, minymph2.print_stats_text() + "\n\n")
        gui.get_game_output_area().insert(tk.END, minymph3.print_stats_text() + "\n\n")

        # Démarrer le combat
        duel = BattleSolo(gui, minymph1, minymph2, minymph1)
        duel.between()

        # Démarrer la boucle tkinter
        root.mainloop()

if __name__ == "__main__":
    Main()  # Instancie la classe Main