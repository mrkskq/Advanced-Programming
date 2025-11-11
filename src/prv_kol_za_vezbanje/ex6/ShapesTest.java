package prv_kol_za_vezbanje.ex6;

import java.util.ArrayList;
import java.util.Scanner;

enum Color {
    RED, GREEN, BLUE
}

interface Scalable{
    void scale(float scaleFactor);
}

interface Stackable{
    float weight();
}

abstract class Shape implements Scalable, Stackable{

    private String id;
    private Color color;

    public Shape(String id, Color color) {
        this.id = id;
        this.color = color;
    }

    public abstract void scale(float scaleFactor);
    public abstract float weight();
    public String getId(){
        return id;
    }
    public String getColor(){
        return color.toString();
    }
}

class Circle extends Shape{

    private float radius;

    public Circle(String id, Color color) {
        super(id, color);
    }

    public Circle(String id, Color color, float radius) {
        super(id, color);
        this.radius = radius;
    }

    @Override
    public void scale(float scaleFactor) {
        radius *= scaleFactor;
    }

    @Override
    public float weight() {
        return (float) (radius * radius * Math.PI);
    }

    @Override
    public String toString() {
        return String.format("C: %-5s%-10s%10.2f", getId(), getColor(), weight());
    }
}

class Rectangle extends Shape{

    private float width;
    private float height;

    public Rectangle(String id, Color color) {
        super(id, color);
    }

    public Rectangle(String id, Color color, float width, float height) {
        super(id, color);
        this.width = width;
        this.height = height;
    }

    @Override
    public void scale(float scaleFactor) {
        width *= scaleFactor;
        height *= scaleFactor;
    }

    @Override
    public float weight() {
        return width * height;
    }

    @Override
    public String toString() {
        return String.format("R: %-5s%-10s%10.2f", getId(), getColor(), weight());
    }
}

class Canvas {
    private String id;
    private Color color;
    private ArrayList<Shape> shapes = new ArrayList<Shape>();

    public void add(String id, Color color, float radius) {
        Circle circle = new Circle(id, color, radius);
        int position = 0;
        while(position < shapes.size() && shapes.get(position).weight() >= circle.weight()) {
            position++;
        }
        shapes.add(position, circle);
    }

    void add(String id, Color color, float width, float height){
        Rectangle rectangle = new Rectangle(id, color, width, height);
        int position = 0;
        while(position < shapes.size() && shapes.get(position).weight() >= rectangle.weight()) {
            position++;
        }
        shapes.add(position, rectangle);
    }

    void scale(String id, float scaleFactor){
        for (Shape shape : shapes) {
            if (shape.getId().equals(id)) {
                shapes.remove(shape);
                shape.scale(scaleFactor);

                int position = 0;
                while(position < shapes.size() && shapes.get(position).weight() >= shape.weight()) {
                    position++;
                }
                shapes.add(position, shape);
                break;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Shape shape : shapes) {
            sb.append(shape);
            sb.append("\n");
        }
        return sb.toString();
    }
}

public class ShapesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Canvas canvas = new Canvas();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int type = Integer.parseInt(parts[0]);
            String id = parts[1];
            if (type == 1) {
                Color color = Color.valueOf(parts[2]);
                float radius = Float.parseFloat(parts[3]);
                canvas.add(id, color, radius);
            } else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
                float width = Float.parseFloat(parts[3]);
                float height = Float.parseFloat(parts[4]);
                canvas.add(id, color, width, height);
            } else if (type == 3) {
                float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("ORIGNAL:");
                System.out.print(canvas);
                canvas.scale(id, scaleFactor);
                System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
                System.out.print(canvas);
            }

        }
    }
}

