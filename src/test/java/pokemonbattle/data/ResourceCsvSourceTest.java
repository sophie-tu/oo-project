package pokemonbattle.data;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ResourceCsvSourceTest {

    @Test
    void readsHeaderFromPokemonCsv() {
        CsvTable table = new ResourceCsvSource("/data/pokemon.csv").read();
        assertEquals(List.of("name", "hp", "speed", "type"), table.header());
    }

    @Test
    void readsTenPokemonRows() {
        CsvTable table = new ResourceCsvSource("/data/pokemon.csv").read();
        assertEquals(10, table.rows().size());
    }

    @Test
    void missingFileThrows() {
        CsvSource source = new ResourceCsvSource("/data/missing.csv");
        assertThrows(IllegalStateException.class, source::read);
    }
}