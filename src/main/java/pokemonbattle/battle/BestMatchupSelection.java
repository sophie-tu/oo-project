package pokemonbattle.battle;

import pokemonbattle.move.Move;
import pokemonbattle.pokemon.Pokemon;

import java.util.Comparator;

public class BestMatchupSelection implements MoveStrategy {

    @Override
    public Move selectMove(Pokemon attacker, Pokemon defender) {
        return attacker.getMoves().stream()
                .max(Comparator.comparingInt(move -> move.damageAgainst(defender.getType())))
                .orElseThrow();
    }
}