package com.shieldbreaker.cli;

import com.shieldbreaker.kernel.ShieldBreaker;
import com.sun.istack.internal.NotNull;
import org.apache.commons.cli.*;

import java.util.*;

public class ParametersManager {
    private static final int defaultNbThreads = 1;
    private static final String graphicalMessage = ShieldBreaker.YELLOW + "\nIf launched in graphical mode, not required." + ShieldBreaker.RESET;

    private final boolean GUI;
    private final String[] args;
    private final Options options;

    private BaseParametersManager pluginParametersManager;
    private int NBTHREADS;

    public ParametersManager(String[] args, boolean GUI) {
        this.args = args;
        this.GUI = GUI;

        NBTHREADS = defaultNbThreads;

        options = new Options();
    }

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
                o.setDescription(o.getDescription() + graphicalMessage);
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
            setNBTHREADS(cmd.getOptionValue("threads", Integer.toString(defaultNbThreads)));
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

    public int getNBTHREADS() {
        return NBTHREADS;
    }

    public String getValue(String key) {
        assert pluginParametersManager != null;
        return pluginParametersManager.getValue(key);
    }
    public void setNBTHREADS(String nbthreads) {
        setNBTHREADS(nbthreads.matches("\\d+") ? Integer.parseInt(nbthreads) : defaultNbThreads);
    }
    public void setNBTHREADS(int nbthreads) {
        NBTHREADS = (nbthreads > 0 ? nbthreads : defaultNbThreads);
    }
    public void setValue(String key, String value) throws IllegalArgumentException {
        assert pluginParametersManager != null;
        pluginParametersManager.setValue(key, value);
    }
    public void checkParameters() throws Exception {
        assert pluginParametersManager != null;
        pluginParametersManager.checkParameters();
    }

    public enum TYPE {
        TEXT_FIELD,
        RADIO_FIELD,
        FILE_CHOOSER_FIELD_MONOSEL,
        FILE_CHOOSER_FIELD_MULTISEL
    }
}