package com.shieldbreaker.bot;

import com.shieldbreaker.cli.ParametersManager;
import com.shieldbreaker.kernel.ShieldBreaker;

/**
 * Class to implement a payload.
 */
public abstract class Bot implements Runnable {
    /**
     * The ShieldBreaker instance.
     */
    protected final ShieldBreaker shieldBreaker;

    /**
     * The ShieldBreaker's parameters manager.
     */
    protected final ParametersManager parametersManager;

    /**
     * The bot's bot manager.
     */
    protected final BotManager manager;

    /**
     * The bot constructor.
     *
     * @param manager the bot's bot manager.
     */
    protected Bot(BotManager manager) {
        shieldBreaker= ShieldBreaker.getInstance();
        parametersManager = shieldBreaker.getParametersManager();
        this.manager = manager;
    }

    /**
     * Tell the manager the bot done a check.
     */
    protected void doneCheck() {
        manager.doneCheck();
    }
}