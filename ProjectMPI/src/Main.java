import mpi.MPI;

public class Main {
    public static void main(String[] args) {
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        if (rank == 0) {
            ParallelMPI.parent();
        } else {
            ParallelMPI.child();
        }
        MPI.Finalize();
    }
}
