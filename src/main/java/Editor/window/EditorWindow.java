package main.java.Editor.window;

import main.java.Editor.constant.EditorConstants;
import main.java.Editor.manager.ProjectManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;

public class EditorWindow extends JFrame {

    // =========================================================
    // 项目管理
    // =========================================================

    private final ProjectManager projectManager;


    // =========================================================
    // UI
    // =========================================================

    private JTree projectTree;

    private JTree resourceTree;

    private JTextArea scriptEditor;

    private JLabel statusLabel;


    // =========================================================
    // 当前正在编辑的文件
    // =========================================================

    private File currentFile;


    // =========================================================
    // 构造
    // =========================================================

    public EditorWindow() {

        projectManager =
                new ProjectManager();


        setTitle(
                EditorConstants.EDITOR_TITLE
        );


        setSize(
                EditorConstants.WINDOW_WIDTH,
                EditorConstants.WINDOW_HEIGHT
        );


        setMinimumSize(
                new Dimension(
                        EditorConstants.MIN_WINDOW_WIDTH,
                        EditorConstants.MIN_WINDOW_HEIGHT
                )
        );


        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );


        setLocationRelativeTo(null);


        setupLookAndFeel();

        initUI();
    }


    // =========================================================
    // 初始化 UI
    // =========================================================

    private void initUI() {

        setLayout(
                new BorderLayout()
        );


        add(
                createTopBar(),
                BorderLayout.NORTH
        );


        add(
                createMainContent(),
                BorderLayout.CENTER
        );


        add(
                createStatusBar(),
                BorderLayout.SOUTH
        );
    }


    // =========================================================
    // Look And Feel
    // =========================================================

    private void setupLookAndFeel() {

        UIManager.put(
                "Panel.background",
                EditorConstants.BACKGROUND
        );


        UIManager.put(
                "ScrollPane.background",
                EditorConstants.BACKGROUND
        );


        UIManager.put(
                "Viewport.background",
                EditorConstants.BACKGROUND
        );


        UIManager.put(
                "Label.foreground",
                EditorConstants.TEXT
        );


        UIManager.put(
                "Tree.background",
                EditorConstants.PANEL
        );


        UIManager.put(
                "Tree.foreground",
                EditorConstants.TEXT
        );


        UIManager.put(
                "Tree.selectionBackground",
                EditorConstants.ACCENT
        );


        UIManager.put(
                "Tree.selectionForeground",
                Color.WHITE
        );
    }


    // =========================================================
    // 顶部工具栏
    // =========================================================

    private JPanel createTopBar() {

        JPanel bar =
                new JPanel(
                        new BorderLayout()
                );


        bar.setBackground(
                EditorConstants.PANEL
        );


        bar.setBorder(
                new MatteBorder(
                        0,
                        0,
                        1,
                        0,
                        EditorConstants.BORDER
                )
        );


        // =====================================================
        // Logo
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
        // 操作按钮
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


        // -----------------------------------------------------
        // 新建
        // -----------------------------------------------------

        JButton newButton =
                createButton(
                        "新建"
                );


        newButton.addActionListener(
                e -> createNewProject()
        );


        actions.add(
                newButton
        );


        // -----------------------------------------------------
        // 打开
        // -----------------------------------------------------

        JButton openButton =
                createButton(
                        "打开"
                );


        openButton.addActionListener(
                e -> openProject()
        );


        actions.add(
                openButton
        );


        // -----------------------------------------------------
        // 保存
        // -----------------------------------------------------

        JButton saveButton =
                createButton(
                        "保存"
                );


        saveButton.addActionListener(
                e -> saveFile()
        );


        actions.add(
                saveButton
        );


        // -----------------------------------------------------
        // 分隔
        // -----------------------------------------------------

        actions.add(
                createSeparator()
        );


        // -----------------------------------------------------
        // 运行
        // -----------------------------------------------------

        JButton runButton =
                createButton(
                        "▶ 运行"
                );


        runButton.setBackground(
                EditorConstants.ACCENT
        );


        runButton.setForeground(
                Color.WHITE
        );


        runButton.addActionListener(
                e -> runGame()
        );


        actions.add(
                runButton
        );


        // -----------------------------------------------------
        // 导出
        // -----------------------------------------------------

        JButton exportButton =
                createButton(
                        "导出"
                );


        exportButton.addActionListener(
                e -> exportGame()
        );


        actions.add(
                exportButton
        );


        bar.add(
                left,
                BorderLayout.WEST
        );


        bar.add(
                actions,
                BorderLayout.CENTER
        );


        return bar;
    }


    // =========================================================
    // 新建项目
    // =========================================================

    private void createNewProject() {

        String projectName =
                JOptionPane.showInputDialog(
                        this,
                        "请输入项目名称：",
                        "新建 Galgame 项目",
                        JOptionPane.PLAIN_MESSAGE
                );


        if (projectName == null) {

            return;
        }


        projectName =
                projectName.trim();


        if (projectName.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "项目名称不能为空",
                    "创建失败",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


        // =====================================================
        // 选择项目位置
        // =====================================================

        JFileChooser chooser =
                new JFileChooser();


        chooser.setDialogTitle(
                "选择项目保存位置"
        );


        chooser.setFileSelectionMode(
                JFileChooser.DIRECTORIES_ONLY
        );


        int result =
                chooser.showSaveDialog(
                        this
                );


        if (
                result
                        != JFileChooser.APPROVE_OPTION
        ) {

            return;
        }


        File parentDirectory =
                chooser.getSelectedFile();


        // =====================================================
        // 创建项目
        // =====================================================

        try {

            projectManager.createProject(
                    parentDirectory,
                    projectName
            );


            loadProject();


            JOptionPane.showMessageDialog(
                    this,
                    "项目创建成功！",
                    "MiniGalEngine",
                    JOptionPane.INFORMATION_MESSAGE
            );


        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "创建项目失败",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    // =========================================================
    // 打开项目
    // =========================================================

    private void openProject() {

        JFileChooser chooser =
                new JFileChooser();


        chooser.setDialogTitle(
                "打开 Galgame 项目"
        );


        chooser.setFileSelectionMode(
                JFileChooser.DIRECTORIES_ONLY
        );


        int result =
                chooser.showOpenDialog(
                        this
                );


        if (
                result
                        != JFileChooser.APPROVE_OPTION
        ) {

            return;
        }


        File directory =
                chooser.getSelectedFile();


        try {

            projectManager.openProject(
                    directory
            );


            loadProject();


        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "打开项目失败",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    // =========================================================
    // 加载项目
    // =========================================================

    private void loadProject() {

        if (
                !projectManager.hasProject()
        ) {

            return;
        }


        currentFile = null;


        loadProjectTree();

        loadResourceTree();

        clearEditor();

        updateStatus();
    }


    // =========================================================
    // 加载 Project Tree
    // =========================================================

    private void loadProjectTree() {

        File root =
                projectManager.getProjectRoot();


        if (
                root == null
                        || !root.exists()
        ) {

            return;
        }


        FileTreeNode rootNode =
                createFileTree(
                        root
                );


        projectTree.setModel(
                new DefaultTreeModel(
                        rootNode
                )
        );


        projectTree.expandRow(
                0
        );
    }


    // =========================================================
    // 加载 Resource Tree
    // =========================================================

    private void loadResourceTree() {

        File resources =
                projectManager
                        .getResourcesDirectory();


        if (
                resources == null
                        || !resources.exists()
        ) {

            return;
        }


        FileTreeNode rootNode =
                createFileTree(
                        resources
                );


        resourceTree.setModel(
                new DefaultTreeModel(
                        rootNode
                )
        );


        resourceTree.expandRow(
                0
        );
    }


    // =========================================================
    // 文件树
    // =========================================================

    private FileTreeNode createFileTree(
            File file
    ) {

        FileTreeNode node =
                new FileTreeNode(
                        file
                );


        if (
                !file.isDirectory()
        ) {

            return node;
        }


        File[] children =
                file.listFiles();


        if (children == null) {

            return node;
        }


        // =====================================================
        // 文件夹排前面
        // =====================================================

        Arrays.sort(
                children,
                (a, b) -> {

                    if (
                            a.isDirectory()
                                    && !b.isDirectory()
                    ) {

                        return -1;
                    }


                    if (
                            !a.isDirectory()
                                    && b.isDirectory()
                    ) {

                        return 1;
                    }


                    return a.getName()
                            .compareToIgnoreCase(
                                    b.getName()
                            );
                }
        );


        for (
                File child :
                children
        ) {

            node.add(
                    createFileTree(
                            child
                    )
            );
        }


        return node;
    }


    // =========================================================
    // Project Tree 点击
    // =========================================================

    private void setupProjectTreeListener() {

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
                            getFileFromTreeNode(
                                    node
                            );


                    if (
                            file == null
                                    || !file.isFile()
                    ) {

                        return;
                    }


                    openFile(
                            file
                    );
                }
        );
    }


    // =========================================================
    // Resource Tree 点击
    // =========================================================

    private void setupResourceTreeListener() {

        resourceTree.addTreeSelectionListener(
                e -> {

                    DefaultMutableTreeNode node =
                            (DefaultMutableTreeNode)
                                    resourceTree
                                            .getLastSelectedPathComponent();


                    if (node == null) {

                        return;
                    }


                    File file =
                            getFileFromTreeNode(
                                    node
                            );


                    if (
                            file == null
                                    || !file.isFile()
                    ) {

                        return;
                    }


                    openFile(
                            file
                    );
                }
        );
    }


    // =========================================================
    // 从树节点获得 File
    // =========================================================

    private File getFileFromTreeNode(
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
    // 打开文件
    // =========================================================

    private void openFile(
            File file
    ) {

        if (
                file == null
                        || !file.isFile()
        ) {

            return;
        }


        try {

            String content =
                    Files.readString(
                            file.toPath(),
                            StandardCharsets.UTF_8
                    );


            scriptEditor.setText(
                    content
            );


            scriptEditor.setCaretPosition(
                    0
            );


            currentFile =
                    file;


            statusLabel.setText(
                    "  正在编辑："
                            + file.getName()
            );


        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "无法打开文件：\n"
                            + ex.getMessage(),
                    "打开失败",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    // =========================================================
    // 清空编辑器
    // =========================================================

    private void clearEditor() {

        scriptEditor.setText("");

        currentFile = null;
    }


    // =========================================================
    // 保存当前文件
    // =========================================================

    private void saveFile() {

        if (currentFile == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "当前没有打开文件",
                    "保存",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        try {

            Files.writeString(
                    currentFile.toPath(),
                    scriptEditor.getText(),
                    StandardCharsets.UTF_8
            );


            statusLabel.setText(
                    "  已保存："
                            + currentFile.getName()
            );


        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "保存失败：\n"
                            + ex.getMessage(),
                    "保存失败",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    // =========================================================
    // 运行游戏
    // =========================================================

    private void runGame() {

        if (
                !projectManager.hasProject()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "请先打开或创建项目",
                    "运行",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        saveFile();


        JOptionPane.showMessageDialog(
                this,
                "运行功能下一步接入 Runtime。",
                "MiniGalEngine",
                JOptionPane.INFORMATION_MESSAGE
        );
    }


    // =========================================================
    // 导出
    // =========================================================

    private void exportGame() {

        JOptionPane.showMessageDialog(
                this,
                "导出功能下一步实现。",
                "MiniGalEngine",
                JOptionPane.INFORMATION_MESSAGE
        );
    }


    // =========================================================
    // 状态栏
    // =========================================================

    private JPanel createStatusBar() {

        JPanel bar =
                new JPanel(
                        new BorderLayout()
                );


        bar.setBackground(
                EditorConstants.PANEL
        );


        bar.setBorder(
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


        bar.add(
                statusLabel,
                BorderLayout.WEST
        );


        bar.add(
                version,
                BorderLayout.EAST
        );


        return bar;
    }


    // =========================================================
    // 更新状态
    // =========================================================

    private void updateStatus() {

        if (
                !projectManager.hasProject()
        ) {

            statusLabel.setText(
                    "  未打开项目"
            );

            return;
        }


        File project =
                projectManager.getProjectRoot();


        statusLabel.setText(
                "  项目："
                        + project.getName()
        );
    }


    // =========================================================
    // 主区域
    // =========================================================

    private JPanel createMainContent() {

        JPanel root =
                new JPanel(
                        new BorderLayout()
                );


        root.setBackground(
                EditorConstants.BACKGROUND
        );


        JPanel projectPanel =
                createProjectPanel();


        JPanel scriptPanel =
                createScriptEditor();


        JPanel resourcePanel =
                createResourcePanel();


        // =====================================================
        // 中间 + 右边
        // =====================================================

        JSplitPane centerRight =
                new JSplitPane(
                        JSplitPane.HORIZONTAL_SPLIT,
                        scriptPanel,
                        resourcePanel
                );


        centerRight.setDividerLocation(
                850
        );


        centerRight.setBorder(
                null
        );


        centerRight.setBackground(
                EditorConstants.BACKGROUND
        );


        // =====================================================
        // 左边 + 中间
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


        mainSplit.setBorder(
                null
        );


        mainSplit.setBackground(
                EditorConstants.BACKGROUND
        );


        root.add(
                mainSplit,
                BorderLayout.CENTER
        );


        return root;
    }


    // =========================================================
    // Project Panel
    // =========================================================

    private JPanel createProjectPanel() {

        JPanel panel =
                createPanel();


        JLabel title =
                createSectionTitle(
                        EditorConstants.PROJECT_TITLE
                );


        panel.add(
                title,
                BorderLayout.NORTH
        );


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


        scroll.setBorder(
                null
        );


        scroll.getViewport()
                .setBackground(
                        EditorConstants.PANEL
                );


        panel.add(
                scroll,
                BorderLayout.CENTER
        );


        setupProjectTreeListener();


        return panel;
    }


    // =========================================================
    // Script Editor
    // =========================================================

    private JPanel createScriptEditor() {

        JPanel panel =
                createPanel();


        JLabel title =
                createSectionTitle(
                        EditorConstants.SCRIPT_TITLE
                );


        panel.add(
                title,
                BorderLayout.NORTH
        );


        scriptEditor =
                new JTextArea();


        scriptEditor.setBackground(
                EditorConstants.EDITOR_BACKGROUND
        );


        scriptEditor.setForeground(
                EditorConstants.TEXT
        );


        scriptEditor.setCaretColor(
                Color.WHITE
        );


        scriptEditor.setSelectionColor(
                new Color(
                        60,
                        75,
                        120
                )
        );


        scriptEditor.setFont(
                new Font(
                        EditorConstants.CODE_FONT,
                        Font.PLAIN,
                        EditorConstants.CODE_FONT_SIZE
                )
        );


        scriptEditor.setTabSize(
                4
        );


        scriptEditor.setLineWrap(
                false
        );


        JScrollPane scroll =
                new JScrollPane(
                        scriptEditor
                );


        scroll.setBorder(
                null
        );


        scroll.getViewport()
                .setBackground(
                        EditorConstants.EDITOR_BACKGROUND
                );


        panel.add(
                scroll,
                BorderLayout.CENTER
        );


        return panel;
    }


    // =========================================================
    // Resource Panel
    // =========================================================

    private JPanel createResourcePanel() {

        JPanel panel =
                createPanel();


        JLabel title =
                createSectionTitle(
                        EditorConstants.RESOURCE_TITLE
                );


        panel.add(
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


        scroll.setBorder(
                null
        );


        scroll.getViewport()
                .setBackground(
                        EditorConstants.PANEL
                );


        panel.add(
                scroll,
                BorderLayout.CENTER
        );


        setupResourceTreeListener();


        return panel;
    }


    // =========================================================
    // 创建 Panel
    // =========================================================

    private JPanel createPanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );


        panel.setBackground(
                EditorConstants.PANEL
        );


        return panel;
    }


    // =========================================================
    // Section Title
    // =========================================================

    private JLabel createSectionTitle(
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
                new MatteBorder(
                        0,
                        0,
                        1,
                        0,
                        EditorConstants.BORDER
                )
        );


        return label;
    }


    // =========================================================
    // Button
    // =========================================================

    private JButton createButton(
            String text
    ) {

        JButton button =
                new JButton(
                        text
                );


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


    // =========================================================
    // 分隔线
    // =========================================================

    private Component createSeparator() {

        JPanel separator =
                new JPanel();


        separator.setPreferredSize(
                new Dimension(
                        15,
                        1
                )
        );


        separator.setOpaque(
                false
        );


        return separator;
    }


    // =========================================================
    // 文件树节点
    // =========================================================

    private static class FileTreeNode
            extends DefaultMutableTreeNode {

        private final File file;


        public FileTreeNode(
                File file
        ) {

            super(
                    file.getName()
            );


            this.file =
                    file;
        }


        public File getFile() {

            return file;
        }


        @Override
        public String toString() {

            return file.getName();
        }
    }
}