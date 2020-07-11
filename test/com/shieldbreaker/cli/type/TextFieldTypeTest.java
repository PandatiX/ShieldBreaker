package com.shieldbreaker.cli.type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.*;

class TextFieldTypeTest {

    @Test
    void setEfficientDefaultValue() {
        TextFieldType textFT = new TextFieldType("option");

        //Test for null
        textFT.setEfficientDefaultValue(null);

        Assertions.assertEquals("", ((JTextField) textFT.efficientComponent).getText());

        //Test for empty
        textFT.setEfficientDefaultValue("");

        Assertions.assertEquals("", ((JTextField) textFT.efficientComponent).getText());

        //Test for standard
        textFT.setEfficientDefaultValue("test");

        Assertions.assertEquals("test", ((JTextField) textFT.efficientComponent).getText());
    }
}