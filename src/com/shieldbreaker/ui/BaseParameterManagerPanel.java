package com.shieldbreaker.ui;

import com.shieldbreaker.cli.BaseParametersManager;
import com.shieldbreaker.cli.OptionValueParameter;
import com.shieldbreaker.cli.ParametersManager;
import com.shieldbreaker.cli.type.BaseType;
import org.apache.commons.cli.Option;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Panel to display plugin options.
 */
public class BaseParameterManagerPanel extends JPanel {
    private final ParametersManager parametersManager;
    private final BaseParametersManager bPM;
    private final Map<String, JComponent> options;

    /**
     * The constructor.
     *
     * @param parametersManager the main parameters manager.
     * @param bPM the plugin parameters manager.
     */
    public BaseParameterManagerPanel(ParametersManager parametersManager, BaseParametersManager bPM) {
        super(new GridBagLayout());

        this.parametersManager = parametersManager;
        this.bPM = bPM;
        this.options = new HashMap<>();

        setupUI();
    }

    /**
     * Set options values.
     */
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
        BaseType type;
        JComponent componentIndicator;
        JComponent componentEfficient;
        for (OptionValueParameter _ovp : ovt) {
            o = _ovp.getOption();
            longOpt = o.getLongOpt();
            type = _ovp.getType();

            //Build indicator field
            componentIndicator = type.getIndicatorComponent();
            componentIndicator.setToolTipText(o.getDescription());
            c.gridx = 0;
            add(componentIndicator, c);

            //Build type field
            c.gridx = 1;
            componentEfficient = type.getEfficientComponent(parametersManager.getValue(longOpt));
            add(componentEfficient, c);

            //Create the link longOpt-efficientField
            options.put(longOpt, componentEfficient);

            c.gridy++;
        }
    }
}