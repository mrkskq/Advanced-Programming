package vtor_kolokvium.decorator;

/*

Decorator design pattern:
->  Component (interface Coffee)
    - Defines the common interface for both the original objects and the decorators.

->  Concrete Component
    - The actual object whose behavior you want to extend  (e.g. Espresso, BrewedCoffee).

->  Base Decorator (wrappee: Component (Coffee coffee)) -> CoffeeDecorator
    - An abstract class that also implements the component interface and holds a reference to a component object.

->  Concrete Decorators
    - Specific wrapper classes (e.g., RegularMilk, OatMilk, Cream) that add new features and delegate to the wrapped component.

---------------------------------------------------------------------------------------------------------

Coffee order
Implement a simple coffee ordering system.

There are two base coffee types: Espresso and BrewedCoffee. Each coffee has a description and a price.

A coffee order can be extended with additional ingredients:

RegularMilk
OatMilk
Cream
Each added ingredient must:

Modify the description of the coffee order
Increase the total price by a fixed amount
It must be possible to:

Add multiple ingredients to the same coffee order
Combine ingredients in any order
Create coffee orders dynamically at runtime

*/



// =======================
// Component
// =======================
interface Coffee{
    String getDescription();
    double getPrice();
}


// =======================
// Concrete Components
// =======================
class Espresso implements Coffee{

    @Override
    public String getDescription() {
        return "Espresso";
    }

    @Override
    public double getPrice() {
        return 1.50;
    }
}

class BrewedCoffee implements Coffee{
    @Override
    public String getDescription() {
        return "Brewed Coffee";
    }

    @Override
    public double getPrice() {
        return 1.20;
    }
}



// =======================
// Base Decorator
// =======================
abstract class CoffeeDecorator implements Coffee{
    protected Coffee coffee;

    public CoffeeDecorator(Coffee coffee) {
        this.coffee = coffee;
    }
}



// =======================
// Concrete Decorators
// =======================
class RegularMilk extends CoffeeDecorator{

    public RegularMilk(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return coffee.getDescription() + ", Regular Milk";
    }

    @Override
    public double getPrice() {
        return coffee.getPrice() + 0.30;
    }
}

class OatMilk extends CoffeeDecorator{

    public OatMilk(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return coffee.getDescription() + ", Oat Milk";
    }

    @Override
    public double getPrice() {
        return coffee.getPrice() + 0.50;
    }
}

class Cream extends CoffeeDecorator{

    public Cream(Coffee coffee) {
        super(coffee);
    }


    @Override
    public String getDescription() {
        return coffee.getDescription() + ", Cream";
    }

    @Override
    public double getPrice() {
        return coffee.getPrice() + 0.40;
    }
}



// =======================
// Main class
// =======================
public class CoffeeShop {

    public static void main(String[] args) {

        Coffee order1 = new Espresso();
        order1 = new RegularMilk(order1);
        order1 = new Cream(order1);

        System.out.println(order1.getDescription());
        System.out.println("Price: " + order1.getPrice());

        System.out.println();

        Coffee order2 = new BrewedCoffee();
        order2 = new OatMilk(order2);

        System.out.println(order2.getDescription());
        System.out.println("Price: " + order2.getPrice());
    }
}
