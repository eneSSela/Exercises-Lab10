package it.unibo.mvc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Utility class for loading configurations from a text file.
 */
public final class ConfigurationLoader {

    private ConfigurationLoader() { }

    /**
     * Loads a configuration from a file.
     *
     * @param pathSource path to the configuration file
     * @return a Configuration instance
     * @throws IOException if an I/O error occurs
     */
    public static Configuration load(final String pathSource) throws IOException {
        final InputStream is = ConfigurationLoader.class.getResourceAsStream(pathSource);
        if (is == null) {
            throw new FileNotFoundException("Resource not found: " + pathSource);
        }

        int min = 0;
        int max = 100;
        int attempts = 10;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8))) {

            String line = reader.readLine();

            while (line != null) {

                final String trimmed = line.trim();

                if (!trimmed.isEmpty() && !trimmed.startsWith("#")) {

                    final String[] parts = trimmed.split(":");

                    if (parts.length == 2) {
                        final String key = parts[0].trim();
                        final int value = Integer.parseInt(parts[1].trim());

                        switch (key) {
                            case "minimum":
                                min = value;
                                break;
                            case "maximum":
                                max = value;
                                break;
                            case "attempts":
                                attempts = value;
                                break;
                            default:
                                break;
                        }
                    }
                }

                line = reader.readLine();
            }
        }

        final Configuration config = new Configuration.Builder()
                .withMin(min)
                .withMax(max)
                .withAttempts(attempts)
                .build();

        if (!config.isConsistent()) {
            throw new IllegalArgumentException("Configuration values are inconsistent");
        }

        return config;
    }
}
