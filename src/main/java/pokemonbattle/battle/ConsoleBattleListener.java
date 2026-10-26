package pokemonbattle.battle;

import pokemonbattle.move.Move;
import pokemonbattle.pokemon.Pokemon;

public class ConsoleBattleListener implements BattleListener {

    @Override
    public void onBattleStarted(Pokemon first, Pokemon second) {
        System.out.println("Battle start: " + first + " vs " + second);
    }

    @Override
    public void onTurnStarted(Pokemon attacker, Pokemon defender) {
        System.out.println();
        System.out.println(attacker.getName() + "'s turn.");
    }

    @Override
    public void onMoveUsed(Pokemon attacker, Move move, Pokemon defender) {
        System.out.println(attacker.getName() + " used " + move.name() + "!");
    }

    @Override
    public void onDamageDealt(Pokemon defender, int damage) {
        System.out.println(defender.getName() + " took " + damage + " damage ("
                + defender.getCurrentHp() + "/" + defender.getMaxHp() + " HP left).");
    }

    @Override
    public void onPokemonFainted(Pokemon pokemon) {
        System.out.println(pokemon.getName() + " fainted!");
    }

    @Override
    public void onBattleEnded(Pokemon winner) {
        System.out.println(winner.getName() + " wins!");
    }
}