package com.shieldbreaker.bot;

import com.shieldbreaker.cli.ParametersManager;
import com.shieldbreaker.kernel.ShieldBreaker;

import java.util.List;

public abstract class Bot implements Runnable {
    protected final ShieldBreaker shieldBreaker;
    protected final ParametersManager parametersManager;
    protected final BotManager manager;
    protected final List<String> passwords;

    protected Bot(BotManager manager, List<String> passwords) {
        shieldBreaker= ShieldBreaker.getInstance();
        parametersManager = shieldBreaker.getParametersManager();
        this.manager = manager;
        this.passwords = passwords;
    }

    protected void doneCheck() {
        manager.doneCheck();
    }
}
