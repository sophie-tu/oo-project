# Pokemon Battle

**Team members:** Sophie Tu and Akhil Meduri
**Language:** Java 25 (Gradle)
**UI:** Console for now, Java Swing planned

## Project description
A simplified Pokemon battle game with 5 types (Fire, Water, Grass, Ice, Rock),
10 Pokemon, and 20 moves.

1. The user chooses one of 10 Pokemon.
2. The computer is randomly assigned one of the remaining 9.
3. The user picks 4 of the 20 moves. Moves deal more or less damage depending on
   the move's type and the opponent's type.
4. The user and computer take turns attacking until one Pokemon faints.

All game data (type chart, Pokemon stats, moves) is loaded from CSV files in
`src/main/resources/data/`, so the game can be changed without changing code.

## How to run the tests
```
./gradlew test
```

## Architecture

| Package | Responsibility |
|---|---|
| `pokemonbattle.data` | Reading CSV files (`CsvSource`, `ResourceCsvSource`), random numbers (`RandomSource`) |
| `pokemonbattle.type` | Types and the effectiveness chart (`Type`, `TypeChart`) |
| `pokemonbattle.move` | Moves and the move factory (`Move`, `MoveFactory`) |
| `pokemonbattle.pokemon` | Pokemon, its Builder, and the Pokemon factory (`Pokemon`, `PokemonFactory`) |
| `pokemonbattle.battle` | Battle, move selection strategies, battle listeners |

## Design patterns

### 1. Builder: `Pokemon.Builder`
**Problem:** A Pokemon has many fields (name, type, HP, speed, level, and a list
of up to 4 moves the player chooses). A constructor with that many parameters is
easy to call in the wrong order, and an invalid Pokemon could slip into the game.

**How we use it:** `Pokemon` can only be created through its static inner
`Builder`. The constructor is private. The Builder sets each field with method
chaining, and `level` is optional (it defaults to 50). `build()` checks every
rule before creating the Pokemon: it needs a name and a type, HP and speed must
be positive, the level must be 1–100, and it must have 1–4 moves with no
duplicates. If any rule is broken, `build()` throws an exception, so an invalid
Pokemon can never exist.

```java
Pokemon blastoise = Pokemon.builder()
        .name("Blastoise").type(water).hp(79).speed(78)
        .moves(chosenMoves)
        .build();
```

### 2. Factory: `PokemonFactory` and `MoveFactory`
**Problem:** The game needs to create Pokemon and moves by name ("Blastoise",
"Surf") without the rest of the code knowing about CSV files, type lookups, or
how a Pokemon is assembled. Hard-coding 10 Pokemon would mean `new` calls or a
switch statement scattered through the code.

**How we use it:** Both factories load their data from CSV into a map at
startup. Callers ask by name, for example `pokemonFactory.create("Blastoise",
moves)` or `moveFactory.create("Surf")`, and get a finished object back.
Adding a Pokemon or move means adding a CSV row, not changing code.
`PokemonFactory` uses `Pokemon.Builder` internally, acting as the Builder's
*director*. It returns a new Pokemon on every call, so battles never share HP.
The factories also handle the game's random choices:
`PokemonFactory.randomOpponent(playerChoice)` picks the computer's Pokemon from
the other 9, and `MoveFactory.randomMoves(4)` picks the computer's moves.

**Factory vs. Builder:** the Factory decides *which* object to create. The
Builder handles *how* to assemble a complex object step by step.

### 3. Strategy: `MoveSelectionStrategy`
**Problem:** :the computer needs a way to pick its move each turn, and Battle shouldn’t be tied to one way of choosing.
**How we use it**: MoveStrategy is an interface with one method, selectMove(attacker, defender). It has two implementations: RandomMoveSelection picks a random move from the attacker’s list, and BestMatchupMoveSelection picks the move that deals the most damage to the defender. Battle only knows the interface, and a strategy is passed in through its constructor.
**Why it fits**: adding a new way to choose (for example a human prompt) means adding a class, not editing Battle.
Damage rule: damage is basePower × effectiveness, rounded to the nearest whole number (Move.damageAgainst). Both the battle and the best-matchup strategy use the same method.

