package aud2.funkciskiInterfejsi;

/*
Да се користат вградените функциски интерфејси од Java библиотеката за да се имплементираат следните функционалности:
- Функционалност која прима еден стринг и враќа неговата должина (користете Function<String, Integer>).
- Функционалност која прима два цели броја и враќа нивен збир (користете BiFunction<Integer, Integer, Integer>).
- Функционалност која прима еден цел број и враќа true ако е парен, инаку false (користете Predicate<Integer>).
- Функционалност која прима еден стринг и го печати на конзола (користете Consumer<String>).
- Функционалност која не прима аргументи и враќа тековно време во милисекунди (користете Supplier<Long>).
 */

import java.util.function.*;

public class FunctionalInterfacesDemo {
    public static void main(String[] args) {

        // Function:
        // 1. function to get the length of a string
        // vo <> levo e to sho go primat, desno e to sho go vrakjat
        Function<String, Integer> stringLength = s -> s.length();
        System.out.println("Length of 'Banana' is: " + stringLength.apply("Banana"));

        // BiFunction:
        // 2. function to sum two integers
        // vo <> prvoto i vtoroto se to sho primat, tretoto e to sho vrakjat
        BiFunction<Integer, Integer, Integer> sum = (a,b) -> a + b;
        System.out.println("Sum of 4 and 5 is: " + sum.apply(4,5));

        // Predicate:
        // 3. function to check if a number is even
        // vo <> e to sho primat, a sekogas vrakjat boolean
        Predicate<Integer> isEven = num -> num % 2 == 0;
        System.out.println("Is 4 even? " + isEven.test(4));
        System.out.println("Is 5 even? " + isEven.test(5));

        // Consumer:
        // function to print a string
        // vo <> e to sho primat, nisto ne vrakjat, neshto ko void
        Consumer<String> printString = str -> System.out.println("Printing: " + str);
        printString.accept("Hello World");

        // Supplier:
        // function to get current time in miliseconds
        // vo <> e to sho vrakjat, nisto ne primat ko argument
        Supplier<Long> currentTimeInMs = () -> System.currentTimeMillis();
        System.out.println("Current time in ms: " + currentTimeInMs.get());
    }
}
