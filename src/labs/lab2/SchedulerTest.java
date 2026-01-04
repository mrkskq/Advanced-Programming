package labs.lab2;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

class Timestamp<T> implements Comparable<Timestamp<T>> {
    private final LocalDateTime ldt;
    private final T element;

    public Timestamp(LocalDateTime ldt, T element) {
        this.ldt = ldt;
        this.element = element;
    }

    public LocalDateTime getTime() {
        return ldt;
    }

    public T getElement() {
        return element;
    }

    @Override
    public int compareTo(Timestamp<T> o) {
        return ldt.compareTo(o.ldt);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        return this.ldt.equals(((Timestamp<T>) obj).ldt);
    }

    @Override
    public String toString() {
        return ldt.toString() + " " + element.toString();
    }
}

class Scheduler<T> {

    private ArrayList<Timestamp<T>> times;
    private T element;

    public Scheduler() {
        times = new ArrayList<Timestamp<T>>();
    }

    public void add(Timestamp<T> t){
        times.add(t);
    }

    public boolean remove(Timestamp<T> t){
        if (times.contains(t)){
            times.remove(t);
            return true;
        }
        return false;
    }

    public Timestamp<T> next(){
        LocalDateTime now = LocalDateTime.now();
        return times.stream()
                .filter(t -> t.getTime().isAfter(now))
                .min(Comparator.comparing(Timestamp<T>::getTime))
                .orElse(null);
    }

    public Timestamp<T> last(){
        LocalDateTime now = LocalDateTime.now();
        return times.stream()
                .filter(t -> t.getTime().isBefore(now))
                .max(Comparator.comparing(Timestamp<T>::getTime))
                .orElse(null);
    }

    public List<Timestamp<T>> getAll(LocalDateTime begin, LocalDateTime end){
        return times.stream()
                .filter(t -> t.getTime().isAfter(begin))
                .filter(t -> t.getTime().isBefore(end))
                .collect(Collectors.toList());
    }


}

public class SchedulerTest {

    static final LocalDateTime TIME = LocalDateTime.of(2016, 10, 25, 10, 15);

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Timestamp with String
            Timestamp<String> t = new Timestamp<String>(TIME, jin.next());
            System.out.println(t);
            System.out.println(t.getTime());
            System.out.println(t.getElement());
        }
        if (k == 1) { //test Timestamp with ints
            Timestamp<Integer> t1 = new Timestamp<Integer>(TIME, jin.nextInt());
            System.out.println(t1);
            System.out.println(t1.getTime());
            System.out.println(t1.getElement());
            Timestamp<Integer> t2 = new Timestamp<Integer>(TIME.plusDays(10), jin.nextInt());
            System.out.println(t2);
            System.out.println(t2.getTime());
            System.out.println(t2.getElement());
            System.out.println(t1.compareTo(t2));
            System.out.println(t2.compareTo(t1));
            System.out.println(t1.equals(t2));
            System.out.println(t2.equals(t1));
        }
        if (k == 2) {//test Timestamp with String, complex
            Timestamp<String> t1 = new Timestamp<String>(ofEpochMS(jin.nextLong()), jin.next());
            System.out.println(t1);
            System.out.println(t1.getTime());
            System.out.println(t1.getElement());
            Timestamp<String> t2 = new Timestamp<String>(ofEpochMS(jin.nextLong()), jin.next());
            System.out.println(t2);
            System.out.println(t2.getTime());
            System.out.println(t2.getElement());
            System.out.println(t1.compareTo(t2));
            System.out.println(t2.compareTo(t1));
            System.out.println(t1.equals(t2));
            System.out.println(t2.equals(t1));
        }
        if (k == 3) { //test Scheduler with String
            Scheduler<String> scheduler = new Scheduler<String>();
            LocalDateTime now = LocalDateTime.now();
            scheduler.add(new Timestamp<String>(now.minusHours(2), jin.next()));
            scheduler.add(new Timestamp<String>(now.minusHours(1), jin.next()));
            scheduler.add(new Timestamp<String>(now.minusHours(4), jin.next()));
            scheduler.add(new Timestamp<String>(now.plusHours(2), jin.next()));
            scheduler.add(new Timestamp<String>(now.plusHours(4), jin.next()));
            scheduler.add(new Timestamp<String>(now.plusHours(1), jin.next()));
            scheduler.add(new Timestamp<String>(now.plusHours(5), jin.next()));
            System.out.println(scheduler.next().getElement());
            System.out.println(scheduler.last().getElement());
            List<Timestamp<String>> result = scheduler.getAll(now.minusHours(3), now.plusHours(4).plusMinutes(15));
            String out = result.stream()
                    .sorted()
                    .map(Timestamp::getElement)
                    .collect(Collectors.joining(", "));
            System.out.println(out);
        }
        if (k == 4) {//test Scheduler with ints complex
            Scheduler<Integer> scheduler = new Scheduler<Integer>();
            int counter = 0;
            ArrayList<Timestamp<Integer>> forRemoval = new ArrayList<Timestamp<Integer>>();
            while (jin.hasNextLong()) {
                Timestamp<Integer> ti = new Timestamp<Integer>(ofEpochMS(jin.nextLong()), jin.nextInt());
                if ((counter & 7) == 0) {
                    forRemoval.add(ti);
                }
                scheduler.add(ti);
                ++counter;
            }
            jin.next();

            while (jin.hasNextLong()) {
                LocalDateTime left = ofEpochMS(jin.nextLong());
                LocalDateTime right = ofEpochMS(jin.nextLong());
                List<Timestamp<Integer>> res = scheduler.getAll(left, right);
                Collections.sort(res);
                System.out.println(left + " <: " + print(res) + " >: " + right);
            }
            System.out.println("test");
            List<Timestamp<Integer>> res = scheduler.getAll(ofEpochMS(0), ofEpochMS(Long.MAX_VALUE));
            Collections.sort(res);
            System.out.println(print(res));
            forRemoval.forEach(scheduler::remove);
            res = scheduler.getAll(ofEpochMS(0), ofEpochMS(Long.MAX_VALUE));
            Collections.sort(res);
            System.out.println(print(res));
        }
    }

    private static LocalDateTime ofEpochMS(long ms) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(ms), ZoneId.systemDefault());
    }

    private static <T> String print(List<Timestamp<T>> res) {
        if (res == null || res.size() == 0) return "NONE";
        return res.stream()
                .map(each -> each.getElement().toString())
                .collect(Collectors.joining(", "));
    }

}

// vashiot kod ovde


