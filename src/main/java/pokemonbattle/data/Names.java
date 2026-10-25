package pokemonbattle.data;

import java.util.Locale;

public class Names {
    private Names() {}

    public static String key(String name) {
        return name.trim().toLowerCase(Locale.ROOT);
    }
}
