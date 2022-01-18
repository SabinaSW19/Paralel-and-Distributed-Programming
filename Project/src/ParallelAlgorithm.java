import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParallelAlgorithm {
    public static void firstColoring(Graph graph, int nrThreads) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(nrThreads);
        int size = graph.getNrNodes() / nrThreads; //get the number of additions performed per thread
        ColorThread[] sums = new ColorThread[nrThreads];
        for (int i = 0; i < nrThreads; i++) //last thread is number (threads -1), and it goes up to the last element only if the rest is not 0
            sums[i] = new ColorThread(graph, i * size, (i == nrThreads - 1) ? (graph.getNrNodes()) : ((i + 1) * size));
        for (int i = 0; i < nrThreads; i++)
            executorService.execute(sums[i]);
        executorService.shutdown();
        while (!executorService.awaitTermination(1, TimeUnit.MINUTES)) {
        }
    }

    public static void recoloring(Graph graph, int nrThreads, List<Integer> r) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(nrThreads);
        int size = graph.getNrNodes() / nrThreads; //get the number of additions performed per thread
        RecolorThread[] sums = new RecolorThread[nrThreads];
        for (int i = 0; i < nrThreads; i++) //last thread is number (threads -1), and it goes up to the last element only if the rest is not 0
            sums[i] = new RecolorThread(graph, r, i * size, (i == nrThreads - 1) ? (graph.getNrNodes()) : ((i + 1) * size));
        for (int i = 0; i < nrThreads; i++)
            executorService.execute(sums[i]);
        executorService.shutdown();
        while (!executorService.awaitTermination(1, TimeUnit.MINUTES)) { }
    }

    public static void colorVertices(Graph graph, int nrThreads) throws InterruptedException {
        List<Integer> vertices = IntStream.rangeClosed(0, graph.getNrNodes()).boxed().collect(Collectors.toList());
        while (!vertices.isEmpty()) {
            List<Integer> r = new ArrayList<>();
            firstColoring(graph, nrThreads);
            recoloring(graph, nrThreads, r);
            vertices = r;
        }
    }
}
