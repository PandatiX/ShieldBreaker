package com.shieldbreaker.bot;

import com.shieldbreaker.cli.ParametersManager;
import com.shieldbreaker.kernel.ShieldBreaker;

public abstract class Bot implements Runnable {
    protected final ShieldBreaker shieldBreaker;
    protected final ParametersManager parametersManager;
    protected final BotManager manager;

    protected Bot(BotManager manager) {
        shieldBreaker= ShieldBreaker.getInstance();
        parametersManager = shieldBreaker.getParametersManager();
        this.manager = manager;
    }

    protected void doneCheck() {
        manager.doneCheck();
    }
}
