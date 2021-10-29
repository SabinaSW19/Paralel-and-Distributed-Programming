import java.util.List;

public class Producer extends Thread {
    //compute the product and feed the consumer
    List<Integer> first;
    List<Integer> second;
    Buffer buffer;

    public Producer(List<Integer> first, List<Integer> second, Buffer buffer) {
        this.first = first;
        this.second = second;
        this.buffer = buffer;
    }

    @Override
    public void run()
    {
        for(int i=0;i<first.size();i++)
        {
            System.out.println("Producer sending:"+first.get(i)*second.get(i));
            try {
                buffer.send(first.get(i)*second.get(i));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
