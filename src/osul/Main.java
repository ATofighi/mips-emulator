package osul;

import osul.swing.*;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        main.startSwing();
    }

    private void startSwing() {
        FileChooser outputFile = new FileChooser(FileChooser.Type.SAVE, "Result File");
        Editor editor = new Editor(800, 600);

        FileChooser inputFile = new FileChooser(FileChooser.Type.OPEN, "Open", (f) -> {
            try {
                Scanner scanner = new Scanner(f);
                StringBuilder text = new StringBuilder();
                while(scanner.hasNextLine()) {
                    text.append(scanner.nextLine()).append("\n");
                }
                editor.getTextArea().setText(text.toString());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        Frame frame = new Frame();
        frame.add(editor);
        inputFile.setBounds(0, 500, 200, 50);
        frame.add(inputFile);

        outputFile.setBounds(200, 500, 200, 50);
        frame.add(outputFile);


        AssembleButton assembleButton = new AssembleButton(editor, outputFile);
        ClockResultButton clockResultButton = new ClockResultButton(assembleButton, outputFile);



        assembleButton.setBounds(400, 500, 200, 50);
        frame.add(assembleButton);


        clockResultButton.setBounds(600, 500, 182, 50);
        frame.add(clockResultButton);


        frame.setVisible(true);

    }
}
