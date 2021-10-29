import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {
    int capacity=1;
    Queue<Integer> queue=new LinkedList<>();
    Lock lock=new ReentrantLock();
    Condition condition=lock.newCondition();

    public void send(int element) throws InterruptedException {
        lock.lock();
        while(queue.size()==capacity)
        {
            System.out.println("Full buffer!");
            condition.await();
        }
        queue.add(element);
        System.out.println("Added "+element+" to buffer.");
        condition.signal();
        lock.unlock();
    }

    public int receive() throws InterruptedException {
        lock.lock();
        while (queue.isEmpty())
        {
            System.out.println("Empty buffer!");
            condition.await();
        }
        Integer element=queue.poll();
        System.out.println("Element "+element+" taken from buffer.");
        condition.signal();
        lock.unlock();
        return element;
    }
}
