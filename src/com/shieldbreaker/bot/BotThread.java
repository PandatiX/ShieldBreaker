package com.shieldbreaker.bot;

/**
 * Class to link a Bot and its Thread.
 */
public class BotThread {
    private final Bot bot;
    private final Thread thread;

    /**
     * The constructor.
     * Set the bot and its associated thread.
     *
     * @param bot the bot.
     * @param thread the bot's thread.
     */
    public BotThread(Bot bot, Thread thread) {
        this.bot = bot;
        this.thread = thread;
    }

    /**
     * Get the bot.
     * @return the bot.
     */
    public Bot getBot() {
        return bot;
    }

    /**
     * Get the thread.
     * @return the thread.
     */
    public Thread getThread() {
        return thread;
    }
}