package com.shieldbreaker.kernel;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to read the config file.
 */
public class ConfigLoader {
    public static final String CONFIG_FILE_PATH = "config.yaml";
    public static final String DEFAULT_PLUGINS_DIRECTORY = "plugins/";
    public static final String DEFAULT_VERSION = "BETA";

    private final String pluginsDirectory;
    private final String version;

    /**
     * The constructor.
     */
    public ConfigLoader() {
        Yaml yaml = new Yaml();

        Map<String, String> obj = new HashMap<>();
        try {
            obj = yaml.load(new FileInputStream(new File(CONFIG_FILE_PATH)));
        } catch (IOException ignored) {
            System.err.println("Failed to open config file (" + CONFIG_FILE_PATH + ").");
        }

        pluginsDirectory = obj.getOrDefault("pluginsDirectory", DEFAULT_PLUGINS_DIRECTORY);
        version = obj.getOrDefault("version", DEFAULT_VERSION);
    }

    /**
     * Get the plugins directory.
     *
     * @return the plugins directory.
     */
    public String getPluginsDirectory() {
        return pluginsDirectory;
    }

    /**
     * Get the version.
     *
     * @return the version.
     */
    public String getVersion() {
        return version;
    }
}