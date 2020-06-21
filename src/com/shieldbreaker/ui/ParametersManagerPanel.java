package com.shieldbreaker.ui;

import com.shieldbreaker.cli.ParametersManager;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class ParametersManagerPanel extends JPanel {

    private final ParametersManager parametersManager;
    private JTextField textFieldDomain;
    private JTextField textFieldCookie;
    private JTextField textFieldAccount;
    private JSpinner spinnerNbThreads;

    public ParametersManagerPanel(ParametersManager parametersManager) {
        super(new GridBagLayout());

        this.parametersManager = parametersManager;

        setupUI();
    }

    public String getDomain() {
        return textFieldDomain.getText();
    }
    public String getCookie() {
        return textFieldCookie.getText();
    }
    public String getAccount() {
        return textFieldAccount.getText();
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

        //Domain
        final JLabel labelDomain = com.shieldbreaker.ui.UIManager.createCenteredJLabel("Domain");
        labelDomain.setHorizontalAlignment(JLabel.CENTER);
        c.gridx = 0;
        c.gridy = 1;
        add(labelDomain, c);

        textFieldDomain = new JTextField(parametersManager.getDOMAIN());
        c.gridx = 1;
        c.gridy = 1;
        add(textFieldDomain, c);

        //Cookie
        final JLabel labelCookie = com.shieldbreaker.ui.UIManager.createCenteredJLabel("Cookie");
        c.gridx = 0;
        c.gridy = 2;
        add(labelCookie, c);

        textFieldCookie = new JTextField(parametersManager.getCOOKIE());
        c.gridx = 1;
        c.gridy = 2;
        add(textFieldCookie, c);

        //Account
        final JLabel labelAccount = com.shieldbreaker.ui.UIManager.createCenteredJLabel("Account");
        c.gridx = 0;
        c.gridy = 3;
        add(labelAccount, c);

        textFieldAccount = new JTextField(parametersManager.getACCOUNT());
        c.gridx = 1;
        c.gridy = 3;
        add(textFieldAccount, c);

        //Passlist
        final JLabel labelPasslist = com.shieldbreaker.ui.UIManager.createCenteredJLabel("Passlist");
        c.gridx = 0;
        c.gridy = 4;
        add(labelPasslist, c);

        final JButton buttonFileChooser = new JButton("FileChooser");
        c.gridx = 1;
        c.gridy = 4;
        buttonFileChooser.addActionListener(actionEvent -> {
            JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
            //Set default file name
            jfc.setSelectedFile(new File(parametersManager.getPASSLIST()));
            //Setup filter for TXT files
            jfc.setAcceptAllFileFilterUsed(false);
            jfc.addChoosableFileFilter(new FileNameExtensionFilter("TXT files", "txt"));
            //Select file
            if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                //Save file path
                parametersManager.setPASSLIST(jfc.getSelectedFile().getPath());
            }
        });
        add(buttonFileChooser, c);

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
