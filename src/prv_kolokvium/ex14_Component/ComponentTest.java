package prv_kolokvium.ex14_Component;

import com.sun.source.tree.Tree;

import java.util.*;
import java.util.stream.Collectors;

class InvalidPositionException extends Exception {
    public InvalidPositionException(String message) {
        super(message);
    }
}

class Component{
    private String color;
    private int weight;
    private Collection<Component> components;

    public Component(String color, int weight) {
        this.color = color;
        this.weight = weight;
        this.components = new ArrayList<Component>();
    }

    public void addComponent(Component component){
        this.components.add(component);
        components = components.stream()
                .sorted(Comparator.comparing(Component::getWeight).thenComparing(Component::getColor))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public String getColor() {
        return color;
    }

    public int getWeight() {
        return weight;
    }

    public static final Comparator<Component> byColor = Comparator.comparing(Component::getColor);
    public static final Comparator<Component> byWeight = Comparator.comparing(Component::getWeight);

    public void setColor(String color) {
        this.color = color;
    }

    public String toString(int level) {
        StringBuilder sb = new StringBuilder();
        //repeat e vgradena funkcija za string
        sb.append("---".repeat(level)).append(String.format("%d:%s\n", weight, color));

        for (Component component : components) {
            sb.append(component.toString(level + 1));
        }
        return sb.toString();
    }

    public void changeColorRecursively(int weight, String color) {
        if (this.weight < weight){
            this.color = color;
        }
        for (Component c : components) {
            c.changeColorRecursively(weight, color);
        }
    }
}

class Window{
    private String name;
    private TreeMap<Integer, Component> components;

    public Window(String name){
        this.name = name;
        this.components = new TreeMap<Integer, Component>();
    }

    public void addComponent(int position, Component component) throws InvalidPositionException {
        Component c = components.get(position);
        if (c == null){
            components.put(position, component);
        }
        else{
            throw new InvalidPositionException("Invalid position "+position+", alredy taken!");
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("WINDOW ").append(name).append("\n");
        int counter = 1;
        for (Component c : components.values()) {
            sb.append(counter++).append(":").append(c.toString(0));
        }
        return sb.toString();
    }

    public void changeColor(int weight, String color){
        for (Component c : components.values()) {
            c.changeColorRecursively(weight, color);
        }
    }

    public void swichComponents(int pos1, int pos2){
        Component c1 = components.getOrDefault(pos1, null);
        Component c2 = components.getOrDefault(pos2, null);

        if (c1 != null && c2 != null){
            components.replace(pos1, c2);
            components.replace(pos2, c1);
        }
    }
}

public class ComponentTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        Window window = new Window(name);
        Component prev = null;
        while (true) {
            try {
                int what = scanner.nextInt();
                scanner.nextLine();
                if (what == 0) {
                    int position = scanner.nextInt();
                    window.addComponent(position, prev);
                } else if (what == 1) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev = component;
                } else if (what == 2) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                    prev = component;
                } else if (what == 3) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                } else if(what == 4) {
                    break;
                }

            } catch (InvalidPositionException e) {
                System.out.println(e.getMessage());
            }
            scanner.nextLine();
        }

        System.out.println("=== ORIGINAL WINDOW ===");
        System.out.println(window);
        int weight = scanner.nextInt();
        scanner.nextLine();
        String color = scanner.nextLine();
        window.changeColor(weight, color);
        System.out.println(String.format("=== CHANGED COLOR (%d, %s) ===", weight, color));
        System.out.println(window);
        int pos1 = scanner.nextInt();
        int pos2 = scanner.nextInt();
        System.out.println(String.format("=== SWITCHED COMPONENTS %d <-> %d ===", pos1, pos2));
        window.swichComponents(pos1, pos2);
        System.out.println(window);
    }
}

// вашиот код овде