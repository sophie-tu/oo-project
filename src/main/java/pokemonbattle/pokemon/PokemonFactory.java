package pokemonbattle.pokemon;

import pokemonbattle.data.CsvSource;
import pokemonbattle.data.Names;
import pokemonbattle.data.RandomSource;
import pokemonbattle.move.Move;
import pokemonbattle.type.TypeChart;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PokemonFactory {
    private static final int NAME_COLUMN = 0;
    private static final int HP_COLUMN = 1;
    private static final int SPEED_COLUMN = 2;
    private static final int TYPE_COLUMN = 3;

    private final Map<String, PokemonSpec> specsByName = new LinkedHashMap<>();
    private final RandomSource random;

    public PokemonFactory(CsvSource source, TypeChart types, RandomSource random) {
        this.random = random;
        for (List<String> row : source.read().rows()) {
            PokemonSpec spec = toSpec(row, types);
            specsByName.put(Names.key(spec.name()), spec);
        }
    }

    public List<PokemonSpec> available() {
        return List.copyOf(specsByName.values());
    }

    public Pokemon create(String name, List<Move> moves) {
        PokemonSpec spec = find(name);
        return Pokemon.builder()
                .name(spec.name())
                .type(spec.type())
                .hp(spec.hp())
                .speed(spec.speed())
                .moves(moves)
                .build();
    }

    public PokemonSpec randomOpponent(String playerChoice) {
        List<PokemonSpec> others = new ArrayList<>(specsByName.values());
        others.remove(find(playerChoice));
        return others.get(random.nextInt(others.size()));
    }

    private PokemonSpec find(String name) {
        PokemonSpec spec = specsByName.get(Names.key(name));
        if (spec == null) {
            throw new IllegalArgumentException("Unknown Pokémon: " + name);
        }
        return spec;
    }

    private static PokemonSpec toSpec(List<String> row, TypeChart types) {
        return new PokemonSpec(
                row.get(NAME_COLUMN),
                types.get(row.get(TYPE_COLUMN)),
                Integer.parseInt(row.get(HP_COLUMN)),
                Integer.parseInt(row.get(SPEED_COLUMN))
        );
    }
}
