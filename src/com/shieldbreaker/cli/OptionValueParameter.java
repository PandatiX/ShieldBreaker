package com.shieldbreaker.cli;

import com.shieldbreaker.cli.type.BaseType;
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
    private final BaseType type;

    public OptionValueParameter(Option o, String value, ParametersManager.KEYS key, BaseType type) {
        this.o = o;
        this.value = value;
        this.parameterKey = key;
        this.type = type;
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
    public BaseType getType() {
        return type;
    }

    public void setValue(@NotNull String value) {
        this.value = value;
    }
}
