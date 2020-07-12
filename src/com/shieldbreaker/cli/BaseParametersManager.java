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

public abstract class BaseParametersManager {
    protected final List<OptionValueParameter> options;

    protected String[] supportedProtocols;

    protected BaseParametersManager() {
        options = new ArrayList<>();

        try {
            createCliOptions();
        } catch (UnsupportedTypeException e) {
            System.err.println(e.getMessage());
        }
    }

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

    protected void addOption(Option o,
                             ParametersManager.TYPE type,
                             Object... params) throws UnsupportedTypeException {
        addOption(o, "", type, params);
    }

    protected abstract void createCliOptions() throws UnsupportedTypeException;
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

    public List<OptionValueParameter> getOptions() {
        return options;
    }

    public String getValue(String key) {
        OptionValueParameter o = getOV(key);
        if (o != null) {
            return o.getValue();
        } else
            throw new IllegalArgumentException();
    }

    public List<String> getKeys() {
        List<String> keys = new ArrayList<>();

        for (OptionValueParameter opt : options) {
            keys.add(opt.getOption().getLongOpt());
        }

        return keys;
    }

    public void setValue(@NotNull String key, String value) {
        OptionValueParameter o = getOV(key);
        if (o != null) {
            o.setValue(value);
        } else
            throw new IllegalArgumentException();
    }

}
