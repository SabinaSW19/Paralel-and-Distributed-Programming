import mpi.MPI;

import java.util.ArrayList;

public class RegularMultiplication {
    public RegularMultiplication() { }

    private static void parent(Polynomial p1, Polynomial p2, int nr) {
        long startTime = System.currentTimeMillis();
        int start = 0, end= 0;
        int len = p1.getCoefficients().size() / (nr - 1);
        for (int i = 1; i < nr; i++) {
            start = end;
            end += len;
            if (i == nr - 1) {
                end = p1.coefficients.size();
            }
            MPI.COMM_WORLD.Send(new Object[]{p1}, 0, 1, MPI.OBJECT, i, 0);
            MPI.COMM_WORLD.Send(new Object[]{p2}, 0, 1, MPI.OBJECT, i, 0);
            MPI.COMM_WORLD.Send(new int[]{start}, 0, 1, MPI.INT, i, 0);
            MPI.COMM_WORLD.Send(new int[]{end}, 0, 1, MPI.INT, i, 0);
        }
        Object[] results = new Object[nr - 1];
        for (int i = 1; i < nr; i++) {
            MPI.COMM_WORLD.Recv(results, i - 1, 1, MPI.OBJECT, i, 0);
        }
        Polynomial result = Operation.result(results);
        long endTime = System.currentTimeMillis();
        System.out.println("Regular multiplication of polynomials:\n" + result.toString());
        System.out.println("Done in: " + (endTime - startTime) + " ms");
    }

    private static void child() {
        Object[] p1 = new Object[2];
        Object[] p2 = new Object[2];
        int[] begin = new int[1];
        int[] end = new int[1];
        MPI.COMM_WORLD.Recv(p1, 0, 1, MPI.OBJECT, 0, 0);
        MPI.COMM_WORLD.Recv(p2, 0, 1, MPI.OBJECT, 0, 0);
        MPI.COMM_WORLD.Recv(begin, 0, 1, MPI.INT, 0, 0);
        MPI.COMM_WORLD.Recv(end, 0, 1, MPI.INT, 0, 0);
        Polynomial result = Operation.multiply(p1[0], p2[0], begin[0], end[0]);
        MPI.COMM_WORLD.Send(new Object[]{result}, 0, 1, MPI.OBJECT, 0, 0);
    }

    public void run(String[] args) {
        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();
        int nr = MPI.COMM_WORLD.Size();
        if (me == 0) {
            Polynomial p1 = new Polynomial(1000);
            Polynomial p2 = new Polynomial(1000);
            System.out.println(p1);
            System.out.println(p2);
            parent(p1, p2, nr);
        } else {
            child();
        }
        MPI.Finalize();
    }
}
