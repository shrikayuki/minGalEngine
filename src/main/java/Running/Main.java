package main.java.Running;

import main.java.Running.window.StartWindow;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            StartWindow window =
                    new StartWindow();

            window.setVisible(true);
        });
    }
}