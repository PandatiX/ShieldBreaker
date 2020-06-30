package com.shieldbreaker.cli.type;

import javax.swing.*;

public class TextFieldType extends BaseType {

    public TextFieldType() {}

    @Override
    public JComponent newComponent(String defaultValue) {
        return new JTextField(defaultValue);
    }
}