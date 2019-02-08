package osul;

import osul.swing.ClockResultButton;
import osul.swing.FileChooser;
import osul.swing.Frame;
import osul.swing.AssembleButton;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        main.startSwing();
    }

    private void startSwing() {
        FileChooser inputFile = new FileChooser(FileChooser.Type.OPEN);
        FileChooser outputFile = new FileChooser(FileChooser.Type.SAVE);

        Frame frame = new Frame();
        frame.add(new JLabel("Input:"));
        frame.add(inputFile);

        frame.add(new JLabel("Output:"));
        frame.add(outputFile);

        AssembleButton assembleButton = new AssembleButton(inputFile, outputFile);
        ClockResultButton clockResultButton = new ClockResultButton(assembleButton, outputFile);

        frame.add(assembleButton);
        frame.add(clockResultButton);


        frame.setVisible(true);

    }
}
