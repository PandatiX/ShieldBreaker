package com.shieldbreaker.cli;

import com.shieldbreaker.cli.exceptions.UnsupportedTypeException;
import com.shieldbreaker.cli.type.TextFieldType;
import org.apache.commons.cli.Option;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BaseParametersManagerTest {
    private static class DemoPluginParametersManager extends BaseParametersManager {
        public static final Option o = new Option("o", "option", true, "");

        public DemoPluginParametersManager() {}

        @Override
        protected void createCliOptions() throws UnsupportedTypeException {
            addOption(o, ParametersManager.TYPE.TEXT_FIELD);
        }

        @Override
        protected void checkParameters() {}
    }

    @Test
    void addOption() {
        DemoPluginParametersManager demoPPM = new DemoPluginParametersManager();

        //Test for double option (-> OVP) add
        Assertions.assertThrows(IllegalArgumentException.class, () -> demoPPM.addOption(DemoPluginParametersManager.o, ParametersManager.TYPE.TEXT_FIELD));
    }

    @Test
    void getOptions() {
        DemoPluginParametersManager demoPPM = new DemoPluginParametersManager();
        OptionValueParameter ovp = new OptionValueParameter(DemoPluginParametersManager.o, "", new TextFieldType(DemoPluginParametersManager.o.getLongOpt()));

        Assertions.assertEquals(ovp, demoPPM.getOptions().get(0));
    }

    @Test
    void getValue() {
        DemoPluginParametersManager demoPPM = new DemoPluginParametersManager();

        Assertions.assertEquals("", demoPPM.getValue("option"));
    }

    @Test
    void getKeys() {
        DemoPluginParametersManager demoPPM = new DemoPluginParametersManager();

        Assertions.assertEquals("option", demoPPM.getKeys().get(0));
    }

    @Test
    void setValue() {
        DemoPluginParametersManager demoPPM = new DemoPluginParametersManager();
        String value = demoPPM.getValue("option");

        demoPPM.setValue("option", " ");

        Assertions.assertNotEquals(value, demoPPM.getValue("option"));
    }
}