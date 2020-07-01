package com.shieldbreaker.cli;

import com.shieldbreaker.cli.type.BaseType;
import com.sun.istack.internal.NotNull;
import org.apache.commons.cli.Option;

public class OptionValueParameter {
    private final Option o;
    private String value;
    private final BaseType type;

    public OptionValueParameter(Option o, String value, BaseType type) {
        this.o = o;
        this.value = value;
        this.type = type;
    }

    public Option getOption() {
        return o;
    }
    public String getValue() {
        return value;
    }
    public BaseType getType() {
        return type;
    }

    public void setValue(@NotNull String value) {
        this.value = value;
    }
}
