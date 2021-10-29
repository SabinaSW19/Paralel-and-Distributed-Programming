import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Main {
    static int numberThreads=16;
    static int numberAccounts=100;
    static int numberTransfers=5000;
    static int limit=100000;
    static List<Account> accountList=new ArrayList<>();
    static List<Operation> operationList=new ArrayList<>();
    static ReadWriteLock lock = new ReentrantReadWriteLock();
    static ExecutorService executorService= Executors.newFixedThreadPool(numberThreads);
    static ScheduledExecutorService scheduledExecutorService=Executors.newScheduledThreadPool(1);

    private static void initialiseAccounts(){
        Random random = new Random();
        for(int i=0;i<numberAccounts;i++)
            accountList.add(new Account(i, random.nextInt(limit)));
    }
    private static void initialiseOperations(){
        Random random = new Random();
        for(int i=0;i<numberTransfers;i++)
        {
            Account initialAccount=accountList.get(random.nextInt(numberAccounts));
            Account finalAccount=accountList.get(random.nextInt(numberAccounts));
            while(initialAccount.id==finalAccount.id)
            {
                initialAccount=accountList.get(random.nextInt(numberAccounts));
                finalAccount=accountList.get(random.nextInt(numberAccounts));
            }
            Operation operation=new Operation(i,random.nextInt(limit),initialAccount,finalAccount);
            operationList.add(operation);

        }
    }

    private static void checkConsistency()
    {
        lock.writeLock().lock();
        int inconsistencies=0;
        for(Account a:accountList)
        {
            int sumAdded=0;
            int sumTaken=0;
            for(Operation o:a.operations)
            {
                if(o.initialAccount.id==a.id)
                    sumTaken+=o.moneyTransferred;
                if(o.finalAccount.id==a.id)
                    sumAdded+=o.moneyTransferred;
            }
            if(a.initialBalance!=a.balance-sumAdded+sumTaken) {
                inconsistencies++;
                System.out.println("Inconsistency found!! Initial sum does not correspond to the operations made!!");
            }
        }

        for(Account a:accountList)
        {
            for(Operation o:a.operations)
            {
                if(o.initialAccount.id==a.id) {
                    Account finalAccount = o.finalAccount;
                    if (!finalAccount.operations.contains(o)) {
                        inconsistencies++;
                        System.out.println("Inconsistency found!! The operation logs do not correspond!!");
                    }
                }
                if(o.finalAccount.id==a.id) {
                    Account initialAccount = o.initialAccount;
                    if (!initialAccount.operations.contains(o)) {
                        inconsistencies++;
                        System.out.println("Inconsistency found!! The operation logs do not correspond!!");
                    }
                }
            }
        }
        if(inconsistencies==0)
        {
            System.out.println("There are no inconsistencies!!");
        }
        lock.writeLock().unlock();

    }

    public static void main(String[] args) throws InterruptedException {
        initialiseAccounts();
        initialiseOperations();
        float start =  System.nanoTime() / 1000000;
        operationList.forEach(t ->
        {
            executorService.submit(()->{
                lock.readLock().lock();
                t.run();
                lock.readLock().unlock();
            });
        });
        scheduledExecutorService.scheduleWithFixedDelay(Main::checkConsistency,0,50,TimeUnit.MILLISECONDS);
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);
        scheduledExecutorService.shutdown();
        checkConsistency();
        float end = System.nanoTime() / 1000000;
        System.out.println("Time elapsed: " + (end - start) / 1000 + " seconds");
    }
}
