package prv_kolokvium.ex10_Triple;

import java.util.*;
import java.util.stream.Collectors;

// vasiot kod ovde
class Triple<T extends Number>{
    T a;
    T b;
    T c;

    Triple(T a, T b, T c){
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public double max(){
        double tempMax = Math.max(a.doubleValue(), b.doubleValue());
        return Math.max(tempMax, c.doubleValue());
    }

    public double average(){
        return (a.doubleValue() + b.doubleValue() + c.doubleValue()) / 3.0;
    }

    @SuppressWarnings("unchecked")
    public void sort(){
        List<Double> sorted = Arrays.asList(a.doubleValue(), b.doubleValue(), c.doubleValue())
                .stream()
                .sorted()
                .collect(Collectors.toList());

        a = (T) Double.valueOf(sorted.get(0));
        b = (T) Double.valueOf(sorted.get(1));
        c = (T) Double.valueOf(sorted.get(2));

    }

    @Override
    public String toString() {
        return String.format("%.2f %.2f %.2f", a.doubleValue(), b.doubleValue(), c.doubleValue());
    }
}


public class TripleTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        Triple<Integer> tInt = new Triple<Integer>(a, b, c);
        System.out.printf("%.2f\n", tInt.max());
        System.out.printf("%.2f\n", tInt.average());
        tInt.sort();
        System.out.println(tInt);
        float fa = scanner.nextFloat();
        float fb = scanner.nextFloat();
        float fc = scanner.nextFloat();
        Triple<Float> tFloat = new Triple<Float>(fa, fb, fc);
        System.out.printf("%.2f\n", tFloat.max());
        System.out.printf("%.2f\n", tFloat.average());
        tFloat.sort();
        System.out.println(tFloat);
        double da = scanner.nextDouble();
        double db = scanner.nextDouble();
        double dc = scanner.nextDouble();
        Triple<Double> tDouble = new Triple<Double>(da, db, dc);
        System.out.printf("%.2f\n", tDouble.max());
        System.out.printf("%.2f\n", tDouble.average());
        tDouble.sort();
        System.out.println(tDouble);
    }
}



