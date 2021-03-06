package com.shieldbreaker.kernel;

import com.shieldbreaker.kernel.exceptions.PluginsEmptyException;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Class to load the plugins.
 */
public class PluginsLoader {
    private final List<Class<BasePlugin>> plugins;

    private String[] files;

    /**
     * The constructor.
     */
    public PluginsLoader() {
        plugins = new ArrayList<>();
    }

    /**
     * Set files to extract plugins from.
     *
     * @param files the files containing the plugins.
     */
    public void setFiles(String[] files) {
        this.files = files;
    }

    /**
     * Load the plugins from the set files.
     *
     * @return the plugins from the files.
     *
     * @throws IllegalAccessException failed to access a bot method.
     * @throws InstantiationException failed to instantiate the bot.
     * @throws ClassNotFoundException failed to found the plugin in files.
     * @throws IOException
     */
    public List<BasePlugin> loadPlugins() throws IllegalAccessException, InstantiationException, ClassNotFoundException, IOException {
        initializeLoader();

        List<BasePlugin> tmpPlugins = new ArrayList<>();

        for (Class<BasePlugin> plugin : plugins) {
            tmpPlugins.add(plugin.newInstance());
        }

        return tmpPlugins;
    }

    private void initializeLoader() throws ClassNotFoundException, IOException {
        //Check that files exists
        if (files == null || files.length == 0)
            throw new PluginsEmptyException();

        //Avoid double plugin load
        if (plugins.size() != 0)
            return;

        File[] f = new File[files.length];
        //To load plugins in memory
        URLClassLoader loader;
        String tmp;
        //For jar archive content
        Enumeration<JarEntry> enumeration;
        //To determine implemented interfaces
        Class<?> tmpClass;

        for (int i = 0; i < f.length; i++) {
            f[i] = new File(files[i]);

            if (!f[i].exists())
                break;

            //Prepare loader
            URI uri = f[i].toURI();
            loader = new URLClassLoader(new URL[]{uri.toURL()});

            //Load jar in memory
            JarFile jar = new JarFile(f[i].getAbsolutePath());

            //Retrieve jar content
            enumeration = jar.entries();

            while (enumeration.hasMoreElements()) {
                tmp = enumeration.nextElement().toString();

                //Check file is .class
                if (tmp.length() > 6 && tmp.endsWith(".class")) {

                    tmp = tmp.substring(0, tmp.length()-6);
                    tmp = tmp.replaceAll("/", ".");

                    tmpClass = Class.forName(tmp, true, loader);

                    int length = tmpClass.getInterfaces().length;
                    for (int y = 0; y < length; y++) {
                        if (tmpClass.getInterfaces()[y].getName().equals("com.shieldbreaker.kernel.BasePlugin")) {
                            plugins.add((Class<BasePlugin>) tmpClass);
                        }
                    }
                }
            }
        }
    }
}