package pokemonbattle.move;

import org.junit.jupiter.api.Test;
import pokemonbattle.data.ResourceCsvSource;
import pokemonbattle.data.FixedRandomSource;
import pokemonbattle.type.TypeChart;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MoveFactoryTest {
    private final TypeChart types = new TypeChart(new ResourceCsvSource("/data/type_chart.csv"));
    private final MoveFactory moves = new MoveFactory(new ResourceCsvSource("/data/moves.csv"), types, new FixedRandomSource(0));

    @Test
    void loadAllTwentyMoves() {
        assertEquals(20, moves.allMoves().size());
    }

    @Test
    void flameThrowerIsFireType() {
        assertSame(types.get("Fire"), moves.create("Flamethrower").type());
    }

    @Test
    void flameThrowerHasBasePowerNinety() {
        assertEquals(90, moves.create("Flamethrower").basePower());
    }

    @Test
    void lookupIgnoresCapitalization() {
        assertSame(moves.create("Ice Beam"), moves.create("ice beam"));
    }

    @Test
    void unknownMoveThrows() {
        assertThrows(IllegalArgumentException.class, () -> moves.create("Hyper Beam"));
    }

    @Test
    void randomMovesReturnsRequestedCount() {
        assertEquals(4, moves.randomMoves(4).size());
    }

    @Test
    void randomMovesHasNoDuplicates() {
        List<Move> picked = moves.randomMoves(4);
        assertEquals(4, new HashSet<>(picked).size());
    }

    @Test
    void askingForMoreMovesThanExistThrows() {
        assertThrows(IllegalArgumentException.class, () -> moves.randomMoves(21));
    }
}
