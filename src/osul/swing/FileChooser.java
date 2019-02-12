package osul.swing;

import javax.swing.*;
import java.io.File;

public class FileChooser extends JButton {
    File file = null;

    public enum Type {
        OPEN, SAVE
    }

    public FileChooser(Type type, String label) {
        this(type, label, (f) -> {
        });
    }

    public FileChooser(Type type, String label, FileSelctedHandler a) {
        super(label);
        addActionListener((event) -> {
            file = null;
            JFileChooser fileChooser = new JFileChooser();
            int returnVal;
            if (type == Type.OPEN) {
                returnVal = fileChooser.showOpenDialog(getParent());
            } else {
                returnVal = fileChooser.showSaveDialog(getParent());
            }
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
                a.action(file);
            }
        });
    }

    public File getFile() {
        return file;
    }

    public interface FileSelctedHandler {
        void action(File f);
    }
}
