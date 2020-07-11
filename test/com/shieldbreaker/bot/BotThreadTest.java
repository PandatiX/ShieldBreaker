package com.shieldbreaker.bot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BotThreadTest {
    @Test
    void getBot() {
        Bot bot = new DemoPlugin.DemoBot(new DemoPlugin.DemoBotManager());
        BotThread botThread = new BotThread(bot, new Thread());

        Assertions.assertEquals(bot, botThread.getBot());
    }

    @Test
    void getThread() {
        Thread thread = new Thread();
        BotThread botThread = new BotThread(new DemoPlugin.DemoBot(new DemoPlugin.DemoBotManager()), thread);

        Assertions.assertEquals(thread, botThread.getThread());
    }
}