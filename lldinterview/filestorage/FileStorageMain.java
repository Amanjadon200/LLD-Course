package lldinterview.filestorage;

import java.util.HashMap;
import java.util.Map;

public class FileStorageMain {
    public static void main(String[] args) {
        Directory root = new Directory("/");
        FileSystemController fileSystemController = new FileSystemController(root);
        fileSystemController.createDirectory("/dir1");
        fileSystemController.createDirectory("/dir2");
        System.out.println("Directory created successfully");

        fileSystemController.createFile("/dir1/file1.txt", "Hello World");
        fileSystemController.createFile("/dir2/file2.txt", "Hello Java");
        System.out.println("Root directory children: " + root.children.keySet());
        System.out.println("dir1 children: " + ((Directory)root.children.get("dir1")).children.keySet());
        System.out.println("dir2 children: " + ((Directory)root.children.get("dir2")).children.keySet());
    }
}

class FileSystemController {
    FileSystemNode root;

    public FileSystemController(FileSystemNode root) {
        this.root = root;
    }
    public boolean validatePath(String path) {
        String[] pathParts = path.split("/");
        FileSystemNode node = root;
        for (int i = 1; i < pathParts.length - 1; i++) {
            if(node ==null || !(node instanceof Directory)) {
                return false;
            }
            Map<String, FileSystemNode> children = ((Directory)node).children;
            if (!children.containsKey(pathParts[i])) {
                return false;
            }
            node = children.get(pathParts[i]);
        }
        return true;
    }

    public void createFile(String path, String content) {
        String[] pathParts = path.split("/");
        FileSystemNode node = root;
        if (!validatePath(path)) {
            throw new IllegalArgumentException("Invalid path");
        }
        String fileName = pathParts[pathParts.length - 1];
        for (int i = 1; i < pathParts.length - 1; i++) {
            Map<String, FileSystemNode> children = ((Directory)node).children;
            node = children.get(pathParts[i]);
        }
        if (((Directory)node).children.containsKey(fileName)) {
            throw new IllegalArgumentException("File already exists");
        } else {
            ((Directory)node).children.put(fileName, new File(fileName, content, fileName.substring(fileName.lastIndexOf(".") + 1)));
        }
    }

    public void createDirectory(String path) {
        String[] pathParts = path.split("/");
        FileSystemNode node = root;
        if (!validatePath(path)) {
            throw new IllegalArgumentException("Invalid path");
        }
        String dirName = pathParts[pathParts.length - 1];
        for (int i = 1; i < pathParts.length - 1; i++) {
            Map<String, FileSystemNode> children = ((Directory)node).children;
            node = children.get(pathParts[i]);
        }
        if (((Directory)node)   .children.containsKey(dirName)) {
            throw new IllegalArgumentException("Directory already exists");
        } else {
            ((Directory)node).children.put(dirName, new Directory(dirName));
        }
    }
}

abstract class FileSystemNode {
    String name;

    public FileSystemNode(String name) {
        this.name = name;
    }
}

class File extends FileSystemNode {
    String content;
    String extension;

    public File(String name, String content, String extension) {
        super(name);
        this.extension = extension;
        this.content = content;
    }
}

class Directory extends FileSystemNode {
    String name;
    Map<String, FileSystemNode> children;

    public Directory(String name) {
        super(name);
        children = new HashMap<>();
    }
}