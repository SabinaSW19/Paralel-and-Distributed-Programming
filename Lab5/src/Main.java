import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Polynomial p1=new Polynomial(10000);
        System.out.println("P1="+p1);
        Polynomial p2=new Polynomial(10000);
        System.out.println("P2="+p2);
        Operations operations=new Operations();
        Instant start = Instant.now();
        operations.productSequential(p1,p2);
        //Polynomial res1=operations.productSequential(p1,p2);
        Instant end = Instant.now();
        System.out.println("Time elapsed: " + Duration.between(start,end).toMillis() + " milliseconds for product sequential");
        start = Instant.now();
        operations.productParallelized(p1,p2,5);
        //Polynomial res2=operations.productParallelized(p1,p2,5);
        end = Instant.now();
        System.out.println("Time elapsed: " + Duration.between(start,end).toMillis() + " milliseconds for product parallelized");
        start = Instant.now();
        operations.KaratsubaSequential(p1,p2);
        end = Instant.now();
        System.out.println("Time elapsed: " + Duration.between(start,end).toMillis() + " milliseconds for Karatsuba sequential");
        start = Instant.now();
        operations.KaratsubaParallelized(p1,p2,5,1);
        end = Instant.now();
        System.out.println("Time elapsed: " + Duration.between(start,end).toMillis() + " milliseconds for Karatsuba parallelized");

    }
}
