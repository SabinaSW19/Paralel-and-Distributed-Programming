import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
    int id;
    List<Operation> operations;
    int initialBalance;
    int balance;
    public Lock lock=new ReentrantLock();

    public Account(int id, int initialBalance) {
        this.id = id;
        this.operations = new ArrayList<>();
        this.initialBalance=initialBalance;
        this.balance = initialBalance;
    }

    public void increaseBalance(double sum)
    {
        this.balance+=sum;
    }

    public void decreaseBalance(double sum)
    {
        this.balance-=sum;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", operations=" + operations +
                ", balance=" + balance +
                '}';
    }
}
