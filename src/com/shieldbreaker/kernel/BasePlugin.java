package com.shieldbreaker.kernel;

import com.shieldbreaker.bot.Bot;
import com.shieldbreaker.bot.BotManager;
import com.shieldbreaker.cli.BaseParametersManager;

/**
 * Interface to create a plugin descriptor.
 */
public interface BasePlugin {
    /**
     * Get the plugin name.
     *
     * @return the plugin name.
     */
    String getName();

    /**
     * Get the plugin bot class.
     *
     * @return the plugin bot class.
     */
    Class<? extends Bot> getClassBot();

    /**
     * Get the plugin bot manager class.
     *
     * @return the plugin bot manager class.
     */
    Class<? extends BotManager> getClassBotManager();

    /**
     * Get the plugin bot parameters manager class.
     *
     * @return the plugin bot parameters manager class.
     */
    Class<? extends BaseParametersManager> getClassParametersManager();
}