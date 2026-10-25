package pokemonbattle.pokemon;

import org.junit.jupiter.api.Test;
import pokemonbattle.data.FixedRandomSource;
import pokemonbattle.data.ResourceCsvSource;
import pokemonbattle.move.MoveFactory;
import pokemonbattle.type.TypeChart;

import static org.junit.jupiter.api.Assertions.*;

class PokemonBuilderTest {
    private final TypeChart types = new TypeChart(new ResourceCsvSource("/data/type_chart.csv"));
    private final MoveFactory moves = new MoveFactory(new ResourceCsvSource("/data/moves.csv"), types, new FixedRandomSource(0));

    private Pokemon.Builder blastoise() {
        return Pokemon.builder()
                .name("Blastoise")
                .type(types.get("Water"))
                .hp(79)
                .speed(78)
                .addMove(moves.create("Surf"));
    }

    @Test
    void buildsPokemonWithGivenName() {
        assertEquals("Blastoise", blastoise().build().getName());
    }

    @Test
    void levelDefaultsToFifty() {
        assertEquals(Pokemon.DEFAULT_LEVEL, blastoise().build().getLevel());
    }

    @Test
    void startsAtFullHp() {
        assertEquals(79, blastoise().build().getCurrentHp());
    }

    @Test
    void missingNameThrows() {
        Pokemon.Builder builder = blastoise().name(null);
        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    void zeroHpThrows() {
        Pokemon.Builder builder = blastoise().hp(0);
        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    void noMovesThrows() {
        Pokemon.Builder builder = Pokemon.builder().name("Blastoise").type(types.get("Water")).hp(79).speed(78);
        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    void moreThanFourMovesThrows() {
        Pokemon.Builder builder = blastoise()
                .addMove(moves.create("Ice Beam"))
                .addMove(moves.create("Hydro Pump"))
                .addMove(moves.create("Waterfall"))
                .addMove(moves.create("Water Gun"));
        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    void duplicateMovesThrows() {
        Pokemon.Builder builder = blastoise().addMove(moves.create("Surf"));
        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    void levelAboveMaxThrows() {
        Pokemon.Builder builder = blastoise().level(Pokemon.MAX_LEVEL + 1);
        assertThrows(IllegalStateException.class, builder::build);
    }
}
