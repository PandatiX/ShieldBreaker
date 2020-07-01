package com.shieldbreaker.cli.type;

import com.sun.istack.internal.NotNull;

import javax.swing.*;

//TODO create a strategy to build a reset JRadioButton for each group
public class RadioFieldType extends BaseType {
    public RadioFieldType(@NotNull ButtonGroup group, String indicatorName) {
        super();

        indicatorComponent = new JRadioButton(camelCaseToReadable(indicatorName));
        group.add((JRadioButton)indicatorComponent);

        efficientComponent = new JTextField();
    }

    @Override
    public void setEfficientDefaultValue(String defaultValue) {
        ((JRadioButton) indicatorComponent).setSelected(defaultValue != null && !defaultValue.isEmpty());
        ((JTextField) efficientComponent).setText(defaultValue);
    }
}