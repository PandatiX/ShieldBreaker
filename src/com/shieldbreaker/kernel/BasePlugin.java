package com.shieldbreaker.kernel;

import com.shieldbreaker.bot.Bot;
import com.shieldbreaker.bot.BotManager;
import com.shieldbreaker.cli.BaseParametersManager;

public interface BasePlugin {
    String getName();
    Class<? extends Bot> getClassBot();
    Class<? extends BotManager> getClassBotManager();
    Class<? extends BaseParametersManager> getClassParametersManager();
}