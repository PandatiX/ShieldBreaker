package com.shieldbreaker.bot;

import com.shieldbreaker.cli.BaseParametersManager;
import com.shieldbreaker.cli.ParametersManager;
import com.shieldbreaker.cli.exceptions.UnsupportedTypeException;
import com.shieldbreaker.kernel.BasePlugin;
import org.apache.commons.cli.Option;

import java.util.concurrent.Semaphore;

public class DemoPlugin implements BasePlugin {
    public static final String NAME = "DEMO_NAME";
    public static final Class<? extends Bot> BOT = DemoBot.class;
    public static final Class<? extends BotManager> BOTMANAGER = DemoBotManager.class;
    public static final Class<? extends BaseParametersManager> BOTPARAMETERSMANAGER = DemoParametersManager.class;

    public static class DemoBot extends Bot {
        //Used to simulate manager.setFound real use case from another thread
        public enum SIGNAL {
            STOP,
            SET_FOUND_TRUE,
            NULL
        }
        private SIGNAL signal;
        private final Semaphore signalSemaphore;
        private final Semaphore actionSemaphore;

        public DemoBot(BotManager manager) {
            super(manager);

            signal = SIGNAL.NULL;
            signalSemaphore = new Semaphore(1);
            actionSemaphore = new Semaphore(0);
        }
        @Override
        public void run() {
            while (true) {
                try {
                    signalSemaphore.acquire();
                    switch (signal) {
                        case STOP:
                            return;
                        case SET_FOUND_TRUE:
                            manager.setFound(true);
                            actionSemaphore.release();
                            signal = SIGNAL.NULL;
                            break;
                        case NULL:
                        default:
                            break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    signalSemaphore.release();
                }
            }
        }
        public void setSignal(SIGNAL signal) {
            try {
                signalSemaphore.acquire();
                this.signal = signal;
            } catch (InterruptedException ignored) {
            } finally {
                signalSemaphore.release();
            }
        }
        public void acquire() throws InterruptedException {
            actionSemaphore.acquire();
        }
    }
    public static class DemoBotManager extends BotManager {
        public DemoBotManager() {
            super();
        }
        @Override
        public void displayStart() {}
    }
    public static class DemoParametersManager extends BaseParametersManager {
        public DemoParametersManager() {
            super();
        }
        @Override
        protected void createCliOptions() throws UnsupportedTypeException {
            Option o = new Option("o", "option", true, "Option description");
            addOption(o, ParametersManager.TYPE.TEXT_FIELD);
        }
        @Override
        protected void checkParameters() {}
    }
    @Override
    public String getName() {
        return NAME;
    }
    @Override
    public Class<? extends Bot> getClassBot() {
        return DemoBot.class;
    }
    @Override
    public Class<? extends BotManager> getClassBotManager() {
        return DemoBotManager.class;
    }
    @Override
    public Class<? extends BaseParametersManager> getClassParametersManager() {
        return DemoParametersManager.class;
    }
}