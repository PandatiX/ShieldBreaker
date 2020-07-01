package com.shieldbreaker.cli.type;

import javax.swing.*;

public class TextFieldType extends BaseType {
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