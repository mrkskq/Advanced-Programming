package prv_kolokvium.ex12_GenericFraction;

import java.util.Scanner;

class ZeroDenominatorException extends Exception{
    public ZeroDenominatorException(String message){
        super(message);
    }
}

class GenericFraction<T extends Number, U extends Number>{
    private T numerator;
    private U denominator;

    public GenericFraction(T numerator, U denominator) throws ZeroDenominatorException {
        if (denominator.equals(0)){
            throw new ZeroDenominatorException("Denominator cannot be zero");
        }
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public GenericFraction<Double, Double> add(GenericFraction<? extends Number, ? extends Number> gf) throws ZeroDenominatorException {
        double num1 = this.numerator.doubleValue();
        double den1 = this.denominator.doubleValue();
        double num2 = gf.numerator.doubleValue();
        double den2 = gf.denominator.doubleValue();

        double newNum = num1 * den2 + num2 * den1;
        double newDen = den1 * den2;

        return new GenericFraction<Double, Double>(newNum, newDen);
    }

    /*
    48 64
    48 16
    16 32
    16 16
    16 0
    16
     */

    public static double NZD(double a, double b){
        if (b == 0)
            return a;
        if (a < b)
            return NZD(a, b - a);
        else
            return NZD(b, a - b);
    }

    public double NZD(){
        return NZD(this.numerator.doubleValue(), this.denominator.doubleValue());
    }

    public double toDouble(){
        return numerator.doubleValue() / denominator.doubleValue();
    }

    @Override
    public String toString() {
        double nzd = this.NZD();
        return String.format("%.2f / %.2f", numerator.doubleValue() / nzd, denominator.doubleValue() / nzd);
    }
}

public class GenericFractionTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double n1 = scanner.nextDouble();
        double d1 = scanner.nextDouble();
        float n2 = scanner.nextFloat();
        float d2 = scanner.nextFloat();
        int n3 = scanner.nextInt();
        int d3 = scanner.nextInt();
        try {
            GenericFraction<Double, Double> gfDouble = new GenericFraction<Double, Double>(n1, d1);
            GenericFraction<Float, Float> gfFloat = new GenericFraction<Float, Float>(n2, d2);
            GenericFraction<Integer, Integer> gfInt = new GenericFraction<Integer, Integer>(n3, d3);
            System.out.printf("%.2f\n", gfDouble.toDouble());
            System.out.println(gfDouble.add(gfFloat));
            System.out.println(gfInt.add(gfFloat));
            System.out.println(gfDouble.add(gfInt));
            gfInt = new GenericFraction<Integer, Integer>(n3, 0);
        } catch(ZeroDenominatorException e) {
            System.out.println(e.getMessage());
        }

        scanner.close();
    }

}

// вашиот код овде
