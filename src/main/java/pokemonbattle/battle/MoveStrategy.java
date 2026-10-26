package pokemonbattle.battle;
import pokemonbattle.move.Move;
import pokemonbattle.pokemon.Pokemon;
public interface MoveStrategy {
    Move selectMove(Pokemon attacker,Pokemon defender);
}
