package com.shieldbreaker.cli.type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.*;

class RadioFieldTypeTest {
    @Test
    void setEfficientDefaultValue() {
        RadioFieldType radioFT = new RadioFieldType(new ButtonGroup(), "option");

        //Test for null
        radioFT.setEfficientDefaultValue(null);

        Assertions.assertFalse(((JRadioButton) radioFT.indicatorComponent).isSelected());
        Assertions.assertEquals("", ((JTextField) radioFT.efficientComponent).getText());

        //Test for empty
        radioFT.setEfficientDefaultValue("");

        Assertions.assertFalse(((JRadioButton) radioFT.indicatorComponent).isSelected());
        Assertions.assertEquals("", ((JTextField) radioFT.efficientComponent).getText());

        //Test for standard
        radioFT.setEfficientDefaultValue("test");

        Assertions.assertTrue(((JRadioButton) radioFT.indicatorComponent).isSelected());
        Assertions.assertEquals("test", ((JTextField) radioFT.efficientComponent).getText());
    }
}