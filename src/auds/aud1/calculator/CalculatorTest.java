package auds.aud1.calculator;

/*
Да се напише програма — едноставен калкулатор.
Калкулаторот чува еден број од тип double со име result, чија почетна вредност е 0.0.
Во циклус се дозволува на корисникот да: додава +, одзема -, множи *, дели / со втор број..
Резултатот од овие операции станува новата вредност на result.
Пресметките завршуваат кога корисникот ќе внесе R (големо или мало) за result.
Потоа корисникот може да започне нова пресметка од почеток или да ја заврши програмата (Y/N).
Ако корисникот внесе оператор различен од +, -, * или /,
тогаш се фрла исклучок UnknownOperatorException и се бара повторно внес.

Пример форматот на влезните податоци:
Calculator is on.
result = 0.0
+ 5
result + 5.0 = 5.0
new result = 5.0
* 2.2
result * 2.2 = 11.0
updated result = 11.0
% 10
% is an unknown operation.
Reenter, your last line:
* 0.1
result * 0.1 = 1.1
updated result = 1.1
r
Final result = 1.1
Again? (y/n)
yes
result = 0.0
+ 10
result + 10.0 = 10.0
new result = 10.0
/ 2
result / 2.0 = 5.0
updated result = 5.0
r
Final result = 5.0
Again? (y/n)
N
End of Program
 */

import java.util.Scanner;

class Calculator {

    private double result;
    private static double INITIAL_RESULT = 0.0;

    public Calculator() {
        this.result = 0.0;
    }

    public double getResult() {
        return result;
    }

    public String init(){
        result = INITIAL_RESULT;
        return String.format("result = %.1f", INITIAL_RESULT);
    }

    public String calculate(char operator, double value) throws UnknownOperatorException {
        if (operator == '+') {
            result += value;
        }
        else if (operator == '-') {
            result -= value;
        }
        else if (operator == '*') {
            result *= value;
        }
        else if (operator == '/') {
            if (value != 0){
                result /= value;
            }
        }
        else {
            throw new UnknownOperatorException(operator);
        }
        return String.format("result %c %.1f = %.1f", operator, value, result);
    }

    class UnknownOperatorException extends Exception {
        public UnknownOperatorException(char operator) {
            super(String.format("%c is unknown operation\nReenter, your last line:", operator));
        }
    }

    public void newResult(){
        System.out.println(String.format("new result = %.1f", result));
    }

    @Override
    public String toString() {
        return String.format("updated result = %.1f", result);
    }
}

public class CalculatorTest {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("Calculator is on.");
        Calculator calculator = new Calculator();
        System.out.println(calculator.init());
        boolean flag = true;

        while (true){
            String line = sc.nextLine();
            if (line.equalsIgnoreCase("r")){
                System.out.println(String.format("Final result = %.1f", calculator.getResult()));
                System.out.println("Again? (y/n)");
                String l = sc.nextLine().toLowerCase();
                if (l.startsWith("y")){
                    System.out.println(calculator.init());
                    flag = true;
                }
                else if (l.startsWith("n")){
                    System.out.println("End of Program");
                    break;
                }
            }
            else{
                String []parts = line.split("\\s+");
                char operator = parts[0].charAt(0);
                double value = Double.parseDouble(parts[1]);
                try{
                    String result = calculator.calculate(operator, value);
                    System.out.println(result);
                    if (flag){
                        calculator.newResult(); //new result =
                        flag = false;
                    }
                    else {
                        System.out.println(calculator); //updated result =
                    }
                }
                catch (Calculator.UnknownOperatorException e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
