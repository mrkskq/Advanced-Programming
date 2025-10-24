package aud2.anonimniKlasiPlusLambdaIzrazi;

/*
Да се дефинираат два интерфејси:
Operation чија задача би била да дефинира некоја бинарна операција (пр. собирање),
со единствен метод apply кој прима два цели броја и враќа цел број.
MessageProvider чија задача би била да врати некаква порака,
со единствен метод getMessage кој не прима аргументи и враќа стринг.
Интерфејсите да се имплементираат преку:
класа која го имплементира интерфејсот,
анонимна класа,
ламбда израз.
 */

interface Operation{
    int apply(int a, int b);
}

interface MessageProvider{
    String getMessage();
}

// 1.preku klasa koja go implementira interfejsot

class Addition implements Operation{
    @Override
    public int apply(int a, int b) {
        return a + b;
    }
}

class StaticMessage implements MessageProvider{
    @Override
    public String getMessage() {
        return "Hello from a regular class";
    }
}

public class InterfaceDemo {
    public static void main(String[] args) {

        //OPERATION
        // 1.preku klasa koja go implementira interfejsot
        Operation op1 = new Addition();
        System.out.println("Addition: " + op1.apply(4, 5));

        // 2. preku anonimna klasa
        Operation op2 = new Operation() {
            @Override
            public int apply(int a, int b) {
                return a * b;
            }
        };
        System.out.println("Multiplication: " + op2.apply(4, 5));

        // 3. preku lambda izraz (vaka mojt samo za funkcionalni interfejsi, t.e. onie so eden metod vnatre)
        Operation op3 = (a,b) -> a - b;
        System.out.println("Subtraction: " + op3.apply(4, 5));



        //MESSAGE_PROVIDER
        // 1.preku klasa koja go implementira interfejsot
        MessageProvider msg1 = new StaticMessage();
        System.out.println(msg1.getMessage());

        // 2. preku anonimna klasa
        MessageProvider msg2 = new MessageProvider() {
            @Override
            public String getMessage() {
                return "Hello from an anonymous class";
            }
        };
        System.out.println(msg2.getMessage());

        // 3. preku lambda izraz
        MessageProvider msg3 = () -> "Hello from a lambda expression";
        System.out.println(msg3.getMessage());
    }
}

