package pokemonbattle.data;

import java.util.Random;

public class JavaRandomSource implements RandomSource {
    private final Random random;

    public JavaRandomSource() {
        this(new Random());
    }

    public JavaRandomSource(Random random) {
        this.random = random;
    }

    @Override
    public int nextInt(int bound) {
        return random.nextInt(bound);
    }
}