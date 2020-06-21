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

    protected List<String> passwords;
    protected List<Bot> bots;
    private volatile boolean found;
    private volatile int donePass;

    public BotManager() {
        shieldBreaker = ShieldBreaker.getInstance();
        parametersManager = shieldBreaker.getParametersManager();

        loadPasswords(parametersManager.getPASSLIST());
    }

    public abstract void displayStart();

    public void startBots(Class<? extends Bot> botClass) {
        displayStart();
        bots = new ArrayList<>();
        found = false;

        int passSize = passwords.size();
        int realNbThreads = Math.min(parametersManager.getNBTHREADS(), passSize);
        int width = passSize/realNbThreads;

        try {
            //Split passwords in ~equals sizes
            for (int i = 0; i < realNbThreads - 1; i++) {
                startBot(botClass, i*width, (i+1)*width);
            }
            startBot(botClass, (realNbThreads-1)*width, passSize);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            shieldBreaker.err("A fatal error occured while starting bots.", ShieldBreaker.OUT_PRIORITY.IMPORTANT);
            System.exit(1);
        }
    }

    public void setFound(boolean found) {
        this.found = found;
        donePass = passwords.size();
    }

    public synchronized void doneCheck() {
        donePass++;
    }

    public boolean isFound() {
        return found;
    }

    public int getProgress() {
        int passSize = passwords.size();
        return passSize > 0 ? 100 * donePass / passwords.size() : 100;
    }

    private void startBot(Class<? extends Bot> botClass, int inf, int sup) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Constructor<? extends Bot> constructor = botClass.getDeclaredConstructor(BotManager.class, List.class);
        constructor.setAccessible(true);
        Bot bot = constructor.newInstance(this, passwords.subList(inf, sup));
        this.bots.add(bot);
        new Thread(bot).start();
    }

    private void loadPasswords(String passlist) {
        passwords = new ArrayList<>();

        try {
            File f = new File(passlist);
            if (!f.exists())
                throw new FileNotFoundException();
            InputStream is = Files.newInputStream(f.toPath());
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = buf.readLine()) != null) {
                if (!passwords.contains(line) && !line.isEmpty())
                    passwords.add(line);
            }
        } catch (FileNotFoundException e) {
            shieldBreaker.err("The specified file to use as a passlist seems not to exists. Please check [" + passlist + "].", ShieldBreaker.OUT_PRIORITY.IMPORTANT);
        } catch (IOException e) {
            shieldBreaker.err("A fatal IOException was thrown while parsing " + passlist + ".", ShieldBreaker.OUT_PRIORITY.IMPORTANT);
        }
    }
}
