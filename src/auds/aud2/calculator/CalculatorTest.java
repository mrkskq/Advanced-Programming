package auds.aud2.calculator;

import java.util.Scanner;

class UnknownOperatorException extends Exception {
    public UnknownOperatorException(char operator) {
        super(String.format("%c is an unknown operation\nReenter, your last line:", operator));
    }
}

interface Operation{
    double apply(double currentResult, double value);
}

class OperationFactory{

    // static operators
    private static final char PLUS = '+';
    private static final char MINUS = '-';
    private static final char MULTIPLY = '*';
    private static final char DIVIDE = '/';

    // static lambda operations
    private static final Operation ADDITION = (a,b) -> a + b;
    private static final Operation SUBTRACTION = (a,b) -> a - b;
    private static final Operation MULTIPLICATION = (a,b) -> a * b;
    private static final Operation DIVISION = (a,b) -> a / b;

    // factory method that returns the correct operation
    public static Operation getOperation(char operator) throws UnknownOperatorException {
        if (operator == PLUS) {
            return ADDITION;
        } else if (operator == MINUS){
            return SUBTRACTION;
        } else if (operator == MULTIPLY){
            return MULTIPLICATION;
        } else if (operator == DIVIDE){
            return DIVISION;
        } else {
            throw new UnknownOperatorException(operator);
        }
    }
}

class Calculator{

    private double result;

    public Calculator(){
        result = 0.0;
    }

    // inicijalno na pocetok se pecatit result = 0.0
    public String init(){
        return String.format("result = %.1f", result);
    }

    public double getResult() {
        return result;
    }

    public String execute(char operator, double value) throws UnknownOperatorException {
        Operation op = OperationFactory.getOperation(operator);
        result = op.apply(result, value);
        return String.format("result %c %.1f = %.1f", operator, value, result);
    }

    @Override
    public String toString() {
        return String.format("updated result = %.1f", result);
    }
}

public class CalculatorTest {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Calculator is on.");

        while (true) {
            Calculator calculator = new Calculator();
            System.out.println(calculator.init());
            while (true) {
                String line = scanner.nextLine();
                if (line.charAt(0) == 'r') {
                    System.out.println(String.format("Final result = %.1f", calculator.getResult()));
                    break;
                }
                String[] parts = line.split("\\s+");
                char operator = parts[0].charAt(0);
                double value = Double.parseDouble(parts[1]);
                try {
                    String result = calculator.execute(operator, value);
                    System.out.println(result);
                    System.out.println(calculator);
                } catch (UnknownOperatorException e) {
                    System.out.println(e.getMessage());
                }
            }
            System.out.println("Again? (y/n)");
            String line = scanner.nextLine();
            char choice = line.toLowerCase().charAt(0);
            if (choice == 'n') {
                System.out.println("End of Program");
                break;
            }
        }
    }
}
