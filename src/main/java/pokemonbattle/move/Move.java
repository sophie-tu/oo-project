package pokemonbattle.move;

import pokemonbattle.type.Type;

public record Move(String name, Type type, int basePower) {
    public Move {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("A move needs a name");
        }
        if (type == null) {
            throw new IllegalArgumentException("Move " + name + " needs a type");
        }
        if (basePower <= 0) {
            throw new IllegalArgumentException("Move " + name + " must have positive base power");
        }
    }

    @Override
    public String toString() {
        return name + " (" + type.getName() + ", " + basePower + ")";
    }

    public int damageAgainst(Type defender){
        return (int) Math.round(basePower * type.effectivenessAgainst(defender));
    }
}
