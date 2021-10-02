//package Canvas_2;

import java.io.*;
import java.util.*;

class IrregularCanvasException extends Exception{
    public IrregularCanvasException(String id,double maxArea){
        super(String.format("Canvas %s has a shape with area larger than %.2f",id,maxArea));
    }
}
enum TYPE {
    CIRCLE,
    SQUARE
}
abstract class Shape{
    int size;

    public Shape(int size){
        this.size = size;
    }

    abstract double area();

    abstract TYPE getType();

    public static Shape createShape(int size, char type, double maxArea){
        switch (type){
            case 'S':
                return new Square(size);
            case 'C':
                return new Circle(size);
            default:
                return null;
        }
    }
}

class Circle extends Shape{

    public Circle(int size) {
        super(size);
    }

    @Override
    double area() {
        return size*size*Math.PI;
    }

    @Override
    TYPE getType() {
        return TYPE.CIRCLE;
    }
}

class Square extends Shape{

    public Square(int size) {
        super(size);
    }

    @Override
    double area() {
        return size*size;
    }

    @Override
    TYPE getType() {
        return TYPE.SQUARE;
    }
}

class Canvas implements Comparable<Canvas> {
    String id;
    List<Shape> shapes;

    public Canvas(String id, List<Shape> shapes) {
        this.id = id;
        this.shapes = shapes;
    }


    @Override
    public int compareTo(Canvas o) {
        return Double.compare(
                this.shapes.stream().mapToDouble(m->m.area()).sum(),
                o.shapes.stream().mapToDouble(m->m.area()).sum()
        );
    }

    public int circlesCount(){
        return (int) shapes.stream().filter(s->s.getType().equals(TYPE.CIRCLE)).count();
    }

    @Override
    public String toString() {
        //ID total_shapes total_circles total_squares min_area max_area average_area
        DoubleSummaryStatistics dss = shapes.stream().mapToDouble(i->i.area()).summaryStatistics();
        return String.format("%s %d %d %d %.2f %.2f %.2f",
                id,
                shapes.size(),
                circlesCount(),
                shapes.size()-circlesCount(),
                dss.getMin(),
                dss.getMax(),
                dss.getAverage());
    }

    public static Canvas createCanvas(String line, double maxArea) throws IrregularCanvasException {

        String[] parts = line.split("\\s+");
        String id = parts[0];
        List<Shape> shapes = new ArrayList<>();

        for (int i=1;i<parts.length;i+=2){
            Shape s = Shape.createShape(Integer.parseInt(parts[i+1]), parts[i].charAt(0),maxArea);
            if (s.area() > maxArea)
                throw new IrregularCanvasException(id,maxArea);
            shapes.add(Shape.createShape(Integer.parseInt(parts[i+1]),parts[i].charAt(0),maxArea));
        }
        return new Canvas(id,shapes);
    }
}

class ShapesApplication {
    List<Canvas> canvas;
    double maxArea;

    public ShapesApplication(double maxArea) {
        this.maxArea = maxArea;
        this.canvas = new ArrayList<>();
    }

    public void readCanvases (InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        while ((line=br.readLine())!=null) {

            try {
                canvas.add(Canvas.createCanvas(line,maxArea));
            }catch (IrregularCanvasException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public void printCanvases (OutputStream os){
        PrintWriter pw = new PrintWriter(os);

        canvas.stream().sorted(Comparator.reverseOrder()).forEach(pw::println);
        pw.flush();
        pw.close();
    }
}


public class Shapes2Test {

    public static void main(String[] args) throws IOException {

        ShapesApplication shapesApplication = new ShapesApplication(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
        shapesApplication.readCanvases(System.in);

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);


    }
}
