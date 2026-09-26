package main.java.Editor.panel;

import main.java.Editor.constant.EditorConstants;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.io.File;

public class StatusBarPanel extends JPanel {

    private final JLabel statusLabel;


    public StatusBarPanel() {

        setLayout(
                new BorderLayout()
        );

        setBackground(
                EditorConstants.PANEL
        );

        setBorder(
                new MatteBorder(
                        1,
                        0,
                        0,
                        0,
                        EditorConstants.BORDER
                )
        );


        statusLabel =
                new JLabel(
                        "  未打开项目"
                );

        statusLabel.setForeground(
                EditorConstants.TEXT_SECONDARY
        );


        JLabel version =
                new JLabel(
                        EditorConstants.APP_NAME
                                + "  "
                );

        version.setForeground(
                EditorConstants.TEXT_SECONDARY
        );


        add(
                statusLabel,
                BorderLayout.WEST
        );

        add(
                version,
                BorderLayout.EAST
        );
    }


    public void update(
            File project,
            File currentFile,
            boolean modified
    ) {

        if (project == null) {

            statusLabel.setText(
                    "  未打开项目"
            );

            return;
        }


        if (currentFile == null) {

            statusLabel.setText(
                    "  项目："
                            + project.getName()
            );

            return;
        }


        String state =
                modified
                        ? " *"
                        : "";


        statusLabel.setText(
                "  项目："
                        + project.getName()
                        + "    |    "
                        + currentFile.getName()
                        + state
        );
    }
}