package prv_kolokvium.ex22_F1;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Driver {
    private static int id;
    private String name;
    private String lap1;
    private String lap2;
    private String lap3;

    public Driver(String name, String lap1, String lap2, String lap3) {
        id = 1;
        this.name = name;
        this.lap1 = lap1;
        this.lap2 = lap2;
        this.lap3 = lap3;
    }

    private String getFastestLap() {
        String fastestLap = lap1;
        if (lap2.compareTo(fastestLap) < 0) {
            fastestLap = lap2;
        }
        if (lap3.compareTo(fastestLap) < 0) {
            fastestLap = lap3;
        }
        return fastestLap;
    }

    public static final Comparator<Driver> byFastestLap = Comparator.comparing(Driver::getFastestLap);

    @Override
    public String toString() {
        return String.format("%d. %-10s%s", id++, name, getFastestLap());
    }
}

class F1Race {
    // vashiot kod ovde
    private ArrayList<Driver> drivers;

    public F1Race() {
        drivers = new ArrayList<>();
    }

    public void readResults(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        while (true){
            line = br.readLine();
            if (line == null || line.isEmpty()){
                break;
            }
            String []parts = line.split("\\s+");
            drivers.add(new Driver(parts[0], parts[1], parts[2], parts[3]));
        }
    }


    public void printSorted(PrintStream out) {
        List<Driver> sorted = drivers.stream()
                .sorted(Driver.byFastestLap)
                .collect(Collectors.toList());
        for (Driver d : sorted) {
            System.out.println(d.toString());
        }
    }
}

public class F1Test {

    public static void main(String[] args) throws IOException {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}

