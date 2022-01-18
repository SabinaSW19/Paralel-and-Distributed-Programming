import mpi.MPI;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        long start, end;
        Graph graph = new Graph(10);
        graph.generate(10, 5);

        System.out.println("sequential\n" + graph);
        start = System.currentTimeMillis();
        SequentialAlgorithm.colorVertices(graph);
        end = System.currentTimeMillis();
        System.out.println("after\n" + graph);
        System.out.println("Time: " + (end - start) + " ms");
        System.out.println(graph.checkColor());


        Graph graph2 = new Graph(10);
        graph2.generate(10, 5);

        System.out.println("parallel\n" + graph2);
        start = System.currentTimeMillis();
        ParallelAlgorithm.colorVertices(graph2, 10);
        end = System.currentTimeMillis();
        System.out.println("after\n" + graph2);
        System.out.println("Time: " + (end - start) + " ms");
        System.out.println(graph2.checkColor());

    }
}
