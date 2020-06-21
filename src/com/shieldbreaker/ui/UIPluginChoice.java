package com.shieldbreaker.ui;

import com.shieldbreaker.kernel.BasePlugin;
import com.shieldbreaker.kernel.ShieldBreaker;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class UIPluginChoice extends JFrame implements Runnable {
    public static final String NAME = UIManager.NAME;

    private int indexSelectedPlugin;
    private final Semaphore semaphore;
    private final java.util.List<BasePlugin> plugins;

    public UIPluginChoice(java.util.List<BasePlugin> plugins) {
        super(NAME);
        semaphore = new Semaphore(0);
        this.plugins= plugins;

        //Setup app icon
        Toolkit kit = Toolkit.getDefaultToolkit();
        Image icon = kit.createImage(UIManager.class.getResource("/resources/images/icon.png"));
        setIconImage(icon);
        //Setup app name
        try {
            Field awtAppClassNameField = kit.getClass().getDeclaredField("awtAppClassName");
            awtAppClassNameField.setAccessible(true);
            awtAppClassNameField.set(kit, NAME);
        } catch (Exception e) {
            System.err.println(ShieldBreaker.RED + "[UIManager] Cannot set title" + ShieldBreaker.RESET);
        }
    }

    @Override
    public void run() {
        setupUI(plugins);
    }

    public int getIndexSelectedPlugin() throws InterruptedException {
        semaphore.acquire();
        return indexSelectedPlugin;
    }

    private void setupUI(java.util.List<BasePlugin> plugins) {
        setResizable(false);

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipadx = 10;
        c.ipady = 5;
        c.weightx = 1.0;
        c.weighty = 0.8;

        final JLabel beginLabel = UIManager.createCenteredJLabel("Found multiple plugins to work with.");
        c.gridx = 0;
        c.gridy = 0;
        add(beginLabel, c);

        final JLabel alertLabel = UIManager.createCenteredJLabel("Please select one to work with.");
        c.gridx = 0;
        c.gridy = 1;
        add(alertLabel, c);

        c.gridy++;
        final ButtonGroup buttonGroup = new ButtonGroup();
        final java.util.List<JRadioButton> radioButtons = new ArrayList<>();
        JRadioButton radioButton;
        for (BasePlugin plugin : plugins) {
            radioButton = new JRadioButton(plugin.getName());
            radioButton.setSelected(true);
            radioButtons.add(radioButton);
            buttonGroup.add(radioButton);

            add(radioButton, c);
            c.gridy++;
        }

        final JButton startButton = new JButton("START");
        startButton.addActionListener(actionEvent -> {
            //Get selected plugin by its index
            JRadioButton j;
            for (int i = 0; i < radioButtons.size(); i++) {
                j = radioButtons.get(i);
                if (j.isSelected()) {
                    setIndexSelectedPlugin(i);
                    break;
                }
            }
            //Close ui
            dispose();
        });
        add(startButton, c);

        pack();
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void setIndexSelectedPlugin(int index) {
        this.indexSelectedPlugin = index;
        semaphore.release();
    }
}
