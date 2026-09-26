package main.java.Editor.panel;

import main.java.Editor.constant.EditorConstants;

import javax.swing.*;
import javax.swing.border.MatteBorder;


import java.awt.*;

public class TopBarPanel extends JPanel {

    public TopBarPanel(
            Runnable onNewProject,
            Runnable onOpenProject,
            Runnable onSave,
            Runnable onRun,
            Runnable onExport
    ) {

        setLayout(
                new BorderLayout()
        );

        setBackground(
                EditorConstants.PANEL
        );

        setBorder(
                new MatteBorder(
                        0,
                        0,
                        1,
                        0,
                        EditorConstants.BORDER
                )
        );


        // =====================================================
        // 左侧 Logo
        // =====================================================

        JPanel left =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                15,
                                8
                        )
                );

        left.setOpaque(false);


        JLabel logo =
                new JLabel(
                        "MG"
                );

        logo.setOpaque(true);

        logo.setBackground(
                EditorConstants.ACCENT
        );

        logo.setForeground(
                Color.WHITE
        );

        logo.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        logo.setFont(
                new Font(
                        EditorConstants.UI_FONT,
                        Font.BOLD,
                        16
                )
        );

        logo.setPreferredSize(
                new Dimension(
                        38,
                        32
                )
        );


        JLabel title =
                new JLabel(
                        EditorConstants.APP_NAME
                );

        title.setForeground(
                EditorConstants.TEXT
        );

        title.setFont(
                new Font(
                        EditorConstants.UI_FONT,
                        Font.BOLD,
                        16
                )
        );


        left.add(logo);
        left.add(title);


        // =====================================================
        // 按钮
        // =====================================================

        JPanel actions =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                5,
                                8
                        )
                );

        actions.setOpaque(false);


        JButton newButton =
                createButton("新建");

        newButton.addActionListener(
                e -> onNewProject.run()
        );


        JButton openButton =
                createButton("打开");

        openButton.addActionListener(
                e -> onOpenProject.run()
        );


        JButton saveButton =
                createButton("保存");

        saveButton.addActionListener(
                e -> onSave.run()
        );


        JButton runButton =
                createButton("▶ 运行");

        runButton.setBackground(
                EditorConstants.ACCENT
        );

        runButton.setForeground(
                Color.WHITE
        );

        runButton.addActionListener(
                e -> onRun.run()
        );


        JButton exportButton =
                createButton("导出");

        exportButton.addActionListener(
                e -> onExport.run()
        );


        actions.add(newButton);
        actions.add(openButton);
        actions.add(saveButton);
        actions.add(createSeparator());
        actions.add(runButton);
        actions.add(exportButton);


        add(
                left,
                BorderLayout.WEST
        );

        add(
                actions,
                BorderLayout.CENTER
        );
    }


    private JButton createButton(
            String text
    ) {

        JButton button =
                new JButton(text);

        button.setForeground(
                EditorConstants.TEXT
        );

        button.setBackground(
                EditorConstants.PANEL_LIGHT
        );

        button.setFocusPainted(
                false
        );

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        7,
                        14,
                        7,
                        14
                )
        );

        button.setFont(
                new Font(
                        EditorConstants.UI_FONT,
                        Font.PLAIN,
                        13
                )
        );

        return button;
    }


    private Component createSeparator() {

        JPanel separator =
                new JPanel();

        separator.setPreferredSize(
                new Dimension(
                        15,
                        1
                )
        );

        separator.setOpaque(false);

        return separator;
    }
}