package vtor_kolokvium.composite;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*

Composite design pattern:
->  Component (interface iFile)
    - The abstract base class or interface declaring operations for both individual objects and composites

->  Leaf (File)
    - Represents individual objects that have no children

->  Composite (Folder)
    - Represents objects that can have child components and implements methods to manage these children.

---------------------------------------------------------------------------------------------------------

zadaca 3 od tie za vezbanje

*/


class FileNameExistsException extends Exception{
    public FileNameExistsException(String message){
        super("There is already a file named " + message + " in the folder test");
    }
}

// =======================
// Component
// =======================
interface iFile{
    String getFileName();
    long getFileSize();
    String getFileInfo(int level);
    void sortBySize();
    int findLargestFile ();
}


// =======================
// Leaf
// =======================
class File implements iFile{
    private String fileName;
    private long fileSize;

    public File(String fileName, long fileSize) {
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public long getFileSize() {
        return fileSize;
    }

    @Override
    public String getFileInfo(int level) {
        return "    ".repeat(level) + String.format("File Name: %10s File Size: %10d\n", fileName, fileSize);
    }

    @Override
    public void sortBySize() {
        return;
    }

    @Override
    public int findLargestFile() {
        return 0;
    }
}


// =======================
// Composite
// =======================
class Folder implements iFile{

    private String fileName;
    private List<iFile> files;

    public Folder(String fileName) {
        this.fileName = fileName;
        this.files = new ArrayList<>();
    }

    public void addFile(iFile file) throws FileNameExistsException {
        for (iFile f : files) {
            if (f.getFileName().equals(file.getFileName())) {
                throw new FileNameExistsException(fileName);
            }
        }
        files.add(file);
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public long getFileSize() {
        long sum = 0;
        for (iFile f : files) {
            sum += f.getFileSize();
        }
        return sum;
    }

    @Override
    public String getFileInfo(int level) {
        StringBuilder sb = new StringBuilder();
        sb.append("    ".repeat(level)).append(String.format("Folder Name: %10s File Size: %10d\n", fileName, getFileSize()));
        for (iFile file : files) {
            sb.append(file.getFileInfo(level + 1));
        }
        return sb.toString();
    }

    @Override
    public void sortBySize() {
        return;
    }

    @Override
    public int findLargestFile() {
        return 0;
    }
}


class FileSystem{
    private Folder root;

    public FileSystem() {
        root = new Folder("root");
    }

    public void addFile (iFile file) throws FileNameExistsException {
        root.addFile(file);
    }

    public long findLargestFile (){
        return 0;
    }

    public void sortBySize(){
        return;
    }

    @Override
    public String toString() {
        return root.getFileInfo(0);
    }
}


public class FileSystemTest {

    public static Folder readFolder (Scanner sc)  {

        Folder folder = new Folder(sc.nextLine());
        int totalFiles = Integer.parseInt(sc.nextLine());

        for (int i=0;i<totalFiles;i++) {
            String line = sc.nextLine();

            if (line.startsWith("0")) {
                String fileInfo = sc.nextLine();
                String [] parts = fileInfo.split("\\s+");
                try {
                    folder.addFile(new File(parts[0], Long.parseLong(parts[1])));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
            else {
                try {
                    folder.addFile(readFolder(sc));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return folder;
    }

    public static void main(String[] args)  {

        //file reading from input

        Scanner sc = new Scanner (System.in);

        System.out.println("===READING FILES FROM INPUT===");
        FileSystem fileSystem = new FileSystem();
        try {
            fileSystem.addFile(readFolder(sc));
        } catch (FileNameExistsException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("===PRINTING FILE SYSTEM INFO===");
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING FILE SYSTEM INFO AFTER SORTING===");
        fileSystem.sortBySize();
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING THE SIZE OF THE LARGEST FILE IN THE FILE SYSTEM===");
        System.out.println(fileSystem.findLargestFile());
    }
}