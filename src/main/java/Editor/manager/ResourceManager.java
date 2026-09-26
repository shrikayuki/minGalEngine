package main.java.Editor.manager;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

public class ResourceManager {

    private final ProjectManager projectManager;

    /**
     * 可以直接在编辑器中编辑的文本资源
     */
    private static final Set<String> TEXT_EXTENSIONS =
            Set.of(
                    ".txt",
                    ".md",
                    ".json",
                    ".xml",
                    ".yaml",
                    ".yml",
                    ".csv",
                    ".ini",
                    ".cfg",
                    ".properties"
            );


    public ResourceManager(
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
    // Create File
    // =========================================================

    public File createFile(
            File parentDirectory,
            String fileName
    ) throws IOException {

        checkProject();

        checkResourceDirectory(
                parentDirectory
        );

        validateName(
                fileName
        );


        File file =
                new File(
                        parentDirectory,
                        fileName.trim()
                );


        checkInsideResources(
                file
        );


        if (file.exists()) {

            throw new IllegalArgumentException(
                    "资源已经存在："
                            + file.getName()
            );
        }


        if (!file.createNewFile()) {

            throw new IOException(
                    "无法创建资源文件："
                            + file.getAbsolutePath()
            );
        }


        return file;
    }


    // =========================================================
    // Create Directory
    // =========================================================

    public File createDirectory(
            File parentDirectory,
            String directoryName
    ) throws IOException {

        checkProject();

        checkResourceDirectory(
                parentDirectory
        );

        validateName(
                directoryName
        );


        File directory =
                new File(
                        parentDirectory,
                        directoryName.trim()
                );


        checkInsideResources(
                directory
        );


        if (directory.exists()) {

            throw new IllegalArgumentException(
                    "资源目录已经存在："
                            + directory.getName()
            );
        }


        if (!directory.mkdirs()) {

            throw new IOException(
                    "无法创建资源目录："
                            + directory.getAbsolutePath()
            );
        }


        return directory;
    }


    // =========================================================
    // Read Text
    // =========================================================

    public String readTextFile(
            File file
    ) throws IOException {

        checkResourceFile(
                file
        );

        if (!isTextFile(file)) {

            throw new IllegalArgumentException(
                    "该资源不是文本文件："
                            + file.getName()
            );
        }


        return Files.readString(
                file.toPath(),
                StandardCharsets.UTF_8
        );
    }


    // =========================================================
    // Save Text
    // =========================================================

    public void saveTextFile(
            File file,
            String content
    ) throws IOException {

        checkResourceFile(
                file
        );

        if (!isTextFile(file)) {

            throw new IllegalArgumentException(
                    "该资源不是文本文件："
                            + file.getName()
            );
        }


        if (content == null) {

            content = "";
        }


        Files.writeString(
                file.toPath(),
                content,
                StandardCharsets.UTF_8
        );
    }


    // =========================================================
    // Rename
    // =========================================================

    public File rename(
            File file,
            String newName
    ) throws IOException {

        checkResourceFile(
                file
        );

        validateName(
                newName
        );


        File newFile =
                new File(
                        file.getParentFile(),
                        newName.trim()
                );


        checkInsideResources(
                newFile
        );


        if (newFile.exists()) {

            throw new IllegalArgumentException(
                    "目标名称已经存在："
                            + newFile.getName()
            );
        }


        Files.move(
                file.toPath(),
                newFile.toPath()
        );


        return newFile;
    }


    // =========================================================
    // Delete
    // =========================================================

    public void delete(
            File file
    ) throws IOException {

        checkResourceFile(
                file
        );


        // 删除目录时递归删除
        if (file.isDirectory()) {

            deleteDirectory(
                    file
            );

            return;
        }


        Files.delete(
                file.toPath()
        );
    }


    private void deleteDirectory(
            File directory
    ) throws IOException {

        File[] children =
                directory.listFiles();


        if (children != null) {

            for (File child : children) {

                if (child.isDirectory()) {

                    deleteDirectory(child);

                } else {

                    Files.delete(
                            child.toPath()
                    );
                }
            }
        }


        Files.delete(
                directory.toPath()
        );
    }


    // =========================================================
    // Text File 判断
    // =========================================================

    public boolean isTextFile(
            File file
    ) {

        if (
                file == null
                        || !file.isFile()
        ) {

            return false;
        }


        String name =
                file.getName()
                        .toLowerCase();


        for (String extension :
                TEXT_EXTENSIONS) {

            if (name.endsWith(extension)) {

                return true;
            }
        }


        return false;
    }


    // =========================================================
    // 校验
    // =========================================================

    private void checkProject() {

        if (!projectManager.hasProject()) {

            throw new IllegalStateException(
                    "当前没有打开项目"
            );
        }
    }


    private void checkResourceDirectory(
            File directory
    ) {

        if (
                directory == null
                        || !directory.isDirectory()
        ) {

            throw new IllegalArgumentException(
                    "无效的资源目录"
            );
        }


        checkInsideResources(
                directory
        );
    }


    private void checkResourceFile(
            File file
    ) {

        checkProject();


        if (
                file == null
                        || !file.exists()
        ) {

            throw new IllegalArgumentException(
                    "无效的资源"
            );
        }


        checkInsideResources(
                file
        );
    }


    private void checkInsideResources(
            File file
    ) {

        File resourcesRoot =
                projectManager
                        .getResourcesDirectory();


        if (
                resourcesRoot == null
                        || !resourcesRoot.isDirectory()
        ) {

            throw new IllegalStateException(
                    "resources 目录不存在"
            );
        }


        Path rootPath =
                resourcesRoot
                        .getAbsoluteFile()
                        .toPath()
                        .normalize();


        Path targetPath =
                file.getAbsoluteFile()
                        .toPath()
                        .normalize();


        if (
                !targetPath.startsWith(
                        rootPath
                )
        ) {

            throw new IllegalArgumentException(
                    "该文件不属于当前项目 resources 目录"
            );
        }
    }


    private void validateName(
            String name
    ) {

        if (
                name == null
                        || name.isBlank()
        ) {

            throw new IllegalArgumentException(
                    "名称不能为空"
            );
        }


        String value =
                name.trim();


        if (
                value.equals(".")
                        || value.equals("..")
                        || value.contains("/")
                        || value.contains("\\")
        ) {

            throw new IllegalArgumentException(
                    "名称包含非法字符"
            );
        }
    }
}