package aud1.bank;

/*
PlatinumCheckingAccount е InterestCheckingAccount.
Повикување addInterest ја зголемува состојбата двојно од каматата за
InterestCheckingAccount (колку и да е таа).
*/

//InterestCheckingAccount extends Account implements InterestBearingAccount
// ==> same for PlatinumCheckingAccount

public class PlatinumCheckingAccount extends Account implements InterestBearingAccount{

    public PlatinumCheckingAccount(String holderName, long cardNumber, double currentAmount) {
        super(holderName, cardNumber, currentAmount);
    }

    @Override
    public void addInterest() {
        addAmount(getCurrentAmount() * InterestCheckingAccount.INTEREST_RATE * 2);
    }
}
