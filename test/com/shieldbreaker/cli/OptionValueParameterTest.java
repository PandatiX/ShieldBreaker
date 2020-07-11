package com.shieldbreaker.cli;

import com.shieldbreaker.cli.type.TextFieldType;
import org.apache.commons.cli.Option;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OptionValueParameterTest {
    @Test
    void getOption() {
        Option o = new Option("t", "test", true, "Test option");
        OptionValueParameter ovp = new OptionValueParameter(
                o,
                "test",
                new TextFieldType("test")
        );
        Assertions.assertEquals(o, ovp.getOption());
    }

    @Test
    void getValue() {
        String value = "test";
        OptionValueParameter ovp = new OptionValueParameter(
                new Option("t", "test", true, "Test option"),
                value,
                new TextFieldType("test")
        );

        Assertions.assertEquals(value, ovp.getValue());
    }

    @Test
    void getType() {
        TextFieldType type = new TextFieldType("test");
        OptionValueParameter ovp = new OptionValueParameter(
                new Option("t", "test", true, "Test option"),
                "test",
                type
        );

        Assertions.assertEquals(type, ovp.getType());
    }

    @Test
    void setValue() {
        String newValue = "TEST";
        OptionValueParameter ovp = new OptionValueParameter(
                new Option("t", "test", true, "Test option"),
                "test",
                new TextFieldType("test")
        );
        ovp.setValue(newValue);

        Assertions.assertEquals(newValue, ovp.getValue());
    }
}