package com.shieldbreaker.ui;

import com.shieldbreaker.cli.ParametersManager;

import javax.swing.*;
import java.awt.*;

public class ParametersManagerPanel extends JPanel {

    private final ParametersManager parametersManager;
    private JSpinner spinnerNbThreads;

    public ParametersManagerPanel(ParametersManager parametersManager) {
        super(new GridBagLayout());

        this.parametersManager = parametersManager;

        setupUI();
    }

    public int getNbThreads() {
        return (int) spinnerNbThreads.getValue();
    }

    private void setupUI() {
        this.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("SHIELDBREAKER PARAMETERS"),
                                BorderFactory.createEmptyBorder(5, 5, 5, 5)
                        ),
                        this.getBorder()
                )
        );

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        //NbThreads
        final JLabel labelNbThreads = com.shieldbreaker.ui.UIManager.createCenteredJLabel("NbThreads");
        c.gridx = 0;
        c.gridy = 5;
        add(labelNbThreads, c);

        spinnerNbThreads = new JSpinner(new SpinnerNumberModel(parametersManager.getNBTHREADS(), 1, 999, 1));
        c.gridx = 1;
        c.gridy = 5;
        add(spinnerNbThreads, c);
    }

}
