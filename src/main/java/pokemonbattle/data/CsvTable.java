package pokemonbattle.data;

import java.util.List;

public record CsvTable(List<String> header, List<List<String>> rows) {
    public CsvTable {
        header = List.copyOf(header);
        rows = List.copyOf(rows);
    }
}