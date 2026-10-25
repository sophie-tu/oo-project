package pokemonbattle.move;

import pokemonbattle.type.TypeChart;
import pokemonbattle.data.CsvSource;
import pokemonbattle.data.RandomSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MoveFactory {
    private static final int NAME_COLUMN = 0;
    private static final int TYPE_COLUMN = 1;
    private static final int POWER_COLUMN = 2;

    private final Map<String, Move>  movesByName = new LinkedHashMap<>();
    private final RandomSource random;

    public MoveFactory(CsvSource source, TypeChart types, RandomSource random) {
        this.random = random;
        for (List<String> row : source.read().rows()) {
            Move move = toMove(row, types);
            movesByName.put(key(move.name()), move);
        }
    }

    public Move create(String name) {
        Move move = movesByName.get(key(name));
        if (move == null) {
            throw new IllegalArgumentException("Unknown move: " + name);
        }
        return move;
    }

    public List<Move> allMoves() {
        return List.copyOf(movesByName.values());
    }

    public List<Move> randomMoves(int count) {
        List<Move> moves = new ArrayList<>(movesByName.values());
        if (count > moves.size()) {
            throw new IllegalArgumentException("Only " + moves.size() + " moves available, asked for " + count);
        }
        shuffle(moves);
        return List.copyOf(moves.subList(0, count));
    }

    private void shuffle(List<Move> moves) {
        for (int i = moves.size() - 1; i > 0; i--) {
            Collections.swap(moves, i, random.nextInt(i + 1));
        }
    }

    private static Move toMove(List<String> row, TypeChart types) {
        return new Move(
                row.get(NAME_COLUMN),
                types.get(row.get(TYPE_COLUMN)),
                Integer.parseInt(row.get(POWER_COLUMN))
        );
    }

    private static String key(String name) {
        return name.trim().toLowerCase(Locale.ROOT);
    }
}
