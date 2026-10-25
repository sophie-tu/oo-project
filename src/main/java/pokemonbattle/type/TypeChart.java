package pokemonbattle.type;

import pokemonbattle.data.CsvSource;
import pokemonbattle.data.CsvTable;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.LinkedHashMap;

public class TypeChart {
    private static final int TYPE_NAME_COLUMN = 0;
    private static final int FIRST_MULTIPLIER_COLUMN = 1;

    private final Map<String, Type> typesByName = new LinkedHashMap<>();

    public TypeChart(CsvSource source) {
        CsvTable table = source.read();
        List<String> defenderNames = table.header().subList(FIRST_MULTIPLIER_COLUMN, table.header().size());

        for (List<String> row : table.rows()) {
            addType(row, defenderNames);
        }
    }

    public Type get(String name) {
        Type type = typesByName.get(ChartType.key(name));
        if (type == null) {
            throw new IllegalArgumentException("Unknown type: " + name);
        }
        return type;
    }

    public List<Type> allTypes() {
        return List.copyOf(typesByName.values());
    }

    private void addType(List<String> row, List<String> defenderNames) {
        requireComplete(row, defenderNames.size());
        String name = row.get(TYPE_NAME_COLUMN);
        typesByName.put(ChartType.key(name), new ChartType(name, readMultipliers(row, defenderNames)));
    }

    private static void requireComplete(List<String> row, int expectedMultipliers) {
        int actual = row.size() - FIRST_MULTIPLIER_COLUMN;
        if (actual != expectedMultipliers) {
            throw new IllegalArgumentException("Type row " + row + " has " + actual + " multipliers, expected " + expectedMultipliers);
        }
    }

    private static Map<String, Double> readMultipliers(List<String> row, List<String> defenderNames) {
        Map<String, Double> multipliers = new HashMap<>();
        for (int i = 0; i < defenderNames.size(); i++) {
            double value = Double.parseDouble(row.get(FIRST_MULTIPLIER_COLUMN + i));
            multipliers.put(ChartType.key(defenderNames.get(i)), value);
        }
        return multipliers;
    }
}
