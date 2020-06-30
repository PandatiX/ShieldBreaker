package com.shieldbreaker.cli.type;

import com.shieldbreaker.kernel.ShieldBreaker;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class FileChooserType extends BaseType {

    private final boolean multiSelection;
    private final String longOpt;
    private final FileNameExtensionFilter filter;

    public FileChooserType(boolean multiSelection, String longOpt, FileNameExtensionFilter filter) {
        this.multiSelection = multiSelection;
        this.longOpt = longOpt;
        this.filter = filter;
    }

    @Override
    public JComponent newComponent(String defaultValue) {
        //Creating button and file chooser
        JButton button = new JButton("Browse");
        button.addActionListener(actionEvent -> {
            JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
            //Set default file name
            jfc.setSelectedFile(new File(defaultValue));
            //Avoid multi-selection if needed
            jfc.setMultiSelectionEnabled(multiSelection);
            //Setup filter
            if (filter != null) {
                jfc.setAcceptAllFileFilterUsed(false);
                jfc.addChoosableFileFilter(filter);
            }
            //Select file
            if (jfc.showOpenDialog(ShieldBreaker.getInstance().getUIManager()) == JFileChooser.APPROVE_OPTION) {
                //Save file path
                ShieldBreaker.getInstance().getParametersManager().setValue(longOpt, jfc.getSelectedFile().getAbsolutePath());
            }
        });
        return button;
    }
}