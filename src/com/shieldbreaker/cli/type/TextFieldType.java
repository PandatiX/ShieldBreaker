package com.shieldbreaker.cli.type;

import javax.swing.*;

/**
 * Class to dictates the GUI to build a Text field.
 */
public class TextFieldType extends BaseType {
    /**
     * The constructor to set strategy parameters.
     *
     * @param indicatorName the name to display in the indicator field.
     */
    public TextFieldType(String indicatorName) {
        super();

        indicatorComponent = new JLabel(camelCaseToReadable(indicatorName));

        efficientComponent = new JTextField();
    }

    @Override
    public void setEfficientDefaultValue(String defaultValue) {
        ((JTextField) efficientComponent).setText(defaultValue);
    }
}