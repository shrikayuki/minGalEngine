package main.java.Editor.manager;

import main.java.Running.manager.GameLauncher;
import main.java.Running.settings.GameSettings;
import main.java.Running.window.GameWindow;

import java.io.File;

public class RuntimeLauncher {

    private final ProjectManager projectManager;


    // =========================================================
    // 构造
    // =========================================================

    public RuntimeLauncher(
            ProjectManager projectManager
    ) {

        if (projectManager == null) {

            throw new IllegalArgumentException(
                    "ProjectManager 不能为空"
            );
        }

        this.projectManager =
                projectManager;
    }


    // =========================================================
    // 启动 Runtime
    // =========================================================

    public GameWindow launch() throws Exception {

        // =====================================================
        // 检查项目
        // =====================================================

        if (!projectManager.hasProject()) {

            throw new IllegalStateException(
                    "当前没有打开项目"
            );
        }


        // =====================================================
        // 获取入口脚本
        // =====================================================

        File mainScript =
                projectManager.getMainScript();

        if (
                mainScript == null
                        || !mainScript.isFile()
        ) {

            throw new IllegalStateException(
                    "找不到入口脚本：main.txt"
            );
        }


        // =====================================================
        // 获取 scripts
        // =====================================================

        File scriptsDirectory =
                projectManager.getScriptsDirectory();

        if (
                scriptsDirectory == null
                        || !scriptsDirectory.isDirectory()
        ) {

            throw new IllegalStateException(
                    "找不到 scripts 目录"
            );
        }


        // =====================================================
        // 获取 resources
        // =====================================================

        File resourcesDirectory =
                projectManager.getResourcesDirectory();

        if (
                resourcesDirectory == null
                        || !resourcesDirectory.isDirectory()
        ) {

            throw new IllegalStateException(
                    "找不到 resources 目录"
            );
        }


        // =====================================================
        // 创建游戏设置
        // =====================================================

        GameSettings settings =
                new GameSettings();


        // =====================================================
        // 启动 Runtime
        // =====================================================

        return GameLauncher.launch(
                mainScript,
                scriptsDirectory,
                resourcesDirectory,
                settings
        );
    }
}