package com.shieldbreaker.kernel;

import com.shieldbreaker.cli.ParametersManager;
import com.shieldbreaker.ui.UIManager;
import com.shieldbreaker.ui.UIPluginChoice;
import com.shieldbreaker.ui.UITerminal;

import java.io.*;
import java.util.*;

/**
 * Main class to rule them all.
 */
public class ShieldBreaker {
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String YELLOW = "\u001B[33m";
    public static final String LOGO =
            "   _____ __    _      __    ______                  __\n" +
            "  / ___// /_  (_)__  / /___/ / __ )________  ____ _/ /_____  _____\n" +
            "  \\__ \\/ __ \\/ / _ \\/ / __  / __  / ___/ _ \\/ __ `/ //_/ _ \\/ ___/\n" +
            " ___/ / / / / /  __/ / /_/ / /_/ / /  /  __/ /_/ / ,< /  __/ /\n" +
            "/____/_/ /_/_/\\___/_/\\__,_/_____/_/   \\___/\\__,_/_/|_|\\___/_/\n";
    private static ShieldBreaker singleton;

    private final ConfigLoader configLoader;
    private final boolean GUI;

    private BasePlugin plugin;
    private ParametersManager parametersManager;
    private UIManager uiManager;

    /**
     * Display priority.
     */
    public enum OUT_PRIORITY {
        IMPORTANT,
        MEDIUM,
        LOW
    }

    private ShieldBreaker(String[] args) {
        configLoader = new ConfigLoader();

        System.out.println(LOGO + "\nThe ShieldBreaker is a POC of a plugin-based framework to simplify attacks simulations.\nVersion: " + configLoader.getVersion());

        //Check GUI parameter
        boolean g = false;
        for (String arg : args) {
            if (arg.equals("-g") || arg.equals("--graphical")) {
                g = true;
                break;
            }
        }
        GUI = g;

        loadPlugins();

        loadParametersManagers(args);
    }

    /**
     * Create the ShieldBreaker instance (DP Singleton).
     *
     * @param args CLI arguments.
     *
     * @return the ShieldBreaker instance.
     */
    public static ShieldBreaker createInstance(String[] args) {
        if (singleton == null)
            singleton = new ShieldBreaker(args);
        return singleton;
    }

    /**
     * Get the ShieldBreaker instance.
     *
     * @return the ShieldBreaker instance.
     */
    public static ShieldBreaker getInstance() {
        return singleton;
    }

    /**
     * Get the ConfigLoader.
     * @return the ConfigLoader.
     */
    public ConfigLoader getConfigLoader() {
        return configLoader;
    }

    /**
     * Start the ShieldBreaker (in CLI or GUI mode, according to given args).
     */
    public void startApp() {
        if (GUI) {
            uiManager = new UIManager(this);
        } else {
            startBotManager();
        }
    }

    /**
     * Start the plugin bot manager.
     */
    public void startBotManager() {
        try {
            parametersManager.checkParameters();
            plugin.getClassBotManager().newInstance().startBots(plugin.getClassBot());
        } catch (IllegalAccessException | InstantiationException e) {
            err("Cannot start " + plugin.getClass() + ": " + e.getMessage(), OUT_PRIORITY.IMPORTANT);
        } catch (Exception e) {
            err(e.getMessage(), OUT_PRIORITY.IMPORTANT);
        }
    }

    /**
     * Display a string according to its priority, as an information.
     * Works on a cascade style: each priority calls its inferior ones.
     *
     * @param s string to display.
     * @param priority priority to display the string.
     */
    public void out(String s, OUT_PRIORITY priority) {
        switch (priority) {
            case IMPORTANT:
                if (uiManager != null) {
                    uiManager.dialogMessage(s);
                }
            case MEDIUM:
                UITerminal terminal;
                if (uiManager != null && (terminal = uiManager.getTerminal()) != null) {
                    terminal.out(s);
                }
            case LOW:
            default:
                System.out.println(s);
        }
    }

