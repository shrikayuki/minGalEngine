package main.java.Editor.tree;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;

public class FileTreeNode
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