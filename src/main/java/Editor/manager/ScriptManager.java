package main.java.Editor.manager;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class ScriptManager {

    private final ProjectManager projectManager;


    // =========================================================
    // 构造
    // =========================================================

    public ScriptManager(
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
    // Create
    // =========================================================

    public File createScript(
            String scriptName
    ) throws IOException {

        checkProject();

        if (
                scriptName == null
                        || scriptName.isBlank()
        ) {

            throw new IllegalArgumentException(
                    "脚本名称不能为空"
            );
        }

        scriptName =
                scriptName.trim();


        // 自动补 .txt

        if (
                !scriptName
                        .toLowerCase()
                        .endsWith(".txt")
        ) {

            scriptName += ".txt";
        }


        File script =
                new File(
                        projectManager.getScriptsDirectory(),
                        scriptName
                );


        if (script.exists()) {

            throw new IllegalArgumentException(
                    "脚本已经存在："
                            + script.getName()
            );
        }


        if (!script.createNewFile()) {

            throw new IOException(
                    "无法创建脚本："
                            + script.getName()
            );
        }


        return script;
    }


    // =========================================================
    // Read
    // =========================================================

    public String readScript(
            File script
    ) throws IOException {

        checkScript(script);

        return Files.readString(
                script.toPath(),
                StandardCharsets.UTF_8
        );
    }


    // =========================================================
    // Update
    // =========================================================

    public void saveScript(
            File script,
            String content
    ) throws IOException {

        checkScript(script);

        if (content == null) {

            content = "";
        }

        Files.writeString(
                script.toPath(),
                content,
                StandardCharsets.UTF_8
        );
    }


    // =========================================================
    // Delete
    // =========================================================

    public void deleteScript(
            File script
    ) throws IOException {

        checkScript(script);

        if (
                script.getName()
                        .equalsIgnoreCase(
                                "main.txt"
                        )
        ) {

            throw new IllegalArgumentException(
                    "main.txt 是项目入口脚本，不能删除"
            );
        }

        Files.delete(
                script.toPath()
        );
    }


    // =========================================================
    // Rename
    // =========================================================

    public File renameScript(
            File script,
            String newName
    ) throws IOException {

        checkScript(script);

        if (
                newName == null
                        || newName.isBlank()
        ) {

            throw new IllegalArgumentException(
                    "新脚本名称不能为空"
            );
        }

        newName =
                newName.trim();


        if (
                !newName
                        .toLowerCase()
                        .endsWith(".txt")
        ) {

            newName += ".txt";
        }


        if (
                script.getName()
                        .equalsIgnoreCase(
                                "main.txt"
                        )
        ) {

            throw new IllegalArgumentException(
                    "main.txt 是项目入口脚本，暂时不能重命名"
            );
        }


        File newScript =
                new File(
                        script.getParentFile(),
                        newName
                );


        if (newScript.exists()) {

            throw new IllegalArgumentException(
                    "脚本已经存在："
                            + newScript.getName()
            );
        }


        Files.move(
                script.toPath(),
                newScript.toPath()
        );


        return newScript;
    }


    // =========================================================
    // 校验项目
    // =========================================================

    private void checkProject() {

        if (
                !projectManager.hasProject()
        ) {

            throw new IllegalStateException(
                    "当前没有打开项目"
            );
        }
    }


    // =========================================================
    // 校验脚本
    // =========================================================

    private void checkScript(
            File script
    ) {

        checkProject();


        if (
                script == null
                        || !script.isFile()
        ) {

            throw new IllegalArgumentException(
                    "无效的脚本文件"
            );
        }


        File scriptsDirectory =
                projectManager
                        .getScriptsDirectory();


        if (
                scriptsDirectory == null
                        || script.getParentFile() == null
                        || !script.getParentFile().equals(
                                scriptsDirectory
                        )
        ) {

            throw new IllegalArgumentException(
                    "该文件不是项目脚本"
            );
        }


        if (
                !script.getName()
                        .toLowerCase()
                        .endsWith(".txt")
        ) {

            throw new IllegalArgumentException(
                    "不是有效的脚本文件"
            );
        }
    }
}