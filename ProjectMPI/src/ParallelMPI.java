import mpi.MPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParallelMPI {
    public static void parent() {
        int nrProcs = MPI.COMM_WORLD.Size();

        Graph graph = new Graph(nrProcs);
        graph.generate(nrProcs, 5);
        System.out.println("Initial: \n" + graph);

        long start = System.currentTimeMillis();

        for (int k = 1; k < nrProcs; k++) {
            Graph[] send = new Graph[1];
            send[0] = graph;
            MPI.COMM_WORLD.Send(send, 0, 1, MPI.OBJECT, k, 0);
        }

        compute(0, graph);

        for (int i = 1; i < nrProcs; i++) { //receive colors
            int[] col = new int[1];
            MPI.COMM_WORLD.Recv(col, 0, 1, MPI.INT, i, 2);
            graph.getNode(i).setColor(col[0]);
        }
        long end = System.currentTimeMillis();

        System.out.println("Final: \n" + graph);
        System.out.println(graph.checkColor());
        System.out.println("Time : " + (end - start) + " ms");
    }

    public static void compute(int node, Graph graph) {
        List<Integer> adjacent = graph.getNode(node).getNeighbours();
        List<Integer> availableColors = IntStream.rangeClosed(0, adjacent.size())
                .boxed().collect(Collectors.toList());

        Random random = new Random();
        int[] col = new int[1];
        int color = -1;
        boolean chosen = false;
        boolean end = false;
        int idx = 0;
        while (idx < graph.getNrNodes()) {
            idx++;
            if (!chosen) {
                color = availableColors.get(random.nextInt(availableColors.size())); //randomly choose color
            }

            List<Integer> neighColors = new ArrayList<>();

            col[0] = color;

            for (int neigh : adjacent) { //inform neighbours about coloring
                MPI.COMM_WORLD.Isend(col, 0, 1, MPI.INT, neigh, 0);
            }

            for (int neigh : adjacent) {
                MPI.COMM_WORLD.Recv(col, 0, 1, MPI.INT, neigh, 0);
                neighColors.add(col[0]);
            }


            if (!neighColors.contains(color)) { //current node keeps it color
                col[0] = color;
                chosen = true;
            } else { //node doesn't keep color
                col[0] = -1;
            }

            for (int neigh : adjacent) { //inform neighbours about if the color is preserved or not
                MPI.COMM_WORLD.Isend(col, 0, 1, MPI.INT, neigh, 1);
            }



            end = true;
            for (int neigh : adjacent) { //receive decisions from the other nodes
                MPI.COMM_WORLD.Recv(col, 0, 1, MPI.INT, neigh, 1);
                int neighColor = col[0];
                if (neighColor != -1) { //if color chosen by neighbour, remove it from available
                    availableColors.remove(new Integer(neighColor));
                }
                if (neighColor == -1) {
                    end = false;
                }
            }

        }


        if (node == 0) {
            graph.getNode(0).setColor(color);
        } else { //send chosen color to node 0
            col[0] = color;
            MPI.COMM_WORLD.Isend(col, 0, 1, MPI.INT, 0, 2);
        }
    }

    public static void child() {
        Graph graph;
        Graph[] recv = new Graph[1];
        MPI.COMM_WORLD.Recv(recv, 0, 1, MPI.OBJECT, 0, 0);
        graph = recv[0];

        int node = MPI.COMM_WORLD.Rank();
        compute(node, graph);

    }
}
