package vtor_kolokvium.decorator;

import java.util.*;
import java.util.stream.Collectors;


// Interface (Coffee)
interface Document {
    String getText();
}

// class (Espresso)
class BaseDocument implements Document {
    private String text;

    public BaseDocument(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }
}

// abstract class decorator (CoffeeDecorator -> Coffee coffee)
abstract class DocumentDecorator implements Document {
    protected Document document;

    public DocumentDecorator(Document document) {
        this.document = document;
    }
}


// decorators (MilkDecorator)
class LineNumberDecorator extends DocumentDecorator {
    public LineNumberDecorator(Document document) {
        super(document);
    }

    @Override
    public String getText() {
        StringBuilder sb = new StringBuilder();
        String[] lines = document.getText().split("\n");
        for (int i = 0; i < lines.length; i++) {
            sb.append(i+1 + ": " + lines[i]);
            if (i < lines.length-1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}

class WordCountDecorator extends DocumentDecorator {
    public WordCountDecorator(Document document) {
        super(document);
    }

    @Override
    public String getText() {
        StringBuilder sb = new StringBuilder();
        int words = document.getText().split("\\s+").length;
        sb.append(document.getText());
        sb.append("\nWords: " + words);
        return sb.toString();
    }
}


class RedactionDecorator extends DocumentDecorator {
    private List<String> forbiddenWords;

    public RedactionDecorator(Document document, List<String> forbiddenWords) {
        super(document);
        this.forbiddenWords = forbiddenWords;
    }

    @Override
    public String getText() {
        StringBuilder sb = new StringBuilder();
        String[] lines = document.getText().split("\n");
        for (String line : lines) {
            String[] words = line.split("\\s+");
            for (String word : words) {
                if (forbiddenWords.stream().map(f -> f.toLowerCase()).collect(Collectors.toList()).contains(word.toLowerCase())) {
                    sb.append("* ");
                }
                else {
                    sb.append(word + " ");
                }
            }
            sb.append("\n");
        }
        return sb.toString().substring(0, sb.length() - 1);
    }
}

class DocumentViewer {
    private Map<String, Document> documents;

    public DocumentViewer() {
        documents = new HashMap<>();
    }

    public void addDocument(String id, String text) {
        documents.put(id, new BaseDocument(text));
    }

    public void enableLineNumbers(String id) {
        documents.put(id, new LineNumberDecorator(documents.get(id)));
    }

    public void enableWordCount(String id) {
        documents.put(id, new WordCountDecorator(documents.get(id)));
    }

    public void enableRedaction(String id, List<String> forbiddenWords) {
        documents.put(id, new RedactionDecorator(documents.get(id), forbiddenWords));
    }

    public void display(String id) {
        System.out.print(documents.get(id).getText());
    }
}

// Main class
public class DocumentViewerTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine());

        DocumentViewer viewer = new DocumentViewer();

        for (int i = 0; i < n; i++) {
            String id = sc.nextLine();
            int lines = Integer.parseInt(sc.nextLine());

            StringBuilder text = new StringBuilder();
            for (int j = 0; j < lines; j++) {
                text.append(sc.nextLine());
                if (j < lines - 1) text.append("\n");
            }

            viewer.addDocument(id, text.toString());
        }

        boolean firstDisplay = true;

        while (true) {
            String line = sc.nextLine();
            if (line.equals("exit")) break;

            String[] parts = line.split(" ");
            String command = parts[0];
            String id = parts[1];

            switch (command) {
                case "enableLineNumbers":
                    viewer.enableLineNumbers(id);
                    break;

                case "enableWordCount":
                    viewer.enableWordCount(id);
                    break;

                case "enableRedaction":
                    List<String> forbiddenWords = new ArrayList<>();
                    for (int k = 2; k < parts.length; k++) {
                        forbiddenWords.add(parts[k].toLowerCase());
                    }
                    viewer.enableRedaction(id, forbiddenWords);
                    break;

                case "display":
                    if (!firstDisplay) System.out.println();
                    firstDisplay = false;

                    System.out.println("=== Document " + id + " ===");
                    viewer.display(id);
                    break;
            }
        }
    }
}

