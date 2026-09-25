package main.java.Editor;

import main.java.Editor.window.EditorWindow;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            EditorWindow window =
                    new EditorWindow();

            window.setVisible(true);
        });
    }
}