### 4. Observer: `BattleListener` 
**Problem**: the battle needs to report what happens without knowing where the output goes.
**How we use it**: Battle is the subject. BattleListener has four methods: move used, damage dealt, Pokémon fainted, and battle ended. ConsoleBattleListener prints each one. Battle calls every listener it was given and never prints anything itself.
**Why it fits**: a Swing listener can be added later without changing Battle.

## OO principles

### Coding to abstractions
- Every class that reads data depends on the `CsvSource` interface, never on
  `ResourceCsvSource` or a file path.
- Moves, Pokemon, and the type chart all hold the `Type` interface. Only
  `TypeChart` knows about the concrete `ChartType`, which is package-private so
  no other code can depend on it.
- Randomness goes through the `RandomSource` interface, never `java.util.Random`
  directly.

### Polymorphism
- `CsvSource` has different implementations that callers treat the same way:
  `ResourceCsvSource` reads a real file, and tests pass in a lambda with fake
  data. `TypeChart`, `MoveFactory`, and `PokemonFactory` just call
  `source.read()` and don't know which one they got.
- `RandomSource` works the same way: `JavaRandomSource` in the game and
  `FixedRandomSource` in tests.
- Type effectiveness has no if/else or switch. `ChartType.effectivenessAgainst()`
  is a single map lookup, and each type answers for itself.
- TODO (Akhil): `MoveSelectionStrategy` implementations

### Dependency injection
All dependencies are passed in through constructors (constructor injection).
No class creates its own collaborators:
```java
TypeChart types = new TypeChart(new ResourceCsvSource("/data/type_chart.csv"));
MoveFactory moves = new MoveFactory(new ResourceCsvSource("/data/moves.csv"), types, new JavaRandomSource());
PokemonFactory pokemon = new PokemonFactory(new ResourceCsvSource("/data/pokemon.csv"), types, new JavaRandomSource());
```
`Main` (or a test) acts as the assembler. This is what lets the tests inject
`FixedRandomSource` and fake CSV data to get predictable results.

## Tests
There are 45 test methods (54 test runs). Some highlights:

| Test | What it checks |
|---|---|
| `TypeChartTest.waterIsSuperEffectiveAgainstFire` | Water vs. Fire = 2.0 |
| `TypeChartTest.fireIsNotVeryEffectiveAgainstWater` | Fire vs. Water = 0.5 |
| `TypeChartTest.rockIsNeutralAgainstRock` | Rock vs. Rock = 1.0 |
| `TypeChartTest.rowWithMissingMultipliersThrows` | A broken CSV row is rejected (uses a fake `CsvSource`) |
| `PokemonBuilderTest.moreThanFourMovesThrows` | The Builder rejects a 5th move |
| `PokemonBuilderTest.duplicateMoveThrows` | The Builder rejects the same move twice |
| `PokemonBuilderTest.missingNameThrows` | The Builder rejects a Pokemon with no name |
| `PokemonTest.takeDamageStopsAtZero` | HP never goes below 0 |
| `PokemonTest.faintsAtZeroHp` | A Pokemon at 0 HP has fainted |
| `PokemonFactoryTest.createsBlastoiseAsWaterType` | The factory builds the right Pokemon from CSV |
| `PokemonFactoryTest.eachCreateReturnsANewPokemon` | Two battles never share one Pokemon |
| `PokemonFactoryTest.randomOpponentIsNeverThePlayersChoice` | Runs for every random value 0–8; the computer never gets the player's Pokemon |
| `MoveFactoryTest.randomMovesHasNoDuplicates` | The computer's 4 random moves are all different |

TODO (Akhil): battle, strategy, and listener tests


