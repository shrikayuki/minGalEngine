package main.java.Editor.manager;

import main.java.Editor.constant.EditorConstants;

import java.io.File;
import java.io.IOException;

public class ProjectManager {

    /**
     * 当前打开的项目
     */
    private File projectRoot;


    // =========================================================
    // 创建项目
    // =========================================================

    public void createProject(
            File parentDirectory,
            String projectName
    ) throws IOException {

        if (parentDirectory == null) {

            throw new IllegalArgumentException(
                    "项目位置不能为空"
            );
        }


        if (!parentDirectory.isDirectory()) {

            throw new IllegalArgumentException(
                    "项目位置无效"
            );
        }


        if (
                projectName == null
                        || projectName.isBlank()
        ) {

            throw new IllegalArgumentException(
                    "项目名称不能为空"
            );
        }


        projectName =
                projectName.trim();


        // =====================================================
        // 项目根目录
        // =====================================================

        File projectDirectory =
                new File(
                        parentDirectory,
                        projectName
                );


        // =====================================================
        // 项目已经存在
        // =====================================================

        if (projectDirectory.exists()) {

            throw new IllegalArgumentException(
                    "项目已经存在："
                            + projectName
            );
        }


        // =====================================================
        // 创建项目目录
        // =====================================================

        if (!projectDirectory.mkdirs()) {

            throw new IOException(
                    "无法创建项目目录："
                            + projectDirectory.getAbsolutePath()
            );
        }


        // =====================================================
        // scripts
        // =====================================================

        File scriptsDirectory =
                new File(
                        projectDirectory,
                        EditorConstants.SCRIPTS_DIR
                );


        createDirectory(
                scriptsDirectory
        );


        // =====================================================
        // resources
        // =====================================================

        File resourcesDirectory =
                new File(
                        projectDirectory,
                        EditorConstants.RESOURCES_DIR
                );


        createDirectory(
                resourcesDirectory
        );


        // =====================================================
        // resources/bg
        // =====================================================

        createDirectory(
                new File(
                        resourcesDirectory,
                        EditorConstants.BG_DIR
                )
        );


        // =====================================================
        // resources/char
        // =====================================================

        createDirectory(
                new File(
                        resourcesDirectory,
                        EditorConstants.CHAR_DIR
                )
        );


        // =====================================================
        // resources/audio
        // =====================================================

        File audioDirectory =
                new File(
                        resourcesDirectory,
                        "audio"
                );


        createDirectory(
                audioDirectory
        );


        // =====================================================
        // resources/audio/bgm
        // =====================================================

        createDirectory(
                new File(
                        audioDirectory,
                        EditorConstants.BGM_DIR
                )
        );


        // =====================================================
        // resources/audio/voice
        // =====================================================

        createDirectory(
                new File(
                        audioDirectory,
                        EditorConstants.VOICE_DIR
                )
        );


        // =====================================================
        // main.txt
        // =====================================================

        File mainScript =
                new File(
                        scriptsDirectory,
                        EditorConstants.MAIN_SCRIPT
                );


        if (!mainScript.createNewFile()) {

            throw new IOException(
                    "无法创建 "
                            + EditorConstants.MAIN_SCRIPT
            );
        }


        // =====================================================
        // resource.txt
        // =====================================================

        File resourceScript =
                new File(
                        resourcesDirectory,
                        EditorConstants.RESOURCE_SCRIPT
                );


        if (!resourceScript.createNewFile()) {

            throw new IOException(
                    "无法创建 "
                            + EditorConstants.RESOURCE_SCRIPT
            );
        }


        // =====================================================
        // 设置当前项目
        // =====================================================

        projectRoot =
                projectDirectory;
    }


    // =========================================================
    // 打开已有项目
    // =========================================================

    public void openProject(
            File projectRoot
    ) {

        if (
                projectRoot == null
                        || !projectRoot.isDirectory()
        ) {

            throw new IllegalArgumentException(
                    "无效的项目目录"
            );
        }


        this.projectRoot =
                projectRoot;
    }


    // =========================================================
    // 当前项目
    // =========================================================

    public File getProjectRoot() {

        return projectRoot;
    }


    public boolean hasProject() {

        return projectRoot != null;
    }


    // =========================================================
    // scripts
    // =========================================================

    public File getScriptsDirectory() {

        if (!hasProject()) {

            return null;
        }


        return new File(
                projectRoot,
                EditorConstants.SCRIPTS_DIR
        );
    }


    // =========================================================
    // resources
    // =========================================================

    public File getResourcesDirectory() {

        if (!hasProject()) {

            return null;
        }


        return new File(
                projectRoot,
                EditorConstants.RESOURCES_DIR
        );
    }


    // =========================================================
    // main.txt
    // =========================================================

    public File getMainScript() {

        File scripts =
                getScriptsDirectory();


        if (scripts == null) {

            return null;
        }


        return new File(
                scripts,
                EditorConstants.MAIN_SCRIPT
        );
    }


    // =========================================================
    // resource.txt
    // =========================================================

    public File getResourceScript() {

        File resources =
                getResourcesDirectory();


        if (resources == null) {

            return null;
        }


        return new File(
                resources,
                EditorConstants.RESOURCE_SCRIPT
        );
    }


    // =========================================================
    // 工具方法
    // =========================================================

    private void createDirectory(
            File directory
    ) throws IOException {

        if (
                !directory.mkdirs()
                        && !directory.isDirectory()
        ) {

            throw new IOException(
                    "无法创建目录："
                            + directory.getAbsolutePath()
            );
        }
    }
}