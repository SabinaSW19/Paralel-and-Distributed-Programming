import java.util.concurrent.locks.Lock;

public class Operation {
    int id;
    int moneyTransferred;
    Account initialAccount;
    Account finalAccount;


    public Operation(int id,int moneyTransferred, Account initialAccount, Account finalAccount) {
        this.id=id;
        this.moneyTransferred = moneyTransferred;
        this.initialAccount = initialAccount;
        this.finalAccount = finalAccount;

    }

    @Override
    public String toString() {
        return "Operation{" +
                "id=" + id +
                ", moneyTransferred=" + moneyTransferred +
                ", initialAccount=" + initialAccount +
                ", finalAccount=" + finalAccount +
                '}';
    }

    public void run() {
        if(initialAccount.id<finalAccount.id)
        {
            initialAccount.lock.lock();
            finalAccount.lock.lock();
        }
        else
        {
            finalAccount.lock.lock();
            initialAccount.lock.lock();
        }
        initialAccount.decreaseBalance(moneyTransferred);
        finalAccount.increaseBalance(moneyTransferred);
        initialAccount.operations.add(this);
        System.out.println("Transaction between:"+this.initialAccount.id+" and "+this.finalAccount.id+" with sum " +this.moneyTransferred);
        finalAccount.operations.add(this);
        initialAccount.lock.unlock();
        finalAccount.lock.unlock();

    }
}
