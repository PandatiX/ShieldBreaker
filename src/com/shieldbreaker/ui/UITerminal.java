package com.shieldbreaker.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * GUI to display text in a terminal.
 */
public class UITerminal extends JFrame {
    public static final String NAME = UIManager.NAME + " - Terminal";

    private final UIManager parent;

    private JTextArea textArea;

    /**
     * The constructor.
     *
     * @param parent the UI manager.
     */
    public UITerminal(UIManager parent) {
        super(NAME);
        this.parent = parent;

        setupUI();
    }

    /**
     * Display a string in the terminal.
     *
     * @param s the string do display.
     */
    public void out(String s) {
        textArea.append(s + "\r\n");
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    /**
     * Close the GUI terminal.
     */
    public void close() {
        setVisible(false);
        dispose();
    }

    private void setupUI() {
        setMinimumSize(new Dimension(480, 320));

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 0.8;

        textArea = new JTextArea(50, 10);
        textArea.setEditable(false);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        add(new JScrollPane(textArea), c);

        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                parent.closeTerminal();
            }
        });
    }
}