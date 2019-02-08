package osul.swing;

import osul.Emulator;
import osul.parser.ParseException;

import javax.swing.*;
import java.io.FileNotFoundException;

public class AssembleButton extends JButton {
    private FileChooser inputFile;
    private FileChooser outputFile;
    private Emulator emulator = null;

    public AssembleButton(FileChooser inputFile, FileChooser outputFile) {
        super("Assemble");
        this.inputFile = inputFile;
        this.outputFile = outputFile;

        addActionListener((event) -> {
            if (inputFile.getFile() == null || !inputFile.getFile().canRead()) {
                JOptionPane.showMessageDialog(getParent(), "Input file is not correct!", "Error!", JOptionPane.ERROR_MESSAGE);
//            } else if (!outputFile.getFile().canWrite()) {
//                JOptionPane.showMessageDialog(getParent(), "Output file is not correct!", "Error!", JOptionPane.ERROR_MESSAGE);
            } else {
                this.emulator = new Emulator(inputFile, outputFile);
                try {
                    emulator.assemble();
                } catch (FileNotFoundException e) {
                    JOptionPane.showMessageDialog(getParent(), e.toString(), "ERROR!", JOptionPane.ERROR_MESSAGE);
                } catch (ParseException e) {
                    JOptionPane.showMessageDialog(getParent(), e.toString(), "ERROR!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public Emulator getEmulator() {
        return emulator;
    }
}
