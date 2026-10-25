package pokemonbattle.move;

import org.junit.jupiter.api.Test;
import pokemonbattle.data.ResourceCsvSource;
import pokemonbattle.type.Type;
import pokemonbattle.type.TypeChart;

import static org.junit.jupiter.api.Assertions.*;

public class MoveTest {
    private final TypeChart types = new TypeChart(new ResourceCsvSource("/data/type_chart.csv"));
    private final Type water = types.get("Water");

    @Test
    void movesWithZeroPowerThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Move("Splash", water, 0));
    }

    @Test
    void moveWithBlankNameThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Move(" ", water, 40));
    }
}
