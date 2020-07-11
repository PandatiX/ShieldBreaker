package com.shieldbreaker.bot;

import com.shieldbreaker.BenchTest;
import com.shieldbreaker.kernel.ShieldBreaker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.BrokenBarrierException;

class BotManagerTest {
    private final BenchTest benchTest;

    public BotManagerTest() {
        benchTest = new BenchTest();
    }

    @Test
    void startBots() {
        //benchTest.setupPluginBenchTest();

        String[] args = new String[]{""};
        ShieldBreaker.createInstance(args);
        DemoPlugin.DemoBotManager demoBM = new DemoPlugin.DemoBotManager();

        //Test for no bot
        demoBM.startBots(DemoPlugin.DemoBot.class);
        Assertions.assertEquals(1, demoBM.bots.length);

        //Test for multiple bots
        ShieldBreaker.getInstance().getParametersManager().setNBTHREADS(2);
        demoBM.startBots(DemoPlugin.DemoBot.class);
        Assertions.assertEquals(2, demoBM.bots.length);

        //benchTest.setdownPluginBenchTest();
    }

    @Test
    void setProgressMax() {
        //benchTest.setupPluginBenchTest();

        String[] args = new String[]{""};
        ShieldBreaker.createInstance(args);
        DemoPlugin.DemoBotManager demoBM = new DemoPlugin.DemoBotManager();

        demoBM.setProgressMax(2);
        demoBM.doneCheck();
        Assertions.assertEquals(50, demoBM.getProgress());  // 100*1/2 = 50

        demoBM.setProgressMax(5);
        demoBM.doneCheck();
        Assertions.assertEquals(40, demoBM.getProgress());  // 100*2/5 = 20

        demoBM.setProgressMax(0);                                    //New value is not possible => reset to 1
        Assertions.assertEquals(100, demoBM.getProgress()); //100*1/1 = 100

        //benchTest.setdownPluginBenchTest();
    }

    @Test
    void setFound() {
        //benchTest.setupPluginBenchTest();

        String[] args = new String[]{""};
        ShieldBreaker.createInstance(args).getParametersManager().setNBTHREADS(1); //Set test bench default nbThreads
        DemoPlugin.DemoBotManager demoBM = new DemoPlugin.DemoBotManager();

        demoBM.startBots(DemoPlugin.DemoBot.class);

        //Test for a not granted thread, in the default case
        Assertions.assertFalse(demoBM.isFound());
        Assertions.assertEquals(0, demoBM.getProgress());
        Assertions.assertEquals(1, demoBM.bots.length);

        //Setup test bench conditions
        ShieldBreaker.getInstance().getParametersManager().setNBTHREADS(1);
        demoBM.startBots(DemoPlugin.DemoBot.class);
        demoBM.setProgressMax(1);

        //Test for a not granted thread
        demoBM.setFound(true);
        Assertions.assertFalse(demoBM.isFound());
        Assertions.assertEquals(0, demoBM.getProgress());

        //Test for granted thread
        try {
            DemoPlugin.DemoBot bot = (DemoPlugin.DemoBot)demoBM.bots[0].getBot();
            bot.setSignal(DemoPlugin.DemoBot.SIGNAL.SET_FOUND_TRUE);  //Send signal to bot (on his thread) to set the found value
            bot.actionDoneBarrier.await();
        } catch (InterruptedException | BrokenBarrierException ignored) {}
        Assertions.assertTrue(demoBM.isFound());
        Assertions.assertEquals(100, demoBM.getProgress());
        Assertions.assertArrayEquals(new BotThread[demoBM.bots.length], demoBM.bots);   //bots are joined and set to null

        //benchTest.setdownPluginBenchTest();
    }

    @Test
    void doneCheck() {
        //benchTest.setupPluginBenchTest();

        String[] args = new String[]{""};
        ShieldBreaker.createInstance(args);
        DemoPlugin.DemoBotManager demoBM = new DemoPlugin.DemoBotManager();

        demoBM.setProgressMax(1);
        Assertions.assertEquals(0, demoBM.getProgress());   //Not any check done before

        demoBM.doneCheck();
        Assertions.assertEquals(100, demoBM.getProgress()); //100/1 = 100

        //benchTest.setdownPluginBenchTest();
    }
}