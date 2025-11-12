package auds.aud5.airports;

import java.util.*;

/*
---------------- input -----------------

5
Skopje International;North Macedonia;SKP;2500000
Zagreb Airport;Croatia;ZAG;3500000
Belgrade Airport;Serbia;BEG;6000000
Sofia Airport;Bulgaria;SOF;4000000
Vienna Airport;Austria;VIE;8000000
6
SKP;ZAG;900;80
SKP;BEG;1100;60
SKP;BEG;800;70
BEG;ZAG;1300;85
SOF;ZAG;700;100
VIE;SKP;1200;95
0 1

---------------- output -----------------

===== FLIGHTS FROM SKP =====
Skopje InternationalSKP BEG 800 70
SKP BEG 1100 60
SKP ZAG 900 80
===== DIRECT FLIGHTS FROM SKP TO ZAG =====
SKP ZAG 900 80
===== DIRECT FLIGHTS TO ZAG =====
BEG ZAG 1300 85
SKP ZAG 900 80
SOF ZAG 700 100

 */

class Flight{
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

    @Override
    public String toString() {
        return String.format("%s %s %d %d", from, to, time, duration);
    }
}

class Airport{
    private String name;
    private String country;
    private String code;
    private int passengers;

    public Airport(String name, String country, String code, int passengers) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}

class Airports{
    private Map<String, Airport> airports;
    Map<String, List<Flight>> flightsFrom;

    public Airports(){
        airports = new HashMap<>();
        flightsFrom = new HashMap<>();
    }

    public void addAirport(String name, String country, String code, int passengers){

        //kluc e kodot na aerodromot

        Airport airport = new Airport(name, country, code, passengers);
        airports.putIfAbsent(code, airport);
    }

    public void addFlights(String from, String to, int time, int duration){

        //kluc e kodot na aerodromot na poagjanje

        Flight flight = new Flight(from, to, time, duration);
        for (String codeFrom : airports.keySet()) {
            if (codeFrom.equals(from)) {
                flightsFrom.computeIfAbsent(codeFrom, k -> new ArrayList<>()).add(flight);
                //aku nemat na toj kluc lista od letovi, dodaj nova lista, a aku imat lista, vo ta lista dodaj go letot
            }
        }
    }

    public void showFlightsFromAirport(String code){
        for (String codeFrom : airports.keySet()) {
            if (codeFrom.equals(code)) {
                List<Flight> flights = flightsFrom.get(codeFrom);
                Airport airport = airports.get(codeFrom);
                System.out.print(airport.getName());
                flights.stream()
                        .sorted(Comparator.comparing(Flight::getTo).thenComparing(Flight::getTime))
                        .forEach(System.out::println);
            }
        }
    }

    public void showDirectFlightsFromTo(String from, String to){
        for (String codeFrom : airports.keySet()) {
            if (codeFrom.equals(from)) {
                List<Flight> flights = flightsFrom.get(codeFrom);
                flights.stream()
                        .filter(f -> f.getTo().equals(to))
                        .sorted(Comparator.comparing(Flight::getTime))
                        .forEach(System.out::println);
            }
        }
    }

    public void showDirectFlightsTo(String to){
        for (String code : flightsFrom.keySet()) {
            List<Flight> flights = flightsFrom.get(code);
            if (flights != null && !flights.isEmpty()) {
                flights.stream()
                        .filter(f -> f.getTo().equals(to))
                        .sorted(Comparator.comparing(Flight::getTime))
                        .forEach(System.out::println);
            }
            else{
                System.out.println("(no flights found)");
            }
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