    /**
     * Display a string according to its priority, as an error.
     * Works on a cascade style: each priority calls its inferior ones.
     *
     * @param s string to display.
     * @param priority priority to display the string.
     */
    public void err(String s, OUT_PRIORITY priority) {
        switch (priority) {
            case IMPORTANT:
                if (uiManager != null) {
                    uiManager.errorDialogMessage(s);
                }
            case MEDIUM:
                UITerminal terminal;
                if (uiManager != null && (terminal = uiManager.getTerminal()) != null) {
                    terminal.out(s);
                }
            case LOW:
            default:
                System.err.println(RED + s + RESET);
        }
    }

    /**
     * Get the plugin.
     *
     * @return the plugin.
     */
    public BasePlugin getPlugin() {
        return plugin;
    }

    /**
     * Get the UI manager.
     *
     * @return the UI manager.
     */
    public UIManager getUIManager() {
        return uiManager;
    }

    /**
     * Get the parameters manager.
     *
     * @return the parameters manager.
     */
    public ParametersManager getParametersManager() {
        return parametersManager;
    }

    private void loadPlugins() {
        //Dynamically load plugins
        PluginsLoader pluginsLoader = new PluginsLoader();
        String pluginsDirectory = configLoader.getPluginsDirectory();
        final File f = new File(pluginsDirectory);

        if (!f.exists()) {
            System.err.println("Directory doesn't exists: creating it.");
            if (!f.mkdir()) {
                System.err.println("An error occurred while creating the directory. Cannot go further.");
                System.exit(1);
            }
        }

        if (f.isDirectory()) {
            File[] files = f.listFiles((file, s) -> s.endsWith(".jar"));
            if (files != null) {
                String[] filesPath = new String[files.length];
                for (int i = 0; i < files.length; i++) {
                    filesPath[i] = pluginsDirectory + files[i].getName();
                }
                pluginsLoader.setFiles(filesPath);
            }
        } else {
            System.err.println("Directory " + f.getPath() + " is not a directory. Cannot go further.");
            System.exit(1);
        }

        List<BasePlugin> plugins;
        try {
            plugins = pluginsLoader.loadPlugins();
        } catch (Exception ignored) {
            plugins = new ArrayList<>();
        }

        //Choose the plugin to work with in all the detected ones
        int pluginsSize = plugins.size();
        if (pluginsSize == 0) {             //NO PLUGIN TO LOAD
            System.err.println("No plugin found to work with. Cannot go further.");
            System.exit(1);
        } else if (pluginsSize == 1) {      //ONE PLUGIN TO LOAD
            plugin = plugins.get(0);
        } else {                            //HAVE MULTIPLE PLUGINS
            if (GUI) {
                try {
                    UIPluginChoice pluginChoice = new UIPluginChoice(plugins);
                    Thread thread = new Thread(pluginChoice);
                    thread.setName("PluginChoice");
                    thread.start();
                    plugin = plugins.get(pluginChoice.getIndexSelectedPlugin());
                } catch (InterruptedException e) {
                    System.err.println("An error occurred while selecting the plugin to work with. Automatically choosing the 1st plugin");
                    plugin = plugins.get(0);
                }
            } else {
                System.out.println("Found multiple plugins to work with.\r\nPlease select one to work with by its number.\r\n");
                BasePlugin p;
                for (int i = 0; i < pluginsSize; i++) {
                    p = plugins.get(i);
                    System.out.println("\t" + YELLOW + "(" + i + ") " + RESET + p.getName());
                }
                System.out.print("\r\nChosen plugin: ");
                int choice = 0;
                try {
                    Scanner scanner = new Scanner(System.in);
                    int choiceI = scanner.nextInt();
                    scanner.close();
                    if (choiceI < 0 || choiceI >= pluginsSize)
                        throw new IndexOutOfBoundsException();
                    choice = choiceI;
                } catch (IndexOutOfBoundsException | InputMismatchException e) {
                    System.err.println("Invalid index. Automatically choosing the 1st plugin.");
                }
                plugin = plugins.get(choice);
            }
        }
        System.out.println("Loaded plugin " + plugin.getName() + "\r\n");
    }

    private void loadParametersManagers(String[] args) {
        parametersManager = new ParametersManager(args, GUI);
        try {
            parametersManager.setPluginParametersManager(plugin.getClassParametersManager().newInstance());
        } catch (IllegalAccessException | InstantiationException ignored) {
            System.err.println("Failed to load plugin " + plugin.getName());
        }
        parametersManager.parseArgs();
    }
}