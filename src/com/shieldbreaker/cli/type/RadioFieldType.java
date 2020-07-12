package com.shieldbreaker.cli.type;

import com.sun.istack.internal.NotNull;

import javax.swing.*;

/**
 * Class to dictates the GUI to build a Radio field.
 *
 * @todo create a strategy to build a reset JRadioButton for each group
 */
public class RadioFieldType extends BaseType {
    /**
     * The constructor to set strategy parameters.
     *
     * @param group the Radio group.
     * @param indicatorName the name to display in the indicator field.
     */
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