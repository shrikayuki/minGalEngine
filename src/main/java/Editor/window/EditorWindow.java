package main.java.Editor.window;

import main.java.Editor.constant.EditorConstants;
import main.java.Editor.manager.ProjectManager;
import main.java.Editor.manager.ResourceManager;
import main.java.Editor.manager.RuntimeLauncher;
import main.java.Editor.manager.ScriptManager;
import main.java.Editor.panel.EditorMainPanel;
import main.java.Editor.panel.ProjectPanel;
import main.java.Editor.panel.ResourcePanel;
import main.java.Editor.panel.ScriptEditorPanel;
import main.java.Editor.panel.StatusBarPanel;
import main.java.Editor.panel.TopBarPanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class EditorWindow extends JFrame {

    // =========================================================
    // Manager
    // =========================================================

    private final ProjectManager projectManager;

    private final ScriptManager scriptManager;

    private final RuntimeLauncher runtimeLauncher;

    private final ResourceManager resourceManager;


    // =========================================================
    // Panels
    // =========================================================

    private final ProjectPanel projectPanel;

    private final ScriptEditorPanel scriptEditorPanel;

    private final ResourcePanel resourcePanel;

    private final StatusBarPanel statusBarPanel;


    // =========================================================
    // 当前打开文件
    // =========================================================

    private File currentFile;


    // =========================================================
    // 构造
    // =========================================================

    public EditorWindow() {

        projectManager =
                new ProjectManager();


        scriptManager =
                new ScriptManager(
                        projectManager
                );


        resourceManager =
                new ResourceManager(
                        projectManager
                );


        runtimeLauncher =
                new RuntimeLauncher(
                        projectManager
                );


        // =====================================================
        // 创建编辑器
        // =====================================================

        scriptEditorPanel =
                new ScriptEditorPanel();


        // =====================================================
        // Project Panel
        // =====================================================

        projectPanel =
                new ProjectPanel(
                        this::createNewScript,
                        this::openScriptFromTree,
                        this::renameScript,
                        this::deleteScript
                );


        // =====================================================
        // Resource Panel
        // =====================================================

        resourcePanel =
                new ResourcePanel(
                        this::createNewResourceFile,
                        this::createNewResourceFolder,
                        this::openResourceText,
                        this::renameResource,
                        this::deleteResource
                );


        // =====================================================
        // Status
        // =====================================================

        statusBarPanel =
                new StatusBarPanel();


        // =====================================================
        // Window
        // =====================================================

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

        updateStatus();
    }


    // =========================================================
    // UI
    // =========================================================

    private void initUI() {

        setLayout(
                new BorderLayout()
        );


        // =====================================================
        // Top Bar
        // =====================================================

        TopBarPanel topBar =
                new TopBarPanel(
                        this::createNewProject,
                        this::openProject,
                        this::saveFile,
                        this::runGame,
                        this::exportGame
                );


        add(
                topBar,
                BorderLayout.NORTH
        );


        // =====================================================
        // Main
        // =====================================================

        EditorMainPanel mainPanel =
                new EditorMainPanel(
                        projectPanel,
                        scriptEditorPanel,
                        resourcePanel
                );


        add(
                mainPanel,
                BorderLayout.CENTER
        );


        // =====================================================
        // Status Bar
        // =====================================================

        add(
                statusBarPanel,
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
    // 新建项目
    // =========================================================

    private void createNewProject() {

        if (!confirmSaveIfNeeded()) {

            return;
        }


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


        try {

            projectManager.createProject(
                    chooser.getSelectedFile(),
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

            showError(
                    "创建项目失败",
                    ex
            );
        }
    }


    // =========================================================
    // 打开项目
    // =========================================================

    private void openProject() {

        if (!confirmSaveIfNeeded()) {

            return;
        }


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


        try {

            projectManager.openProject(
                    chooser.getSelectedFile()
            );


            loadProject();


        } catch (Exception ex) {

            showError(
                    "打开项目失败",
                    ex
            );
        }
    }


    // =========================================================
    // 加载项目
    // =========================================================

    private void loadProject() {

        if (!projectManager.hasProject()) {

            return;
        }


        currentFile = null;


        scriptEditorPanel.clear();


        projectPanel.reload(
                projectManager.getProjectRoot()
        );


        resourcePanel.reload(
                projectManager.getResourcesDirectory()
        );


        updateStatus();
    }


    // =========================================================
    // 从项目树打开脚本
    // =========================================================

    private void openScriptFromTree(
            File file
    ) {

        if (!confirmSaveIfNeeded()) {

            return;
        }


        openScript(file);
    }


    // =========================================================
    // 打开脚本
    // =========================================================

    private void openScript(
            File file
    ) {

        try {

            String content =
                    scriptManager.readScript(
                            file
                    );


            scriptEditorPanel.setContent(
                    content
            );


            currentFile =
                    file;


            updateStatus();


        } catch (Exception ex) {

            showError(
                    "打开脚本失败",
                    ex
            );
        }
    }


    // =========================================================
    // 打开资源文本
    // =========================================================

    private void openResourceText(
            File file
    ) {

        if (!confirmSaveIfNeeded()) {

            return;
        }


        // =====================================================
        // 非文本资源
        // =====================================================

        if (
                !file.isFile()
                        || !resourceManager.isTextFile(file)
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "该资源不是文本文件，不能在文本编辑器中打开：\n"
                            + file.getName(),
                    "资源文件",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }


        try {

            String content =
                    resourceManager.readTextFile(
                            file
                    );


            scriptEditorPanel.setContent(
                    content
            );


            currentFile =
                    file;


            updateStatus();


        } catch (Exception ex) {

            showError(
                    "打开资源失败",
                    ex
            );
        }
    }


    // =========================================================
    // 保存当前文件
    // =========================================================

    private void saveFile() {

        saveCurrentFile();
    }


    // =========================================================
    // 保存当前文件
    // =========================================================

    private boolean saveCurrentFile() {

        if (currentFile == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "当前没有打开文件",
                    "保存",
                    JOptionPane.WARNING_MESSAGE
            );

            return false;
        }


        try {

            File scriptsDirectory =
                    projectManager.getScriptsDirectory();


            File resourcesDirectory =
                    projectManager.getResourcesDirectory();


            // =================================================
            // scripts
            // =================================================

            if (
                    scriptsDirectory != null
                            && isInside(
                            scriptsDirectory,
                            currentFile
                    )
            ) {

                scriptManager.saveScript(
                        currentFile,
                        scriptEditorPanel.getContent()
                );
            }

            // =================================================
            // resources
            // =================================================

            else if (
                    resourcesDirectory != null
                            && isInside(
                            resourcesDirectory,
                            currentFile
                    )
            ) {

                resourceManager.saveTextFile(
                        currentFile,
                        scriptEditorPanel.getContent()
                );
            }

            // =================================================
            // 非项目文件
            // =================================================

            else {

                throw new IllegalArgumentException(
                        "当前文件不属于项目"
                );
            }


            scriptEditorPanel.markSaved();


            updateStatus();


            return true;


        } catch (Exception ex) {

            showError(
                    "保存失败",
                    ex
            );


            return false;
        }
    }


    // =========================================================
    // 新建脚本
    // =========================================================

    private void createNewScript() {

        if (!projectManager.hasProject()) {

            return;
        }


        String scriptName =
                JOptionPane.showInputDialog(
                        this,
                        "请输入脚本名称：",
                        "新建脚本",
                        JOptionPane.PLAIN_MESSAGE
                );


        if (scriptName == null) {

            return;
        }


        scriptName =
                scriptName.trim();


        if (scriptName.isEmpty()) {

            return;
        }


        try {

            File script =
                    scriptManager.createScript(
                            scriptName
                    );


            projectPanel.reload(
                    projectManager.getProjectRoot()
            );


            openScript(script);


        } catch (Exception ex) {

            showError(
                    "新建脚本失败",
                    ex
            );
        }
    }


    // =========================================================
    // 重命名脚本
    // =========================================================

    private void renameScript(
            File script
    ) {

        if (script == null) {

            return;
        }


        String newName =
                JOptionPane.showInputDialog(
                        this,
                        "请输入新的脚本名称：",
                        script.getName()
                );


        if (newName == null) {

            return;
        }


        newName =
                newName.trim();


        if (newName.isEmpty()) {

            return;
        }


        // =====================================================
        // 当前脚本有修改
        // =====================================================

        if (
                currentFile != null
                        && currentFile.equals(script)
                        && scriptEditorPanel.isModified()
        ) {

            if (!saveCurrentFile()) {

                return;
            }
        }


        try {

            File newScript =
                    scriptManager.renameScript(
                            script,
                            newName
                    );


            // =================================================
            // 当前文件同步更新
            // =================================================

            if (
                    currentFile != null
                            && currentFile.equals(script)
            ) {

                currentFile =
                        newScript;
            }


            projectPanel.reload(
                    projectManager.getProjectRoot()
            );


            updateStatus();


        } catch (Exception ex) {

            showError(
                    "重命名失败",
                    ex
            );
        }
    }


    // =========================================================
    // 删除脚本
    // =========================================================

    private void deleteScript(
            File script
    ) {

        if (script == null) {

            return;
        }


        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        "确定删除脚本：\n"
                                + script.getName()
                                + "？",
                        "删除脚本",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );


        if (
                result
                        != JOptionPane.YES_OPTION
        ) {

            return;
        }


        try {

            scriptManager.deleteScript(
                    script
            );


            // =================================================
            // 当前文件同步清空
            // =================================================

            if (
                    currentFile != null
                            && currentFile.equals(script)
            ) {

                currentFile = null;

                scriptEditorPanel.clear();
            }


            projectPanel.reload(
                    projectManager.getProjectRoot()
            );


            updateStatus();


        } catch (Exception ex) {

            showError(
                    "删除失败",
                    ex
            );
        }
    }


    // =========================================================
    // 新建资源文件
    // =========================================================

    private void createNewResourceFile(
            File parentDirectory
    ) {

        if (!projectManager.hasProject()) {

            return;
        }


        String fileName =
                JOptionPane.showInputDialog(
                        this,
                        "请输入资源文件名称：",
                        "新建资源文件",
                        JOptionPane.PLAIN_MESSAGE
                );


        if (fileName == null) {

            return;
        }


        fileName =
                fileName.trim();


        if (fileName.isEmpty()) {

            return;
        }


        try {

            File file =
                    resourceManager.createFile(
                            parentDirectory,
                            fileName
                    );


            resourcePanel.reload(
                    projectManager.getResourcesDirectory()
            );


            // =================================================
            // 文本资源自动打开
            // =================================================

            if (resourceManager.isTextFile(file)) {

                openResourceText(file);
            }


        } catch (Exception ex) {

            showError(
                    "新建资源失败",
                    ex
            );
        }
    }


    // =========================================================
    // 新建资源文件夹
    // =========================================================

    private void createNewResourceFolder(
            File parentDirectory
    ) {

        if (!projectManager.hasProject()) {

            return;
        }


        String folderName =
                JOptionPane.showInputDialog(
                        this,
                        "请输入文件夹名称：",
                        "新建资源文件夹",
                        JOptionPane.PLAIN_MESSAGE
                );


        if (folderName == null) {

            return;
        }


        folderName =
                folderName.trim();


        if (folderName.isEmpty()) {

            return;
        }


        try {

            resourceManager.createDirectory(
                    parentDirectory,
                    folderName
            );


            resourcePanel.reload(
                    projectManager.getResourcesDirectory()
            );


        } catch (Exception ex) {

            showError(
                    "新建资源文件夹失败",
                    ex
            );
        }
    }


    // =========================================================
    // 重命名资源
    // =========================================================

    private void renameResource(
            File file
    ) {

        if (file == null) {

            return;
        }


        // =====================================================
        // 当前资源有修改
        // =====================================================

        if (
                currentFile != null
                        && currentFile.equals(file)
                        && scriptEditorPanel.isModified()
        ) {

            if (!saveCurrentFile()) {

                return;
            }
        }


        String newName =
                JOptionPane.showInputDialog(
                        this,
                        "请输入新的名称：",
                        file.getName()
                );


        if (newName == null) {

            return;
        }


        newName =
                newName.trim();


        if (newName.isEmpty()) {

            return;
        }


        try {

            File newFile =
                    resourceManager.rename(
                            file,
                            newName
                    );


            // =================================================
            // 当前资源同步更新
            // =================================================

            if (
                    currentFile != null
                            && currentFile.equals(file)
            ) {

                currentFile =
                        newFile;
            }


            resourcePanel.reload(
                    projectManager.getResourcesDirectory()
            );


            updateStatus();


        } catch (Exception ex) {

            showError(
                    "资源重命名失败",
                    ex
            );
        }
    }


    // =========================================================
    // 删除资源
    // =========================================================

    private void deleteResource(
            File file
    ) {

        if (file == null) {

            return;
        }


        // =====================================================
        // 处理非空文件夹
        // =====================================================

        if (
                file.isDirectory()
                        && file.listFiles() != null
                        && file.listFiles().length > 0
        ) {

            int result =
                    JOptionPane.showConfirmDialog(
                            this,
                            "该文件夹不是空的。\n"
                                    + "确定递归删除整个文件夹吗？",
                            "删除资源",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );


            if (
                    result
                            != JOptionPane.YES_OPTION
            ) {

                return;
            }

        } else {

            int result =
                    JOptionPane.showConfirmDialog(
                            this,
                            "确定删除：\n"
                                    + file.getName()
                                    + "？",
                            "删除资源",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );


            if (
                    result
                            != JOptionPane.YES_OPTION
            ) {

                return;
            }
        }


        // =====================================================
        // 删除
        // =====================================================

        try {

            resourceManager.delete(
                    file
            );


            // =================================================
            // 当前文件已经不存在
            // =================================================

            if (
                    currentFile != null
                            && (
                            currentFile.equals(file)
                                    || isInside(
                                    file,
                                    currentFile
                            )
                    )
            ) {

                currentFile = null;

                scriptEditorPanel.clear();
            }


            resourcePanel.reload(
                    projectManager.getResourcesDirectory()
            );


            updateStatus();


        } catch (Exception ex) {

            showError(
                    "删除资源失败",
                    ex
            );
        }
    }


    // =========================================================
    // 运行游戏
    // =========================================================

    private void runGame() {

        if (!projectManager.hasProject()) {

            JOptionPane.showMessageDialog(
                    this,
                    "请先打开或创建项目",
                    "运行",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        // =====================================================
        // 保存当前修改
        // =====================================================

        if (
                scriptEditorPanel.isModified()
        ) {

            if (!saveCurrentFile()) {

                return;
            }
        }


        // =====================================================
        // 启动 Runtime
        // =====================================================

        try {

            setVisible(false);


            runtimeLauncher.launch();


        } catch (Exception ex) {

            setVisible(true);


            showError(
                    "游戏启动失败",
                    ex
            );
        }
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
    // 未保存确认
    // =========================================================

    private boolean confirmSaveIfNeeded() {

        if (
                !scriptEditorPanel.isModified()
                        || currentFile == null
        ) {

            return true;
        }


        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        "文件 "
                                + currentFile.getName()
                                + " 已被修改，是否保存？",
                        "未保存修改",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );


        // =====================================================
        // Cancel
        // =====================================================

        if (
                result
                        == JOptionPane.CANCEL_OPTION
        ) {

            return false;
        }


        // =====================================================
        // Yes
        // =====================================================

        if (
                result
                        == JOptionPane.YES_OPTION
        ) {

            return saveCurrentFile();
        }


        // =====================================================
        // No
        // =====================================================

        if (
                result
                        == JOptionPane.NO_OPTION
        ) {

            scriptEditorPanel.markSaved();

            return true;
        }


        return false;
    }


    // =========================================================
    // 判断 file 是否在 root 内
    // =========================================================

    private boolean isInside(
            File root,
            File file
    ) {

        if (
                root == null
                        || file == null
        ) {

            return false;
        }


        try {

            return file.getCanonicalFile()
                    .toPath()
                    .normalize()
                    .startsWith(
                            root.getCanonicalFile()
                                    .toPath()
                                    .normalize()
                    );

        } catch (Exception e) {

            return false;
        }
    }


    // =========================================================
    // 状态
    // =========================================================

    private void updateStatus() {

        statusBarPanel.update(
                projectManager.getProjectRoot(),
                currentFile,
                scriptEditorPanel.isModified()
        );
    }


    // =========================================================
    // 错误
    // =========================================================

    private void showError(
            String title,
            Exception ex
    ) {

        String message =
                ex.getMessage();


        if (
                message == null
                        || message.isBlank()
        ) {

            message =
                    ex.getClass()
                            .getSimpleName();
        }


        JOptionPane.showMessageDialog(
                this,
                message,
                title,
                JOptionPane.ERROR_MESSAGE
        );
    }
}

