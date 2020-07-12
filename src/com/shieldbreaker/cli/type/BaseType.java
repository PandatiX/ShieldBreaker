package com.shieldbreaker.cli.type;

import javax.swing.*;

/**
 * Abstract class to dictates the GUI how to behave according to an option.
 */
public abstract class BaseType {
    /**
     * Indicator component to indicate the user in the GUI.
     */
    protected JComponent indicatorComponent;
    /**
     * Efficient component to fill the option in the GUI.
     */
    protected JComponent efficientComponent;

    /**
     * The constructor.
     */
    protected BaseType() {}

    /**
     * Get indicator component.
     * @return the indicator component.
     */
    public JComponent getIndicatorComponent() {
        return indicatorComponent;
    }

    /**
     * Get efficient component.
     *
     * @param defaultValue value to fill the efficient component at its build.
     *
     * @return the efficient component.
     */
    public JComponent getEfficientComponent(String defaultValue) {
        setEfficientDefaultValue(defaultValue);
        return efficientComponent;
    }

    /**
     * Set efficient default value.
     *
     * @param defaultValue value to fill the efficient component at its build.
     */
    public abstract void setEfficientDefaultValue(String defaultValue);

    /**
     * Converts a camel case string to a readable string.
     *
     * @param s the camel case string to convert.
     *
     * @return the readable string.
     */
    public static String camelCaseToReadable(String s) {
        StringBuilder sb = new StringBuilder();

        //Set first char
        char c = s.charAt(0);
        sb.append(Character.toUpperCase(c));

        //Set others char
        int length = s.length();
        for (int i = 1; i < length; i++) {
            c = s.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(" ").append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        else return this.getClass() == other.getClass()
                && indicatorComponent.getClass() == ((BaseType) other).indicatorComponent.getClass()
                && efficientComponent.getClass() == ((BaseType) other).efficientComponent.getClass();
    }
}