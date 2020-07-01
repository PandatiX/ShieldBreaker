package com.shieldbreaker.cli.type;

import javax.swing.*;

public abstract class BaseType {
    protected JComponent indicatorComponent;
    protected JComponent efficientComponent;

    public BaseType() {}

    public JComponent getIndicatorComponent() {
        return indicatorComponent;
    }
    public JComponent getEfficientComponent() {
        return efficientComponent;
    }
    public JComponent getEfficientComponent(String defaultValue) {
        setEfficientDefaultValue(defaultValue);
        return efficientComponent;
    }
    public abstract void setEfficientDefaultValue(String defaultValue);

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
}