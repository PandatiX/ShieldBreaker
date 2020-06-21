package com.shieldbreaker.cli;

import com.sun.istack.internal.NotNull;
import org.apache.commons.cli.Option;

public class OptionValueParameter {
    //Option o : the option itself
    //String value : the value for this parameter
    //String parameterKey : the key (DEPENDS_ON_GUI...)
    //key : o.getLongOpt()
    private final Option o;
    private String value;
    private final ParametersManager.KEYS parameterKey;

    public OptionValueParameter(Option o, String value, ParametersManager.KEYS key) {
        this.o = o;
        this.value = value;
        this.parameterKey = key;
    }

    public Option getOption() {
        return o;
    }

    public String getValue() {
        return value;
    }

    public ParametersManager.KEYS getParameterKey() {
        return parameterKey;
    }

    public void setValue(@NotNull String value) {
        this.value = value;
    }
}
