package pokemonbattle.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class ResourceCsvSource implements CsvSource {
    private static final String DELIMITER = ",";
    private final String resourcePath;

    public ResourceCsvSource(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    @Override
    public CsvTable read() {
        List<List<String>> lines = readNonBlankLines().stream().map(ResourceCsvSource::split).toList();
        if (lines.isEmpty()) {
            throw new IllegalStateException("CSV file is empty: " + resourcePath);
        }
        return new CsvTable(lines.get(0), lines.subList(1, lines.size()));
    }

    private List<String> readNonBlankLines() {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(open(), StandardCharsets.UTF_8))) {
            return reader.lines().filter(line -> !line.isBlank()).toList();
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read " + resourcePath, e);
        }
    }

    private InputStream open() {
        InputStream in = getClass().getResourceAsStream(resourcePath);
        if (in == null) {
            throw new IllegalStateException("Resource not found: " + resourcePath);
        }
        return in;
    }

    private static List<String> split(String line) {
        return Arrays.stream(line.split(DELIMITER)).map(String::trim).toList();
    }
}