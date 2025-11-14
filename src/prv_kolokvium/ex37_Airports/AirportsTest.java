
//  ne e doresena :)

package prv_kolokvium.ex37_Airports;

import com.sun.source.tree.Tree;

import java.util.*;
import java.util.stream.Collectors;

class Flight implements Comparable<Flight> {
    private String from;
    private String to;
    private int time;
    private int duration;

    public Flight(String from, String to, int time, int duration) {
        this.from = from;
        this.to = to;
        this.time = time;
        this.duration = duration;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getTime() {
        return time;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        int end = time + duration;
        int plus = end / (24 * 60);
        end %= (24 * 60);
        return String.format(
                "%s-%s %02d:%02d-%02d:%02d%s %dh%02dm", from, to, time / 60, time % 60,
                end / 60, end % 60, plus > 0 ? " +1d" : "", duration / 60, duration % 60
        );
    }

    @Override
    public int compareTo(Flight o) {
        int x = Integer.compare(this.time, o.time);
        if (x == 0) {
            return this.from.compareTo(o.from);
        }
        return x;
    }
}

class Airport{
    private String name;
    private String country;
    private String code;
    private int passengers;
    private List<Flight> flights;

    public Airport(String name, String country, String code, int passengers) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;
        this.flights = new ArrayList<Flight>();
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getCode() {
        return code;
    }

    public int getPassengers() {
        return passengers;
    }

    public List<Flight> getFlights() {
        return flights;
    }

}

class Airports{
    private List<Airport> airports;
    private TreeMap<String, List<Flight>> flights;

    public Airports(){
        airports = new ArrayList<Airport>();
        flights = new TreeMap<String, List<Flight>>();
    }

    public void addAirport(String name, String country, String code, int passengers){
        Airport a = new Airport(name, country, code, passengers);
        airports.add(a);
    }

    public void addFlights(String from, String to, int time, int duration){
        Flight f = new Flight(from, to, time, duration);
        flights.computeIfAbsent(from, k -> new ArrayList<>()).add(f);
    }

    public void showFlightsFromAirport(String code){
        for (Airport a : airports){
            if (a.getCode().equals(code)){
                System.out.println(a.getName() + "(" + a.getCode() + ")");
                System.out.println(a.getCountry());
                System.out.println(a.getPassengers());
                List<Flight> letovi = flights.get(a.getCode());
                letovi = letovi.stream()
                        .sorted(Comparator.comparing(Flight::getFrom).thenComparing(Flight::getTo))
                        .collect(Collectors.toList());

                int id = 1;
                for (Flight f : letovi) {
                    System.out.printf("%d. %s\n", id++, f);
                }
            }
        }
    }

    public void showDirectFlightsFromTo(String from, String to){

        boolean flag = false;

        for (String codeFrom : flights.keySet()){
            List<Flight> flightsFromAirport = flights.get(codeFrom);
            for (Flight f : flightsFromAirport){
                if (f.getFrom().equals(from) && f.getTo().equals(to)){
                    System.out.println(f);
                    flag = true;
                }
            }
        }

        if (!flag){
            System.out.println("No flights from " + from + " to " + to);
        }
    }

    public void showDirectFlightsTo(String to){
        for (String code : flights.keySet()){
            List<Flight> flightsFromAirport = flights.get(code);
            flightsFromAirport.stream()
                    .filter(flight -> flight.getTo().equals(to))
                    .sorted()
                    .forEach(System.out::println);
        }
    }
}

public class AirportsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Airports airports = new Airports();
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] codes = new String[n];
        for (int i = 0; i < n; ++i) {
            String al = scanner.nextLine();
            String[] parts = al.split(";");
            airports.addAirport(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
            codes[i] = parts[2];
        }
        int nn = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nn; ++i) {
            String fl = scanner.nextLine();
            String[] parts = fl.split(";");
            airports.addFlights(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }
        int f = scanner.nextInt();
        int t = scanner.nextInt();
        String from = codes[f];
        String to = codes[t];
        System.out.printf("===== FLIGHTS FROM %S =====\n", from);
        airports.showFlightsFromAirport(from);
        System.out.printf("===== DIRECT FLIGHTS FROM %S TO %S =====\n", from, to);
        airports.showDirectFlightsFromTo(from, to);
        t += 5;
        t = t % n;
        to = codes[t];
        System.out.printf("===== DIRECT FLIGHTS TO %S =====\n", to);
        airports.showDirectFlightsTo(to);
    }
}

// vashiot kod ovde


