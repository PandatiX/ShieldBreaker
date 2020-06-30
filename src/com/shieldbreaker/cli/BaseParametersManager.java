package com.shieldbreaker.cli;

import com.shieldbreaker.cli.type.BaseType;
import com.sun.istack.internal.NotNull;
import org.apache.commons.cli.Option;

import java.util.*;

public abstract class BaseParametersManager {
    protected final List<OptionValueParameter> options;

    protected String[] supportedProtocols;

    protected BaseParametersManager() {
        options = new ArrayList<>();

        createCliOptions();
    }

    protected void addOption(@NotNull Option o, @NotNull String defaultValue, @NotNull ParametersManager.KEYS key, @NotNull BaseType type) {
        if (getOV(o.getLongOpt()) == null) {
            options.add(new OptionValueParameter(o, defaultValue, key, type));
        } else
            throw new IllegalArgumentException();
    }

    protected void addOption(Option o, ParametersManager.KEYS key, BaseType type) {
        addOption(o, "", key, type);
    }

    protected abstract void createCliOptions();
    protected abstract void checkParameters() throws Exception;

    private OptionValueParameter getOV(@NotNull String key) {
        for (OptionValueParameter opt : options) {
            if (opt.getOption().getLongOpt().equals(key)) {
                return opt;
            }
        }
        return null;
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
