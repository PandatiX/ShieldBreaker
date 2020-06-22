package com.shieldbreaker.bot;

import com.shieldbreaker.cli.ParametersManager;
import com.shieldbreaker.kernel.ShieldBreaker;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class BotManager {
    protected final ShieldBreaker shieldBreaker;
    protected final ParametersManager parametersManager;

    protected List<Bot> bots;
    private volatile boolean found;
    private int progressMax;
    private volatile int doneProgress;

    public BotManager() {
        shieldBreaker = ShieldBreaker.getInstance();
        parametersManager = shieldBreaker.getParametersManager();
    }

    public abstract void displayStart();

    public void startBots(Class<? extends Bot> botClass) {
        displayStart();

        System.out.println("SPY2");

        bots = new ArrayList<>();
        found = false;

        int nbBots = parametersManager.getNBTHREADS();
        try {
            for (int i = 0; i < nbBots; i++) {
                startBot(botClass);
            }
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            shieldBreaker.err("A fatal error occurred while starting bots.", ShieldBreaker.OUT_PRIORITY.IMPORTANT);
            System.exit(1);
        }
    }

    public synchronized void setProgressMax(int max) {
        progressMax = max > 0 ? max : 1;
        doneProgress = Math.min(doneProgress, progressMax);
    }

    public void setFound(boolean found) {
        this.found = found;
        if (found)
            doneProgress = progressMax;
    }

    public synchronized void doneCheck() {
        doneProgress++;
    }

    public boolean isFound() {
        return found;
    }

    public int getProgress() {
        return 100 * doneProgress / progressMax;
    }

    protected Bot startBot(Class<? extends Bot> botClass) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Constructor<? extends Bot> constructor = botClass.getDeclaredConstructor(BotManager.class);
        constructor.setAccessible(true);
        Bot bot = constructor.newInstance(this);
        this.bots.add(bot);
        new Thread(bot).start();
        return bot;
    }
}
