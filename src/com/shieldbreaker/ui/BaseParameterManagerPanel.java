package com.shieldbreaker.ui;

import com.shieldbreaker.cli.BaseParametersManager;
import com.shieldbreaker.cli.ParametersManager;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class BaseParameterManagerPanel extends JPanel {

    private final ParametersManager parametersManager;
    private final BaseParametersManager bPM;
    private final Map<String, JTextField> options;

    public BaseParameterManagerPanel(ParametersManager parametersManager, BaseParametersManager bPM) {
        super(new GridBagLayout());

        this.parametersManager = parametersManager;
        this.bPM = bPM;
        this.options = new HashMap<>();

        setupUI();
    }

    public void setValues() {
        Set<Map.Entry<String, JTextField>> setOptions = options.entrySet();
        Iterator<Map.Entry<String, JTextField>> it = setOptions.iterator();
        Map.Entry<String, JTextField> e;
        while (it.hasNext()) {
            e = it.next();
            parametersManager.setValue(e.getKey(), e.getValue().getText());
        }
    }

    private void setupUI() {
        this.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("PLUGIN PARAMETERS"),
                                BorderFactory.createEmptyBorder(5, 5, 5, 5)
                        ),
                        this.getBorder()
                )
        );

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.weighty = 0.8;
        c.ipadx = 10;
        c.ipady = 5;

        c.gridy = 0;
        //Set this.options values
        java.util.List<String> opt = bPM.getOptionsLongOpt();
        JLabel label;
        JTextField textField;
        for (String o : opt) {
            //Avoid displaying protocol again
            if (o.contains("protocol"))
                continue;

            //Build label
            label = new JLabel(o);
            c.gridx = 0;
            add(label, c);

            //Build text field
            textField = new JTextField(parametersManager.getValue(o));
            c.gridx = 1;
            add(textField, c);

            //Create the link
            options.put(o, textField);

            c.gridy++;
        }
    }

}
