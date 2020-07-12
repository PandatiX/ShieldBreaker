package com.shieldbreaker.bot;

import com.shieldbreaker.cli.ParametersManager;
import com.shieldbreaker.kernel.ShieldBreaker;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Semaphore;

/**
 * Class to manage bots (start, join, progress & found).
 */
public abstract class BotManager {
    /**
     * The ShieldBreaker instance.
     */
    protected final ShieldBreaker shieldBreaker;

    /**
     * The ShieldBreaker's parameters manager.
     */
    protected final ParametersManager parametersManager;

    /**
     * The bot manager's bots.
     */
    protected BotThread[] bots;

    private final Semaphore semaphoreBots;
    private volatile boolean found;
    private final Semaphore semaphoreFound;
    private int progressMax;
    private volatile int doneProgress;

    /**
     * The bot manager constructor.
     */
    public BotManager() {
        shieldBreaker = ShieldBreaker.getInstance();
        parametersManager = shieldBreaker.getParametersManager();
        semaphoreBots = new Semaphore(1);
        semaphoreFound = new Semaphore(1);

        progressMax = 1;
    }

    /**
     * Display the start indication.
     */
    public abstract void displayStart();

    /**
     * Start bots.
     *
     * @param botClass bot class to instantiate.
     */
    public void startBots(Class<? extends Bot> botClass) {
        displayStart();

        int nbBots = parametersManager.getNbThreads();

        try {
            semaphoreBots.acquire();
            bots = new BotThread[nbBots];
        } catch (InterruptedException ignored) {
        } finally {
            semaphoreBots.release();
        }
        try {
            semaphoreFound.acquire();
            found = false;
        } catch (InterruptedException ignored) {
        } finally {
            semaphoreFound.release();
        }

        try {
            for (int i = 0; i < nbBots; i++) {
                startBot(botClass);
            }
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            shieldBreaker.err("A fatal error occurred while starting bots.", ShieldBreaker.OUT_PRIORITY.IMPORTANT);
            System.exit(1);
        }
    }

    /**
     * Set the max process value.
     *
     * @param max maximal value.
     */
    public synchronized void setProgressMax(int max) {
        progressMax = max > 0 ? max : 1;
        doneProgress = Math.min(doneProgress, progressMax);
    }

    /**
     * Set if bot has found.
     * Can't be used by an unknown bot (thread).
     *
     * @param found set working status.
     */
    public void setFound(boolean found) {
        //Check if the calling thread is one of the bot
        Thread currThread = Thread.currentThread();
        try {
            semaphoreBots.acquire();
            for (BotThread botThread : bots) {
                if (!currThread.equals(botThread.getThread())) {
                    return;
                }
            }
        } catch (InterruptedException ignored) {
        } finally {
            semaphoreBots.release();
        }

        try {
            semaphoreFound.acquire();
            this.found = found;
            if (found) {
                doneProgress = progressMax;
                //Join all threads
                try {
                    semaphoreBots.acquire();
                    BotThread botThread;
                    for (int i = 0; i < bots.length; i++) {
                        botThread = bots[i];
                        try {
                            if (currThread.equals(botThread.getThread())) {
                                //TODO find a way to join this thread: can't join his own thread
                            } else {
                                botThread.getThread().join();
                            }
                        } catch (InterruptedException e) {
                            System.err.println("Failed to join " + botThread.getBot().getClass() + ".");
                        } finally {
                            bots[i] = null;
                        }
                    }
                } catch (InterruptedException ignored) {
                } finally {
                    semaphoreBots.release();
                }
            }
        } catch (InterruptedException ignored) {
        } finally {
            semaphoreFound.release();
        }
    }

    /**
     * Update progress state.
     * Alert if progress reaches maximal value.
     */
    public synchronized void doneCheck() {
        doneProgress++;
        if (doneProgress == progressMax)
            shieldBreaker.out("Finished running", ShieldBreaker.OUT_PRIORITY.IMPORTANT);
    }

    /**
     * Get the found state.
     *
     * @return the found state.
     */
    public synchronized boolean isFound() {
        return found;
    }

    /**
     * Get the progress state.
     *
     * @return the progress state.
     */
    public int getProgress() {
        return progressMax == 0 ? 0 : 100 * doneProgress / progressMax;
    }

    /**
     * Start a bot and saves it.
     *
     * @param botClass bot class to instantiate.
     *
     * @return the started bot.
     *
     * @throws IllegalAccessException    failed to access a bot method.
     * @throws InstantiationException    failed to instantiate the bot.
     * @throws NoSuchMethodException     failed to find a bot method.
     * @throws InvocationTargetException
     */
    protected Bot startBot(Class<? extends Bot> botClass) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Constructor<? extends Bot> constructor = botClass.getDeclaredConstructor(BotManager.class);
        constructor.setAccessible(true);
        Bot bot = constructor.newInstance(this);
        Thread thread = new Thread(bot);
        try {
            semaphoreBots.acquire();
            //Add the new BotThread
            for (int i = 0; i < bots.length; i++) {
                if (bots[i] == null) {
                    bots[i] = new BotThread(bot, thread);
                    break;
                }
            }
        } catch (InterruptedException ignored) {
        } finally {
            semaphoreBots.release();
        }
        thread.start();
        return bot;
    }
}