package com.shieldbreaker.cli.type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BaseTypeTest {
    @Test
    void camelCaseToReadable() {
        String expected = "Camel case wor#ing";
        String actualToTest = "camelCaseWor#ing";

        Assertions.assertEquals(expected, BaseType.camelCaseToReadable(actualToTest));
    }
}