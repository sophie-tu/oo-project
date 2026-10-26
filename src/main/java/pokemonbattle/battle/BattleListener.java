package pokemonbattle.battle;

import pokemonbattle.move.Move;
import pokemonbattle.pokemon.Pokemon;

public interface BattleListener {
    void onBattleStarted(Pokemon first, Pokemon second);
    void onTurnStarted(Pokemon attacker, Pokemon defender);
    void onMoveUsed(Pokemon attacker, Move move, Pokemon defender);
    void onDamageDealt(Pokemon defender, int damage);
    void onPokemonFainted(Pokemon pokemon);
    void onBattleEnded(Pokemon winner);
}