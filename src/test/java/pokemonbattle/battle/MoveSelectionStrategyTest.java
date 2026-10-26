package pokemonbattle.battle;

import org.junit.jupiter.api.Test;
import pokemonbattle.data.FixedRandomSource;
import pokemonbattle.data.ResourceCsvSource;
import pokemonbattle.move.Move;
import pokemonbattle.move.MoveFactory;
import pokemonbattle.pokemon.Pokemon;
import pokemonbattle.pokemon.PokemonFactory;
import pokemonbattle.type.TypeChart;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MoveSelectionStrategyTest {
    private final TypeChart types = new TypeChart(new ResourceCsvSource("/data/type_chart.csv"));
    private final MoveFactory moveFactory =
            new MoveFactory(new ResourceCsvSource("/data/moves.csv"), types, new FixedRandomSource(0));
    private final PokemonFactory pokemonFactory =
            new PokemonFactory(new ResourceCsvSource("/data/pokemon.csv"), types, new FixedRandomSource(0));

    // Blastoise (Water) knows: Razor Leaf, Ember, Surf, Ice Shard
    // Surf is deliberately NOT first, so a strategy can't pass by just picking index 0.
    private final Pokemon attacker = pokemonFactory.create("Blastoise", List.of(
            moveFactory.create("Razor Leaf"),
            moveFactory.create("Ember"),
            moveFactory.create("Surf"),
            moveFactory.create("Ice Shard")));

    // Typhlosion is Fire, so Water moves are super effective against it.
    private final Pokemon firePokemon =
            pokemonFactory.create("Typhlosion", List.of(moveFactory.create("Ember")));

    private MoveStrategy random(int value) {
        return new RandomMoveSelection(new FixedRandomSource(value));
    }

    @Test
    void randomSelectionPicksTheFirstMoveWhenRandomReturnsZero() {
        Move chosen = random(0).selectMove(attacker, firePokemon);
        assertEquals("Razor Leaf", chosen.name());
    }

    @Test
    void randomSelectionPicksTheMoveAtTheInjectedIndex() {
        Move chosen = random(2).selectMove(attacker, firePokemon);
        assertEquals("Surf", chosen.name());
    }

    @Test
    void randomSelectionReturnsTheAttackerMoves() {
        for (int value = 0; value < 4; value++) {
            Move chosen = random(value).selectMove(attacker, firePokemon);
            assertTrue(attacker.getMoves().contains(chosen));
        }
    }

    @Test
    void bestMatchupPicksTheBestMove() {
        MoveStrategy strategy = new BestMatchupSelection();
        Move chosen = strategy.selectMove(attacker, firePokemon);
        assertEquals("Surf", chosen.name());
    }
}