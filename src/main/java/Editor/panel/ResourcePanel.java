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

public class ResourcePanel extends JPanel {

    private final JTree resourceTree;

    private final FileTreeBuilder treeBuilder;

    private final Consumer<File> onNewResourceFile;

    private final Consumer<File> onNewResourceFolder;

    private final Consumer<File> onOpenResourceText;

    private final Consumer<File> onRenameResource;

    private final Consumer<File> onDeleteResource;


    public ResourcePanel(
            Consumer<File> onNewResourceFile,
            Consumer<File> onNewResourceFolder,
            Consumer<File> onOpenResourceText,
            Consumer<File> onRenameResource,
            Consumer<File> onDeleteResource
    ) {

        this.onNewResourceFile =
                onNewResourceFile;

        this.onNewResourceFolder =
                onNewResourceFolder;

        this.onOpenResourceText =
                onOpenResourceText;

        this.onRenameResource =
                onRenameResource;

        this.onDeleteResource =
                onDeleteResource;


        treeBuilder =
                new FileTreeBuilder();


        setLayout(
                new BorderLayout()
        );

        setBackground(
                EditorConstants.PANEL
        );


        JLabel title =
                createTitle(
                        EditorConstants.RESOURCE_TITLE
                );


        add(
                title,
                BorderLayout.NORTH
        );


        FileTreeNode root =
                new FileTreeNode(
                        new File(
                                "暂无资源"
                        )
                );


        resourceTree =
                new JTree(
                        root
                );


        resourceTree.setBackground(
                EditorConstants.PANEL
        );

        resourceTree.setForeground(
                EditorConstants.TEXT
        );

        resourceTree.setRowHeight(
                EditorConstants.RESOURCE_TREE_ROW_HEIGHT
        );

        resourceTree.setBorder(
                new EmptyBorder(
                        8,
                        5,
                        8,
                        5
                )
        );

        resourceTree.setFont(
                new Font(
                        EditorConstants.UI_FONT,
                        Font.PLAIN,
                        EditorConstants.UI_FONT_SIZE
                )
        );


        JScrollPane scroll =
                new JScrollPane(
                        resourceTree
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


        setupDoubleClick();

        setupPopupMenu();
    }


    // =========================================================
    // Reload
    // =========================================================

    public void reload(
            File resources
    ) {

        if (
                resources == null
                        || !resources.isDirectory()
        ) {

            return;
        }


        FileTreeNode root =
                treeBuilder.build(
                        resources
                );


        resourceTree.setModel(
                new DefaultTreeModel(
                        root
                )
        );


        resourceTree.expandRow(0);
    }


    // =========================================================
    // 双击
    // =========================================================

    private void setupDoubleClick() {

        resourceTree.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            MouseEvent e
                    ) {

                        if (
                                e.getClickCount() != 2
                        ) {

                            return;
                        }


                        TreeSelection selection =
                                getSelection(
                                        e
                                );


                        if (selection == null) {

                            return;
                        }


                        File file =
                                selection.file;


                        if (
                                file.isFile()
                        ) {

                            onOpenResourceText.accept(
                                    file
                            );
                        }
                    }
                }
        );
    }


    // =========================================================
    // 右键
    // =========================================================

    private void setupPopupMenu() {

        resourceTree.addMouseListener(
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
                resourceTree.getClosestRowForLocation(
                        e.getX(),
                        e.getY()
                );


        if (row < 0) {

            return;
        }


        resourceTree.setSelectionRow(
                row
        );


        DefaultMutableTreeNode node =
                (DefaultMutableTreeNode)
                        resourceTree
                                .getLastSelectedPathComponent();


        if (node == null) {

            return;
        }


        File file =
                getFile(node);


        if (file == null) {

            return;
        }


        JPopupMenu menu =
                new JPopupMenu();


        // =====================================================
        // 目录
        // =====================================================

        if (file.isDirectory()) {

            JMenuItem newFile =
                    new JMenuItem(
                            "新建文件"
                    );


            newFile.addActionListener(
                    e1 ->
                            onNewResourceFile.accept(
                                    file
                            )
            );


            JMenuItem newFolder =
                    new JMenuItem(
                            "新建文件夹"
                    );


            newFolder.addActionListener(
                    e1 ->
                            onNewResourceFolder.accept(
                                    file
                            )
            );


            menu.add(newFile);

            menu.add(newFolder);


            // resources 根目录不允许自身删除
            if (isResourcesRoot(file)) {

                menu.show(
                        resourceTree,
                        e.getX(),
                        e.getY()
                );

                return;
            }
        }


        // =====================================================
        // 文件 / 子目录
        // =====================================================

        if (!isResourcesRoot(file)) {

            JMenuItem rename =
                    new JMenuItem(
                            "重命名"
                    );


            rename.addActionListener(
                    e1 ->
                            onRenameResource.accept(
                                    file
                            )
            );


            JMenuItem delete =
                    new JMenuItem(
                            "删除"
                    );


            delete.addActionListener(
                    e1 ->
                            onDeleteResource.accept(
                                    file
                            )
            );


            menu.add(rename);

            menu.add(delete);
        }


        if (menu.getComponentCount() == 0) {

            return;
        }


        menu.show(
                resourceTree,
                e.getX(),
                e.getY()
        );
    }


    // =========================================================
    // 判断 resources 根目录
    // =========================================================

    private boolean isResourcesRoot(
            File file
    ) {

        TreeModelRootData rootData =
                getRootData();


        if (rootData == null) {

            return false;
        }


        return rootData.root.equals(
                file
        );
    }


    private TreeModelRootData getRootData() {

        DefaultMutableTreeNode root =
                (DefaultMutableTreeNode)
                        resourceTree
                                .getModel()
                                .getRoot();


        File rootFile =
                getFile(root);


        if (rootFile == null) {

            return null;
        }


        return new TreeModelRootData(
                rootFile
        );
    }


    // =========================================================
    // 获取 Tree 节点
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


    private TreeSelection getSelection(
            MouseEvent e
    ) {

        int row =
                resourceTree.getClosestRowForLocation(
                        e.getX(),
                        e.getY()
                );


        if (row < 0) {

            return null;
        }


        DefaultMutableTreeNode node =
                (DefaultMutableTreeNode)
                        resourceTree
                                .getPathForRow(row)
                                .getLastPathComponent();


        if (node == null) {

            return null;
        }


        File file =
                getFile(node);


        if (file == null) {

            return null;
        }


        return new TreeSelection(
                file
        );
    }


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


    private record TreeSelection(
            File file
    ) {
    }


    private record TreeModelRootData(
            File root
    ) {
    }
}