import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args){
        Graph graph=new Graph(20);
        graph.generateRandomGraph(4);
        System.out.println(graph);
        Visit visit=new Visit(100);
        List<Integer> path=new ArrayList<>();
        path.add(1);
        long start = System.nanoTime();
        visit.parallelized(graph,path,1,1);
        visit.pool.shutdown();
        try {
            visit.pool.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        long end = System.nanoTime();
        System.out.println(path);
        System.out.println("Time for task: " + (end - start) / 1000000 + " ms \n");
    }
}
