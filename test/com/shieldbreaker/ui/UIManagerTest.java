package com.shieldbreaker.ui;

import com.shieldbreaker.BenchTest;
import com.shieldbreaker.kernel.ShieldBreaker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.*;

class UIManagerTest {
    @Test
    void terminalUpdating() {
        BenchTest benchTest = new BenchTest();

        //benchTest.setupPluginBenchTest();

        String[] args = new String[]{""};
        ShieldBreaker.createInstance(args);
        UIManager uiManager = new UIManager(ShieldBreaker.getInstance());

        //Test default value
        Assertions.assertNull(uiManager.getTerminal());

        //Test terminal opening
        uiManager.openTerminal();

        Assertions.assertNotNull(uiManager.getTerminal());

        //Test terminal closing
        uiManager.closeTerminal();

        Assertions.assertNull(uiManager.getTerminal());

        //benchTest.setdownPluginBenchTest();
    }

    @Test
    void createCenteredJLabel() {
        String LABEL_NAME = "LABEL_NAME";
        JLabel label = UIManager.createCenteredJLabel(LABEL_NAME);

        Assertions.assertEquals(LABEL_NAME, label.getText());
        Assertions.assertEquals(JLabel.CENTER, label.getHorizontalAlignment());
    }
}