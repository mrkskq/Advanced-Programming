package auds.aud1.bank;

/*
InterestCheckingAccount е сметка Account која е исто така InterestBearingAccount.
Повикување addInterest ја зголемува состојбата за 3%.
 */

public class InterestCheckingAccount extends Account implements InterestBearingAccount{

    public static final double INTEREST_RATE = 0.03;

    public InterestCheckingAccount(String holderName, long cardNumber, double currentAmount) {
        super(holderName, cardNumber, currentAmount);
    }

    @Override
    public void addInterest() {
        addAmount(getCurrentAmount() * INTEREST_RATE);
    }
}

