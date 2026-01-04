package labs.lab9;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


interface XMLComponent{
    void addAttribute(String key, String value);
    String print(int level);
}

class XMLLeaf implements XMLComponent{
    private String tagName;
    private String value;
    private Map<String, String> attributes;

    public XMLLeaf(String tagName, String value) {
        this.tagName = tagName;
        this.value = value;
        this.attributes = new LinkedHashMap<>();
    }

    @Override
    public void addAttribute(String key, String value) {
        attributes.put(key, value);
    }

    @Override
    public String print(int level) {
        String indentStr = "    ".repeat(level);

        String attrString = attributes.entrySet()
                .stream()
                .map(e -> e.getKey() + "=\"" + e.getValue() + "\"") // primer key=type, value=redoven -> type="redoven"
                .collect(Collectors.joining(" "));

        if (!attrString.isEmpty()) {
            attrString = " " + attrString;
        }

        return indentStr + "<" + tagName + attrString + ">" + value + "</" + tagName + ">";
    }
}

class XMLComposite  implements XMLComponent{
    private String tagName;
    private Map<String, String> attributes;
    private List<XMLComponent> components;

    public XMLComposite(String tagName) {
        this.tagName = tagName;
        this.attributes = new LinkedHashMap<>();
        this.components = new ArrayList<>();
    }

    public void addComponent(XMLComponent component){
        this.components.add(component);
    }

    @Override
    public void addAttribute(String key, String value) {
        attributes.put(key, value);
    }

    @Override
    public String print(int level) {
        String indentStr = "    ".repeat(level);

        String attrString = attributes.entrySet()
                .stream()
                .map(e -> e.getKey() + "=\"" + e.getValue() + "\"")
                .collect(Collectors.joining(" "));

        if (!attrString.isEmpty()) {
            attrString = " " + attrString;
        }

        if (components.isEmpty()) {
            return indentStr + "<" + tagName + attrString + "></" + tagName + ">";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(indentStr).append("<").append(tagName).append(attrString).append(">\n");
        for (XMLComponent child : components) {
            sb.append(child.print(level+1));
            sb.append("\n");
        }
        sb.append(indentStr).append("</").append(tagName).append(">");
        return sb.toString();
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

