package prv_kolokvium.ex01_Shapes1;

import java.io.*;
import java.util.ArrayList;

class ShapesApplication{

    public ShapesApplication(){}
    public ArrayList<String> lines = new ArrayList<>();

    int readCanvases (InputStream inputStream) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        int count = 0;
        String[] parts;

        while (true) {
            line = br.readLine();
            if (line == null || line.isEmpty()) {
                break;
            }

            lines.add(line);
            parts = line.split(" ");
            for (int i = 1; i < parts.length; i++){
                count++;

            }
        }
        return count;
    }

    void printLargestCanvasTo (OutputStream outputStream) throws IOException {

        String index_MAX = null;
        int count_MAX = 0;
        int perimeter_MAX = 0;

        for (String line : lines){

            String[] parts = line.split(" ");
            int count = 0;
            int perimeter = 0;

            for (int i = 1; i < parts.length; i++){
                count++;
                perimeter += (Integer.parseInt(parts[i]));
            }

            perimeter = perimeter*4;

            if (perimeter > perimeter_MAX){
                perimeter_MAX = perimeter;
                count_MAX = count;
                index_MAX = parts[0];
            }
        }

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
        bw.write(index_MAX + " " + count_MAX + " " + perimeter_MAX);
        bw.flush();
        bw.close();
    }

}

public class Shapes1Test {

    public static void main(String[] args) throws IOException {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}
