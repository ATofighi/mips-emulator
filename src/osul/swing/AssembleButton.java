package osul.swing;

import osul.Emulator;
import osul.parser.ParseException;

import javax.swing.*;
import java.io.FileNotFoundException;

public class AssembleButton extends JButton {
    private FileChooser outputFile;
    private Emulator emulator = null;
    private Editor editor;

    public AssembleButton(Editor editor, FileChooser outputFile) {
        super("Assemble");
        this.editor = editor;
        this.outputFile = outputFile;

        addActionListener((event) -> {
            this.emulator = new Emulator(editor, outputFile);
            try {
                emulator.assemble();
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(getParent(), e.toString(), "ERROR!", JOptionPane.ERROR_MESSAGE);
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(getParent(), e.toString(), "ERROR!", JOptionPane.ERROR_MESSAGE);
            }

        });
    }

    public Emulator getEmulator() {
        return emulator;
    }
}
