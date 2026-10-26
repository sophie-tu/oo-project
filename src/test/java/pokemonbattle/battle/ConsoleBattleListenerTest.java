package pokemonbattle.battle;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pokemonbattle.data.FixedRandomSource;
import pokemonbattle.data.ResourceCsvSource;
import pokemonbattle.move.MoveFactory;
import pokemonbattle.pokemon.Pokemon;
import pokemonbattle.pokemon.PokemonFactory;
import pokemonbattle.type.TypeChart;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConsoleBattleListenerTest {
    private final TypeChart types = new TypeChart(new ResourceCsvSource("/data/type_chart.csv"));
    private final MoveFactory moveFactory =
            new MoveFactory(new ResourceCsvSource("/data/moves.csv"), types, new FixedRandomSource(0));
    private final PokemonFactory pokemonFactory =
            new PokemonFactory(new ResourceCsvSource("/data/pokemon.csv"), types, new FixedRandomSource(0));

    private final Pokemon blastoise =
            pokemonFactory.create("Blastoise", List.of(
                    moveFactory.create("Surf"),
                    moveFactory.create("Ice Shard"),
                    moveFactory.create("Waterfall"),
                    moveFactory.create("Energy Ball")));
    private final Pokemon typhlosion =
            pokemonFactory.create("Typhlosion", List.of(moveFactory.create("Ember")));

    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream captured = new ByteArrayOutputStream();
    private final BattleListener listener = new ConsoleBattleListener();

    @BeforeEach
    void redirectSystemOut() {
        System.setOut(new PrintStream(captured));
    }

    @AfterEach
    void restoreSystemOut() {
        System.setOut(originalOut);
    }

    @Test
    void announcesWhichMoveWasUsed() {
        listener.onMoveUsed(blastoise, moveFactory.create("Waterfall"), typhlosion);
        assertTrue(captured.toString().contains("Blastoise used Waterfall!"));
    }

    @Test
    void announcesDamageAndRemainingHp() {
        typhlosion.takeDamage(10);
        listener.onDamageDealt(typhlosion, 10);
        assertTrue(captured.toString().contains("Typhlosion took 10 damage (68/78 HP left)."));
    }

    @Test
    void announcesFaintingAndWinner() {
        listener.onPokemonFainted(typhlosion);
        listener.onBattleEnded(blastoise);
        String text = captured.toString();
        assertTrue(text.contains("Typhlosion fainted!"));
        assertTrue(text.contains("Blastoise wins!"));
    }
}