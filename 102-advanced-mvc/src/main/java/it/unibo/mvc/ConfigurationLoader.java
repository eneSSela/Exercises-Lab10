package it.unibo.mvc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConfigurationLoader {
    
    public static Configuration load(String pathSource) throws Exception {
        InputStream is = ConfigurationLoader.class.getResourceAsStream(pathSource);
        if (is == null) {
            throw new FileNotFoundException("Resource not found: " + pathSource);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        int min = 0;
        int max = 100;
        int attempts = 10;

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }

            String[] parts = line.split(":");
            if (parts.length != 2) {
                continue;
            }

            String key = parts[0].trim();
            int value = Integer.parseInt(parts[1].trim());

            switch (key) {
                case "minimum": min = value; break;
                case "maximum": max = value; break;
                case "attempts": attempts = value; break;
                default: break;
            }
        }
        reader.close();
        Configuration config = new Configuration.Builder()
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
