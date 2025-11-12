package prv_kolokvium.ex15_WeatherStation;

import javax.swing.text.DateFormatter;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

class Measurement{
    private float temperature;
    private float wind;
    private float humidity;
    private float visibility;
    private Date date;

    public Measurement(float temperature, float wind, float humidity, float visibility, Date date) {
        this.temperature = temperature;
        this.wind = wind;
        this.humidity = humidity;
        this.visibility = visibility;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getTemperature() {
        return temperature;
    }

    public static final Comparator<Measurement> byDate = Comparator.comparing(Measurement::getDate);

    @Override
    public String toString() {
        char sign = '%';
        return String.format("%.1f %.1f km/h %.1f%c %.1f km %s", temperature, wind, humidity, sign, visibility, date.toString().replace("UTC", "GMT"));
    }
}

class WeatherStation{
    private int days;
    private ArrayList<Measurement> measurements;

    public WeatherStation(int days) {
        this.days = days;
        this.measurements = new ArrayList<Measurement>();
    }


    public void addMeasurment(float temperature, float wind, float humidity, float visibility, Date date){

        //Исто така ако времето на новото мерење кое се додава се разликува за помалку од 2.5 минути
        //од времето на некое претходно додадено мерење, тоа треба да се игнорира (не се додава).
        for (Measurement m : measurements){
            long differenceInMillis = Math.abs(date.getTime() - m.getDate().getTime());
            long inMinutes = differenceInMillis / (1000 * 60); //vo sec vo min
            if (inMinutes < 2.5){
                return;
            }
        }

        Measurement measurement =  new Measurement(temperature, wind, humidity, visibility, date);
        measurements.add(measurement);

        //при додавање на податоци за ново мерење, сите мерења чие што време е постаро за x денови од новото се бришат
        long dateInMillis = date.getTime();
        long daysInMillis = (long)days * 24 * 60 * 60 * 1000; //h, min, sec, ms
        measurements.removeIf(m -> m.getDate().getTime() < (dateInMillis-daysInMillis));

    }

    public int total(){
        return measurements.size();
    }

    public void status(Date from, Date to){
        float sum = 0;
        int count = 0;

        for (Measurement m : measurements){
            if (m.getDate().getTime() >= from.getTime() && m.getDate().getTime() <= to.getTime()){
                System.out.println(m.toString());
                sum += m.getTemperature();
                count++;
            }
        }

        if (sum != 0 || count != 0){
            System.out.println(String.format("Average temperature: %.2f", sum/count));
        }
        else {
            throw new RuntimeException();
        }
    }
}

public class WeatherStationTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        int n = scanner.nextInt();
        scanner.nextLine();
        WeatherStation ws = new WeatherStation(n);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("=====")) {
                break;
            }
            String[] parts = line.split(" ");
            float temp = Float.parseFloat(parts[0]);
            float wind = Float.parseFloat(parts[1]);
            float hum = Float.parseFloat(parts[2]);
            float vis = Float.parseFloat(parts[3]);
            line = scanner.nextLine();
            Date date = df.parse(line);
            ws.addMeasurment(temp, wind, hum, vis, date);
        }
        String line = scanner.nextLine();
        Date from = df.parse(line);
        line = scanner.nextLine();
        Date to = df.parse(line);
        scanner.close();
        System.out.println(ws.total());
        try {
            ws.status(from, to);
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }
}

// vashiot kod ovde