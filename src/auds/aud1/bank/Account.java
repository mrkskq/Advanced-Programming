package auds.aud1.bank;

/*
Account е апстрактна класа. Во секој сметка се чуваат името на сопственикот на сметката,
бројот на сметката (секвенцијален број доделен автоматски), моменталната состојба.
Во класата се имплементираат конструктор за иницијализација на податочните членови,
методи за пристап до моменталната состојба, како и за додавање и одземање од моменталната состојба.
 */

abstract class Account {

    private String holderName;
    private long cardNumber;
    private double currentAmount;

    public Account(String holderName, long cardNumber, double currentAmount) {
        this.holderName = holderName;
        this.cardNumber = cardNumber;
        this.currentAmount = currentAmount;
    }

    public double getCurrentAmount() {
        return currentAmount;
    }

    public void addAmount(double amount) {
        currentAmount += amount;
    }

    public void withdrawAmount(double amount) {
        if(amount <= currentAmount) {
            currentAmount -= amount;
        }
    }
}
