package com.shieldbreaker.ui;

import com.shieldbreaker.cli.BaseParametersManager;
import com.shieldbreaker.cli.OptionValueParameter;
import com.shieldbreaker.cli.ParametersManager;
import com.shieldbreaker.cli.type.BaseType;
import com.shieldbreaker.cli.type.FileChooserType;
import com.shieldbreaker.cli.type.TextFieldType;
import com.shieldbreaker.kernel.ShieldBreaker;
import org.apache.commons.cli.Option;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class BaseParameterManagerPanel extends JPanel {

    private final ParametersManager parametersManager;
    private final BaseParametersManager bPM;
    private final Map<String, JComponent> options;

    public BaseParameterManagerPanel(ParametersManager parametersManager, BaseParametersManager bPM) {
        super(new GridBagLayout());

        this.parametersManager = parametersManager;
        this.bPM = bPM;
        this.options = new HashMap<>();

        setupUI();
    }

    public void setValues() {
        Set<Map.Entry<String, JComponent>> setOptions = options.entrySet();
        Iterator<Map.Entry<String, JComponent>> it = setOptions.iterator();
        Map.Entry<String, JComponent> e;
        JComponent component;
        String value;
        while (it.hasNext()) {
            e = it.next();
            component = e.getValue();
            if (component instanceof JTextField) {
                value = ((JTextField) component).getText();
            } else if (component instanceof JFileChooser) {
                value = ((JFileChooser) component).getSelectedFile().getAbsolutePath();
            } else continue;

            parametersManager.setValue(e.getKey(), value);
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
        java.util.List<OptionValueParameter> ovt = bPM.getOptions();
        Option o;
        String longOpt;
        JComponent component;
        JLabel label;
        for (OptionValueParameter _ovp : ovt) {
            o = _ovp.getOption();
            longOpt = o.getLongOpt();

            //Build label
            label = new JLabel(camelCaseToReadable(longOpt));
            label.setToolTipText(o.getDescription());
            c.gridx = 0;
            add(label, c);

            //Build type field
            c.gridx = 1;
            component = _ovp.getType().newComponent(parametersManager.getValue(longOpt));
            add(component, c);

            //Create the link longOpt-field
            options.put(longOpt, component);

            c.gridy++;
        }
    }

    private String camelCaseToReadable(String s) {
        StringBuilder sb = new StringBuilder();

        //Set first char
        char c = s.charAt(0);
        sb.append(Character.toUpperCase(c));

        //Set others char
        int length = s.length();
        for (int i = 1; i < length; i++) {
            c = s.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(" ").append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

}
