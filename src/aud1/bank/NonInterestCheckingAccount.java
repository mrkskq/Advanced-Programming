package aud1.bank;

/*
NonInterestCheckingAccount е сметка Account но не е InterestBearingAccount.
Нема дополнителни функционалности надвор од основните од класата Account.
 */

public class NonInterestCheckingAccount extends Account{

    public NonInterestCheckingAccount(String holderName, long cardNumber, double currentAmount) {
        super(holderName, cardNumber, currentAmount);
    }
}
