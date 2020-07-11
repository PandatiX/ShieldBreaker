package com.shieldbreaker.bot;

public class BotThread {
    private final Bot bot;
    private final Thread thread;

    public BotThread(Bot bot, Thread thread) {
        this.bot = bot;
        this.thread = thread;
    }

    public Bot getBot() {
        return bot;
    }
    public Thread getThread() {
        return thread;
    }
}