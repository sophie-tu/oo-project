package pokemonbattle.type;

public interface Type {
    String getName();
    double effectivenessAgainst(Type defender);
}
