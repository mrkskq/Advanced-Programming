package prv_kolokvium.ex42_DailyTemperatures;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * I partial exam 2016
 */

class Measurement{
    private int day;
    private List<Double> temperatures;

    public Measurement(){
        temperatures = new ArrayList<Double>();
    }

    public Measurement(int day, List<Double> temperatures) {
        this.day = day;
        this.temperatures = temperatures;
    }

    public int getDay() {
        return day;
    }

    public List<Double> getTemperatures() {
        return temperatures;
    }

    public void setTemperatures(List<Double> temperatures) {
        this.temperatures = temperatures;
    }

    public String print(char c) {
        StringBuilder sb = new StringBuilder();
        double min = temperatures.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
        double max = temperatures.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
        double average = temperatures.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        sb.append(String.format("%3d: Count: %3d Min: %6.2f%c Max: %6.2f%c Avg: %6.2f%c", day, temperatures.size(), min, c, max, c, average, c));
        return sb.toString();
    }
}

class DailyTemperatures {
    private List<Measurement> measurements;

    public DailyTemperatures() {
        measurements = new ArrayList<Measurement>();
    }

    public void readTemperatures(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while (true){
            line = br.readLine();
            if (line == null || line.isEmpty()){
                break;
            }

            String splitBy = line.contains("C") ? "C" : "F";

            String[] parts = line.split("\\s+");
            int day = Integer.parseInt(parts[0]);
            List<Double> temperatures = new ArrayList<>();

            for (int i = 1; i < parts.length; i++){
                double t = Double.parseDouble(parts[i].split(splitBy)[0]);
                if (splitBy.equals("F")){
                    t = FtoC(t);
                }
                temperatures.add(t);
            }

            measurements.add(new Measurement(day, temperatures));
        }
    }

    public double FtoC(double temp){
        return ((temp - 32) * 5) / 9;
    }

    public double CtoF(double temp){
        return (temp * 9) / 5 + 32;
    }

    void writeDailyStats(OutputStream outputStream, char scale){
        PrintWriter pw = new PrintWriter(outputStream);

        measurements = measurements.stream()
                .sorted(Comparator.comparing(Measurement::getDay))
                .collect(Collectors.toList());

        for (Measurement measurement : measurements){
            if (scale=='F'){
                List<Double> temps = measurement.getTemperatures().stream().map(t -> CtoF(t)).collect(Collectors.toList());
                measurement.setTemperatures(temps);
                System.out.println(measurement.print(scale));
            }
            else {
                System.out.println(measurement.print(scale));
            }
        }
    }
}

public class DailyTemperatureTest {
    public static void main(String[] args) throws IOException {
        DailyTemperatures dailyTemperatures = new DailyTemperatures();
        dailyTemperatures.readTemperatures(System.in);
        System.out.println("=== Daily temperatures in Celsius (C) ===");
        dailyTemperatures.writeDailyStats(System.out, 'C');
        System.out.println("=== Daily temperatures in Fahrenheit (F) ===");
        dailyTemperatures.writeDailyStats(System.out, 'F');
    }
}

// Vashiot kod ovde