package Stacked_Canvas;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;


enum Color {
    RED, GREEN, BLUE
}


interface Scalable {
    void scale(float scaleFactor);
}

interface Stackable {
    float weight();
}

abstract class Shape implements Scalable, Stackable{
    private String id;
    private Color color;

    public Shape(String id, Color color) {
        this.id = id;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public Color getColor() {
        return color;
    }

    abstract String getType();
}

class Circle extends Shape{
    private float radius;

    public Circle(String id, Color color, float radius) {
        super(id, color);
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }


    @Override
    public void scale(float scaleFactor) {
        radius = radius * scaleFactor;
    }

    @Override
    public float weight() {
        return (float) (radius * radius * Math.PI);
    }

    @Override
    String getType() {
        return "Circle";
    }

    @Override
    public String toString() {
        return String.format("C: %-5s%-10s%10.2f\n", getId(),getColor().toString(),weight());
    }
}

class Rectangle extends Shape {
    private float width;
    private float height;

    public Rectangle(String id, Color color, float width, float height) {
        super(id, color);
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }


    @Override
    public void scale(float scaleFactor) {
        height = height * scaleFactor;
        width = width * scaleFactor;
    }

    @Override
    public float weight() {
        return height * width;
    }

    @Override
    String getType() {
        return "Rectangle";
    }

    @Override
    public String toString() {
        return String.format("R: %-5s%-10s%10.2f\n", getId(), getColor().toString(), weight());
    }
}

class Canvas {
    List<Shape> shapes;

    public Canvas() {
        this.shapes = new ArrayList<>();
    }

    public void add(String id, Color color, float radius) {
        shapes.add(new Circle(id, color, radius));
        shapes.sort(Comparator.comparing(Shape::weight).reversed());
    }

    public void add(String id, Color color, float width, float height) {
        shapes.add(new Rectangle(id, color, width, height));
        shapes.sort(Comparator.comparing(Shape::weight).reversed());
    }

    public void scale(String id, float scaleFactor) {
        shapes.stream().filter(shape ->
                shape.getId().equals(id)).
                forEach(shape ->
                        shape.scale(scaleFactor));
        shapes.sort(Comparator.comparing(Shape::weight).reversed());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        shapes.forEach(sb::append);
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
