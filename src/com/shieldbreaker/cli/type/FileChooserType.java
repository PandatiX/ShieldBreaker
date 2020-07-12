package com.shieldbreaker.cli.type;

import com.shieldbreaker.kernel.ShieldBreaker;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

/**
 * Class to dictates the GUI to build a FileChooser field.
 */
public class FileChooserType extends BaseType {
    /**
     * The FileChooser field JFileChooser.
     */
    protected JFileChooser jfc;

    /**
     * The constructor to set strategy parameters.
     *
     * @param multiSelection can select multiple files.
     * @param longOpt the option longOpt to display in the indicator field.
     * @param filter the FileChooser filter.
     */
    public FileChooserType(boolean multiSelection, String longOpt, FileNameExtensionFilter filter) {
        super();

        indicatorComponent = new JLabel(camelCaseToReadable(longOpt));

        efficientComponent = new JButton("Browse");
        jfc = new JFileChooser(System.getProperty("user.dir"));
        //Set default file name
        jfc.setSelectedFile(new File(""));
        //Avoid multi-selection if needed
        jfc.setMultiSelectionEnabled(multiSelection);
        //Setup filter
        if (filter != null) {
            jfc.setAcceptAllFileFilterUsed(false);
            jfc.addChoosableFileFilter(filter);
        }
        ((JButton) efficientComponent).addActionListener(actionEvent -> {
            //Select file
            if (jfc.showOpenDialog(ShieldBreaker.getInstance().getUIManager()) == JFileChooser.APPROVE_OPTION) {
                //Save file path
                ShieldBreaker.getInstance().getParametersManager().setValue(longOpt, jfc.getSelectedFile().getAbsolutePath());
            }
        });
    }

    @Override
    public void setEfficientDefaultValue(String defaultValue) {
        jfc.setSelectedFile(new File(defaultValue));
    }
}