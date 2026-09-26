package main.java.Editor.panel;

import main.java.Editor.constant.EditorConstants;

import javax.swing.*;
import java.awt.*;

public class EditorMainPanel extends JPanel {

    public EditorMainPanel(
            ProjectPanel projectPanel,
            ScriptEditorPanel scriptEditorPanel,
            ResourcePanel resourcePanel
    ) {

        setLayout(
                new BorderLayout()
        );

        setBackground(
                EditorConstants.BACKGROUND
        );


        // =====================================================
        // Script + Resource
        // =====================================================

        JSplitPane centerRight =
                new JSplitPane(
                        JSplitPane.HORIZONTAL_SPLIT,
                        scriptEditorPanel,
                        resourcePanel
                );


        centerRight.setDividerLocation(
                850
        );

        centerRight.setBorder(null);

        centerRight.setBackground(
                EditorConstants.BACKGROUND
        );


        // =====================================================
        // Project + Center
        // =====================================================

        JSplitPane mainSplit =
                new JSplitPane(
                        JSplitPane.HORIZONTAL_SPLIT,
                        projectPanel,
                        centerRight
                );


        mainSplit.setDividerLocation(
                EditorConstants.PROJECT_PANEL_WIDTH
        );

        mainSplit.setBorder(null);

        mainSplit.setBackground(
                EditorConstants.BACKGROUND
        );


        add(
                mainSplit,
                BorderLayout.CENTER
        );
    }
}