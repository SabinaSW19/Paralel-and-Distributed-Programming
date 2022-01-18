import mpi.MPI;

public class Main {
    public static void main(String[] args) {
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        DSM distributedSharedMemory = new DSM();
        Runnable run = new Observer(distributedSharedMemory);
        Thread thread = new Thread(run);
        thread.start();
        if (rank == 0) {
            distributedSharedMemory.subscribeOperation("v1");
            distributedSharedMemory.subscribeOperation("v2");
            distributedSharedMemory.subscribeOperation("v3");

            distributedSharedMemory.changeValueOperation("v1", 18);
            System.out.println("Process 0, " + distributedSharedMemory);
            distributedSharedMemory.changeValueOperation("v2", 23);
            System.out.println("Process 0, " + distributedSharedMemory);
            distributedSharedMemory.changeValueOperation("v3", 31);
            System.out.println("Process 0, " + distributedSharedMemory);

            distributedSharedMemory.compareExchangeOperation("v2", 23, 20);
            System.out.println("Process 0, " + distributedSharedMemory);
            distributedSharedMemory.unsubscribeOperation();
            thread.stop();
        }
        if (rank == 1) {
            distributedSharedMemory.subscribeOperation("v1");
            distributedSharedMemory.subscribeOperation("v3");
        }
        if (rank == 2) {
            distributedSharedMemory.subscribeOperation("v2");
            distributedSharedMemory.subscribeOperation("v3");
        }
        if (rank == 3)
            distributedSharedMemory.subscribeOperation("v2");
        MPI.Finalize();
    }
}
