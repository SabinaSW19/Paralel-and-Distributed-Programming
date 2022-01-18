import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Visit {
    ExecutorService pool;
    Lock lock=new ReentrantLock();

    public Visit(int numberThreads) {
        this.pool = Executors.newFixedThreadPool(numberThreads);
    }


    public boolean parallelized(Graph graph, List<Integer> path, int vertex,int index) {
        if(path.size()==graph.vertices.size())
            return graph.getNeighbours(vertex).contains(1);
        for(int i=0;i<graph.getNeighbours(vertex).size();i++)
        {
            int currentNeighbour=graph.getNeighbours(vertex).get(i);
            if(!path.contains(currentNeighbour)) {
                Future<Boolean> f1 = pool.submit(() ->
                {
                    lock.lock();
                    path.add(index, currentNeighbour);
                    lock.unlock();
                    Future<Boolean> f2 = pool.submit(() -> parallelized(graph, path, currentNeighbour, index + 1));
                    if (f2.get())
                        return true;
                    lock.lock();
                    path.remove(index);
                    lock.unlock();
                    return false;
                });
                try {
                    if (f1.get())
                        return true;
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                } catch (ExecutionException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return false;
    }
}
