package pokemonbattle.data;

public class FixedRandomSource implements RandomSource{
    private final int value;

    public FixedRandomSource(int value) {
        this.value = value;
    }

    @Override
    public int nextInt(int bound) {
        return value % bound;
    }
}