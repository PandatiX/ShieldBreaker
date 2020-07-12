package com.shieldbreaker.cli;

import com.shieldbreaker.kernel.ShieldBreaker;
import com.sun.istack.internal.NotNull;
import org.apache.commons.cli.*;

import java.util.*;

/**
 * Class to manage all CLI parameters (kernel and plugin).
 */
public class ParametersManager {
    private static final int DEFAULT_NB_THREADS = 1;
    private static final String GRAPHICAL_MESSAGE = ShieldBreaker.YELLOW + "\nIf launched in graphical mode, not required." + ShieldBreaker.RESET;

    private final boolean GUI;
    private final String[] args;
    private final Options options;

    private BaseParametersManager pluginParametersManager;
    private int nbThreads;

    /**
     * The constructor.
     *
     * @param args CLI arguments.
     * @param GUI works in the GUI mode.
     */
    public ParametersManager(String[] args, boolean GUI) {
        this.args = args;
        this.GUI = GUI;

        nbThreads = DEFAULT_NB_THREADS;

        options = new Options();
    }

    /**
     * Set the plugin parameters manager.
     *
     * @param pluginParametersManager the plugin parameters manager.
     */
    public void setPluginParametersManager(@NotNull BaseParametersManager pluginParametersManager) {
        this.pluginParametersManager = pluginParametersManager;
    }

    private void addOptions(@NotNull List<OptionValueParameter> ovp) {
        Option o;
        String key;
        for (OptionValueParameter opt : ovp) {
            o = opt.getOption();
            //Check if required and started in the GUI
            if (o.isRequired()) {
                o.setDescription(o.getDescription() + GRAPHICAL_MESSAGE);
                o.setRequired(!GUI);
            }
            //Add option to known ones
            key = o.getLongOpt();
            if (options.hasOption(key)) {
                System.err.println("Cannot add option \"" + key + "\". Please verify plugin(s).");
            } else {
                options.addOption(o);
            }
        }
    }

    /**
     * Parse CLI arguments.
     */
    public void parseArgs() {
        assert pluginParametersManager != null;

        //Build children
        addOptions(pluginParametersManager.getOptions());

        //Set default options
        Option help = new Option("h", "help", false, "Display help.");
        options.addOption(help);

        Option threads = new Option("t", "threads", true, "Set the number of (JAVA) threads to work on. Default is 1.");
        threads.setArgName("n");
        options.addOption(threads);

        Option graphical = new Option("g", "graphical", false, "Start GUI.");
        options.addOption(graphical);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        //Check if asked for help
        for (String arg : args) {
            if (arg.equals("-h") || arg.equals("--help")) {
                printHelp(formatter, options);
                System.exit(0);
            }
        }

        try {
            cmd = parser.parse(options, args);

            //Set all parameters
            setNbThreads(cmd.getOptionValue("threads", Integer.toString(DEFAULT_NB_THREADS)));
            List<String> keys = pluginParametersManager.getKeys();
            for (String key : keys) {
                pluginParametersManager.setValue(key, cmd.getOptionValue(key, ""));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void printHelp(HelpFormatter formatter, Options options) {
        String footer = "\n" + ShieldBreaker.RED + "USE ONLY FOR GRANTED TESTS !" + ShieldBreaker.RESET;
        formatter.printHelp("ShieldBreaker", "\n", options, footer, true);
    }

    /**
     * Get number of threads.
     *
     * @return the number of threads.
     */
    public int getNbThreads() {
        return nbThreads;
    }

    /**
     * Get the value from the plugin.
     *
     * @param key the plugin's options longOpt to get the value.
     *
     * @return the value of the plugin's option.
     */
    public String getValue(String key) {
        assert pluginParametersManager != null;
        return pluginParametersManager.getValue(key);
    }

    /**
     * Set the number of threads.
     *
     * @param nbthreads the number of threads.
     */
    public void setNbThreads(String nbthreads) {
        setNBTHREADS(nbthreads.matches("\\d+") ? Integer.parseInt(nbthreads) : DEFAULT_NB_THREADS);
    }

    /**
     * Set the number of threads.
     *
     * @param nbthreads the number of threads.
     */
    public void setNBTHREADS(int nbthreads) {
        nbThreads = (nbthreads > 0 ? nbthreads : DEFAULT_NB_THREADS);
    }

    /**
     * Set the plugin's option value.
     *
     * @param key the plugin's option longOpt.
     * @param value the plugin's option value.
     *
     * @throws IllegalArgumentException failed to get the key's OptionValueParameter.
     */
    public void setValue(String key, String value) throws IllegalArgumentException {
        assert pluginParametersManager != null;
        pluginParametersManager.setValue(key, value);
    }

    /**
     * Check parameters.
     *
     * @throws Exception failed to fulfill options conditions.
     */
    public void checkParameters() throws Exception {
        assert pluginParametersManager != null;
        pluginParametersManager.checkParameters();
    }

    /**
     * GUI type.
     */
    public enum TYPE {
        TEXT_FIELD,
        RADIO_FIELD,
        FILE_CHOOSER_FIELD_MONOSEL,
        FILE_CHOOSER_FIELD_MULTISEL
    }
}