import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Integer> first= Arrays.asList(2,2,2,2,2);
        List<Integer> second=Arrays.asList(2,2,2,2,2);
        Buffer buffer=new Buffer();
        Producer producer=new Producer(first,second,buffer);
        Consumer consumer=new Consumer(buffer,5);
        producer.start();
        consumer.start();

    }
}
