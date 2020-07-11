package com.shieldbreaker.cli.type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

class FileChooserTypeTest {
    @Test
    void setEfficientDefaultValue() {
        String newFilePath = "tmp.txt";
        FileChooserType fileCT = new FileChooserType(false, "option", null);

        Assertions.assertNotEquals(newFilePath, fileCT.jfc.getSelectedFile());

        fileCT.setEfficientDefaultValue(newFilePath);
        Assertions.assertEquals(new File(newFilePath), fileCT.jfc.getSelectedFile());
    }
}