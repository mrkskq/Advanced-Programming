package vtor_kolokvium.composite;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// interface iFile
interface XMLComponent{
    public void addAttribute(String key, String value);
    public String print(int level);
}

// class File
class XMLLeaf implements XMLComponent{
    private String key;
    private String value;
    private Map<String, String> attributes;

    public XMLLeaf(String key, String value) {
        this.key = key;
        this.value = value;
        this.attributes = new TreeMap<>(Comparator.reverseOrder());
    }

    @Override
    public void addAttribute(String key, String value) {
        attributes.put(key, value);
    }

    @Override
    public String print(int level) {
        StringBuilder sb = new StringBuilder();
        String indent = "    ".repeat(level);
//        <student type="redoven" program="KNI">Trajce Trajkovski</student>

        sb.append(indent).append("<").append(key);
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            sb.append(" ").append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"");
        }
        sb.append(">");

        sb.append(value);

        sb.append("</").append(key).append(">");
        return sb.toString();
    }
}


// class Folder
class XMLComposite implements XMLComponent{
    private String tagName;
    private List<XMLComponent> components;
    private Map<String, String> attributes;

    public XMLComposite(String tagName) {
        this.tagName = tagName;
        this.components = new ArrayList<>();
        this.attributes = new TreeMap<>();
    }

    @Override
    public void addAttribute(String key, String value) {
        attributes.put(key, value);
    }

    @Override
    public String print(int level) {
        StringBuilder sb = new StringBuilder();
        String indent = "    ".repeat(level);
//        <name type="redoven">
//                <first-name>trajce</first-name>
//                <last-name>trajkovski</last-name>
//        </name>

        sb.append(indent).append("<").append(tagName);
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            sb.append(" ").append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"");
        }
        sb.append(">\n");

        for (XMLComponent component : components) {
            sb.append(component.print(level+1)).append("\n");
        }

        sb.append(indent).append("</").append(tagName).append(">");
        return sb.toString();
    }

    public void addComponent(XMLComponent component) {
        components.add(component);
    }
}


public class XMLTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = sc.nextInt();
        XMLComponent component = new XMLLeaf("student", "Trajce Trajkovski");
        component.addAttribute("type", "redoven");
        component.addAttribute("program", "KNI");

        XMLComposite composite = new XMLComposite("name");
        composite.addComponent(new XMLLeaf("first-name", "trajce"));
        composite.addComponent(new XMLLeaf("last-name", "trajkovski"));
        composite.addAttribute("type", "redoven");
        component.addAttribute("program", "KNI");

        if (testCase==1) {
            //TODO Print the component object
            System.out.println(component.print(0));
        } else if(testCase==2) {
            //TODO print the composite object
            System.out.println(composite.print(0));
        } else if (testCase==3) {
            XMLComposite main = new XMLComposite("level1");
            main.addAttribute("level","1");
            XMLComposite lvl2 = new XMLComposite("level2");
            lvl2.addAttribute("level","2");
            XMLComposite lvl3 = new XMLComposite("level3");
            lvl3.addAttribute("level","3");
            lvl3.addComponent(component);
            lvl2.addComponent(lvl3);
            lvl2.addComponent(composite);
            lvl2.addComponent(new XMLLeaf("something", "blabla"));
            main.addComponent(lvl2);
            main.addComponent(new XMLLeaf("course", "napredno programiranje"));

            //TODO print the main object
            System.out.println(main.print(0));
        }
    }
}
