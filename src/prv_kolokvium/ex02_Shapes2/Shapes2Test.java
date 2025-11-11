package prv_kolokvium.ex02_Shapes2;

import java.io.*;
import java.util.ArrayList;

class IrregularCanvasException extends Exception {
    public IrregularCanvasException(String message) {
        super(message);
    }
}

class ShapesApplication{

    private double maxArea;
    private ArrayList<String> lines = new ArrayList<>();

    public ShapesApplication(double maxArea){
        this.maxArea = maxArea;
    }

    private double getArea(String index, char type, int x) throws IrregularCanvasException {
        double area = 0;
        if (type == 'C'){
            area = x * x * Math.PI;
        }
        else if (type == 'S'){
            area = x * x;
        }
        if (area > maxArea){
            throw new IrregularCanvasException(String.format("Canvas %s has a shape with area larger than %.2f", index, maxArea));
        }
        return area;
    }

    private int getTotalAreaPerLine(String line){
        String [] parts = line.split("\\s+");
        int sum = 0;
        for (int i = 2; i < parts.length; i+=2){
            sum += Integer.parseInt(parts[i]);
        }
        return sum;
    }

    private double getMaxShapeArea(String line) throws IrregularCanvasException {
        String [] parts = line.split("\\s+");
        char type;
        int x;
        double area;
        double maxArea = Integer.MIN_VALUE;

        for (int i = 1; i < parts.length; i+=2){
            type = parts[i].charAt(0);
            x = Integer.parseInt(parts[i+1]);
            area = getArea(parts[0], type, x);
            if (area > maxArea){
                maxArea = area;
            }
        }
        return maxArea;
    }

    private double getMinShapeArea(String line) throws IrregularCanvasException {
        String [] parts = line.split("\\s+");
        char type;
        int x;
        double area;
        double minArea = Integer.MAX_VALUE;

        for (int i = 1; i < parts.length; i+=2){
            type = parts[i].charAt(0);
            x = Integer.parseInt(parts[i+1]);
            area = getArea(parts[0], type, x);
            if (area < minArea){
                minArea = area;
            }
        }
        return minArea;
    }

    private double getAverageShapeArea(String line) throws IrregularCanvasException {
        String [] parts = line.split("\\s+");
        char type;
        int x;
        double area = 0;
        int count = 0;

        for (int i = 1; i < parts.length; i+=2){
            type = parts[i].charAt(0);
            x = Integer.parseInt(parts[i+1]);
            area += getArea(parts[0], type, x);
            count ++;
        }
        return area / count;
    }

    private int countCircles(String line){
        String [] parts = line.split("\\s+");
        int count = 0;
        for (int i = 1; i < parts.length; i+=2){
            if (parts[i].charAt(0) == 'C'){
                count++;
            }
        }
        return count;
    }

    private int countSquares(String line){
        String [] parts = line.split("\\s+");
        int count = 0;
        for (int i = 1; i < parts.length; i+=2){
            if (parts[i].charAt(0) == 'S'){
                count++;
            }
        }
        return count;
    }

    private double getSumOfAreas(String line) throws IrregularCanvasException {
        String [] parts = line.split("\\s+");
        String index = parts[0];
        double sum = 0;
        for (int i = 1; i < parts.length; i++){
            char type = parts[i].charAt(0);
            i++;
            int x = Integer.parseInt(parts[i]);
            double area = getArea(index, type, x);
            sum += area;
        }
        return sum;
    }

    private ArrayList<String> sortLines(ArrayList<String> lines) {
        lines.sort((line1, line2) -> {
            try {
                return Double.compare(getSumOfAreas(line2), getSumOfAreas(line1));
            } catch (IrregularCanvasException e) {
                throw new RuntimeException(e);
            }
        });
        return lines;
    }

    void readCanvases (InputStream inputStream) throws IOException, IrregularCanvasException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while (true){
            line = br.readLine();
            if (line == null || line.isEmpty()){
                break;
            }

            String[] parts = line.split("\\s+");

            try{
                for (int i = 1; i < parts.length; i++){
                    char type = parts[i].charAt(0);
                    i++;
                    getArea(parts[0], type, Integer.parseInt(parts[i]));
                }

                lines.add(line);
            }
            catch (IrregularCanvasException e){
                System.out.println(e.getMessage());
            }
        }
    }

    void printCanvases(OutputStream os) throws IrregularCanvasException, IOException {
        String index = null;

        int lineArea_MAX = 0;
        int lineArea = 0;
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

        lines = sortLines(lines);

        for (String line : lines){
            int total_shapes = 0;
            int total_circles = 0;
            int total_squares = 0;
            double min_area = Integer.MAX_VALUE;
            double max_area = Integer.MIN_VALUE;
            double average_area = 0;

            lineArea = getTotalAreaPerLine(line);
            if (lineArea > lineArea_MAX){
                lineArea_MAX = lineArea;
            }

            index = line.split("\\s+")[0];
            total_shapes += countCircles(line) + countSquares(line);
            total_circles += countCircles(line);
            total_squares += countSquares(line);
            min_area = getMinShapeArea(line);
            max_area = getMaxShapeArea(line);
            average_area = getAverageShapeArea(line);

            bw.write(String.format("%s %d %d %d %.2f %.2f %.2f\n", index, total_shapes, total_circles, total_squares, min_area, max_area, average_area));
            bw.flush();
        }
        bw.close();

    }
}

public class Shapes2Test {

    public static void main(String[] args) throws IOException, IrregularCanvasException {

        ShapesApplication shapesApplication = new ShapesApplication(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
        shapesApplication.readCanvases(System.in);

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);


    }
}
