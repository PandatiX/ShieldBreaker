package com.shieldbreaker.kernel;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class ConfigLoader {
    private static final String configFilePath = "config.yaml";
    private static ConfigLoader singleton;

    private final String pluginsDirectory;
    private final String version;

    private ConfigLoader() {
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(configFilePath);
        Map<String, String> obj = yaml.load(inputStream);

        pluginsDirectory = obj.getOrDefault("pluginsDirectory", "plugins/");
        version = obj.getOrDefault("version", "");
    }

    public static ConfigLoader getInstance() {
        if (singleton == null)
            singleton = new ConfigLoader();
        return singleton;
    }

    public String getPluginsDirectory() {
        return pluginsDirectory;
    }
    public String getVersion() {
        return version;
    }

}
