package osul.swing;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class ClockResultButton extends JButton {
    public ClockResultButton(AssembleButton assembleButton, FileChooser outputFile) {
        super("Get Result");
        addActionListener((event) -> {
            if (assembleButton.getEmulator() == null) {
                JOptionPane.showMessageDialog(getParent(), "Please run program before get data", "Error!", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    PrintStream printStream = new PrintStream(outputFile.getFile());
                    while (true) {
                        String clockNumberS = JOptionPane.showInputDialog(getParent(), "Please enter clock number");
                        int clockNumber;
                        try {
                            clockNumber = Integer.parseInt(clockNumberS);
                        } catch (Exception e) {
                            break;
                        }
                        for (int i = assembleButton.getEmulator().getClock(); i < clockNumber; i++) {
                            assembleButton.getEmulator().clock();
                        }

                        printStream.println(assembleButton.getEmulator().getResult(clockNumber));
                        System.out.println(clockNumber);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
