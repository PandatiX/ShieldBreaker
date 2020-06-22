package com.shieldbreaker.cli;

import com.shieldbreaker.kernel.ShieldBreaker;
import com.sun.istack.internal.NotNull;
import org.apache.commons.cli.*;

import java.util.*;

public class ParametersManager {
    public enum KEYS {
        NO_KEY,
        GUI_KEY
    }
    private static final String graphicalMessage = ShieldBreaker.YELLOW + "\nIf launched in graphical mode, not required." + ShieldBreaker.RESET;
    private static final int defaultNbThreads = 1;

    private final boolean GUI;
    private final String[] args;
    private final Options options;
    private final List<BaseParametersManager> parametersManagers;

    private int NBTHREADS;

    public ParametersManager(String[] args, boolean GUI) {
        this.args = args;
        this.GUI = GUI;

        options = new Options();
        parametersManagers = new ArrayList<>();
    }

    public void addParametersManager(@NotNull BaseParametersManager parametersManager) {
        if (!parametersManagers.contains(parametersManager)) {
            parametersManagers.add(parametersManager);
        } else
            throw new IllegalArgumentException();
    }

    private void addOptions(List<OptionValueParameter> ovp) {
        Option o;
        String key;
        for (OptionValueParameter opt : ovp) {
            o = opt.getOption();
            //Check for GUI_KEY in options
            if (opt.getParameterKey().equals(KEYS.GUI_KEY)) {
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
        //Build children
        for (BaseParametersManager p : parametersManagers) {
            addOptions(p.getOptions());
        }

        //Set default options
        Option help = new Option("h", "help", false, "Display help.");
        help.setRequired(false);
        options.addOption(help);

        Option threads = new Option("t", "threads", true, "Set the number of (JAVA) threads to work on. Default is 1.");
        threads.setArgName("n");
        threads.setRequired(false);
        options.addOption(threads);

        Option graphical = new Option("g", "graphical", false, "Start GUI.");
        graphical.setRequired(false);
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
            List<String> keys;
            for (BaseParametersManager p : parametersManagers) {
                keys = p.getKeys();
                for (String key : keys) {
                    p.setValue(key, cmd.getOptionValue(key, ""));
                }
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
    public String getValue(@NotNull String key) {
        for (BaseParametersManager bp : parametersManagers) {
            try {
                return bp.getValue(key);
            } catch (IllegalArgumentException ignored) {}
        }
        throw new IllegalArgumentException();
    }

    public void setNBTHREADS(String nbthreads) {
        setNBTHREADS(nbthreads.matches("\\d+") ? Integer.parseInt(nbthreads) : defaultNbThreads);
    }
    public void setNBTHREADS(int nbthreads) {
        NBTHREADS = (nbthreads > 0 ? nbthreads : defaultNbThreads);
    }
    public void setValue(String key, String value) {
        for (BaseParametersManager bp : parametersManagers) {
            try {
                bp.setValue(key, value);
                return;
            } catch (IllegalArgumentException ignored) {}
        }
        throw new IllegalArgumentException();
    }
}