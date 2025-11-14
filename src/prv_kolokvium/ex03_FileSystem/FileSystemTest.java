package prv_kolokvium.ex03_FileSystem;

//printot ne e okej so tab

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class FileNameExistsException extends Exception {
    public FileNameExistsException(String message) {
        super(message);
    }
}

interface IFile{
    String getFileName();
    long getFileSize();
    String getFileInfo(String fileName);
    void sortBySize();
    int findLargestFile();
}

class File implements IFile{
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
    public String getFileInfo(String fileName) {
        return String.format("%10s %10s", fileName, fileSize);
    }

    @Override
    public void sortBySize() {
        return;
    }

    @Override
    public int findLargestFile() {
        return 0;
    }

    public String toString(int level) {
        return "    ".repeat(level) + String.format("File name: %10s File size: %10s\n", fileName, getFileSize());
    }
}

class Folder implements IFile{
    private String folderName;
    private long folderSize;
    private List<IFile> fileList;

    public Folder(String folderName) {
        this.folderName = folderName;
        fileList = new ArrayList<>();
    }

    public List<IFile> getFileList() {
        return fileList;
    }

    @Override
    public String getFileName() {
        return folderName;
    }

    @Override
    public long getFileSize() {
        return fileList.stream().mapToLong(IFile::getFileSize).sum();
    }

    @Override
    public String getFileInfo(String fileName) {
        //return String.format("File name: %10s File size: %10s\n", fileName, getFileSize());
        return null;
    }

    @Override
    public void sortBySize() {
        for (IFile f : fileList) {
            if (f instanceof Folder) {
                f.sortBySize();
            }
        }

        fileList.sort(Comparator.comparingLong(IFile::getFileSize));
    }

    @Override
    public int findLargestFile() {
        return (int) getLargestFile();
    }

    public void addFile (IFile file) throws FileNameExistsException {
        for (IFile f : fileList) {
            if (f.getFileName().equals(file.getFileName())) {
                throw new FileNameExistsException("There is already a file named " + file.getFileName() + " in the folder " + folderName);
            }
        }
        fileList.add(file);
    }

    public long getLargestFile(){
        long maxFile = 0;

        for (IFile f : fileList) {
            if (f instanceof Folder) {
                maxFile = Math.max(maxFile, ((Folder) f).getLargestFile());
            }
            else if (f instanceof File) {
                maxFile = Math.max(maxFile, f.getFileSize());
            }
        }
        return maxFile;
    }

    public String toString(int level) {
        StringBuilder sb = new StringBuilder();
        sb.append("    ".repeat(level)).append(String.format("Folder name: %10s Folder size: %10s\n", folderName, getFileSize()));
        for (IFile file : fileList) {
            if (file instanceof Folder)
                sb.append(((Folder) file).toString(level + 1));
            else
                sb.append(((File) file).toString(level + 1));
        }
        return sb.toString();
    }
}

class FileSystem{
    private Folder rootDirectory;

    public FileSystem() {
        rootDirectory = new Folder("root");
    }

    public void addFile (IFile file) throws FileNameExistsException {
        rootDirectory.addFile(file);
    }

    public long findLargestFile (){
        return rootDirectory.getLargestFile();
    }

    public void sortBySize(){
        rootDirectory.sortBySize();
    }

    @Override
    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append(String.format("Folder name: %10s Folder size: %10s\n", rootDirectory.getFileName(), rootDirectory.getFileSize()));
//        for (IFile file : rootDirectory.getFileList()) {
//            sb.append("\\t").append(String.format(file.toString()));
//        }
//        return sb.toString();
        return rootDirectory.toString(0);
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