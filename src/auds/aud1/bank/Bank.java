package auds.aud1.bank;

/*
Во Bank чува листа од сите видови сметки, вклучувајќи сметки за штедење и за трошење,
некои од нив подложни на камата, а некои не. Во Bank постои метод totalAssets кој
ја враќа сумата на состојбата на сите сметки. Исто така содржи метод addInterest
кој го повикува методот addInterest на сите сметки кои се подложни на камата.
 */

import java.util.ArrayList;
import java.util.List;

public class Bank {

    private List<Account> accounts;

    public Bank() {
        this.accounts = new ArrayList<>();
    }

    public void addAccount(Account account) {
        this.accounts.add(account);
    }

    public double totalAssets(){
        double sum = 0;
        for (Account account : this.accounts) {
            sum += account.getCurrentAmount();
        }
        return sum;
    }

    public void addInterest(){
        for (Account account : this.accounts) {
            if(account instanceof InterestBearingAccount){
                InterestBearingAccount ibAcc = (InterestBearingAccount) account;
                ibAcc.addInterest();
            }
        }
    }
}
