import random
import threading
import time
from typing import TYPE_CHECKING, Optional, Callable, List

if TYPE_CHECKING:
    from ..game.GuiBattle import GuiBattle
    from ..characters.Minymph import Minymph
    from ..core.Moves import Moves
    from ..core.Bag import Bag
    from ..core.Objects import Objects
    from ..characters.Ai import Ai

class BattleSolo:
    """
    BattleSolo - Manages a solo battle between two Minymphs.
    Contains options for various moves, bag usage, and handling player actions.
    """

    def __init__(
        self,
        gui: 'GuiBattle',
        minymph1: 'Minymph',
        minymph2: 'Minymph',
        my_current_minymph: 'Minymph'
    ) -> None:
        """
        Initializes the battle with the GUI, player's minymph, and opponent's minymph.

        Args:
            gui: The GUI for displaying battle messages.
            minymph1: Player's active Minymph.
            minymph2: Opponent's active Minymph.
            my_current_minymph: The current active minymph.
        """
        self.gui = gui
        self.minymph1 = minymph1
        self.minymph2 = minymph2
        self.my_current_minymph = my_current_minymph
        self.my_previous_minymph: Optional['Minymph'] = None
        self.move = Moves(gui)
        self.bag = Bag(gui)
        self.ai = Ai()

        # Battle state variables
        self.all_players_minymph_dead = False
        self.all_ai_minymph_dead = False
        self.move_selected = True
        self.ai_multiplicative_factor = 1.0
        self.players_multiplicative_factor = 1.0
        self.extra_damage_ai = 0.0
        self.extra_damage_player = 0.0
        self.compteur_tour = 1
        self.players_turn = True
        self.options_displayed = False
        self.battle_timer: Optional[threading.Timer] = None

    def between(self) -> None:
        """
        Starts the battle timer and displays the player's options each turn.
        """
        print(f"Minymph currently in battle: {self.my_current_minymph.get_name()}")

        def display_options() -> None:
            if not self.options_displayed:
                self.gui.get_game_output_area().append(
                    f"Player's current minymph is {self.my_current_minymph.get_name()}\n"
                )
                self.gui.get_game_output_area().append(
                    f"AI current minymph is {self.minymph2.get_name()}\n"
                )
                self.gui.get_game_output_area().append("\n--Tour-- 1\n")
                self.options_displayed = True

        # Utilisation de threading.Timer pour simuler le Timer de Java
        self.battle_timer = threading.Timer(1.0, display_options)
        self.battle_timer.start()

    def get_turn(self) -> bool:
        """
        Returns True if it's the player's turn.

        Returns:
            The value of players_turn.
        """
        return self.players_turn

    def execute_attack_ai(self) -> None:
        """
        AI's attack method.
        """
        if not self.move_selected:
            return

        if self.minymph2.get_status() != "KO":
            self.players_turn = False
            moves_list = Moves.get_ai_moves()
            random_move = random.choice(moves_list)  # AI chooses a random move

            # Check for accuracy nerf side effect
            if (
                random_move.get_side_effect() == "accuracyNerf"
                and Moves.accuracy_check(random_move.get_accuracy() * (1 - (1 - self.ai_multiplicative_factor)))
            ):
                self.players_multiplicative_factor = max(
                    self.players_multiplicative_factor - 0.33, 0.10
                )
                self.gui.get_game_output_area().append(
                    f"\n{self.my_current_minymph.get_name()}'s accuracy fell!\n"
                )

            # Check if the move hits
            if Moves.accuracy_check(
                random_move.get_accuracy() * (1 - (1 - self.ai_multiplicative_factor))
            ):
                if random_move.get_pp_ai() != 0:
                    self.gui.get_game_output_area().append(
                        f"\n{self.minymph2.get_name()} uses {random_move.get_name()}!\n"
                    )
                    random_move.set_pp_ai(random_move.get_pp_ai() - 1)

                    # Check for critical hit
                    if Moves.accuracy_check(random_move.get_crit_rate()):
                        self.extra_damage_ai = random_move.get_damage() * 0.15
                        self.gui.get_game_output_area().append("\nCritical hit!\n")
                        print(f"\nvalue of extraDamage {self.extra_damage_ai}\n")

                    # Handle counterblow side effect
                    if random_move.get_side_effect() == "counterblow":
                        self.minymph2.set_hp(
                            self.minymph2.get_hp() - random_move.get_damage() * 0.3
                        )
                        self.my_current_minymph.set_hp(
                            max(
                                0.0,
                                self.my_current_minymph.get_hp() - random_move.get_damage() - self.extra_damage_ai
                            )
                        )

                        if self.my_current_minymph.get_hp() <= 0.0:
                            self.gui.get_game_output_area().append(
                                f"\n{self.my_current_minymph.get_name()} now has 0 HP\n"
                            )
                        else:
                            self.gui.get_game_output_area().append(
                                f"\n{self.my_current_minymph.get_name()} now has {self.my_current_minymph.get_hp()} HP\n"
                            )
                        self.gui.get_game_output_area().append(
                            f"\n{self.minymph2.get_name()} is hurt by recoil!\n"
                        )
                        self.gui.get_game_output_area().append(
                            f"\n{self.minymph2.get_name()} has {self.minymph2.get_hp()} HP\n"
                        )

                    elif random_move.get_side_effect() == "accuracyNerf":
                        self.gui.get_game_output_area().append(
                            f"\n{self.my_current_minymph.get_name()}'s accuracy fell!\n"
                        )

                    else:
                        self.my_current_minymph.set_hp(
                            max(
                                0.0,
                                self.my_current_minymph.get_hp() - random_move.get_damage() - self.extra_damage_ai
                            )
                        )
                        if self.my_current_minymph.get_hp() <= 0:
                            self.gui.get_game_output_area().append(
                                f"\n{self.my_current_minymph.get_name()} now has 0 HP\n"
                            )
                        else:
                            self.gui.get_game_output_area().append(
                                f"\n{self.my_current_minymph.get_name()} now has {self.my_current_minymph.get_hp()} HP\n"
                            )

                else:
                    self.gui.get_game_output_area().append(
                        f"\n{self.minymph2.get_name()} uses {random_move.get_name()}!\n"
                    )
                    self.gui.get_game_output_area().append("\nNo PP left for this move\n")
                    self.execute_attack_ai()  # Recursive call to choose another move

            else:
                self.gui.get_game_output_area().append(
                    f"\n{self.minymph2.get_name()} uses {random_move.get_name()}!\n"
                )
                self.gui.get_game_output_area().append("\nBut it failed!\n")

            self.players_turn = True
            self.check_for_ko()
            print("on teste si ko")
            print("on teste si combat fini, soit si tt minymph ko")
            self.check_for_end_battle()

            if self.all_ai_minymph_dead:
                self.desactivate_buttons()
                if self.battle_timer:
                    self.battle_timer.cancel()

    def sleep(self, value: int) -> None:
        """
        Pauses execution for a specified number of milliseconds.

        Args:
            value: Time to sleep in milliseconds.
        """
        time.sleep(value / 1000)  # Convert milliseconds to seconds

    @staticmethod
    def get_my_current_minymph() -> 'Minymph':
        """
        Retrieves the current Minymph of the player.

        Returns:
            The current active Minymph of the player.
        """
        return BattleSolo.my_current_minymph

    @staticmethod
    def get_my_previous_minymph() -> 'Minymph':
        """
        Retrieves the previous Minymph of the player.

        Returns:
            The previous Minymph if a switch occurred, otherwise the current Minymph.
        """
        if BattleSolo.my_previous_minymph == BattleSolo.my_current_minymph:
            print("No minymph switch yet!")
            return BattleSolo.my_current_minymph
        return BattleSolo.my_previous_minymph

    def handle_user_input(self, input_str: str) -> None:
        """
        Handles user input for each action in the battle (attack, use bag, switch minymph, or surrender).

        Args:
            input_str: The player's choice as a string (1 for attack, 2 for bag, etc.).
        """
        if self.minymph2.get_status() == "KO":
            if self.battle_timer:
                self.battle_timer.cancel()
            return

        if self.players_turn:
            match input_str:
                case "1":
                    self.move.choose_move(lambda move_chosen: self._handle_attack_choice(move_chosen))

                case "2":
                    self.gui.get_game_output_area().append("\nYou reach for your bag...\n")
                    self.bag.choose_bag(lambda object_chosen: self._handle_bag_choice(object_chosen))

                case "3":
                    self.gui.get_game_output_area().append("Which Minymph will you choose?\n")
                    self.minymph1.print_my_minymph()
                    self.minymph1.chose_minymph(
                        lambda minymph_chosen: self._handle_minymph_choice(minymph_chosen)
                    )

                case "4":
                    self.gui.get_game_output_area().append("You're fleeing!\n")
                    self.desactivate_buttons()
                    if self.battle_timer:
                        self.battle_timer.cancel()
                    return

                case _:
                    self.gui.get_game_output_area().append("Unrecognized choice\n")

            self.options_displayed = False

    def _handle_attack_choice(self, move_chosen: Optional['Moves']) -> None:
        """
        Handles the logic after the player chooses an attack.

        Args:
            move_chosen: The move chosen by the player.
        """
        if (
            move_chosen is not None
            and self.my_current_minymph.get_status() != "KO"
            and move_chosen.get_pp() != 0
        ):
            if self.my_current_minymph.get_speed() > self.minymph2.get_speed():
                self.execute_attack(move_chosen, self.my_current_minymph, self.minymph2)
                self.execute_attack_ai()
            elif self.my_current_minymph.get_speed() < self.minymph2.get_speed():
                self.execute_attack_ai()
                self.execute_attack(move_chosen, self.my_current_minymph, self.minymph2)
            else:
                if random.randint(0, 1) == 0:
                    self.execute_attack(move_chosen, self.my_current_minymph, self.minymph2)
                    self.execute_attack_ai()
                else:
                    self.execute_attack_ai()
                    self.execute_attack(move_chosen, self.my_current_minymph, self.minymph2)

            self.compteur_tour += 1
            self.gui.get_game_output_area().append(f"\n--Tour-- {self.compteur_tour}\n")

    def _handle_bag_choice(self, object_chosen: Optional['Objects']) -> None:
        """
        Handles the logic after the player chooses an item from the bag.

        Args:
            object_chosen: The item selected by the player.
        """
        if object_chosen is not None and self.use_bag_item(object_chosen):
            self.execute_attack_ai()
            self.compteur_tour += 1
            self.gui.get_game_output_area().append(f"\n--Tour-- {self.compteur_tour}\n")

    def _handle_minymph_choice(self, minymph_chosen: Optional['Minymph']) -> None:
        """
        Handles the logic after the player chooses a Minymph to switch to.

        Args:
            minymph_chosen: The Minymph chosen by the player.
        """
        if minymph_chosen is not None and minymph_chosen.get_status() != "KO":
            self.my_previous_minymph = self.my_current_minymph
            self.my_current_minymph = minymph_chosen
            self.gui.get_game_output_area().append(
                f"The minymph {minymph_chosen.get_name()} has been chosen!\n"
            )
            self.gui.enable_attack()
            self.gui.enable_bag()
            self.execute_attack_ai()
            self.compteur_tour += 1
            self.gui.get_game_output_area().append(f"\n--Tour-- {self.compteur_tour}\n")
        else:
            self.gui.get_game_output_area().append(
                f"Impossible! {minymph_chosen.get_name()} is KO!\n"
            )

    def execute_attack(
        self,
        move_chosen: 'Moves',
        minymph1: 'Minymph',
        minymph2: 'Minymph'
    ) -> None:
        """
        Executes the player's chosen attack, adjusting health and checking for KO status.

        Args:
            move_chosen: The move chosen by the player.
            minymph1: The attacking Minymph.
            minymph2: The defending Minymph.
        """
        if (
            move_chosen.get_side_effect() == "accuracyNerf"
            and Moves.accuracy_check(move_chosen.get_accuracy() * (1 - (1 - self.players_multiplicative_factor)))
        ):
            self.ai_multiplicative_factor = max(self.ai_multiplicative_factor - 0.33, 0.10)

        if Moves.accuracy_check(
            move_chosen.get_accuracy() * (1 - (1 - self.players_multiplicative_factor))
        ):
            if move_chosen.get_pp() != 0:
                self.gui.get_game_output_area().append(
                    f"\n{minymph1.get_name()} uses {move_chosen.get_name()}!\n"
                )
                move_chosen.set_pp(move_chosen.get_pp() - 1)

                if Moves.accuracy_check(move_chosen.get_crit_rate()):
                    self.extra_damage_player = move_chosen.get_damage() * 0.15
                    self.gui.get_game_output_area().append("\nCritical hit!\n")

                if move_chosen.get_side_effect() == "counterblow":
                    minymph1.set_hp(
                        minymph1.get_hp() - move_chosen.get_damage() * 0.3
                    )
                    minymph2.set_hp(
                        max(
                            0.0,
                            minymph2.get_hp() - move_chosen.get_damage() - self.extra_damage_player
                        )
                    )
                    if minymph2.get_hp() <= 0.0:
                        self.gui.get_game_output_area().append(
                            f"\n{minymph2.get_name()} now has 0 HP\n"
                        )
                    else:
                        self.gui.get_game_output_area().append(
                            f"\n{minymph2.get_name()} now has {minymph2.get_hp()} HP\n"
                        )
                    self.gui.get_game_output_area().append(
                        f"\n{minymph1.get_name()} is hurt by recoil!\n"
                    )
                    self.gui.get_game_output_area().append(
                        f"\n{minymph1.get_name()} has {minymph1.get_hp()} HP\n"
                    )

                elif move_chosen.get_side_effect() == "accuracyNerf":
                    self.gui.get_game_output_area().append(
                        f"\n{minymph2.get_name()}'s accuracy fell!\n"
                    )

                else:
                    minymph2.set_hp(
                        max(
                            0.0,
                            minymph2.get_hp() - move_chosen.get_damage() - self.extra_damage_player
                        )
                    )
                    if minymph2.get_hp() <= 0:
                        self.gui.get_game_output_area().append(
                            f"\n{minymph2.get_name()} now has 0 HP\n"
                        )
                    else:
                        self.gui.get_game_output_area().append(
                            f"\n{minymph2.get_name()} now has {minymph2.get_hp()} HP\n"
                        )

            else:
                self.gui.get_game_output_area().append(
                    f"\n{minymph1.get_name()} uses {move_chosen.get_name()}!\n"
                )
                self.gui.get_game_output_area().append("\nNo PP left!\n")
                self.move_selected = False

        else:
            self.gui.get_game_output_area().append(
                f"\n{minymph1.get_name()} uses {move_chosen.get_name()}!\n"
            )
            self.gui.get_game_output_area().append("\nBut it failed!\n")

        self.check_for_ko()
        print("on teste si ko")
        print("on teste si combat fini, soit si tt minymph ko")
        self.check_for_end_battle()

        if self.all_players_minymph_dead:
            self.desactivate_buttons()
            self.gui.get_game_output_area().append("\nAll your Minymphs are KO. You have lost\n")
            if self.battle_timer:
                self.battle_timer.cancel()

    def desactivate_buttons(self) -> None:
        """
        Disables the GUI buttons and closes the window after a delay.
        """
        def disable_and_close() -> None:
            self.gui.disable_buttons()
            self.sleep(2000)
            self.gui.close_window()

        # Utilisation de threading pour simuler SwingUtilities.invokeLater
        threading.Thread(target=disable_and_close).start()

    def use_bag_item(self, object_chosen: 'Objects') -> bool:
        """
        Uses the chosen item from the bag on the player's minymph if applicable.

        Args:
            object_chosen: The item selected by the player from the bag.

        Returns:
            True if the item was used successfully, False otherwise.
        """
        if (
            self.my_current_minymph.get_hp() < self.my_current_minymph.get_base_hp()
            and object_chosen.get_type() == "Health"
        ):
            new_hp = self.my_current_minymph.get_hp() + object_chosen.get_hp()
            self.my_current_minymph.set_hp(
                min(new_hp, self.my_current_minymph.get_base_hp())
            )
            self.gui.get_game_output_area().append(
                f"{self.my_current_minymph.get_name()} healed! New HP: {self.my_current_minymph.get_hp()}\n"
            )
            self.bag.remove_from_bag(object_chosen.get_name())
            self.compteur_tour += 1
            self.gui.get_game_output_area().append(f"\n--Tour-- {self.compteur_tour}\n")
            return True

        elif object_chosen.get_name() == "Revive" and self.my_current_minymph.get_status() == "KO":
            self.my_current_minymph.set_status("Alive")
            self.my_current_minymph.set_hp(self.my_current_minymph.get_base_hp() * 0.33)
            self.gui.get_game_output_area().append(
                f"{self.my_current_minymph.get_name()} has been revived! HP: {self.my_current_minymph.get_hp()}\n"
            )
            self.bag.remove_from_bag("Revive")
            self.compteur_tour += 1
            self.gui.get_game_output_area().append(f"\n--Tour-- {self.compteur_tour}\n")
            return True

        elif object_chosen.get_type() == "Battle Items" and object_chosen.get_side_effect() != "null":
            self.apply_effect(object_chosen, object_chosen.get_side_effect())
            self.bag.remove_from_bag(object_chosen.get_name())
            return True

        elif object_chosen.get_type() == "Battle Items" and object_chosen.get_side_effect() == "null":
            self.apply_effect(object_chosen, object_chosen.get_side_effect())
            self.bag.remove_from_bag(object_chosen.get_name())
            self.gui.get_game_output_area().append(
                f"{self.minymph2.get_name()} has {self.minymph2.get_hp()} hp\n"
            )
            return True

        else:
            self.gui.get_game_output_area().append(
                f"{self.my_current_minymph.get_name()} is already at full health or alive!\n"
            )
            return False

    def apply_effect(self, object_chosen: 'Objects', effect: str) -> None:
        """
        Applies the side effect of an item or move.

        Args:
            object_chosen: The item or move causing the effect.
            effect: The effect to apply.
        """
        if effect == "defenseBuff":
            self.my_current_minymph.set_def(self.my_current_minymph.get_def() + 5)
            self.gui.get_game_output_area().append(
                f"\n{self.my_current_minymph.get_name()}'s defense slightly went up\n"
            )

        elif effect == "speedBuff":
            self.my_current_minymph.set_speed(self.my_current_minymph.get_speed() + 5)
            self.gui.get_game_output_area().append(
                f"\n{self.my_current_minymph.get_name()}'s speed slightly went up\n"
            )

        elif effect == "null":
            self.minymph2.set_hp(self.minymph2.get_hp() - object_chosen.get_hp())
            self.gui.get_game_output_area().append(
                f"\n{self.minymph2.get_name()}{object_chosen.get_message()}"
            )

    def check_for_ko(self) -> None:
        """
        Checks the health of both players' minymphs to determine if either one is KO'd.
        """
        if self.minymph2.get_hp() <= 0.0:
            self.minymph2.set_hp(0.0)
            self.minymph2.set_status("KO")
            self.gui.get_game_output_area().append(
                f"{self.minymph2.get_name()} is now KO!\nYou won! Congrats!\n"
            )
            self.desactivate_buttons()
            if self.battle_timer:
                self.battle_timer.cancel()
            return

        if self.my_current_minymph.get_hp() <= 0.0:
            self.gui.disable_attack()
            self.gui.disable_bag()
            self.my_current_minymph.set_hp(0.0)
            self.my_current_minymph.set_status("KO")
            self.gui.get_game_output_area().append(
                f"{self.my_current_minymph.get_name()} is now KO!\n"
            )
            self.check_for_end_battle()
            if not self.all_players_minymph_dead:
                self.gui.get_game_output_area().append("\nPlease choose another minymph\n")
            if self.all_players_minymph_dead:
                self.gui.get_game_output_area().append("\nAll your minymphs are KO! You lost!\n")
                self.desactivate_buttons()
            self.players_turn = True

    def check_for_end_battle(self) -> None:
        """
        Checks if all of one player's minymphs are KO to end the battle.
        """
        compteur1 = 0
        compteur2 = 0

        for minymph in self.my_current_minymph.get_my_minymphs():
            if minymph.get_status() == "KO":
                compteur1 += 1

        for minymph in self.minymph2.get_opponent_minymphs():
            if minymph.get_status() == "KO":
                compteur2 += 1

        if compteur1 == len(self.my_current_minymph.get_my_minymphs()):
            self.all_players_minymph_dead = True

        elif compteur2 == len(self.minymph2.get_opponent_minymphs()):
            self.all_ai_minymph_dead = True