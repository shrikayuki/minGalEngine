package main.java.Editor.panel;

import main.java.Editor.constant.EditorConstants;
import main.java.Editor.tree.FileTreeBuilder;
import main.java.Editor.tree.FileTreeNode;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.function.Consumer;

public class ProjectPanel extends JPanel {

    private final JTree projectTree;

    private final FileTreeBuilder treeBuilder;

    private final Runnable onNewScript;

    private final Consumer<File> onOpenScript;

    private final Consumer<File> onRenameScript;

    private final Consumer<File> onDeleteScript;


    public ProjectPanel(
            Runnable onNewScript,
            Consumer<File> onOpenScript,
            Consumer<File> onRenameScript,
            Consumer<File> onDeleteScript
    ) {

        this.onNewScript = onNewScript;

        this.onOpenScript = onOpenScript;

        this.onRenameScript = onRenameScript;

        this.onDeleteScript = onDeleteScript;


        treeBuilder =
                new FileTreeBuilder();


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
                        EditorConstants.PROJECT_TITLE
                );

        add(
                title,
                BorderLayout.NORTH
        );


        // =====================================================
        // Tree
        // =====================================================

        FileTreeNode root =
                new FileTreeNode(
                        new File(
                                "未打开项目"
                        )
                );


        projectTree =
                new JTree(
                        root
                );


        projectTree.setBackground(
                EditorConstants.PANEL
        );

        projectTree.setForeground(
                EditorConstants.TEXT
        );

        projectTree.setRowHeight(
                EditorConstants.TREE_ROW_HEIGHT
        );

        projectTree.setBorder(
                new EmptyBorder(
                        8,
                        5,
                        8,
                        5
                )
        );

        projectTree.setFont(
                new Font(
                        EditorConstants.UI_FONT,
                        Font.PLAIN,
                        EditorConstants.UI_FONT_SIZE
                )
        );


        JScrollPane scroll =
                new JScrollPane(
                        projectTree
                );

        scroll.setBorder(null);

        scroll.getViewport()
                .setBackground(
                        EditorConstants.PANEL
                );


        add(
                scroll,
                BorderLayout.CENTER
        );


        setupTreeListener();

        setupPopupMenu();
    }


    // =========================================================
    // 加载项目
    // =========================================================

    public void reload(
            File projectRoot
    ) {

        if (
                projectRoot == null
                        || !projectRoot.exists()
        ) {

            return;
        }


        FileTreeNode root =
                treeBuilder.build(
                        projectRoot
                );


        projectTree.setModel(
                new DefaultTreeModel(
                        root
                )
        );


        projectTree.expandRow(0);
    }


    // =========================================================
    // Tree 点击
    // =========================================================

    private void setupTreeListener() {

        projectTree.addTreeSelectionListener(
                e -> {

                    DefaultMutableTreeNode node =
                            (DefaultMutableTreeNode)
                                    projectTree
                                            .getLastSelectedPathComponent();


                    if (node == null) {

                        return;
                    }


                    File file =
                            getFile(node);


                    if (
                            file == null
                                    || !file.isFile()
                    ) {

                        return;
                    }


                    if (
                            !file.getName()
                                    .toLowerCase()
                                    .endsWith(".txt")
                    ) {

                        return;
                    }


                    onOpenScript.accept(
                            file
                    );
                }
        );
    }


    // =========================================================
    // 右键
    // =========================================================

    private void setupPopupMenu() {

        projectTree.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mousePressed(
                            MouseEvent e
                    ) {

                        showPopup(e);
                    }


                    @Override
                    public void mouseReleased(
                            MouseEvent e
                    ) {

                        showPopup(e);
                    }
                }
        );
    }


    private void showPopup(
            MouseEvent e
    ) {

        if (!e.isPopupTrigger()) {

            return;
        }


        int row =
                projectTree.getClosestRowForLocation(
                        e.getX(),
                        e.getY()
                );


        if (row < 0) {

            return;
        }


        projectTree.setSelectionRow(
                row
        );


        DefaultMutableTreeNode node =
                (DefaultMutableTreeNode)
                        projectTree
                                .getLastSelectedPathComponent();


        if (node == null) {

            return;
        }


        File file =
                getFile(node);


        if (file == null) {

            return;
        }


        File scriptsDirectory =
                getScriptsDirectoryFromSelection(
                        file
                );


        JPopupMenu menu =
                new JPopupMenu();


        // =====================================================
        // scripts 文件夹
        // =====================================================

        if (
                file.isDirectory()
                        && scriptsDirectory != null
                        && file.equals(
                                scriptsDirectory
                        )
        ) {

            JMenuItem newScript =
                    new JMenuItem(
                            "新建脚本"
                    );


            newScript.addActionListener(
                    e1 -> onNewScript.run()
            );


            menu.add(
                    newScript
            );


            menu.show(
                    projectTree,
                    e.getX(),
                    e.getY()
            );


            return;
        }


        // =====================================================
        // 脚本文件
        // =====================================================

        if (
                file.isFile()
                        && scriptsDirectory != null
                        && file.getParentFile() != null
                        && file.getParentFile().equals(
                                scriptsDirectory
                        )
                        && file.getName()
                        .toLowerCase()
                        .endsWith(".txt")
        ) {

            JMenuItem rename =
                    new JMenuItem(
                            "重命名"
                    );


            rename.addActionListener(
                    e1 ->
                            onRenameScript.accept(
                                    file
                            )
            );


            JMenuItem delete =
                    new JMenuItem(
                            "删除"
                    );


            delete.addActionListener(
                    e1 ->
                            onDeleteScript.accept(
                                    file
                            )
            );


            menu.add(rename);

            menu.add(delete);


            menu.show(
                    projectTree,
                    e.getX(),
                    e.getY()
            );
        }
    }


    // =========================================================
    // 找 scripts 目录
    // =========================================================

    private File getScriptsDirectoryFromSelection(
            File file
    ) {

        if (file == null) {

            return null;
        }


        // scripts 本身
        if (
                file.isDirectory()
                        && file.getName()
                        .equalsIgnoreCase(
                                EditorConstants.SCRIPTS_DIR
                        )
        ) {

            return file;
        }


        // scripts 下的文件
        if (
                file.isFile()
                        && file.getParentFile() != null
        ) {

            File parent =
                    file.getParentFile();


            if (
                    parent.getName()
                            .equalsIgnoreCase(
                                    EditorConstants.SCRIPTS_DIR
                            )
            ) {

                return parent;
            }
        }


        return null;
    }


    // =========================================================
    // File
    // =========================================================

    private File getFile(
            DefaultMutableTreeNode node
    ) {

        if (
                node instanceof FileTreeNode fileNode
        ) {

            return fileNode.getFile();
        }


        return null;
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