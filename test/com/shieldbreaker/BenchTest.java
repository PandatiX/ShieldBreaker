package com.shieldbreaker;

import com.shieldbreaker.kernel.ConfigLoader;
import com.shieldbreaker.kernel.ShieldBreaker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BenchTest {
    //Plugins
    private final File pluginsDirectory;
    private final File tmpPluginsDirectory;

    //CFL
    public static final String TEST_PLUGINS_DIRECTORY = "testPlugins/";
    public static final String TEST_VERSION = "TEST_VERSION";

    private final File cflFile;
    private final File tmpCflFile;

    public BenchTest() {
        String[] args = new String[]{""};
        ConfigLoader cfl = ShieldBreaker.createInstance(args).getConfigLoader();

        //Plugins
        String pluginsDirPath = cfl.getPluginsDirectory();
        pluginsDirPath = pluginsDirPath.substring(0, pluginsDirPath.length() - 1);
        String tmpPluginsDirPath = pluginsDirPath + "_";

        pluginsDirectory = new File(pluginsDirPath);
        tmpPluginsDirectory = new File(tmpPluginsDirPath);

        //CFL
        String cflFileName = ConfigLoader.CONFIG_FILE_PATH;
        cflFileName = cflFileName.substring(0, cflFileName.length() - 5);
        String tmpCflFileName = cflFileName + "_.yaml";
        cflFileName += ".yaml";

        cflFile = new File(cflFileName);
        tmpCflFile = new File(tmpCflFileName);
    }

    private void failure(String caller) {
        System.err.println("CANNOT PROCEED TO " + caller + " TESTS.\nPlease manually retrieve previous configuration.");
        System.exit(1);
    }
    private void failureP() {
        failure("PLUGIN");
    }
    private void failureC() {
        failure("CONFIGLOADER");
    }

    private boolean deleteRecursively(File directory) {
        File[] dirFiles = directory.listFiles();
        if (dirFiles != null) {
            for (File file : dirFiles) {
                deleteRecursively(file);
            }
        }
        return directory.delete();
    }

    public void setupPluginBenchTest() {
        if (pluginsDirectory.exists()) {
            //Reset tmp dir
            if (tmpPluginsDirectory.exists() && !deleteRecursively(tmpPluginsDirectory)) {
                failureP();
            }
            //Move plugins in the tmp dir
            if (!pluginsDirectory.renameTo(tmpPluginsDirectory)) {
                failureP();
            }
        }
        //Create tmp dir
        if (!pluginsDirectory.mkdir()) {
            failureP();
        }

        //TODO compile DemoPlugin.java and drop it in the tmp dir
    }

    public void setdownPluginBenchTest() {
        //Move plugins in the real dir
        if (pluginsDirectory.exists()) {
            if (!deleteRecursively(pluginsDirectory) || !tmpPluginsDirectory.renameTo(pluginsDirectory)) {
                failureP();
            }
        } else {
            failureP();
        }
    }

    public void setupConfigLoaderBenchTest() {
        //Reset cfl file
        if (tmpCflFile.exists() && !tmpCflFile.delete()) {
            failureC();
        }
        //Move cfl file to tmp
        if (!cflFile.renameTo(tmpCflFile)) {
            failureC();
        }

        try {
            //Create tmp cfl file
            if (!cflFile.createNewFile()) {
                failureC();
            }
            //Write bench test cfl file
            BufferedWriter writer = new BufferedWriter(new FileWriter(cflFile));

            writer.write("pluginsDirectory: " + TEST_PLUGINS_DIRECTORY + "\n" +
                    "version: " + TEST_VERSION);

            writer.close();
        } catch (IOException ignored) {
            failureC();
        }
    }

    public void setdownConfigLoaderBenchTest() {
        //Delete tmp cfl file
        if (!cflFile.delete()) {
            failureC();
        }
        //Reset real cfl file
        if (tmpCflFile.exists() && !tmpCflFile.renameTo(cflFile)) {
            failureC();
        }
    }
}