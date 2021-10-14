//package Компоненти;

import java.util.Scanner;
import java.util.*;


class InvalidPositionException extends Exception{
    public InvalidPositionException(String message){
        super(message);
    }
}

class Component {
    String color;
    int weight;
    Set<Component> components;

    public Component(String color, int weight) {
        this.color = color;
        this.weight = weight;
        components = new TreeSet<>(
                Comparator.comparing(Component::getWeight)
                        .thenComparing(Component::getColor)
        );
    }

    public String getColor() {
        return color;
    }

    public int getWeight() {
        return weight;
    }

    public void addComponent(Component component){
        components.add(component);
    }

    public String toString(String tab) {
        StringBuilder sb = new StringBuilder();
        sb.append(tab + weight + ":" + color + "\n");
        tab += "---";
        for (Component c : components){
            sb.append(c.toString(tab));
        }
        return sb.toString();
    }

    public void changeColor(String color, int weight){
        if (this.weight < weight) {
            this.color = color;
        }
        for (Component c : components)
            c.changeColor(color,weight);

    }
}

class Window {
    String name;
    Map<Integer,Component> componentMap;

    public Window(String name) {
        this.name = name;
        componentMap = new HashMap<>();
    }


    public void addComponent(int position, Component component) throws InvalidPositionException {
        if (componentMap.containsKey(position))
            throw new InvalidPositionException("Invalid position "+ position + ", alredy taken!");
        componentMap.put(position, component);
    }

    public void changeColor(int weight, String color){
        componentMap.values().stream()
                .forEach(c -> c.changeColor(color,weight));
    }

    public void swichComponents(int pos1, int pos2){
        Component c1 = componentMap.get(pos1);
        Component c2 = componentMap.get(pos2);

        componentMap.put(pos1, c2);
        componentMap.put(pos2, c1);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i=1;

        sb.append("WINDOW ").append(name).append("\n");
        for (Component c : componentMap.values()){
            sb.append(i + ":" + c.toString(""));
            i++;
        }
        return sb.toString();
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
