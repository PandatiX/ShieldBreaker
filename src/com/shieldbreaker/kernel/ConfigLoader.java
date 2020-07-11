package com.shieldbreaker.kernel;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigLoader {
    public static final String CONFIG_FILE_PATH = "config.yaml";
    public static final String defaultPluginsDirectory = "plugins/";
    public static final String defaultVersion = "BETA";

    private final String pluginsDirectory;
    private final String version;

    public ConfigLoader() {
        Yaml yaml = new Yaml();

        Map<String, String> obj = new HashMap<>();
        try {
            obj = yaml.load(new FileInputStream(new File(CONFIG_FILE_PATH)));
        } catch (IOException ignored) {
            System.err.println("Failed to open config file (" + CONFIG_FILE_PATH + ").");
        }

        pluginsDirectory = obj.getOrDefault("pluginsDirectory", defaultPluginsDirectory);
        version = obj.getOrDefault("version", defaultVersion);
    }

    public String getPluginsDirectory() {
        return pluginsDirectory;
    }
    public String getVersion() {
        return version;
    }
}