package pokemonbattle.type;

import java.util.Locale;
import java.util.Map;

class ChartType implements Type {
    static final double NEUTRAL = 1.0;

    private final String name;
    private final Map<String, Double> multipliers;

    ChartType(String name, Map<String, Double> multipliers) {
        this.name = name;
        this.multipliers = Map.copyOf(multipliers);
    }

    static String key(String typeName) {
        return typeName.trim().toLowerCase(Locale.ROOT);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double effectivenessAgainst(Type defender) {
        return multipliers.getOrDefault(key(defender.getName()), NEUTRAL);
    }

    @Override
    public String toString() {
        return name;
    }
}
