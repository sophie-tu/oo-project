package pokemonbattle.pokemon;

import pokemonbattle.type.Type;

public record PokemonSpec(String name, Type type, int hp, int speed) {

    @Override
    public String toString() {
        return name + " (" + type.getName() + ", HP " + hp + ", Speed " + speed + ")";
    }
}
