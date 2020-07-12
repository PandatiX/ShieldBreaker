package com.shieldbreaker.cli;

import com.shieldbreaker.cli.type.BaseType;
import com.sun.istack.internal.NotNull;
import org.apache.commons.cli.Option;

/**
 * Class to link an option, its value and its GUI type.
 */
public class OptionValueParameter {
    private final Option o;
    private String value;
    private final BaseType type;

    /**
     * The constructor.
     * Set an option and its associated value and GUI type.
     *
     * @param o the option.
     * @param value the option's value.
     * @param type the option's GUI type.
     */
    public OptionValueParameter(Option o, String value, BaseType type) {
        this.o = o;
        this.value = value;
        this.type = type;
    }

    /**
     * Get the option.
     * @return the option.
     */
    public Option getOption() {
        return o;
    }

    /**
     * Get the value.
     * @return the value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the GUI type.
     * @return the GUI type.
     */
    public BaseType getType() {
        return type;
    }

    /**
     * Set the value.
     * @param value the value to set.
     */
    public void setValue(@NotNull String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        else return this.getClass() == other.getClass()
                && o.equals(((OptionValueParameter) other).o)
                && value.equals(((OptionValueParameter) other).value)
                && type.equals(((OptionValueParameter) other).type);
    }
}