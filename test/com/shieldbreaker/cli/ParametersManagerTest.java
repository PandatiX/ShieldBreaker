package com.shieldbreaker.cli;

import com.shieldbreaker.cli.exceptions.UnsupportedTypeException;
import org.apache.commons.cli.Option;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ParametersManagerTest {
    private static class DemoPluginParametersManager extends BaseParametersManager {
        public DemoPluginParametersManager() {}

        @Override
        protected void createCliOptions() throws UnsupportedTypeException {
            Option option = new Option("o", "option", true, "Demo option");
            addOption(option, ParametersManager.TYPE.TEXT_FIELD);
        }

        @Override
        protected void checkParameters() {}
    }

    @Test
    void getNBTHREADS() {
        ParametersManager parametersManager = buildDemoParametersManager(new String[]{"-t", "5"});

        Assertions.assertEquals(5, parametersManager.getNBTHREADS());
    }

    @Test
    void getValue() {
        ParametersManager parametersManager = buildDemoParametersManager(new String[]{"-o", "test"});

        Assertions.assertEquals("test", parametersManager.getValue("option"));
    }

    @Test
    void setNBTHREADSString() {
        ParametersManager parametersManager = buildDemoParametersManager(new String[]{});

        parametersManager.setNBTHREADS("2");
        Assertions.assertEquals(2, parametersManager.getNBTHREADS());
    }

    @Test
    void setNBTHREADSInt() {
        ParametersManager parametersManager = buildDemoParametersManager(new String[]{});

        parametersManager.setNBTHREADS(2);
        Assertions.assertEquals(2, parametersManager.getNBTHREADS());
    }

    @Test
    void setValue() {
        ParametersManager parametersManager = buildDemoParametersManager(new String[]{"-o", "test"});
        String newValue = "TEST";

        parametersManager.setValue("option", newValue);
        Assertions.assertEquals(newValue, parametersManager.getValue("option"));
    }

    private ParametersManager buildDemoParametersManager(String[] args) {
        ParametersManager parametersManager = new ParametersManager(args, false);

        DemoPluginParametersManager demoPPM = new DemoPluginParametersManager();
        parametersManager.setPluginParametersManager(demoPPM);

        parametersManager.parseArgs();

        return parametersManager;
    }
}