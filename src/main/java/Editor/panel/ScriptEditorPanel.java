package main.java.Editor.panel;

import main.java.Editor.constant.EditorConstants;

import javax.swing.*;
import java.awt.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ScriptEditorPanel extends JPanel {

    private final JTextArea editor;

    private boolean modified = false;

    private boolean suppressChange = false;


    public ScriptEditorPanel() {

        setLayout(
                new BorderLayout()
        );

        setBackground(
                EditorConstants.PANEL
        );


        // =====================================================
        // 标题
        // =====================================================

        JLabel title =
                createTitle(
                        EditorConstants.SCRIPT_TITLE
                );

        add(
                title,
                BorderLayout.NORTH
        );


        // =====================================================
        // 编辑器
        // =====================================================

        editor =
                new JTextArea();

        editor.setBackground(
                EditorConstants.EDITOR_BACKGROUND
        );

        editor.setForeground(
                EditorConstants.TEXT
        );

        editor.setCaretColor(
                Color.WHITE
        );

        editor.setSelectionColor(
                new Color(
                        60,
                        75,
                        120
                )
        );

        editor.setFont(
                new Font(
                        EditorConstants.CODE_FONT,
                        Font.PLAIN,
                        EditorConstants.CODE_FONT_SIZE
                )
        );

        editor.setTabSize(4);

        editor.setLineWrap(false);


        // =====================================================
        // 修改监听
        // =====================================================

        editor.getDocument()
                .addDocumentListener(
                        new DocumentListener() {

                            private void changed() {

                                if (!suppressChange) {

                                    modified = true;
                                }
                            }

                            @Override
                            public void insertUpdate(
                                    DocumentEvent e
                            ) {

                                changed();
                            }

                            @Override
                            public void removeUpdate(
                                    DocumentEvent e
                            ) {

                                changed();
                            }

                            @Override
                            public void changedUpdate(
                                    DocumentEvent e
                            ) {

                                changed();
                            }
                        }
                );


        JScrollPane scroll =
                new JScrollPane(
                        editor
                );

        scroll.setBorder(null);

        scroll.getViewport()
                .setBackground(
                        EditorConstants.EDITOR_BACKGROUND
                );


        add(
                scroll,
                BorderLayout.CENTER
        );
    }


    // =========================================================
    // 设置文件内容
    // =========================================================

    public void setContent(
            String content
    ) {

        suppressChange = true;

        editor.setText(
                content == null
                        ? ""
                        : content
        );

        editor.setCaretPosition(0);

        suppressChange = false;

        modified = false;
    }


    // =========================================================
    // 获取内容
    // =========================================================

    public String getContent() {

        return editor.getText();
    }


    // =========================================================
    // 修改状态
    // =========================================================

    public boolean isModified() {

        return modified;
    }


    public void markSaved() {

        modified = false;
    }


    public void markModified() {

        modified = true;
    }


    // =========================================================
    // 清空
    // =========================================================

    public void clear() {

        suppressChange = true;

        editor.setText("");

        suppressChange = false;

        modified = false;
    }


    // =========================================================
    // 标题
    // =========================================================

    private JLabel createTitle(
            String text
    ) {

        JLabel label =
                new JLabel(
                        "  " + text
                );

        label.setPreferredSize(
                new Dimension(
                        0,
                        42
                )
        );

        label.setForeground(
                EditorConstants.TEXT_SECONDARY
        );

        label.setFont(
                new Font(
                        EditorConstants.UI_FONT,
                        Font.BOLD,
                        12
                )
        );

        label.setBorder(
                BorderFactory.createMatteBorder(
                        0,
                        0,
                        1,
                        0,
                        EditorConstants.BORDER
                )
        );

        return label;
    }
}