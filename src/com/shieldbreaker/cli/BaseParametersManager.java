package com.shieldbreaker.cli;

import com.shieldbreaker.cli.type.BaseType;
import com.shieldbreaker.cli.exceptions.UnsupportedTypeException;
import com.shieldbreaker.cli.type.FileChooserType;
import com.shieldbreaker.cli.type.RadioFieldType;
import com.shieldbreaker.cli.type.TextFieldType;
import com.sun.istack.internal.NotNull;
import org.apache.commons.cli.Option;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.*;

/**
 * Abstract class to create CLI options from a plugin.
 */
public abstract class BaseParametersManager {
    /**
     * Plugin options.
     */
    protected final List<OptionValueParameter> options;

    /**
     * Plugin supported protocols.
     */
    protected String[] supportedProtocols;

    /**
     * The plugin parameters manager constructor.
     */
    protected BaseParametersManager() {
        options = new ArrayList<>();

        try {
            createCliOptions();
        } catch (UnsupportedTypeException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Add an option to the plugin CLI options.
     *
     * @param o the option.
     * @param defaultValue the option default value.
     * @param type the option type to dictates the GUI how to build the corresponding field.
     * @param params the type parameters.
     *
     * @throws UnsupportedTypeException given type failed to match a supported type.
     * @throws IllegalArgumentException the option is known.
     */
    protected void addOption(@NotNull Option o,
                             @NotNull String defaultValue,
                             @NotNull ParametersManager.TYPE type,
                             Object... params) throws UnsupportedTypeException, IllegalArgumentException {
        if (getOV(o.getLongOpt()) == null) {

            BaseType baseType;
            switch (type) {
                case TEXT_FIELD:
                    baseType = new TextFieldType(o.getLongOpt());
                    break;
                case RADIO_FIELD:
                    ButtonGroup group = params[0] instanceof ButtonGroup ? (ButtonGroup)params[0] : new ButtonGroup();
                    baseType = new RadioFieldType(group, o.getLongOpt());
                    break;
                case FILE_CHOOSER_FIELD_MONOSEL:
                    baseType = new FileChooserType(false, o.getLongOpt(), getFilter(params[0]));
                    break;
                case FILE_CHOOSER_FIELD_MULTISEL:
                    baseType = new FileChooserType(true, o.getLongOpt(), getFilter(params[0]));
                    break;
                default:
                    throw new UnsupportedTypeException();
            }

            options.add(new OptionValueParameter(o, defaultValue, baseType));
        } else
            throw new IllegalArgumentException();
    }

    /**
     * Add an option to the plugin CLI options.
     *
     * @param o the option.
     * @param type the option type to dictates the GUI how to build the corresponding field.
     * @param params the type parameters.
     *
     * @throws UnsupportedTypeException given type failed to match a supported type.
     * @throws IllegalArgumentException the option is known.
     */
    protected void addOption(Option o,
                             ParametersManager.TYPE type,
                             Object... params) throws UnsupportedTypeException, IllegalArgumentException {
        addOption(o, "", type, params);
    }

    /**
     * Create CLI options.
     *
     * @throws UnsupportedTypeException given type failed to match a supported type.
     */
    protected abstract void createCliOptions() throws UnsupportedTypeException;

    /**
     * Check parameters.
     *
     * @throws Exception failed to fulfill options conditions.
     */
    protected abstract void checkParameters() throws Exception;

    private OptionValueParameter getOV(@NotNull String key) {
        for (OptionValueParameter opt : options) {
            if (opt.getOption().getLongOpt().equals(key)) {
                return opt;
            }
        }
        return null;
    }
    private FileNameExtensionFilter getFilter(Object param) {
        return param instanceof FileNameExtensionFilter ? (FileNameExtensionFilter)param : null;
    }

    /**
     * Get plugin options.
     *
     * @return the plugin options.
     */
    public List<OptionValueParameter> getOptions() {
        return options;
    }

    /**
     * Get CLI option value according to the option's longOpt.
     *
     * @param key the longOpt to get its CLI value.
     *
     * @return the CLI option value.
     */
    public String getValue(String key) throws IllegalArgumentException {
        OptionValueParameter o = getOV(key);
        if (o != null) {
            return o.getValue();
        } else
            throw new IllegalArgumentException();
    }

    /**
     * Get plugin keys (options' longOpt).
     *
     * @return the plugin keys.
     */
    public List<String> getKeys() {
        List<String> keys = new ArrayList<>();

        for (OptionValueParameter opt : options) {
            keys.add(opt.getOption().getLongOpt());
        }

        return keys;
    }

    /**
     * Set CLI options value according to the option's longOpt.
     *
     * @param key the longOpt to set its CLI value.
     * @param value the value to set.
     *
     * @throws IllegalArgumentException failed to get the key's OptionValueParameter.
     */
    public void setValue(@NotNull String key, String value) throws IllegalArgumentException {
        OptionValueParameter o = getOV(key);
        if (o != null) {
            o.setValue(value);
        } else
            throw new IllegalArgumentException();
    }
}