package main.java.Editor.tree;

import java.io.File;
import java.util.Arrays;

public class FileTreeBuilder {


    // =========================================================
    // 创建文件树
    // =========================================================

    public FileTreeNode build(
            File file
    ) {

        FileTreeNode node =
                new FileTreeNode(
                        file
                );


        if (!file.isDirectory()) {

            return node;
        }


        File[] children =
                file.listFiles();


        if (children == null) {

            return node;
        }


        Arrays.sort(
                children,
                (a, b) -> {

                    // 文件夹排前面

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
                    build(child)
            );
        }


        return node;
    }
}