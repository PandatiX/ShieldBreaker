package com.shieldbreaker.kernel;

import com.shieldbreaker.BenchTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ConfigLoaderTest {
    private final BenchTest benchTest;

    public ConfigLoaderTest() {
        benchTest = new BenchTest();
    }

    @Test
    void getPluginsDirectory() {
        benchTest.setupConfigLoaderBenchTest();

        ConfigLoader cfl = new ConfigLoader();

        Assertions.assertEquals(BenchTest.TEST_PLUGINS_DIRECTORY, cfl.getPluginsDirectory());

        benchTest.setdownConfigLoaderBenchTest();
    }

    @Test
    void getVersion() {
        benchTest.setupConfigLoaderBenchTest();

        ConfigLoader cfl = new ConfigLoader();

        Assertions.assertEquals(BenchTest.TEST_VERSION, cfl.getVersion());

        benchTest.setdownConfigLoaderBenchTest();
    }
}