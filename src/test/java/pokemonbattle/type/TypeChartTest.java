package pokemonbattle.type;

import org.junit.jupiter.api.Test;
import pokemonbattle.data.CsvSource;
import pokemonbattle.data.CsvTable;
import pokemonbattle.data.ResourceCsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class TypeChartTest {
    private final TypeChart chart = new TypeChart(new ResourceCsvSource("/data/type_chart.csv"));

    @Test
    void waterIsSuperEffectiveAgainstFire() {
        assertEquals(2.0, chart.get("Water").effectivenessAgainst(chart.get("Fire")));
    }

    @Test
    void fireIsNotVeryEffectiveAgainstWater() {
        assertEquals(0.5, chart.get("Fire").effectivenessAgainst(chart.get("Water")));
    }

    @Test
    void rockIsNeutralAgainstRock() {
        assertEquals(1.0, chart.get("Rock").effectivenessAgainst(chart.get("Rock")));
    }

    @Test
    void loadsAllFiveTypes() {
        assertEquals(5, chart.allTypes().size());
    }

    @Test
    void lookupIgnoresCapitalization() {
        assertSame(chart.get("Ice"), chart.get("ice"));
    }

    @Test
    void unknownTypeThrows() {
        assertThrows(IllegalArgumentException.class, () -> chart.get("Dragon"));
    }

    @Test
    void rowsWithMissingMultipliersThrow() {
        CsvSource broken = () -> new CsvTable(List.of("attack", "Fire", "Water"), List.of(List.of("Fire", "0.5")));
        assertThrows(IllegalArgumentException.class, () -> new TypeChart(broken));

    }
}
