package structuraldesignpattern;

import java.util.List;

public class CompositeDemo {
  public static void main(String[] args) {
    Folder rootFolder = new Folder("Root");
    File file1 = new File("File 1");
    File file2 = new File("File 2");
    rootFolder.add(file1);
    rootFolder.add(file2);

    Folder subFolder1 = new Folder("Subfolder 1");
    File file3 = new File("File 3");
    subFolder1.add(file3);
    rootFolder.add(subFolder1);

    Folder subFolder2 = new Folder("Subfolder 2");
    File file4 = new File("File 4");
    subFolder2.add(file4);
    rootFolder.add(subFolder2);

    rootFolder.display();
  }
    
}
interface FileSystemComponent {
    void display();
}
class Folder implements FileSystemComponent{
    private String name;
    private List<FileSystemComponent> children;

    public Folder(String name) {
        this.name = name;
        this.children = new java.util.ArrayList<>();
    }

    public void add(FileSystemComponent file) {
        children.add(file);
    }

    public void display() {
        System.out.println("Folder: " + name);
        for (FileSystemComponent child : children) {
            child.display();
            }
        }
}
class File implements FileSystemComponent{
    private String name;

    public File(String name) {
        this.name = name;
    }

    public void display() {
        System.out.println("File: " + name);
    }
}
// without composite design pattern, we would have to create separate classes for each type of shape and manage them individually. This can lead to a lot of duplicated code and make it difficult to add new shapes in the future. With the composite design pattern, we can create a single interface for all shapes and manage them as a group, making it easier to add new shapes and manage them as a whole.
// class WithoutComposite {
//     public static void main(String[] args) {
//         Folder folder1 = new Folder("Folder 1");
//         File file1 = new File("File 1");
//         folder1.addFile(file1);
//         Folder folder2 = new Folder("Folder 2");
//         File file2 = new File("File 2");
//         folder2.addFile(file2);
//         folder1.addFolder(folder2);
//         folder1.display();
//         // in this case if any other type comes we have to create a new class for that type and manage it separately. This is not scalable and maintainable.
//     }
// }
// class Folder {
//     private String name;
//     List<Object> children;
//     public Folder(String name) {
//         this.name = name;
//         this.children = new java.util.ArrayList<>();
//     }

//     public void addFile(Object file) {
//         children.add(file);
//     }

//     public void addFolder(Folder folder) {
//         children.add(folder);
//     }

//     public void display() {
//         System.out.println("Folder: " + name);
//         for (Object child : children) {
//             if (child instanceof File) {
//                 ((File) child).display();
//             } else if (child instanceof Folder) {
//                 ((Folder) child).display();
//             }
//         }
//     }
// }
// class File {
//     private String name;
//     public File(String name) {
//         this.name = name;
//     }

//     public void display() {
//         System.out.println("File: " + name);
//     }
// }