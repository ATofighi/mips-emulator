package osul.swing;

import javax.swing.*;

public class Frame extends JFrame {
    public Frame() {
        setTitle("MIPS osul.Emulator");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(800-15, 600-20);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
    }
}
