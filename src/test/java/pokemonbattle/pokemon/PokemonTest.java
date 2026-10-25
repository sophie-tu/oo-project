package pokemonbattle.pokemon;

import org.junit.jupiter.api.Test;
import pokemonbattle.data.FixedRandomSource;
import pokemonbattle.data.ResourceCsvSource;
import pokemonbattle.move.MoveFactory;
import pokemonbattle.type.TypeChart;

import static org.junit.jupiter.api.Assertions.*;

class PokemonTest {
    private final TypeChart types = new TypeChart(new ResourceCsvSource("/data/type_chart.csv"));
    private final MoveFactory moves = new MoveFactory(
            new ResourceCsvSource("/data/moves.csv"), types, new FixedRandomSource(0));

    private Pokemon blastoise() {
        return Pokemon.builder()
                .name("Blastoise")
                .type(types.get("Water"))
                .hp(79)
                .speed(78)
                .addMove(moves.create("Surf"))
                .build();
    }

    @Test
    void takeDamageLowersHp() {
        Pokemon pokemon = blastoise();
        pokemon.takeDamage(30);
        assertEquals(49, pokemon.getCurrentHp());
    }

    @Test
    void takeDamageStopsAtZero() {
        Pokemon pokemon = blastoise();
        pokemon.takeDamage(500);
        assertEquals(0, pokemon.getCurrentHp());
    }

    @Test
    void takeDamageReturnsOnlyDamageActuallyDealt() {
        Pokemon pokemon = blastoise();
        assertEquals(79, pokemon.takeDamage(500));
    }

    @Test
    void faintsAtZeroHp() {
        Pokemon pokemon = blastoise();
        pokemon.takeDamage(79);
        assertTrue(pokemon.isFainted());
    }

    @Test
    void notFaintedWhileHpRemains() {
        Pokemon pokemon = blastoise();
        pokemon.takeDamage(78);
        assertFalse(pokemon.isFainted());
    }

    @Test
    void negativeDamageThrows() {
        Pokemon pokemon = blastoise();
        assertThrows(IllegalArgumentException.class, () -> pokemon.takeDamage(-5));
    }
}