package osul.swing;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;

public class Editor extends JPanel {
    private RSyntaxTextArea textArea;

    public Editor(int w, int h) {
        setLayout(new BorderLayout());
        setBounds(0, 0, w - 18, h - 100);
        textArea = new RSyntaxTextArea(20, 60);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_MAKEFILE);
        textArea.setCodeFoldingEnabled(true);
        RTextScrollPane sp = new RTextScrollPane(textArea);
        add(sp);
    }

    public RSyntaxTextArea getTextArea() {
        return textArea;
    }
}
