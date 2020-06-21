package com.shieldbreaker.ui;

import com.shieldbreaker.cli.BaseParametersManager;
import com.shieldbreaker.kernel.ConfigLoader;
import com.shieldbreaker.kernel.ShieldBreaker;
import com.sun.istack.internal.NotNull;
import com.shieldbreaker.cli.ParametersManager;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;

public class UIManager extends JFrame {
    public static final String NAME = "ShieldBreaker (" + ConfigLoader.getInstance().getVersion() + ")";

    private final ShieldBreaker parent;
    private final ParametersManager parametersManager;

    private BaseParametersManager bPM;
    private BaseParameterManagerPanel bPMP;
    private UITerminal terminal;

    public UIManager(@NotNull ShieldBreaker parent) {
        super(NAME);
        this.parent = parent;
        this.parametersManager = parent.getParametersManager();

        try {
            bPM = parent.getPlugin().getClassParametersManager().newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            System.err.println("Failed to load plugin's parameters manager. Cannot go further.");
            System.exit(1);
        }

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

        setupUI();
    }

    public void dialogMessage(String message) {
        Toolkit.getDefaultToolkit().beep();
        JOptionPane.showMessageDialog(this, message);
    }

    public UITerminal getTerminal() {
        return terminal;
    }

    public void errorDialogMessage(String message) {
        dialogMessage("[ERROR] " + message + "\nCannot finish.");
    }

    public void terminalIsClosed() {
        terminal = null;
    }

    private void setupUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.ipadx = 10;
        c.ipady = 5;
        c.gridwidth = 1;
        c.gridheight = 1;

        //Create ShieldBreaker parameters panel
        final ParametersManagerPanel parametersManagerPanel = new ParametersManagerPanel(parametersManager);
        c.gridx = 1;
        c.gridy = 0;
        add(parametersManagerPanel, c);

        //Create plugin's parameters panel
        c.gridx = 0;
        c.gridy = 0;
        bPMP = new BaseParameterManagerPanel(parametersManager, bPM);
        add(bPMP, c);

        //Start button
        final JButton start = new JButton("START");
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.gridheight = 1;
        start.addActionListener(actionEvent -> {
            parametersManager.setDOMAIN(parametersManagerPanel.getDomain());
            parametersManager.setCOOKIE(parametersManagerPanel.getCookie());
            parametersManager.setACCOUNT(parametersManagerPanel.getAccount());
            parametersManager.setNBTHREADS(parametersManagerPanel.getNbThreads());

            bPMP.setValues();

            if (terminal != null)
                terminal.close();
            terminal = new UITerminal( this);
            parent.startBotManager();
        });
        add(start, c);

        //End ui setup
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static JLabel createCenteredJLabel(String label) {
        JLabel l = new JLabel(label);
        l.setHorizontalAlignment(JLabel.CENTER);
        return l;
    }
}