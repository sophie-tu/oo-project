package pokemonbattle.battle;

import pokemonbattle.data.RandomSource;
import pokemonbattle.move.Move;
import pokemonbattle.pokemon.Pokemon;

import java.util.List;

public class RandomMoveSelection implements MoveStrategy {
    private final RandomSource random;

    public RandomMoveSelection(RandomSource random) {
        this.random = random;
    }

    @Override
    public Move selectMove(Pokemon attacker, Pokemon defender) {
        List<Move> moves = attacker.getMoves();
        return moves.get(random.nextInt(moves.size()));
    }
}