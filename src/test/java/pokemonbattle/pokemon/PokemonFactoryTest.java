package pokemonbattle.pokemon;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pokemonbattle.data.FixedRandomSource;
import pokemonbattle.data.ResourceCsvSource;
import pokemonbattle.move.Move;
import pokemonbattle.move.MoveFactory;
import pokemonbattle.type.TypeChart;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PokemonFactoryTest {
    private final TypeChart types = new TypeChart(new ResourceCsvSource("/data/type_chart.csv"));
    private final MoveFactory moveFactory = new MoveFactory(new ResourceCsvSource("/data/moves.csv"), types, new FixedRandomSource(0));
    private final List<Move> waterMoves = List.of(moveFactory.create("Surf"), moveFactory.create("Hydro Pump"));

    private PokemonFactory factory(int randomValue) {
        return new PokemonFactory(new ResourceCsvSource("/data/pokemon.csv"), types, new FixedRandomSource(randomValue));
    }

    @Test
    void loadsAllTenPokemon() {
        assertEquals(10, factory(0).available().size());
    }

    @Test
    void createsBlastoiseAsWaterType() {
        assertSame(types.get("Water"), factory(0).create("Blastoise", waterMoves).getType());
    }

    @Test
    void createsBlastoiseWithItsHp() {
        assertEquals(79, factory(0).create("Blastoise", waterMoves).getMaxHp());
    }

    @Test
    void createsBlastoiseWithItsSpeed() {
        assertEquals(78, factory(0).create("Blastoise", waterMoves).getSpeed());
    }

    @Test
    void createdPokemonKnowsChosenMoves() {
        assertEquals(waterMoves, factory(0).create("Blastoise", waterMoves).getMoves());
    }

    @Test
    void lookupIgnoresCapitalization() {
        assertEquals("Blastoise", factory(0).create("blastoise", waterMoves).getName());
    }

    @Test
    void eachCreateReturnsANewPokemon() {
        PokemonFactory factory = factory(0);
        assertNotSame(factory.create("Blastoise", waterMoves), factory.create("Blastoise", waterMoves));
    }

    @Test
    void unknownPokemonThrows() {
        assertThrows(IllegalArgumentException.class, () -> factory(0).create("Pikachu", waterMoves));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8})
    void randomOpponentIsNeverThePlayersChoice(int randomValue) {
        PokemonSpec opponent = factory(randomValue).randomOpponent("Blastoise");
        assertNotEquals("Blastoise", opponent.name());
    }

    @Test
    void randomOpponentWithUnknownPlayerChoiceThrows() {
        assertThrows(IllegalArgumentException.class, () -> factory(0).randomOpponent("Pikachu"));
    }
}
