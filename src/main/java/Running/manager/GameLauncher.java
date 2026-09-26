package main.java.Running.manager;

import main.java.Running.parser.GameVM;
import main.java.Running.parser.ScriptParser;
import main.java.Running.parser.ScriptRepository;
import main.java.Running.settings.GameSettings;
import main.java.Running.state.SaveData;
import main.java.Running.window.GameWindow;

import java.io.File;
import java.io.IOException;

public final class GameLauncher {

    private GameLauncher() {
    }


    // =========================================================
    // 启动游戏
    // =========================================================

    public static GameWindow launch(
            File mainScript,
            File scriptsDirectory,
            File resourcesDirectory,
            GameSettings settings
    ) throws IOException {

        // =====================================================
        // 检查入口脚本
        // =====================================================

        if (
                mainScript == null
                        || !mainScript.isFile()
        ) {

            throw new IOException(
                    "入口脚本不存在："
                            + mainScript
            );
        }


        // =====================================================
        // 检查 scripts
        // =====================================================

        if (
                scriptsDirectory == null
                        || !scriptsDirectory.isDirectory()
        ) {

            throw new IOException(
                    "scripts 目录不存在："
                            + scriptsDirectory
            );
        }


        // =====================================================
        // ScriptRepository
        // =====================================================

        ScriptRepository repository =
                new ScriptRepository(
                        scriptsDirectory
                );


        // =====================================================
        // 创建多脚本 VM
        // =====================================================

        GameVM vm =
                new GameVM(
                        repository,
                        mainScript.getName()
                );


        // =====================================================
        // 创建窗口
        // =====================================================

        GameWindow window =
                new GameWindow(
                        vm,
                        settings,
                        resourcesDirectory
                );


        // =====================================================
        // 显示
        // =====================================================

        window.setVisible(
                true
        );


        return window;
    }


    // =========================================================
    // 兼容旧版 launch
    // =========================================================

    /**
     * 保留这个方法，避免项目中其他地方调用旧接口时报错。
     *
     * 默认认为 mainScript 所在目录就是 scripts 目录。
     */
    public static GameWindow launch(
            File mainScript,
            File resourcesDirectory,
            GameSettings settings
    ) throws IOException {

        if (mainScript == null) {

            throw new IOException(
                    "mainScript 不能为空"
            );
        }


        File scriptsDirectory =
                mainScript.getParentFile();


        return launch(
                mainScript,
                scriptsDirectory,
                resourcesDirectory,
                settings
        );
    }


    // =========================================================
    // 读取存档
    // =========================================================

    public static GameWindow launchLoaded(
            File script,
            File resources,
            File saves,
            GameSettings settings,
            SaveData data
    ) throws IOException {

        /*
         * 这里暂时保留原来的单脚本存档逻辑。
         *
         * 因为你现在的 SaveData / GameWindow.loadState()
         * 具体保存了哪些 VM 信息还没改成“脚本名 + PC + 调用栈”。
         *
         * 所以这里先保证原来的存档功能不被破坏。
         */

        GameVM vm =
                new GameVM(
                        new ScriptParser()
                                .parseProgram(
                                        script
                                )
                );


        GameWindow window =
                new GameWindow(
                        vm,
                        settings,
                        resources,
                        saves,
                        false
                );


        window.loadState(
                data
        );


        window.setVisible(
                true
        );


        return window;
    }
}