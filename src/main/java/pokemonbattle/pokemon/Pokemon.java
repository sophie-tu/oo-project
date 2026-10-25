package pokemonbattle.pokemon;

import pokemonbattle.move.Move;
import pokemonbattle.type.Type;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Pokemon {
    public static final int MAX_MOVES = 4;
    public static final int DEFAULT_LEVEL = 50;
    public static final int MAX_LEVEL = 100;

    private final String name;
    private final Type type;
    private final int maxHp;
    private final int speed;
    private final int level;
    private final List<Move> moves;
    private int currentHp;

    private Pokemon(Builder builder) {
        this.name = builder.name;
        this.type = builder.type;
        this.maxHp = builder.maxHp;
        this.speed = builder.speed;
        this.level = builder.level;
        this.moves = List.copyOf(builder.moves);
        this.currentHp = builder.maxHp;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public int getSpeed() {
        return speed;
    }

    public int getLevel() {
        return level;
    }

    public List<Move> getMoves() {
        return List.copyOf(moves);
    }

    public int takeDamage(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Damage amount cannot be negative: " + amount);
        }
        int dealt = Math.min(amount, currentHp);
        currentHp -= dealt;
        return dealt;
    }

    public boolean isFainted() {
        return currentHp == 0;
    }

    @Override
    public String toString() {
        return name + " (" + type.getName() + ")" + currentHp + "/" + maxHp + " HP";
    }

    public static final class Builder {
        private String name;
        private Type type;
        private int maxHp;
        private int speed;
        private int level = DEFAULT_LEVEL;
        private final List<Move> moves = new ArrayList<>();

        private Builder() {}

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder type(Type type) {
            this.type = type;
            return this;
        }

        public Builder hp(int maxHp) {
            this.maxHp = maxHp;
            return this;
        }

        public Builder speed(int speed) {
            this.speed = speed;
            return this;
        }

        public Builder level(int level) {
            this.level = level;
            return this;
        }

        public Builder addMove(Move move) {
            this.moves.add(move);
            return this;
        }

        public Builder moves(List<Move> moves) {
            this.moves.addAll(moves);
            return this;
        }

        public Pokemon build() {
            requireName();
            requireType();
            requirePositive(maxHp, "HP");
            requirePositive(speed, "speed");
            requireValidLevel();
            requireValidMoves();
            return new Pokemon(this);
        }

        private void requireName() {
            if (name == null || name.isBlank()) {
                throw new IllegalStateException("A Pokemon needs a name");
            }
        }

        private void requireType() {
            if (type == null) {
                throw new IllegalStateException(name + " needs a type");
            }
        }

        private void requirePositive(int value, String stat) {
            if (value <= 0) {
                throw new IllegalStateException(name + "'s " + stat + " must be positive, was " + value);
            }
        }

        private void requireValidLevel() {
            if (level < 1 || level > MAX_LEVEL) {
                throw new IllegalStateException(name + "'s level must be 1 to " + MAX_LEVEL + ", was " + level);
            }
        }

        private void requireValidMoves() {
            if (moves.isEmpty() || moves.size() > MAX_MOVES) {
                throw new IllegalStateException(name + " needs 1 to " + MAX_MOVES + " moves, had " + moves.size());
            }
            if (new HashSet<>(moves).size() != moves.size()) {
                throw new IllegalStateException(name + " cannot know the same move twice");
            }
        }
    }
}