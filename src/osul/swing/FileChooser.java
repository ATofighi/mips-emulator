package osul.swing;

import javax.swing.*;
import java.io.File;

public class FileChooser extends JButton {
    File file = null;

    public enum Type {
        OPEN, SAVE
    }

    public FileChooser(Type type) {
        super("Browse...");
        addActionListener((event) -> {
            file = null;
            JFileChooser fileChooser = new JFileChooser();
            int returnVal;
            if(type == Type.OPEN) {
                returnVal = fileChooser.showOpenDialog(getParent());
            } else {
                returnVal = fileChooser.showSaveDialog(getParent());
            }
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
            }
        });
    }

    public File getFile() {
        return file;
    }
}
